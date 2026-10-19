package ATM.AtmStates;

import ATM.Atm;
import ATM.Card;

/** A card is present; the machine is waiting for PIN authentication. */
public class HasCardState extends State {

    @Override
    public void authenticatePin(Atm atm, Card card, int pin) {
        if (!card.isPinCorrect(pin)) {
            System.out.println("Entered PIN is invalid. Ejecting card.");
            exit(atm);
            return;
        }
        System.out.println("PIN verified.");
        atm.setCurrentAtmState(new SelectOperationState());
    }

    @Override
    public void exit(Atm atm) {
        System.out.println("Thank you for using our service.");
        atm.setCurrentAtmState(new IdleState());
    }
}
