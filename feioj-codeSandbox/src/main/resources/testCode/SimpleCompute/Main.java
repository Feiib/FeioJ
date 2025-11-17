/*
public class Main {
    public static void main(String[] args) {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        System.out.println(a + b);
    }
}
*/
import java.io.File;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        try {
            // 尝试使用Files类（在黑名单中）
            File file = new File("test.txt");
            Files.createFile(file.toPath());
            System.out.println("File created");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}