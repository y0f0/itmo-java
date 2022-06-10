package info.kgeorgiy.ja.pologov.bank;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public final class Client {
    /** Utility class. */
    private Client() {}

    public static void main(final String... args) throws RemoteException {
        if (args == null || args.length != 5) {
            System.err.println("error args is not correct");
            return;
        }
        String name = args[0];
        String surname = args[1];
        String passport = args[2];
        String accountId = args[3];
        int difference = Integer.parseInt(args[4]);

        final Bank bank;
        try {
            bank = (Bank) Naming.lookup("//localhost/bank");
        } catch (final NotBoundException e) {
            System.out.println("Bank is not bound");
            return;
        } catch (final MalformedURLException e) {
            System.out.println("Bank URL is invalid");
            return;
        }

        Account account = bank.getAccount(accountId);
        if (account == null) {
            System.out.println("Creating account");
            account = bank.createAccount(accountId);
        } else {
            System.out.println("Account already exists");
        }
        System.out.println("Account id: " + account.getId());
        System.out.println("before: " + account.getAmount() + " money");
        account.setAmount(account.getAmount() + difference);
        System.out.println("after: " + account.getAmount() + " money");
    }
}
