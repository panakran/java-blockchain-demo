package com.blockchain;

import java.util.ArrayList;

/**
 *
 * @author panos_kr
 */
public class BlockChain {

    public ArrayList<Block> blockchain = new ArrayList<>();
    public Integer difficulty = 5;

    public BlockChain(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public ArrayList<Block> getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(ArrayList<Block> blockchain) {
        this.blockchain = blockchain;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }

        }
        return true;
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(this.difficulty);
        this.blockchain.add(newBlock);
    }

    public String getLastBlockHash() {
        return this.blockchain.get(this.blockchain.size() - 1).hash;
    }
}
