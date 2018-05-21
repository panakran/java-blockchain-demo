package com.blockchain.simpleBlockChainWithJava;

import com.blockchain.blockchain.Block;
import com.blockchain.blockchain.BlockChain;
import com.blockchain.transactions.Transaction;
import com.blockchain.transactions.TransactionOutput;
import com.blockchain.users.Wallet;
import java.security.Security;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleBlockChainWithJavaApplication {

    public static Integer DIFFICULTY = 5;
    public static Wallet coinbase;
    public static Block genesis;
    public static BlockChain blockChain;

    public static void main(String[] args) {
        SpringApplication.run(SimpleBlockChainWithJavaApplication.class, args);
        blockChain = new BlockChain(DIFFICULTY);

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create wallets:
        coinbase = new Wallet();

        //create genesis transaction, which sends 10000 coins to coinbase: 
        BlockChain.genesisTransaction = new Transaction(coinbase.publicKey, coinbase.publicKey, 10000, null);
        BlockChain.genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
        BlockChain.genesisTransaction.transactionId = "0"; //manually set the transaction id
        BlockChain.genesisTransaction.outputs.add(new TransactionOutput(BlockChain.genesisTransaction.reciepient, BlockChain.genesisTransaction.value, BlockChain.genesisTransaction.transactionId)); //manually add the Transactions Output
        BlockChain.UTXOs.put(BlockChain.genesisTransaction.outputs.get(0).id, BlockChain.genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");
        genesis = new Block("0");
        genesis.addTransaction(BlockChain.genesisTransaction);
        blockChain.addBlock(genesis);


    }

}
