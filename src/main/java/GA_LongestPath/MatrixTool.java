package GA_LongestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrixTool {

    public static void deletePath(List<List<Integer>> a, Integer prev, Integer current) {
        a.get(prev).set(current, 0);
        a.get(current).set(prev, 0);
    }
    //определяем следующую вершину из возможных
    //вероятность высчитывается следующим образом. У каждой вершины есть количество следующих вариантов путей
    //столько раз мы и добавляем нашу вершину в массив вероятности.
    // В итоге наш массив состоит из повторящихся вариантов возможных вершин. И чем их больше, тем выше вероятность,
    // что именно эта вершина и будет выбрана. А дальше я использую функцию рандома с равномерным распределением.
    // это и гарантирует то, что неважно в каком месте массива находятся наши вершины
    public static Integer getNextVertex(List<Integer> nextVertices, List<Integer> agrees) {
        ArrayList<Integer> probability = new ArrayList<Integer>();
        for (int j = 0; j < nextVertices.size(); j++) {
            for (int k = 0; k < agrees.get(j); k++) {
                probability.add(nextVertices.get(j));
            }
        }
        Random random = new Random();
        int i = random.nextInt(probability.size());
        return probability.get(i);
    }

    //образуем массив колличесва дальнейших путей у каждой из возможных следующих вершин
    // нужно для того, чтобы опреелить наиболее вероятное направление
    public static List<Integer> getAgrees(List<List<Integer>> a, List<Integer> nextVertices) {
        List<Integer> agrees = new ArrayList<Integer>();
        for (Integer nextVertex : nextVertices) {
            int sum = 0;
            for (int j = 0; j < a.size(); j++) {
                sum += a.get(nextVertex).get(j);
            }
            agrees.add(sum);
        }
        return agrees;
    }

    public static List<Integer> getNextVertices(List<List<Integer>> a, int currentVertex) {
        List<Integer> array = a.get(currentVertex);
        List<Integer> nextPath = new ArrayList<Integer>();
        for (int j = 0; j < a.size(); j++) {
            if (array.get(j) == 1) {
                nextPath.add(j);
            }
        }
        return nextPath;
    }
}
