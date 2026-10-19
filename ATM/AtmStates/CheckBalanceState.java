package ATM.AtmStates;

import ATM.Atm;
import ATM.Card;

/** Displays the account balance, then returns the machine to idle. */
public class CheckBalanceState extends State {

    @Override
    public void showBalance(Atm atm, Card card) {
        System.out.println("Your balance is: " + card.getBalance());
        exit(atm);
    }

    @Override
    public void exit(Atm atm) {
        atm.setCurrentAtmState(new IdleState());
        System.out.println("Thank you for using our service.");
    }
}
