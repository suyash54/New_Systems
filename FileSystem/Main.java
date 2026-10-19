package FileSystem;

public class Main {
    public static void main(String[] args) {
        FileSystems fs = new FileSystems();

        fs.mkdir("/a/b/c");
        System.out.println(fs.pwd()); // /

        fs.cd("a/b");
        System.out.println(fs.pwd()); // /a/b

        fs.cd("*");
        System.out.println(fs.pwd()); // /a/b/c

        fs.cd("../*");
        System.out.println(fs.pwd()); // /a/b/c

        fs.cd("/x/y"); // invalid
        System.out.println(fs.pwd()); // unchanged
    }
}