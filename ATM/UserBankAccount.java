package ATM;

public class UserBankAccount {

    int balance;

    UserBankAccount(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void withdrawAmount(int amount) {

        balance = balance - amount;
    }
}
