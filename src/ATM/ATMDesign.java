package ATM;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

// Enum representing different transaction types
enum TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    BALANCE_INQUIRY
}

// Represents a bank account
class Account {
    private int accountNumber;
    private int pin;
    private double balance;

    public Account(int accountNumber, int pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount) {
        balance += amount;
    }

    public synchronized void withdraw(double amount) {
        balance -= amount;
    }
}

// Represents an ATM machine
class ATM {
    private Map<Integer, Account> accounts;

    public ATM() {
        // Use ConcurrentHashMap for thread safety
        accounts = new ConcurrentHashMap<>();
    }

    public synchronized void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public synchronized boolean authenticateUser(int accountNumber, int pin) {
        Account account = accounts.get(accountNumber);
        return account != null && account.getPin() == pin;
    }

    public synchronized void performTransaction(int accountNumber, TransactionType type, double amount) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        switch (type) {
            case DEPOSIT:
                account.deposit(amount);
                System.out.println("Deposit successful. New balance: " + account.getBalance());
                break;
            case WITHDRAWAL:
                if (account.getBalance() < amount) {
                    System.out.println("Insufficient funds.");
                    return;
                }
                account.withdraw(amount);
                System.out.println("Withdrawal successful. New balance: " + account.getBalance());
                break;
            case BALANCE_INQUIRY:
                System.out.println("Current balance: " + account.getBalance());
                break;
            default:
                System.out.println("Invalid transaction type.");
        }
    }
}

public class ATMDesign {
    public static void main(String[] args) {
        // Create an ATM
        ATM atm = new ATM();

        // Create accounts and add them to the ATM
        Account account1 = new Account(123456, 1234, 1000);
        Account account2 = new Account(789012, 5678, 500);
        atm.addAccount(account1);
        atm.addAccount(account2);

        // Perform transactions
        int accountNumber = 123456;
        int pin = 1234;
        if (atm.authenticateUser(accountNumber, pin)) {
            atm.performTransaction(accountNumber, TransactionType.DEPOSIT, 200);
            atm.performTransaction(accountNumber, TransactionType.WITHDRAWAL, 300);
            atm.performTransaction(accountNumber, TransactionType.BALANCE_INQUIRY, 0);
        } else {
            System.out.println("Authentication failed.");
        }
    }
}
