package RedoUndo;

import java.util.Deque;
import java.util.List;
import java.util.*;


// Text Editor
class TextEditor {

    private List<StringBuilder> rows;
    private Deque<Command> undoStack;
    private Deque<Command> redoStack;

    public TextEditor() {
        rows = new ArrayList<>();
        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();
    }

    // -------- ADD TEXT --------
    public void addText(int row, int column, String text) {

        // create row if needed
        if (row == rows.size()) {
            rows.add(new StringBuilder());
        }

        Command cmd = new AddCommand(rows, row, column, text);
        cmd.execute();

        undoStack.push(cmd);
        redoStack.clear(); // 🔥 important
    }

    // -------- DELETE TEXT --------
    public void deleteText(int row, int startColumn, int length) {

        Command cmd = new DeleteCommand(rows, row, startColumn, length);
        cmd.execute();

        undoStack.push(cmd);
        redoStack.clear(); // 🔥 important
    }

    // -------- UNDO --------
    public void undo() {
        if (undoStack.isEmpty()) return;

        Command cmd = undoStack.pop();
        cmd.undo();

        redoStack.push(cmd);
    }

    // -------- REDO --------
    public void redo() {
        if (redoStack.isEmpty()) return;

        Command cmd = redoStack.pop();
        cmd.execute();

        undoStack.push(cmd);
    }

    // -------- READ --------
    public String readLine(int row) {
        return rows.get(row).toString();
    }
}