package ATM.Withdrawl;

import ATM.Atm;

import java.util.Map;

/** Handles the 500 denomination, then defers the rest to the next handler. */
public class FiveHundredWithdrawl extends CashWithdrawl {

    private static final int DENOMINATION = 500;

    public FiveHundredWithdrawl(CashWithdrawl nextCashWithdrawl) {
        super(nextCashWithdrawl);
    }

    @Override
    public int withdraw(Atm atm, int amount, Map<Integer, Integer> plan) {
        return plan(atm, DENOMINATION, amount, plan);
    }
}
