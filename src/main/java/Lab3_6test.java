import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Lab3_6test {
    private static Node[] nodeList;
    private static Node root;
    private static final Node rootnull = null;
    static String filename = "src/lab3_5.3test/words.txt";
    static String[] words = FileReadOneWord.readWords(filename);
    //前端函数
    public static void frontfun()
    {

        nodeList = DirectedGraph.directGraph(words);
        if (nodeList != null) {
            root = nodeList[0];
        }

    }
    @Test //word在图中，node为不为空
    public void randomWalkTest() {
        frontfun();
        String randomWalk = root.randomWalk("She",nodeList);
        //打印随机游走结果
        System.out.println(randomWalk);

    }
    @Test //word不在图中，node为不为空
    public void randomWalkTest2() {
        frontfun();
        String randomWalk = root.randomWalk("dog",nodeList);
        //打印随机游走结果
        System.out.println(randomWalk);

    }
}
