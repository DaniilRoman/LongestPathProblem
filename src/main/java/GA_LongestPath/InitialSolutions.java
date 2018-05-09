package GA_LongestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class InitialSolutions {
    private static final String FILE_NAME = "array";
    private static final int POPULATION_SIZE = 40;

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
//        System.out.println(rouletteSelect(getInitialSolution(FILE_NAME)));
        ArrayList<ArrayList<Integer>> startPopulation = getStartPopulation(POPULATION_SIZE);
        printArray(applyElitism(startPopulation, 10));
        printArray(rouletteSelect(startPopulation, 10));
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
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

    private static ArrayList<ArrayList<Integer>> getStartPopulation(int populationSize) throws FileNotFoundException {
        ArrayList<ArrayList<Integer>> startPopulation = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < populationSize; i++) {
            startPopulation.add(getInitialSolution(FILE_NAME));
        }
        return startPopulation;
    }

    private static ArrayList<Integer> getInitialSolution(String fileName) throws FileNotFoundException {
        int prev, current;
        ArrayList<Integer> resultPath = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> a = getArrayFromFile(fileName);
        ArrayList<ArrayList<Integer>> b = getArrayFromFile(fileName);
        ArrayList<Integer> resultTemp = new ArrayList<Integer>();
        //b.addAll(a);

//        printArray(a);
        Random random = new Random();
        Integer nextVertex = random.nextInt(a.size());
        resultPath.add(nextVertex);
        prev = nextVertex;
//        ArrayList<Integer> nextVertices = getNextVertices(a, i);
//        System.out.println("RandomPoint: " + i);
//        System.out.println(nextVertices);
//        ArrayList<Integer> agrees = getAgrees(a, nextVertices);
//        System.out.println(agrees);
        boolean flag = true;
        print(nextVertex);
        ArrayList<Integer> nextVertices = new ArrayList<Integer>();
        while (true) {
            nextVertices = getNextVertices(a, nextVertex);
            if (nextVertices.size() == 0) {
                if (flag) {
                    ArrayList<Integer> nextVerticesOfFirst = getNextVertices(b, resultPath.get(0));
                    if (nextVerticesOfFirst.size() > 1) {
                        ArrayList<Integer> temp = new ArrayList<Integer>();
                        temp.add(resultPath.get(0));
                        nextVerticesOfFirst.remove(resultPath.get(1));
                        temp.add(nextVerticesOfFirst.get(0));//сделать выбор через вероятность
                        resultTemp.addAll(resultPath);//Collections.copy(resultTemp,resultPath);
                        resultPath.clear();
                        resultPath = temp;
                        prev = resultPath.get(0);
                        current = resultPath.get(1);
                        deletePath(b, prev, current);
                        prev = current;
                        flag = false;
                        a = b;
                        nextVertices = getNextVertices(a, current);
                    } else break;
                } else break;
            }

            print(nextVertices);
            if (nextVertices.isEmpty()) {
                break;
            }
            ArrayList<Integer> agrees = getAgrees(a, nextVertices);
            print(agrees);
            nextVertex = nextVertices.size() > 1 ? getNextVertex(nextVertices, agrees) : nextVertices.get(0);
            resultPath.add(nextVertex);
            print(resultPath);
            current = nextVertex;
            deletePath(a, prev, current);
            prev = nextVertex;
//            printArray(a);
        }


        System.out.println("resultPath: " + getResultPath(resultPath, resultTemp));
        return getResultPath(resultPath, resultTemp);
    }

    private static ArrayList<Integer> getResultPath(ArrayList<Integer> resultPath, ArrayList<Integer> resultTemp) {
        if (resultTemp != null) {
            if (resultPath.size() > resultTemp.size()) return resultPath;
            else return resultTemp;
        } else return resultPath;
    }

    private static void deletePath(ArrayList<ArrayList<Integer>> a, Integer prev, Integer current) {
        a.get(prev).set(current, 0);
        a.get(current).set(prev, 0);
    }

    //определяем следующую вершину из возможных
    //вероятность высчитывается следующим образом. У каждой вершины есть количество следующих вариантов путей
    //столько раз мы и добавляем нашу вершину в массив вероятности.
    // В итоге наш массив состоит из повторящихся вариантов возможных вершин. И чем их больше, тем выше вероятность,
    // что именно эта вершина и будет выбрана. А дальше я использую функцию рандома с равномерным распределением.
    // это и гарантирует то, что неважно в каком месте массива находятся наши вершины
    private static Integer getNextVertex(ArrayList<Integer> nextVertices, ArrayList<Integer> agrees) {
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
    private static ArrayList<Integer> getAgrees(ArrayList<ArrayList<Integer>> a, ArrayList<Integer> nextVertices) {
        ArrayList<Integer> agrees = new ArrayList<Integer>();
        for (Integer nextVertex : nextVertices) {
            int sum = 0;
            for (int j = 0; j < a.size(); j++) {
                sum += a.get(nextVertex).get(j);
            }
            agrees.add(sum);
        }
        return agrees;
    }

    private static ArrayList<Integer> getNextVertices(ArrayList<ArrayList<Integer>> a, int currentVertex) {
        ArrayList<Integer> array = a.get(currentVertex);
        ArrayList<Integer> nextPath = new ArrayList<Integer>();
        for (int j = 0; j < a.size(); j++) {
            if (array.get(j) == 1) {
                nextPath.add(j);
            }
        }
        return nextPath;
    }


    private static void print(Object object) {
//        System.out.println(object);
    }

    private static void printArray(ArrayList<ArrayList<Integer>> a) {
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

    private static ArrayList<ArrayList<Integer>> getArrayFromFile(String fileName) throws FileNotFoundException {
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

    //TimeUnit.MILLISECONDS.toSeconds
//        long second = (millis / 1000) % 60;
//        long minute = (millis / (1000 * 60)) % 60;
//        long hour = (millis / (1000 * 60 * 60)) % 24;
//
//        String time = String.format("%02d:%02d:%02d:%d", hour, minute, second, millis);
}