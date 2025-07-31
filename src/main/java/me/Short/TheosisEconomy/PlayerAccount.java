package me.Short.TheosisEconomy;

import java.math.BigDecimal;

public class PlayerAccount
{

    private BigDecimal balance;

    private boolean acceptingPayments;

    private String lastKnownUsername;

    // Constructor
    public PlayerAccount(BigDecimal balance, boolean acceptingPayments, String lastKnownUsername)
    {
        this.balance = balance;
        this.acceptingPayments = acceptingPayments;
        this.lastKnownUsername = lastKnownUsername;
    }

    // Method to get a `PlayerAccountSnapshot` of this `PlayerAccount`
    public PlayerAccountSnapshot snapshot()
    {
        return new PlayerAccountSnapshot(balance, acceptingPayments, lastKnownUsername);
    }

    // ----- Getters -----

    // Getter for 'balance'
    public BigDecimal getBalance()
    {
        return balance;
    }

    // Getter for 'acceptingPayments'
    public boolean getAcceptingPayments()
    {
        return acceptingPayments;
    }

    // Getter for 'lastKnownUsername'
    public String getLastKnownUsername()
    {
        return lastKnownUsername;
    }

    // ----- Setters -----

    // Setter for "balance"
    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    // Setter for "acceptingPayments"
    public void setAcceptingPayments(boolean acceptingPayments)
    {
        this.acceptingPayments = acceptingPayments;
    }

    // Setter for "lastKnownUsername"
    public void setLastKnownUsername(String lastKnownUsername)
    {
        this.lastKnownUsername = lastKnownUsername;
    }

}