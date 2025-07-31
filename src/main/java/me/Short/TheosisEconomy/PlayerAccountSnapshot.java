package me.Short.TheosisEconomy;

import java.math.BigDecimal;

public final class PlayerAccountSnapshot
{

    private final BigDecimal balance;
    private final boolean acceptingPayments;
    private final String lastKnownUsername;

    // Constructor
    public PlayerAccountSnapshot(BigDecimal balance, boolean acceptingPayments, String lastKnownUsername)
    {
        this.balance = balance;
        this.acceptingPayments = acceptingPayments;
        this.lastKnownUsername = lastKnownUsername;
    }

}
