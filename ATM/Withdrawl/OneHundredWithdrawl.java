package ATM.Withdrawl;

import ATM.Atm;

import java.util.Map;

/** Handles the 100 denomination — the last (smallest) link in the chain. */
public class OneHundredWithdrawl extends CashWithdrawl {

    private static final int DENOMINATION = 100;

    public OneHundredWithdrawl(CashWithdrawl nextCashWithdrawl) {
        super(nextCashWithdrawl);
    }

    @Override
    public int withdraw(Atm atm, int amount, Map<Integer, Integer> plan) {
        return plan(atm, DENOMINATION, amount, plan);
    }
}
