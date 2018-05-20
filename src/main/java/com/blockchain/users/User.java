package com.blockchain.users;

/**
 *
 * @author panos_kr
 */
public class User {

    private Integer id;
    private String username;
    private Wallet wallet;

    public User(Integer id, String username, Wallet wallet) {
        this.id = id;
        this.username = username;
        this.wallet = wallet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", id=" + id + ", wallet=" + wallet + '}';
    }

}
