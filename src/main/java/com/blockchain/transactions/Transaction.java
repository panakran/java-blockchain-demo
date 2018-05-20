package com.blockchain.transactions;

/**
 *
 * @author panos_kr
 */
import com.blockchain.blockchain.BlockChain;
import com.blockchain.Utilities.StringUtil;
import java.security.*;
import java.util.ArrayList;

public class Transaction {

    public String transactionId; // this is also the hash of the transaction.
    public PublicKey sender; // senders address/public key.
    public PublicKey reciepient; // Recipients address/public key.
    public float value;
    public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0; // a rough count of how many transactions have been generated. 

    // Constructor: 
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    // This Calculates the transaction hash (which will be used as its Id)
    private String calulateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        StringBuilder concatString = new StringBuilder();
        concatString
                .append(StringUtil.getStringFromKey(sender))
                .append(StringUtil.getStringFromKey(reciepient))
                .append(Float.toString(value).concat(String.valueOf(sequence)));
        return StringUtil.applySha256(concatString.toString());
    }

    //Signs all the data we dont wish to be tampered with.
    public void generateSignature(PrivateKey privateKey) {
        StringBuilder concatString = new StringBuilder();
        String data = concatString
                .append(StringUtil.getStringFromKey(sender))
                .append(StringUtil.getStringFromKey(reciepient))
                .append(Float.toString(value))
                .toString();
        signature = StringUtil.applyECDSASig(privateKey, data);
    }
//Verifies the data we signed hasnt been tampered with

    public boolean verifySignature() {
        StringBuilder concatString = new StringBuilder();
        String data = concatString
                .append(StringUtil.getStringFromKey(sender))
                .append(StringUtil.getStringFromKey(reciepient))
                .append(Float.toString(value))
                .toString();
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    //Returns true if new transaction could be created.	
    public boolean processTransaction() {

        if (!verifySignature()) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }
        inputs.forEach(i -> i.UTXO = BlockChain.UTXOs.get(i.transactionOutputId));

        //gather transaction inputs (Make sure they are unspent):
        //check if transaction is valid:
        if (getInputsValue() < BlockChain.minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        //generate transaction outputs:
        float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
        transactionId = calulateHash();
        outputs.add(new TransactionOutput(this.reciepient, value, transactionId)); //send value to recipient
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); //send the left over 'change' back to sender		

        //add outputs to Unspent list
        outputs.forEach(o -> BlockChain.UTXOs.put(o.id, o));

        //remove transaction inputs from UTXO lists as spent:
        inputs.stream()
                .filter(i -> i.UTXO != null)
                .forEach(i -> BlockChain.UTXOs.remove(i.UTXO.id));

        return true;
    }

//returns sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        return inputs.stream()
                .filter(i -> i.UTXO != null)
                .map(i -> i.UTXO.value)
                .reduce(total, (x, y) -> x + y);
    }

//returns sum of outputs:
    public float getOutputsValue() {
        float total = 0;
        return outputs.stream()
                .map(o -> o.value)
                .reduce(total, (x, y) -> x + y);
    }
}
