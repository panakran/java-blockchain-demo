package com.blockchain.simpleBlockChainWithJava;

import com.blockchain.blockchain.Block;
import com.blockchain.users.User;
import com.blockchain.Utilities.StringUtil;
import com.blockchain.users.Wallet;
import static com.blockchain.simpleBlockChainWithJava.SimpleBlockChainWithJavaApplication.blockChain;
import static com.blockchain.simpleBlockChainWithJava.SimpleBlockChainWithJavaApplication.walletA;
import static com.blockchain.simpleBlockChainWithJava.SimpleBlockChainWithJavaApplication.walletB;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author panos_kr
 */
@RestController
public class BlockChainController {

    @RequestMapping(path = "/blockchain", produces = MediaType.APPLICATION_JSON_VALUE)
    public String blockchain() {

        System.out.println("BLOCKCHAIN VALIDATION::" + (blockChain.isChainValid() ? "VALID" : "INVALID"));
        return StringUtil.getJson(blockChain.getBlockchain());
    }

    @RequestMapping(path = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public String send(@RequestParam Integer amount) {
        Block newBlock = new Block(blockChain.getLastBlockHash());
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds " + amount + " to WalletB...");
        newBlock.addTransaction(walletA.sendFunds(walletB.publicKey, amount));
        blockChain.addBlock(newBlock);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        System.out.println("BLOCKCHAIN VALIDATION::" + (blockChain.isChainValid() ? "VALID" : "INVALID"));
        return StringUtil.getJson(walletA);
    }

    @RequestMapping(path = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createUser(@RequestParam String username) {
        UUID newUniqueId = UUID.randomUUID();
        Wallet newWallet = new Wallet();
        User newUser = new User(newUniqueId.hashCode(), username, newWallet);

        blockChain.getUserList().add(newUser);

        System.out.println("New User created" + newUser);
        System.out.println("User list" + blockChain.getUserList());
        return StringUtil.getJson(blockChain.getUserList());
    }

}
