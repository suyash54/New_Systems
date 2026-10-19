package RedoUndo;

import java.util.*;

class AddCommand implements Command {

    private List<StringBuilder> rows;
    private int row, col;
    private String text;

    public AddCommand(List<StringBuilder> rows, int row, int col, String text) {
        this.rows = rows;
        this.row = row;
        this.col = col;
        this.text = text;
    }

    @Override
    public void execute() {
        rows.get(row).insert(col, text);
    }

    @Override
    public void undo() {
        rows.get(row).delete(col, col + text.length());
    }
}