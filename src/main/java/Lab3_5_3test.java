import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Lab3_5_3test {
    private static Node[] nodeList;
    private static Node root;
    private static final Node rootnull = null;
    //前端函数
    public static void frontfun()
    {
        String filename = "src/lab3_5.3test/words.txt";
        String[] words = FileReadOneWord.readWords(filename);
        nodeList = DirectedGraph.directGraph(words);
        if (nodeList != null) {
            root = nodeList[0];
        }

    }
    //bridge test main
    public static void main(String[] args){
        //执行前端函数
        frontfun();
        //测试word1和word2都在图中 且存在桥接词
        String bridge = root.queryBridgeWords("She", "an",root);
        System.out.println(bridge);
        //测试word1和word2都在图中 但不存在桥接词
        bridge = root.queryBridgeWords("She", "easygoing",root);
        System.out.println(bridge);
        //测试root为null
        //bridge = rootnull.queryBridgeWords("She", "an",rootnull);
        //System.out.println(bridge);
        //测试word1和word2都不在图中
        bridge = root.queryBridgeWords("dog", "cat",root);
        System.out.println(bridge);
        //测试word1在图中 word2不在图中
        bridge = root.queryBridgeWords("She", "cat",root);
        System.out.println(bridge);
        //word1或word2为null或空字符串。
        bridge = root.queryBridgeWords("", "an",root);
        System.out.println(bridge);
    }

    @Test // 测试word1和word2都在图中 且存在桥接词
    public void testQueryBridgeWords_BothWordsInGraph_WithBridge() {
        frontfun();
        String bridge = root.queryBridgeWords("She", "an", root);

        assertEquals("Bridge words from She to an are: is ", bridge);
    }

    @Test // 测试word1和word2都在图中 但不存在桥接词
    public void testQueryBridgeWords_BothWordsInGraph_NoBridge() {
        frontfun();
        String bridge = root.queryBridgeWords("She", "easygoing", root);

        assertEquals("No bridge words from She to easygoing!", bridge);
    }

    @Test // 测试word1和word2都不在图中
    public void testQueryBridgeWords_BothWordsNotInGraph() {
        frontfun();
        String bridge = root.queryBridgeWords("dog", "cat", root);

        assertEquals("No word1 or word2 in the graph!", bridge);
    }

    @Test // 测试word1在图中 word2不在图中
    public void testQueryBridgeWords_OneWordInGraph_OneWordNotInGraph() {
        frontfun();
        String bridge = root.queryBridgeWords("She", "cat", root);

        assertEquals("No bridge words from She to cat!", bridge);
    }

    @Test // word1或word2为null或空字符串。
    public void testQueryBridgeWords_Word1OrWord2IsNull() {
        frontfun();
        String bridge = root.queryBridgeWords("", "an", root);

        assertEquals("No bridge words from  to an!", bridge);
    }
}
