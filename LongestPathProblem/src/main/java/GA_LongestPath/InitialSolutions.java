package GA_LongestPath;

import java.io.FileNotFoundException;
import java.util.*;

public class InitialSolutions {



    public static List<List<Integer>> getStartPopulation(List<List<Integer>> matrixFromFile, int populationSize,String fileName) throws FileNotFoundException {
        List<List<Integer>> startPopulation = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            // -1 означает то, что мы будем инициализировать начальную точку рандомно
            // или же мы можем ее задать напрямую
            startPopulation.add(getInitialSolution(matrixFromFile, -1,fileName));
        }
        return startPopulation;
    }

    private static List<Integer> getInitialSolution(List<List<Integer>> matrixFromFile, int nextVertex, String fileName) throws FileNotFoundException {
        int prev, current;
        ArrayList<Integer> resultPath = new ArrayList<Integer>();
//        List<List<Integer>> a = new ArrayList<>(matrixFromFile);
        List<List<Integer>> a = PrintTool.getArrayFromFile(fileName);//new ArrayList<>(matrixFromFile);
        List<List<Integer>> b = new ArrayList<>(a);//PrintTool.getArrayFromFile(FILE_NAME);//
        List<Integer> resultTemp = new ArrayList<Integer>();

        if (nextVertex == -1) {
            Random random = new Random();
            nextVertex = random.nextInt(a.size());
        }
        resultPath.add(nextVertex);
        prev = nextVertex;
//        ArrayList<Integer> nextVertices = getNextVertices(a, i);
//        System.out.println("RandomPoint: " + i);
//        System.out.println(nextVertices);
//        ArrayList<Integer> agrees = getAgrees(a, nextVertices);
//        System.out.println(agrees);
        boolean flag = true;
        PrintTool.print(nextVertex);
        List<Integer> nextVertices = new ArrayList<Integer>();
        while (true) {
            nextVertices = MatrixTool.getNextVertices(a, nextVertex);
            if (nextVertices.size() == 0) {
                if (flag) {
                    List<Integer> nextVerticesOfFirst = MatrixTool.getNextVertices(b, resultPath.get(0));
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
                        MatrixTool.deletePath(b, prev, current);
                        prev = current;
                        flag = false;
                        a = b;
                        nextVertices = MatrixTool.getNextVertices(a, current);
                    } else break;
                } else break;
            }

            PrintTool.print(nextVertices);
            if (nextVertices.isEmpty()) {
                break;
            }
            List<Integer> agrees = MatrixTool.getAgrees(a, nextVertices);
            PrintTool.print(agrees);
            nextVertex = nextVertices.size() > 1 ? MatrixTool.getNextVertex(nextVertices, agrees) : nextVertices.get(0);
            resultPath.add(nextVertex);
            PrintTool.print(resultPath);
            current = nextVertex;
            MatrixTool.deletePath(a, prev, current);
            prev = nextVertex;
//            PrintTool.printArray(a);
        }


//        System.out.println("resultPath: " + getResultPath(resultPath, resultTemp));
        return getResultPath(resultPath, resultTemp);
    }

    private static List<Integer> getResultPath(List<Integer> resultPath, List<Integer> resultTemp) {
        if (resultTemp != null) {
            if (resultPath.size() > resultTemp.size()) return resultPath;
            else return resultTemp;
        } else return resultPath;
    }





    //TimeUnit.MILLISECONDS.toSeconds
//        long second = (millis / 1000) % 60;
//        long minute = (millis / (1000 * 60)) % 60;
//        long hour = (millis / (1000 * 60 * 60)) % 24;
//
//        String time = String.format("%02d:%02d:%02d:%d", hour, minute, second, millis);
}