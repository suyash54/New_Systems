package RedoUndo;

import java.util.*;

// Command Interface
interface Command {
    void execute();
    void undo();
}

