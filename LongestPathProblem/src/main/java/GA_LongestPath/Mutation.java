package GA_LongestPath;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Mutation {
    public static List<List<Integer>> applyMutation(List<List<Integer>> a,
                                                    List<List<Integer>> parents, int mutationCount) throws FileNotFoundException {
        List<List<Integer>> children = new ArrayList<>();
        List<Integer> tmpParent, child1,child2;
        for(List<Integer> parent: parents){
            tmpParent = parent;
            child1 = getChildren(a,parent);
            Collections.reverse(tmpParent);
            child2 = getChildren(a, tmpParent);
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
    private static List<Integer> getChildren(
            List<List<Integer>> a, List<Integer> parent) throws FileNotFoundException {
        List<List<Integer>> tmpMatrixA = new ArrayList<>(a);//PrintTool.getArrayFromFile(InitialSolutions.FILE_NAME);
        List<Integer> child = new ArrayList<Integer>();
        //получаем значение возмущения, т.е. узел в графе от которого будем перестраивать
        //дальнейший путь. Мы сначала рандомно генерим число от 0 до половины длины пути родителя
        //потом получаем значение вершины графа по сгенеренному индексу
        int pressureValue = new Random().nextInt(Math.round(parent.size()/2));
        for(int i=0;i<=pressureValue;i++){
            child.add(parent.get(i));
        }
        if(pressureValue>0){
            for (int i=1;i<=pressureValue+1;i++){
                MatrixTool.deletePath(tmpMatrixA,parent.get(i-1),parent.get(i));
            }
        }


        int currentVertex = parent.get(pressureValue);

//        System.out.println("Parent:"+parent);
//        System.out.println("currentVertex:"+currentVertex);
//        PrintTool.printArray(a);
        List<Integer> nextVertices = MatrixTool.getNextVertices(tmpMatrixA,currentVertex);
        if(nextVertices.size()<3 || pressureValue==0){return null;}


        int preventVertex = parent.get(pressureValue-1);
        nextVertices.remove(parent.get(pressureValue+1));
        nextVertices.remove(parent.get(pressureValue-1));
        List<Integer> availableVertices = nextVertices;
        int nextVertex;

        while (true) {
            if (availableVertices.isEmpty()) {
                break;
            }
            List<Integer> agrees = MatrixTool.getAgrees(tmpMatrixA, availableVertices);
//            PrintTool.print(agrees);
            nextVertex = availableVertices.size() > 1 ?
                    MatrixTool.getNextVertex(availableVertices, agrees) : availableVertices.get(0);
            child.add(nextVertex);
//            PrintTool.print(child);
            currentVertex = nextVertex;
            MatrixTool.deletePath(tmpMatrixA, preventVertex, currentVertex);
            preventVertex = nextVertex;
            availableVertices = MatrixTool.getNextVertices(tmpMatrixA, nextVertex);
        }
//        System.out.println("Child: "+child);

//        System.out.println("AAAAA:");PrintTool.printArray(a);
//        System.out.println("TMPPPP:");PrintTool.printArray(tmpMatrixA);
        return child;
    }
}
