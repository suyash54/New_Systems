package RedoUndo;

import java.util.*;

class DeleteCommand implements Command {

    private List<StringBuilder> rows;
    private int row, col;
    private int length;
    private String deletedText; // 🔥 important

    public DeleteCommand(List<StringBuilder> rows, int row, int col, int length) {
        this.rows = rows;
        this.row = row;
        this.col = col;
        this.length = length;
    }

    @Override
    public void execute() {
        StringBuilder sb = rows.get(row);
        deletedText = sb.substring(col, col + length);
        sb.delete(col, col + length);
    }

    @Override
    public void undo() {
        rows.get(row).insert(col, deletedText);
    }
}