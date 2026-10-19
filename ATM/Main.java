package ATM;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Self-verifying driver for the ATM.
 *
 * Instead of only printing output, each scenario ASSERTS the expected end
 * state (account balance, remaining note inventory, machine state) and reports
 * PASS/FAIL, finishing with a tally. This doubles as a lightweight test suite
 * without pulling in a test framework. The scenarios exercise the state
 * machine (Idle -> HasCard -> SelectOperation -> CheckBalance / CashWithdrawl)
 * and the plan -> validate -> commit dispensing logic, including edge cases.
 */
public class Main {

    private static final int CORRECT_PIN = 1234;
    private static final int WRONG_PIN = 9999;

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        scenarioCheckBalance();
        scenarioSuccessfulWithdrawal();
        scenarioWrongPin();
        scenarioInsufficientAtmCash();
        scenarioInsufficientAccountBalance();
        scenarioMixedNoteDispensing();
        scenarioNonMultipleOfHundred();
        scenarioCannotDispenseExact();

        System.out.println("\n===== SUMMARY: " + passed + " passed, " + failed + " failed =====");
        if (failed > 0) {
            System.exit(1);
        }
    }

    /* ---------------- scenarios ---------------- */

    private static void scenarioCheckBalance() {
        banner("Check balance (happy path)");
        Atm atm = freshAtm(standardInventory());
        Card card = newCard(new UserBankAccount(3000));

        atm.getCurrentAtmState().insertCard(atm, card);
        atm.getCurrentAtmState().authenticatePin(atm, card, CORRECT_PIN);
        atm.getCurrentAtmState().selectOperation(atm, card, TransactionType.CHECK_BALANCE);
        atm.getCurrentAtmState().showBalance(atm, card);

        check("account unchanged", card.getBalance() == 3000);
        check("machine returned to idle", isIdle(atm));
    }

    private static void scenarioSuccessfulWithdrawal() {
        banner("Cash withdrawal 2600 (happy path)");
        Atm atm = freshAtm(standardInventory());
        Card card = newCard(new UserBankAccount(10000));

        drivePinAndOperation(atm, card, TransactionType.CASH_WITHDRAWAL);
        atm.getCurrentAtmState().cashWithdrawl(atm, card, 2600); // 1x2000 + 1x500 + 1x100

        check("account debited to 7400", card.getBalance() == 7400);
        check("one 2000 note dispensed", atm.getNoteCount(2000) == 9);
        check("one 500 note dispensed", atm.getNoteCount(500) == 19);
        check("one 100 note dispensed", atm.getNoteCount(100) == 9);
        check("machine returned to idle", isIdle(atm));
    }

    private static void scenarioWrongPin() {
        banner("Wrong PIN");
        Atm atm = freshAtm(standardInventory());
        Card card = newCard(new UserBankAccount(3000));

        atm.getCurrentAtmState().insertCard(atm, card);
        atm.getCurrentAtmState().authenticatePin(atm, card, WRONG_PIN);

        check("account unchanged", card.getBalance() == 3000);
        check("card ejected -> idle", isIdle(atm));
    }

    private static void scenarioInsufficientAtmCash() {
        banner("Insufficient cash in ATM");
        Map<Integer, Integer> tinyInventory = new HashMap<>();
        tinyInventory.put(500, 2); // machine holds only 1000
        Atm atm = freshAtm(tinyInventory);
        Card card = newCard(new UserBankAccount(10000));

        drivePinAndOperation(atm, card, TransactionType.CASH_WITHDRAWAL);
        atm.getCurrentAtmState().cashWithdrawl(atm, card, 5000);

        check("account not debited", card.getBalance() == 10000);
        check("atm cash untouched", atm.getAtmBalance() == 1000);
        check("machine returned to idle", isIdle(atm));
    }

    private static void scenarioInsufficientAccountBalance() {
        banner("Insufficient account balance");
        Atm atm = freshAtm(standardInventory());
        Card card = newCard(new UserBankAccount(500));

        drivePinAndOperation(atm, card, TransactionType.CASH_WITHDRAWAL);
        atm.getCurrentAtmState().cashWithdrawl(atm, card, 2000);

        check("account unchanged", card.getBalance() == 500);
        check("machine returned to idle", isIdle(atm));
    }

    private static void scenarioMixedNoteDispensing() {
        banner("Mixed note dispensing 3800");
        Atm atm = freshAtm(standardInventory());
        Card card = newCard(new UserBankAccount(10000));

        drivePinAndOperation(atm, card, TransactionType.CASH_WITHDRAWAL);
        atm.getCurrentAtmState().cashWithdrawl(atm, card, 3800); // 1x2000 + 3x500 + 3x100

        check("account debited to 6200", card.getBalance() == 6200);
        check("2000 notes: 10 -> 9", atm.getNoteCount(2000) == 9);
        check("500 notes: 20 -> 17", atm.getNoteCount(500) == 17);
        check("100 notes: 10 -> 7", atm.getNoteCount(100) == 7);
    }

    private static void scenarioNonMultipleOfHundred() {
        banner("Amount not a multiple of 100 (150)");
        Atm atm = freshAtm(standardInventory());
        Card card = newCard(new UserBankAccount(10000));

        drivePinAndOperation(atm, card, TransactionType.CASH_WITHDRAWAL);
        atm.getCurrentAtmState().cashWithdrawl(atm, card, 150);

        check("account unchanged", card.getBalance() == 10000);
        check("machine returned to idle", isIdle(atm));
    }

    private static void scenarioCannotDispenseExact() {
        banner("Cannot make exact amount from available notes (2500)");
        Map<Integer, Integer> only2000s = new HashMap<>();
        only2000s.put(2000, 10); // no 500s or 100s to make the trailing 500
        Atm atm = freshAtm(only2000s);
        Card card = newCard(new UserBankAccount(10000));

        drivePinAndOperation(atm, card, TransactionType.CASH_WITHDRAWAL);
        atm.getCurrentAtmState().cashWithdrawl(atm, card, 2500);

        check("account NOT debited (no partial withdrawal)", card.getBalance() == 10000);
        check("notes untouched", atm.getNoteCount(2000) == 10);
        check("machine returned to idle", isIdle(atm));
    }

    /* ---------------- helpers ---------------- */

    /** Standard demo inventory: 10x100, 20x500, 10x2000 = 51000. */
    private static Map<Integer, Integer> standardInventory() {
        Map<Integer, Integer> notes = new HashMap<>();
        notes.put(100, 10);
        notes.put(500, 20);
        notes.put(2000, 10);
        return notes;
    }

    /** Reset the singleton to idle and load a known inventory. */
    private static Atm freshAtm(Map<Integer, Integer> inventory) {
        Atm atm = Atm.getInstance();
        atm.reset();
        atm.loadCash(inventory);
        return atm;
    }

    private static Card newCard(UserBankAccount account) {
        Card card = new Card(1111_2222, 123, new Date(), "Suyash Pandey", CORRECT_PIN);
        card.setUserBankAccount(account);
        return card;
    }

    private static void drivePinAndOperation(Atm atm, Card card, TransactionType type) {
        atm.getCurrentAtmState().insertCard(atm, card);
        atm.getCurrentAtmState().authenticatePin(atm, card, CORRECT_PIN);
        atm.getCurrentAtmState().selectOperation(atm, card, type);
    }

    private static boolean isIdle(Atm atm) {
        return atm.getCurrentAtmState() instanceof ATM.AtmStates.IdleState;
    }

    private static void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + description);
        } else {
            failed++;
            System.out.println("  [FAIL] " + description);
        }
    }

    private static void banner(String title) {
        System.out.println("\n==================================================");
        System.out.println(title);
        System.out.println("==================================================");
    }
}
