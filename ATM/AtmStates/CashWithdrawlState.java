package ATM.AtmStates;

import ATM.Atm;
import ATM.Card;
import ATM.Withdrawl.CashWithdrawl;
import ATM.Withdrawl.FiveHundredWithdrawl;
import ATM.Withdrawl.OneHundredWithdrawl;
import ATM.Withdrawl.TwoThousanNotes;

import java.util.Map;
import java.util.TreeMap;

/**
 * State active while the user is withdrawing cash.
 *
 * The withdrawal follows a strict PLAN -> VALIDATE -> COMMIT sequence so that
 * no balance is ever touched unless the machine can fully honour the request:
 *   1. Validate the amount (positive, a multiple of the smallest note).
 *   2. Check ATM cash and account funds.
 *   3. Ask the note chain to PLAN a breakdown (no mutation).
 *   4. Only if the plan covers the amount exactly, COMMIT: debit the account
 *      and remove the notes from the machine.
 */
public class CashWithdrawlState extends State {

    private static final int SMALLEST_NOTE = 100;

    @Override
    public void cashWithdrawl(Atm atm, Card card, int withdrawlAmount) {

        // 1. amount sanity checks
        if (withdrawlAmount <= 0 || withdrawlAmount % SMALLEST_NOTE != 0) {
            System.out.println("Amount must be a positive multiple of " + SMALLEST_NOTE);
            exit(atm);
            return;
        }

        // 2. funds checks
        if (withdrawlAmount > atm.getAtmBalance()) {
            System.out.println("Insufficient cash in ATM");
            exit(atm);
            return;
        }
        if (withdrawlAmount > card.getBalance()) {
            System.out.println("Insufficient balance in your account");
            exit(atm);
            return;
        }

        // 3. plan the note breakdown WITHOUT mutating anything
        CashWithdrawl noteChain =
                new TwoThousanNotes(new FiveHundredWithdrawl(new OneHundredWithdrawl(null)));
        Map<Integer, Integer> plan = new TreeMap<>();
        int leftover = noteChain.withdraw(atm, withdrawlAmount, plan);

        if (leftover != 0) {
            // e.g. asking for 2500 when the machine has only 2000 notes left
            System.out.println("ATM cannot dispense this amount with available denominations");
            exit(atm);
            return;
        }

        // 4. commit atomically: debit account, then remove notes
        card.deductAmount(withdrawlAmount);
        for (Map.Entry<Integer, Integer> e : plan.entrySet()) {
            atm.deductNotes(e.getKey(), e.getValue());
        }

        System.out.println("Please collect your cash:");
        for (Map.Entry<Integer, Integer> e : plan.entrySet()) {
            System.out.println("  " + e.getValue() + " x " + e.getKey());
        }
        exit(atm);
    }

    @Override
    public void exit(Atm atm) {
        atm.setCurrentAtmState(new IdleState());
        System.out.println("Thank you for using our service!");
    }
}
