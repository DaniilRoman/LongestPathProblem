package GA_LongestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrintTool {
    public static List<List<Integer>> getArrayFromFile(String fileName) throws FileNotFoundException {
        List<List<Integer>> a = new ArrayList<>();
        Scanner input = new Scanner(new File(fileName + ".txt"));
        while (input.hasNextLine()) {
            Scanner colReader = new Scanner(input.nextLine());
            ArrayList col = new ArrayList();
            while (colReader.hasNextInt()) {
                col.add(colReader.nextInt());
            }
            a.add(col);
        }
        return a;
    }

    public static void print(Object object) {
        System.out.println(object);
    }

    public static void printArray(List<List<Integer>> a) {
        int length = a.size();
        StringBuilder indexes = new StringBuilder();
        indexes.append("    ");
        for (int i = 0; i < length; i++) {
            indexes.append(i + "  ");
        }
        System.out.println(indexes);
        for (int i = 0; i < length; i++) {
            System.out.println(i + ") " + a.get(i));
        }
    }
}
