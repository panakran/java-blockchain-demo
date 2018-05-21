package com.blockchain.simpleBlockChainWithJava;

import com.blockchain.blockchain.Block;
import com.blockchain.users.User;
import com.blockchain.Utilities.StringUtil;
import com.blockchain.users.Wallet;
import static com.blockchain.simpleBlockChainWithJava.SimpleBlockChainWithJavaApplication.blockChain;
import static com.blockchain.simpleBlockChainWithJava.SimpleBlockChainWithJavaApplication.coinbase;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author panos_kr
 */
@RestController
public class BlockChainController {

    @RequestMapping(path = "/blockchain", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public String blockchain() {

        System.out.println("BLOCKCHAIN VALIDATION::" + (blockChain.isChainValid() ? "VALID" : "INVALID"));
        return StringUtil.getJson(blockChain.getBlockchain());
    }

    @RequestMapping(path = "/send", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String send(@RequestParam String from, @RequestParam Integer amount, @RequestParam String to) {
        Block newBlock = new Block(blockChain.getLastBlockHash());
        User sender = blockChain.getUserByUsername(from);
        User receiver = blockChain.getUserByUsername(to);
        Wallet senderWallet = blockChain.getUserByUsername(from).getWallet();
        Wallet receiverWallet = blockChain.getUserByUsername(to).getWallet();

        System.out.println("\n" + sender.getUsername() + " balance: " + senderWallet.getBalance());
        System.out.println("\n" + sender.getUsername() + " is Attempting to send funds " + amount + " to receiver...");
        newBlock.addTransaction(senderWallet.sendFunds(receiverWallet.publicKey, amount));
        blockChain.addBlock(newBlock);
        System.out.println("\n" + sender.getUsername() + " balance is: " + senderWallet.getBalance());
        System.out.println("" + receiver.getUsername() + " balance is: " + receiverWallet.getBalance());

        System.out.println("BLOCKCHAIN VALIDATION::" + (blockChain.isChainValid() ? "VALID" : "INVALID"));
        return StringUtil.getJson(senderWallet);
    }

    @RequestMapping(path = "/createuser", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String createUser(@RequestParam String username) {
        UUID newUniqueId = UUID.randomUUID();
        Wallet newWallet = new Wallet();
        User newUser = new User(newUniqueId.hashCode(), username, newWallet);
        System.out.println("New User created" + newUser);

        blockChain.getUserList().add(newUser);

        Block newBlock = new Block(blockChain.getLastBlockHash());
        newBlock.addTransaction(coinbase.sendFunds(newWallet.publicKey, 100f));
        blockChain.addBlock(newBlock);
        System.out.println("\nUser:" + username + " balance is: " + newWallet.getBalance());
        System.out.println("\nGenerate and add " + 100 + " User:" + username);

        System.out.println("BLOCKCHAIN VALIDATION::" + (blockChain.isChainValid() ? "VALID" : "INVALID"));

        System.out.println("User list" + blockChain.getUserList());
        return StringUtil.getJson(blockChain.getUserList());
    }

    @RequestMapping(path = "/getuser", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String getUser(@RequestParam String username) {
        return StringUtil.getJson(blockChain.getUserByUsername(username));
    }

}
