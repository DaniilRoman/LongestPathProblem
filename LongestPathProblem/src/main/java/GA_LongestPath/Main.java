package GA_LongestPath;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String FILE_NAME = "array2";
    private static final int POPULATION_SIZE = 80;
    private static int MUTATION_COUNT = 40;
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        final List<List<Integer>> matrixFromFile =  PrintTool.getArrayFromFile(FILE_NAME);
//        List<List<Integer>> matrixFromFile = ImmutableList.copyOf(a);
        PrintTool.printArray(matrixFromFile);
        PrintTool.print("------------------------------------------------------------------------");
        List<List<Integer>> startPopulation = InitialSolutions.getStartPopulation(matrixFromFile,POPULATION_SIZE,FILE_NAME);
        List<List<Integer>> currentPopulation = new ArrayList<>();
        currentPopulation.addAll(startPopulation);
//        System.out.println("FIRST:");
        PrintTool.printArray(currentPopulation);
        System.out.println("------------------------------------------------------------------------");

        for (int i = 0; i < 150; i++) {
//            System.out.println(i);
            currentPopulation.addAll(Mutation.applyMutation(matrixFromFile, startPopulation, MUTATION_COUNT));
            currentPopulation = Selection.applySelection(currentPopulation, POPULATION_SIZE);
//            System.out.println("CURRENT POP:");
//            PrintTool.printArray(a);
//            PrintTool.printArray(currentPopulation);
//            System.out.println("------------------------------------------------------------------------\n");
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
        PrintTool.printArray(currentPopulation);
        PrintTool.print("Result Path: " + currentPopulation.get(0));
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
    }
}
