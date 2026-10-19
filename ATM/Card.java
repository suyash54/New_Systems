package ATM;

import java.util.Date;

/**
 * A debit card linked to exactly one {@link UserBankAccount}.
 *
 * The PIN is a per-card instance field (previously it was a single shared
 * {@code static final} value, which meant every card had the same PIN). In a
 * real system you would store a salted hash and never the raw PIN.
 */
public class Card {

    int cardNumber;
    int cvv;
    Date expirayDate;
    String cardHolderName;

    private final int pin;

    private UserBankAccount userBankAccount;

    public void setUserBankAccount(UserBankAccount userBankAccount) {
        this.userBankAccount = userBankAccount;
    }

    Card(int cardNumber, int cvv, Date expirayDate, String cardHolderName, int pin) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirayDate = expirayDate;
        this.cardHolderName = cardHolderName;
        this.pin = pin;
    }

    public int getBalance() {
        return userBankAccount.getBalance();
    }

    public boolean isPinCorrect(int enteredPin) {
        return enteredPin == pin;
    }

    /** Debit the linked account. Caller is responsible for validating funds first. */
    public void deductAmount(int withdrawAmount) {
        userBankAccount.withdrawAmount(withdrawAmount);
    }
}
