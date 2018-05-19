package com.blockchain.simpleBlockChainWithJava;

import com.blockchain.Block;
import com.blockchain.BlockChain;
import com.blockchain.Utilities.StringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleBlockChainWithJavaApplication {

    public static Integer DIFFICULTY = 5;

    public static void main(String[] args) {
        SpringApplication.run(SimpleBlockChainWithJavaApplication.class, args);

        BlockChain blockChain = new BlockChain(DIFFICULTY);

        System.out.println("Trying to Mine block 1... ");
        blockChain.addBlock(new Block("New data for the genesis block", "0"));

        System.out.println("Trying to Mine block 2... ");
        blockChain.addBlock(new Block("Second block data", blockChain.getLastBlockHash()));

        System.out.println("Trying to Mine block 3... ");
        blockChain.addBlock(new Block("Third block data", blockChain.getLastBlockHash()));

        System.out.println("\nBlockchain is Valid: " + blockChain.isChainValid());

        String blockchainJson = StringUtil.getJson(blockChain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }

}
