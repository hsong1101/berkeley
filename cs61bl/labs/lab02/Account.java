/**
 * This class represents a bank account whose current balance is a nonnegative
 * amount in US dollars.
 */
public class Account {

    private int balance;

    Account parentAccount;

    /** Initialize an account with the given BALANCE. */
    public Account(int balance) {
        this.balance = balance;
        parentAccount = null;
    }

    public Account(int balance, Account account){
        this.balance = balance;
        parentAccount = account;
    }

    /** Return the number of dollars in the account. */
    public int getBalance() {
        return this.balance;
    }

    /** Deposits AMOUNT into the current account. */
    public void deposit(int amount) {
        if (amount < 0) {
            System.out.println("Cannot deposit negative amount.");
        } else {
            this.balance = this.balance + amount;
        }
    }

    /** Subtract AMOUNT from the account if possible. If subtracting AMOUNT
     *	would leave a negative balance, print an error message and leave the
     *	balance unchanged.
     */
    public boolean withdraw(int amount) {

        if(parentAccount == null) {
            if (amount < 0) {
                System.out.println("Cannot withdraw negative amount.");
                return false;
            } else if (this.balance < amount) {
                System.out.println("Insufficient funds");
                return false;
            } else {
                this.balance = this.balance - amount;
                return true;
            }
        }else {
            if(amount < 0){
                System.out.println("Cannot withdraw negative amount.");
                return false;
            } else if(this.balance >= amount){
                this.balance -= amount;
                return true;
            } else{
                amount -= this.balance;
                if(parentAccount.withdraw(amount)){
                    this.balance = 0;
                    return true;
                }
                return false;
            }
        }
    }

    /** Merge account OTHER into this account by removing all money from OTHER
     *	and depositing it into this account.
     */
    public void merge(Account other) {
        this.deposit(other.balance);
        other.balance = 0;
    }
}
