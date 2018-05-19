package com.blockchain.simpleBlockChainWithJava;

import com.blockchain.Block;
import com.blockchain.BlockChain;
import com.blockchain.Transaction;
import com.blockchain.TransactionOutput;
import com.blockchain.Wallet;
import java.security.Security;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleBlockChainWithJavaApplication {

    public static Integer DIFFICULTY = 1;
    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args) {
        SpringApplication.run(SimpleBlockChainWithJavaApplication.class, args);

        //blockchain test
        BlockChain blockChain = new BlockChain(DIFFICULTY);

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create wallets:
        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        //create genesis transaction, which sends 100 coins to walletA: 
        BlockChain.genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        BlockChain.genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
        BlockChain.genesisTransaction.transactionId = "0"; //manually set the transaction id
        BlockChain.genesisTransaction.outputs.add(new TransactionOutput(BlockChain.genesisTransaction.reciepient, BlockChain.genesisTransaction.value, BlockChain.genesisTransaction.transactionId)); //manually add the Transactions Output
        BlockChain.UTXOs.put(BlockChain.genesisTransaction.outputs.get(0).id, BlockChain.genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(BlockChain.genesisTransaction);
        blockChain.addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        blockChain.addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        blockChain.addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20));
        blockChain.addBlock(block3);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        blockChain.isChainValid();

    }

}
