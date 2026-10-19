package ATM.AtmStates;

import ATM.Atm;
import ATM.Card;
import ATM.TransactionType;

public abstract class  State {

    public void insertCard(Atm atm, Card card){
        System.out.println("Opps");
    }

    public void authenticatePin(Atm atm, Card card, int pin){
        System.out.println("Oops");
    }

    public void selectOperation(Atm atm, Card card, TransactionType type){
        System.out.println("Oops");
    }

    public void cashWithdrawl(Atm atm,Card card,int withdrawlAmount){
        System.out.println("Oops");
    }

    public void showBalance(Atm atm, Card card){
        System.out.println("OOps");
    }

    public void returnCard(Atm atm, Card card){
        System.out.println("Oops");
    }

    public void exit(Atm atm){
        System.out.println("Oops");
    }
}
