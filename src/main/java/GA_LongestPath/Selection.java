package GA_LongestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Selection {

    protected static List<List<Integer>> applySelection(List<List<Integer>> currentPopulation, int count){
        List<List<Integer>> nextPopulation = new ArrayList<>();
        nextPopulation.addAll(Selection.applyElitism(currentPopulation, count/4));
        nextPopulation.addAll(Selection.rouletteSelect(currentPopulation, 3*count/4));
        return nextPopulation;
    }

    private static List<List<Integer>> rouletteSelect
            (List<List<Integer>> currentPopulation, int count) {

        List<List<Integer>> selectedPart = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            double weight_sum = 0;
            for (List<Integer> path : currentPopulation) {
                weight_sum += path.size();
            }
            double value = new Random().nextDouble() * weight_sum;
            int removeIndex = -1;
            for (List<Integer> path : currentPopulation) {

                removeIndex++;
                value -= path.size();
                if (value < 0) {
                    selectedPart.add(path);
                    currentPopulation.remove(removeIndex);
                    break;
                }
            }
            //если рандом получился 1, то у нас не получится value < 0
            //и поэтому будет value = 0 и мы выбираем просто последний путь и его удаляем

//            if (value == 0) {
//                selectedPart.add(currentPopulation.get(currentPopulation.size() - 1));
//                currentPopulation.remove((currentPopulation.size() - 1));
//            }
        }
//        System.out.println("SELECTED:");
//        Tool.printArray(selectedPart);
        return selectedPart;
    }

    private static List<List<Integer>> applyElitism(
            List<List<Integer>> currentPopulation, int eliteCount) {
        List<List<Integer>> elitePart = new ArrayList<>();
        for (int i = 0; i < eliteCount; i++) {
            int maxIndex = -1, maxLength = 0, pathLength, currentIndex = -1;
            for (List<Integer> path : currentPopulation) {
                currentIndex++;
                pathLength = path.size();
                if (pathLength > maxLength) {
                    maxLength = pathLength;
                    maxIndex = currentIndex;
                }
            }
            if (maxIndex != -1) {
                elitePart.add(currentPopulation.get(maxIndex));
                currentPopulation.remove(maxIndex);
            }
        }
//        System.out.println("ELITE:");
//        Tool.printArray(elitePart);
        return elitePart;
    }
}
