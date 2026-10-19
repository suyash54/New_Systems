package ATM.Withdrawl;

import ATM.Atm;

import java.util.Map;

/** Handles the 2000 denomination, then defers the rest to the next handler. */
public class TwoThousanNotes extends CashWithdrawl {

    private static final int DENOMINATION = 2000;

    public TwoThousanNotes(CashWithdrawl nextCashWithdrawl) {
        super(nextCashWithdrawl);
    }

    @Override
    public int withdraw(Atm atm, int amount, Map<Integer, Integer> plan) {
        return plan(atm, DENOMINATION, amount, plan);
    }
}
