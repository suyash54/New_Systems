package ATM;

public class User {

    Card card;
    UserBankAccount bankAccount;

    User(Card card, UserBankAccount bankAccount) {

        this.card = card;
        this.bankAccount = bankAccount;
    }

    public void withdrawAmount(int amount) {
        this.bankAccount.withdrawAmount(amount);
    }

    public int getBalance() {
        return this.bankAccount.getBalance();
    }
}
