package GA_LongestPath;

import java.util.ArrayList;
import java.util.Random;

public class Selection {

    protected static ArrayList<ArrayList<Integer>> applySelection(ArrayList<ArrayList<Integer>> currentPopulation, int count){
        ArrayList<ArrayList<Integer>> nextPopulation = new ArrayList<ArrayList<Integer>>();
        nextPopulation.addAll(Selection.applyElitism(currentPopulation, count/2));
        nextPopulation.addAll(Selection.rouletteSelect(currentPopulation, count/2));
        Tool.printArray(nextPopulation);
        return nextPopulation;
    }

    private static ArrayList<ArrayList<Integer>> rouletteSelect
            (ArrayList<ArrayList<Integer>> currentPopulation, int count) {

        ArrayList<ArrayList<Integer>> selectedPart = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < count; i++) {
            double weight_sum = 0;
            for (ArrayList<Integer> path : currentPopulation) {
                weight_sum += path.size();
            }
            double value = new Random().nextDouble() * weight_sum;
            int removeIndex = -1;
            for (ArrayList<Integer> path : currentPopulation) {

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
            if (value == 0) {
                selectedPart.add(currentPopulation.get(currentPopulation.size() - 1));
                currentPopulation.remove((currentPopulation.size() - 1));
            }
        }
        return selectedPart;
    }

    private static ArrayList<ArrayList<Integer>> applyElitism(
            ArrayList<ArrayList<Integer>> currentPopulation, int eliteCount) {
        ArrayList<ArrayList<Integer>> elitePart = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < eliteCount; i++) {
            int maxIndex = -1, maxLength = 0, pathLength, currentIndex = -1;
            for (ArrayList<Integer> path : currentPopulation) {
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
        return elitePart;
    }
}
