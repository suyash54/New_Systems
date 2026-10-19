package ATM.Withdrawl;

import ATM.Atm;

import java.util.Map;

/**
 * Chain of Responsibility for breaking a withdrawal amount into notes.
 *
 * IMPORTANT: this is a PLANNING step only. Handlers do NOT mutate the ATM.
 * Each handler records how many of its denomination it would use into
 * {@code plan} and returns the amount it could not cover, delegating the
 * remainder to the next (smaller) denomination. The caller inspects the
 * returned leftover: if it is non-zero the machine cannot make the exact
 * amount and the whole transaction is rejected without touching any balance.
 * Only once leftover == 0 does the caller commit the plan.
 */
public abstract class
CashWithdrawl {

    protected final CashWithdrawl nextCashWithdrawl;

    protected CashWithdrawl(CashWithdrawl nextCashWithdrawl) {
        this.nextCashWithdrawl = nextCashWithdrawl;
    }

    /**
     * @param atm    machine whose (unmodified) inventory bounds how many notes we can use
     * @param amount amount still to be broken into notes
     * @param plan   denomination -> note-count accumulator, filled by the chain
     * @return the portion of {@code amount} that could not be planned (0 == success)
     */
    public abstract int withdraw(Atm atm, int amount, Map<Integer, Integer> plan);

    /** Delegate the remainder to the next handler, or report it as leftover. */
    protected int passToNext(Atm atm, int amount, Map<Integer, Integer> plan) {
        if (nextCashWithdrawl != null) {
            return nextCashWithdrawl.withdraw(atm, amount, plan);
        }
        return amount; // no smaller denomination available
    }

    /** Shared greedy step used by every concrete handler. */
    protected int plan(Atm atm, int denomination, int amount, Map<Integer, Integer> plan) {
        int available = atm.getNoteCount(denomination);
        int needed = Math.min(amount / denomination, available);
        if (needed > 0) {
            plan.put(denomination, needed);
        }
        int remaining = amount - needed * denomination;
        return passToNext(atm, remaining, plan);
    }
}
