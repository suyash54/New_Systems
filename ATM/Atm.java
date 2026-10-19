package ATM;

import ATM.AtmStates.IdleState;
import ATM.AtmStates.State;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Represents a single physical ATM. Modelled as a Singleton because one JVM
 * process drives one machine.
 *
 * Design notes:
 *  - Cash is stored as a denomination -> note-count map instead of separate
 *    fields. This removes the earlier "atmBalance drifting out of sync with
 *    the note counts" bug: the balance is now DERIVED from the inventory and
 *    can never disagree with it.
 *  - The current {@link State} lives here and is initialised once (in the
 *    private constructor), not reset on every accessor call.
 */
public class Atm {

    // Denominations kept highest-first so the dispensing chain / greedy
    // breakdown naturally consumes large notes before small ones.
    private final Map<Integer, Integer> noteInventory = new TreeMap<>(Collections.reverseOrder());

    private State currentState;

    private static final Atm INSTANCE = new Atm();

    private Atm() {
        this.currentState = new IdleState();
    }

    public static Atm getInstance() {
        return INSTANCE;
    }

    /* ---------------- state ---------------- */

    public void setCurrentAtmState(State atmState) {
        this.currentState = atmState;
    }

    public State getCurrentAtmState() {
        return this.currentState;
    }

    /** Return the machine to its initial (idle, empty-ish) posture. */
    public void reset() {
        this.currentState = new IdleState();
    }

    /* ---------------- cash inventory ---------------- */

    /** Load / overwrite the machine's cash inventory. Copies the input map. */
    public void loadCash(Map<Integer, Integer> notes) {
        noteInventory.clear();
        noteInventory.putAll(notes);
    }

    /** Denominations the machine knows about, ordered high -> low. */
    public Set<Integer> getDenominationsHighToLow() {
        return noteInventory.keySet();
    }

    public int getNoteCount(int denomination) {
        return noteInventory.getOrDefault(denomination, 0);
    }

    /** Remove {@code count} notes of the given denomination from the machine. */
    public void deductNotes(int denomination, int count) {
        int current = getNoteCount(denomination);
        if (count > current) {
            // Should never happen: the plan is validated before commit.
            throw new IllegalStateException(
                    "Tried to dispense " + count + " x " + denomination
                            + " but only " + current + " available");
        }
        noteInventory.put(denomination, current - count);
    }

    /** Total cash in the machine, derived from the note inventory. */
    public int getAtmBalance() {
        int total = 0;
        for (Map.Entry<Integer, Integer> e : noteInventory.entrySet()) {
            total += e.getKey() * e.getValue();
        }
        return total;
    }

    public void printCurrentAtmBalance() {
        System.out.println("Current ATM balance: " + getAtmBalance());
    }
}
