package FileSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileSystems {
    private final Directory root;
    private Directory currentNode;

    public FileSystems(){
        root = new Directory("/",null);
        root.parent = root;
        currentNode = root;
    }

    private List<String> splitParts(String path){
        List<String> parts = new ArrayList<>();
        for(String p : path.split("/")){
            if(!p.isEmpty()){
                parts.add(p);
            }
        }
        return parts;
    }

    public String pwd(){
       if(currentNode == root)
            return root.name;

       List<String> path = new ArrayList<>();
       Directory tmp = currentNode;

       while(tmp!=root){
           path.add(tmp.name);
           tmp = tmp.parent;
       }
       path.add(root.name);
       Collections.reverse(path);
       return path.toString();
    }

    public void cd(String path){
       Directory tmp = path.startsWith("/")?root:currentNode;

       List<String> parts = splitParts(path);


       for(String p: parts){
           if(p.equals(".")){
               continue;
           }
           else if(p.equals("..")){
                   tmp = tmp.parent;
               }
           else if(p.equals("*")){
                if(!tmp.children.isEmpty()){
                    String smallest = Collections.min(tmp.children.keySet());
                    tmp =  tmp.children.get(smallest);
                }
           }
           else if(!tmp.children.containsKey(p)){
               return;
           }
           else{
               tmp = tmp.children.get(p);
           }

       }

       currentNode = tmp;
    }

    public void mkdir(String path){
        Directory tmp = path.startsWith("/")?root:currentNode;

        List<String> parts = splitParts(path);

        for(String p: parts){

            if(p.equals("."))
                 continue;
            else if(p.equals("..")){
                tmp = tmp.parent;
            }
            else{
              Directory child =   tmp.children.getOrDefault(p,new Directory(p,tmp));
              tmp.children.put(p,child);
              tmp = tmp.children.get(p);
            }
        }

    }


}
