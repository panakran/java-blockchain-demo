package com.blockchain;

import com.blockchain.Utilities.StringUtil;
import java.util.Date;

public class Block {

    public String hash;
    public String previousHash;
    private String data; //our data will be a simple message.
    private long timeStamp; //as number of milliseconds since 1/1/1970.
    private int nonce;

    //Block Constructor.  
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

    //Calculate new hash based on blocks contents
    public final String calculateHash() {
        StringBuilder stringToApplySha256 = new StringBuilder();
        stringToApplySha256.append(previousHash)
                .append(Long.toString(timeStamp))
                .append(Integer.toString(nonce))
                .append(data);
        String calculatedhash = StringUtil.applySha256(stringToApplySha256.toString());
        return calculatedhash;
    }

    //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
        String target = StringUtil.getDifficultyString(difficulty); //Create a string with difficulty * "0" 
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

}
