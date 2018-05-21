package com.blockchain.blockchain;

import com.blockchain.transactions.Transaction;
import com.blockchain.Utilities.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    public String hash;
    public String previousHash;
    public String merkleRoot;
    public List<Transaction> transactions = new ArrayList<>(); //our data will be a simple message.
    public long timeStamp; //as number of milliseconds since 1/1/1970.
    public int nonce;

    //Block Constructor.  
    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

    //Calculate new hash based on blocks contents
    public String calculateHash() {
        StringBuilder concatenatedString = new StringBuilder();
        String data = concatenatedString
                .append(previousHash)
                .append(Long.toString(timeStamp))
                .append(Integer.toString(nonce))
                .append(merkleRoot)
                .toString();

        String calculatedhash = StringUtil.applySha256(data);

        return calculatedhash;
    }

    //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty); //Create a string with difficulty * "0" 
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) {
            return false;
        }
        if (!previousHash.equals("0")) {
            if ((!transaction.processTransaction())) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

}
