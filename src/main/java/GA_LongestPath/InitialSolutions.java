package GA_LongestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class InitialSolutions {
    public static void main(String[] args) throws FileNotFoundException {
        getInitialSolution("array");
    }


    private static ArrayList<Integer> getInitialSolution(String fileName) throws FileNotFoundException {
        int prev, current;
        ArrayList<Integer> resultPath = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> a = getArrayFromFile(fileName);
        ArrayList<ArrayList<Integer>> b = getArrayFromFile(fileName);
        ArrayList<Integer> resultTemp = new ArrayList<Integer>();
        //b.addAll(a);
        printArray(a);
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
        System.out.println(nextVertex);
        ArrayList<Integer> nextVertices = new ArrayList<Integer>();
        while (true) {
            nextVertices = getNextVertices(a, nextVertex);
            if (nextVertices.size()==0) {
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
                        deletePath(b,prev,current);
                        prev = current;
                        flag = false;
                        a = b;
                        nextVertices = getNextVertices(a, current);
                    }else break;
                } else break;
            }

            System.out.println(nextVertices);
            if(nextVertices.isEmpty()){break;}
            ArrayList<Integer> agrees = getAgrees(a, nextVertices);
            System.out.println(agrees);
            nextVertex = nextVertices.size() > 1 ? getNextVertex(nextVertices, agrees) : nextVertices.get(0);
            resultPath.add(nextVertex);
            System.out.println(resultPath);
            current = nextVertex;
            deletePath(a,prev,current);
            prev = nextVertex;
            printArray(a);
        }



        System.out.println("resultPath: " + getResultPath(resultPath,resultTemp));
        return getResultPath(resultPath,resultTemp);
    }

    private static ArrayList<Integer> getResultPath(ArrayList<Integer> resultPath,ArrayList<Integer> resultTemp){
        if(resultTemp!=null){
            if(resultPath.size()>resultTemp.size()) return resultPath;
            else return resultTemp;
        }else return resultPath;
    }

    private static void deletePath(ArrayList<ArrayList<Integer>> a, Integer prev, Integer current){
        a.get(prev).set(current, 0);
        a.get(current).set(prev, 0);
    }

    //private static Integer
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
}