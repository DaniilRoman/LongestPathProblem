package GA_LongestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tool {
    public static ArrayList<ArrayList<Integer>> getArrayFromFile(String fileName) throws FileNotFoundException {
        ArrayList<ArrayList<Integer>> a = new ArrayList<ArrayList<Integer>>();
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
//        System.out.println(object);
    }

    public static void printArray(ArrayList<ArrayList<Integer>> a) {
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
