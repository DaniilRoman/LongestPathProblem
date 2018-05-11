package GA_LongestPath;

import java.io.FileNotFoundException;
import java.util.*;

public class InitialSolutions {
    private static final String FILE_NAME = "array2";
    private static final int POPULATION_SIZE = 80;
    private static int MUTATION_COUNT = 20;

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        final ArrayList<ArrayList<Integer>> a = Tool.getArrayFromFile(FILE_NAME);
        Tool.printArray(a);
        ArrayList<ArrayList<Integer>> startPopulation = getStartPopulation(POPULATION_SIZE);
        ArrayList<ArrayList<Integer>> currentPopulation = new ArrayList<ArrayList<Integer>>();
        currentPopulation.addAll(startPopulation);
//        System.out.println("FIRST:");
        Tool.printArray(currentPopulation);
        System.out.println("------------------------------------------------------------------------");

        for (int i=0;i<40;i++) {
//            System.out.println(i);
            currentPopulation.addAll(applyMutation(a,startPopulation,MUTATION_COUNT));
            currentPopulation = Selection.applySelection(currentPopulation, POPULATION_SIZE);
//            System.out.println("CURRENT POP:");
//            Tool.printArray(a);
//            Tool.printArray(currentPopulation);
//            System.out.println("------------------------------------------------------------------------\n");
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
        Tool.printArray(currentPopulation);
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
    }


    private static ArrayList<ArrayList<Integer>> applyMutation(ArrayList<ArrayList<Integer>> a,
            ArrayList<ArrayList<Integer>> parents,int mutationCount) throws FileNotFoundException {
        ArrayList<ArrayList<Integer>> children = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> tmp, child1,child2;// = new ArrayList<>();
        for(ArrayList<Integer> parent: parents){
            tmp = parent;
            child1 = getChildren(a, parents,parent);
            Collections.reverse(tmp);
            child2 = getChildren(a, parents,tmp);
            if(child1 == null || child2 == null) {continue;}
            children.add(child1);
            children.add(child2);
            mutationCount--;
            if(mutationCount<=0){break;}
        }
        return children;
    }

    // если возвращает null, то значит, что мутация не была успешной, нужно обработать выше
    // не уменьшать счетчик успешых мутаций
    private static ArrayList<Integer> getChildren(
            ArrayList<ArrayList<Integer>> a,ArrayList<ArrayList<Integer>> parents, ArrayList<Integer> parent) throws FileNotFoundException {
        ArrayList<ArrayList<Integer>> tmpMatrixA = Tool.getArrayFromFile(FILE_NAME);
        ArrayList<Integer> child = new ArrayList<Integer>();
        //получаем значение возмущения, т.е. узел в графе от которого будем перестраивать
        //дальнейший путь. Мы сначала рандомно генерим число от 0 до половины длины пути родителя
        //потом получаем значение вершины графа по сгенеренному индексу
        int pressureValue = new Random().nextInt(Math.round(parent.size()/2));
        for(int i=0;i<=pressureValue;i++){
            child.add(parent.get(i));
        }
        if(pressureValue>0){
            for (int i=1;i<=pressureValue+1;i++){
                deletePath(tmpMatrixA,parent.get(i-1),parent.get(i));
            }
        }


        int currentVertex = parent.get(pressureValue);

//        System.out.println("Parent:"+parent);
//        System.out.println("currentVertex:"+currentVertex);
//        Tool.printArray(a);
        ArrayList<Integer> nextVertices = getNextVertices(tmpMatrixA,currentVertex);
        if(nextVertices.size()<3 || pressureValue==0){return null;}


        int preventVertex = parent.get(pressureValue-1);
        nextVertices.remove(parent.get(pressureValue+1));
        nextVertices.remove(parent.get(pressureValue-1));
        ArrayList<Integer> availableVertices = nextVertices;
        int nextVertex;

        while (true) {
            if (availableVertices.isEmpty()) {
                break;
            }
            ArrayList<Integer> agrees = getAgrees(tmpMatrixA, availableVertices);
//            Tool.print(agrees);
            nextVertex = availableVertices.size() > 1 ? getNextVertex(availableVertices, agrees) : availableVertices.get(0);
            child.add(nextVertex);
//            Tool.print(child);
            currentVertex = nextVertex;
            deletePath(tmpMatrixA, preventVertex, currentVertex);
            preventVertex = nextVertex;
            availableVertices = getNextVertices(tmpMatrixA, nextVertex);
        }
//        System.out.println("Child: "+child);

//        System.out.println("AAAAA:");Tool.printArray(a);
//        System.out.println("TMPPPP:");Tool.printArray(tmpMatrixA);
        return child;
    }


    private static ArrayList<ArrayList<Integer>> getStartPopulation(int populationSize) throws FileNotFoundException {
        ArrayList<ArrayList<Integer>> startPopulation = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < populationSize; i++) {
            // -1 означает то, что мы будем инициализировать начальную точку рандомно
            // или же мы можем ее задать напрямую
            startPopulation.add(getInitialSolution(FILE_NAME,-1));
        }
        return startPopulation;
    }

    private static ArrayList<Integer> getInitialSolution(String fileName, int nextVertex) throws FileNotFoundException {
        int prev, current;
        ArrayList<Integer> resultPath = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> a = Tool.getArrayFromFile(fileName);
        ArrayList<ArrayList<Integer>> b = Tool.getArrayFromFile(fileName);
        ArrayList<Integer> resultTemp = new ArrayList<Integer>();
        //b.addAll(a);

//        Tool.printArray(a);

        if(nextVertex==-1) {
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
        Tool.print(nextVertex);
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

            Tool.print(nextVertices);
            if (nextVertices.isEmpty()) {
                break;
            }
            ArrayList<Integer> agrees = getAgrees(a, nextVertices);
            Tool.print(agrees);
            nextVertex = nextVertices.size() > 1 ? getNextVertex(nextVertices, agrees) : nextVertices.get(0);
            resultPath.add(nextVertex);
            Tool.print(resultPath);
            current = nextVertex;
            deletePath(a, prev, current);
            prev = nextVertex;
//            Tool.printArray(a);
        }


//        System.out.println("resultPath: " + getResultPath(resultPath, resultTemp));
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
    //TimeUnit.MILLISECONDS.toSeconds
//        long second = (millis / 1000) % 60;
//        long minute = (millis / (1000 * 60)) % 60;
//        long hour = (millis / (1000 * 60 * 60)) % 24;
//
//        String time = String.format("%02d:%02d:%02d:%d", hour, minute, second, millis);
}