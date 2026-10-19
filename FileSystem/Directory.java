package FileSystem;

import java.util.HashMap;
import java.util.Map;

public class Directory {
       String  name;
       Directory parent;
       Map<String,Directory> children;

       public Directory(String directory,Directory parent){
           this.name = directory;
           this.parent = parent;
           this.children = new HashMap<>();
       }
}
