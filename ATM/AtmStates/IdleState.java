package ATM.AtmStates;

import ATM.Atm;
import ATM.Card;

/** Starting state: the machine is waiting for a card to be inserted. */
public class IdleState extends State {

    @Override
    public void insertCard(Atm atm, Card card) {
        System.out.println("Card inserted.");
        atm.setCurrentAtmState(new HasCardState());
    }
}
