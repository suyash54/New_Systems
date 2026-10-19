package ATM.AtmStates;

import ATM.Atm;
import ATM.Card;
import ATM.TransactionType;

/** PIN is authenticated; the user chooses which transaction to perform. */
public class SelectOperationState extends State {

    @Override
    public void selectOperation(Atm atm, Card card, TransactionType txnType) {
        if (txnType == TransactionType.CASH_WITHDRAWAL) {
            System.out.println("Selected: Cash withdrawal");
            atm.setCurrentAtmState(new CashWithdrawlState());
        } else if (txnType == TransactionType.CHECK_BALANCE) {
            System.out.println("Selected: Check balance");
            atm.setCurrentAtmState(new CheckBalanceState());
        } else {
            System.out.println("Invalid option");
            exit(atm);
        }
    }

    @Override
    public void exit(Atm atm) {
        atm.setCurrentAtmState(new IdleState());
        System.out.println("Thank you for using our service.");
    }
}
