import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoadEnvironment {
    public static Map<String,String> getVariables(){
        Map<String,String> variables = new HashMap<>();
        try {
            File[] files = File.listRoots();
            File file = new File("out/production/ycmastermind/.env");
            System.out.println(file.getAbsoluteFile());
            Scanner filereader = new Scanner(file);
            while (filereader.hasNextLine()) {
                String line = filereader.nextLine();
                String[] parts = line.split("=");
                variables.put(parts[0],parts[1]);
            }
            filereader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return variables;
    }
}
