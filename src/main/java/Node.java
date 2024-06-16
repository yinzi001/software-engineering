import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;



//Node结点class
public class Node {
    public String name; //word name
    public int number; //word value
    ArrayList<Node> children = new ArrayList<>(); //children of the node

    //method
    //new node
    public Node(String name, int number) {
        this.name = name;
        this.number = number;
    }

    //add child to the node
    public void addChild(Node child) {
        children.add(child);
    }

    //search the map  考虑成环，需要设置一个集合来维护已访问的结点
    public Node searchNode(Node node, String name, Set<Node> visited) {
        if (node == null || visited.contains(node)) {
            return null;
        }
        visited.add(node);
        if (node.name.equals(name)) {
            return node;
        }
        for (Node child : node.children) {
            Node found = searchNode(child, name, visited);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public Node searchNodeByNumber(Node node, int number, Set<Node> visited) {
        if (node == null || visited.contains(node)) {
            return null;
        }
        visited.add(node);
        if (node.number == number) {
            return node;
        }
        for (Node child : node.children) {
            Node found = searchNodeByNumber(child, number, visited);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public String queryBridgeWords(String word1, String word2, Node root) {
        Set<Node> visited1 = new HashSet<>();
        Node node1 = new Node(null, -1);
        node1 = node1.searchNode(root, word1, visited1);
        Set<Node> visited2 = new HashSet<>();
        Node node2 = new Node(null, -1);
        node2 = node2.searchNode(root, word2, visited2);

        StringBuilder bridgeWords = new StringBuilder();
        if (node1 != null || node2 != null) {
            //判断node1的子节点的子节点为node2或者node2的子节点的子节点为node1,打印所有bridge words
            if (node1 != null && node2 != null) {
                for (Node child1 : node1.children) {
                    for (Node child11 : child1.children) {
                        if (child11.name.equals(node2.name)) {
                            bridgeWords.append(child1.name).append(" ");
                        }
                    }
                }
            }
            if (node2 != null && node1 != null) {
                for (Node child2 : node2.children) {
                    for (Node child22 : child2.children) {
                        if (child22.name.equals(node1.name)) {
                            bridgeWords.append(child2.name).append(" ");
                        }
                    }
                }
            }
        } else {
            return "No word1 or word2 in the graph!";
        }
        if (!bridgeWords.isEmpty()) {
            return "Bridge words from " + word1 + " to " + word2 + " are: " + bridgeWords.toString();
        } else {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        }
    }

    public String[] generateNewText(String[] words, Node root) {
        ArrayList<String> newWords = new ArrayList<>();
        newWords.add(words[0]);
        for (int i = 0; i < words.length - 1; i++) {
            String bridge = queryBridgeWords(words[i], words[i + 1], root);
            if (!bridge.equals("No bridge words from " + words[i] + " to "
                    + words[i + 1] + "!") && !bridge.equals("No word1 or word2 in the graph!")) {
                String[] parts = bridge.split(":");
                newWords.add(parts[1].trim());
            }
            newWords.add(words[i + 1]);
        }
        return newWords.toArray(new String[0]);
    }

    static String calculateAllShortestPath(
            String word1, String word2, Node[] nodeList, List<Node> path, List<List<Node>> pathList) {
        Node node1 = null, node2 = null;
        for (Node node : nodeList) {
            if (node.name.equals(word1)) {
                node1 = node;
            }
            if (node.name.equals(word2)) {
                node2 = node;
            }
        }
        if (node1 == null || node2 == null) {
            return "No word1 or word2 in the graph!";
        }
        path.add(node1);
        if (node1 == node2) {
            pathList.add(new ArrayList<>(path));

        } else {
            for (Node child : node1.children) {
                if (!path.contains(child)) {
                    calculateAllShortestPath(child.name, word2, nodeList, path, pathList);
                }
            }
        }
        path.remove(path.size() - 1);
        return "success";
    }

    //random walk without stop keyboard
    public String randomWalk(String word, Node[] nodeList) {
        Node node = null;
        for (Node n : nodeList) {
            if (n.name.equals(word)) {
                node = n;
            }
        }
        if (node == null) {
            return "No such word in the graph!";
        }
        //new txt file
        File file = new File(
                "src/test/resources6.txt");
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to open file for writing";
        }
        //untraveled vertex and untraveled edge
        Set<Node> untraveled = new HashSet<>();
        //格式：word1 word2
        Set<String> untraveledEdge = new HashSet<>();

        //random traverse
        Random random = new Random();
        System.out.println("randomwalk：");
        try {
            while (!node.children.isEmpty()) {
                //判断是否已经访问过边和点，如果访问过，就stop
                if (untraveled.contains(node)) {
                    break;
                } else {
                    untraveled.add(node);
                }
                int index = random.nextInt(node.children.size());
                Node nextnode = node.children.get(index);

                if (untraveledEdge.contains(node.name + " " + nextnode.name)) {
                    break;
                } else {
                    untraveledEdge.add(node.name + " " + nextnode.name);
                }

                node = nextnode;
                System.out.println(node.name);
                fileWriter.write(node.name + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to write to file";
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed to close file";
            }

        }
        return "success";
    }


}


class FileReadOneWord {

    static String[] readWords(String filename) {
        List<String> wordList = new ArrayList<>();
        try {
            File file = new File(filename);
            FileReader fr = new FileReader(file); // 创建一个 FileReader 实例
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String cleanedLine = line.replaceAll("[\\r\\n]+", "");

                cleanedLine = cleanedLine.replaceAll("[^a-zA-Z]", " ");
                cleanedLine = cleanedLine.trim();
                String[] words = cleanedLine.split("\\s+"); // 使用空格作为分隔符，将该行分割成单词
                for (String word : words) { // 遍历分割后的单词数组

                    System.out.println(word); // 输出每个单词

                    wordList.add(word);
                }
            }
            br.close(); // 关闭 BufferedReader
            fr.close(); // 关闭 FileReader
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return wordList.toArray(new String[0]);
    }
}

class DirectedGraph {
    static Node[] directGraph(String[] words) {
        if (words.length == 0) {
            return null;
        }
        Map<String, Node> nodeMap = new HashMap<>();

        List<Node> nodeList = new ArrayList<>();
        Node root = new Node(words[0], 0);
        Node parentnode = root;
        nodeMap.put(words[0], root);
        nodeList.add(root);
        for (int i = 1; i < words.length; i++) {
            String word = words[i];
            Node node = nodeMap.get(word);
            if (node == null) {
                node = new Node(word, i);
                nodeMap.put(word, node);
                nodeList.add(node);
            }
            parentnode.addChild(node);
            parentnode = node;
        }
        return nodeList.toArray(new Node[0]);
    }
}

class Graph {
    static DefaultDirectedGraph<String, DefaultEdge> graph;

    static void CreateDirectedGraph(Node[] nodeList) {
        if (nodeList == null) {
            return;
        }
        //String是节点的类型，DefaultEdge是边的类型
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        //添加节点
        for (Node node : nodeList) {
            graph.addVertex(node.name);
        }
        //添加边
        for (Node node : nodeList) {
            for (Node child : node.children) {
                graph.addEdge(node.name, child.name);
            }
        }
    }

    static void showDirectedGraph() throws IOException {
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(graph);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        BufferedImage image = mxCellRenderer.createBufferedImage(
                graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File(
                "src/test/graph2.png");
        if (!imgFile.getParentFile().exists()) {
            imgFile.getParentFile().mkdirs();
        }
        if (graph == null) {
            System.err.println("Graph object is null!");
            return;
        }
        ImageIO.write(image, "PNG", imgFile);

    }

    static String calculateShortestPath(
            String word1, String word2, Node[] nodeList, DefaultDirectedGraph<String, DefaultEdge> graph) {
        Node node1 = null, node2 = null;
        for (Node node : nodeList) {
            if (node.name.equals(word1)) {
                node1 = node;
            }
            if (node.name.equals(word2)) {
                node2 = node;
            }
        }
        if (node1 == null || node2 == null) {
            return "No word1 or word2 in the graph!";
        }
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(node1.name, node2.name).getVertexList();

        return "The shortest path from " + word1 + " to " + word2 + " is: " + shortestPath.toString();
    }


}


class Main {
    public static void main(String[] args) {

        String filename = "src/test/resources.txt";
        String[] words = FileReadOneWord.readWords(filename);
        //测试words是否读取成功

        for (String word : words) {
            System.out.println(word);
        }
        Node[] nodeList = DirectedGraph.directGraph(words);
        Node root = nodeList[0];
        //测试root是否构建成功
        //bridge test
        String bridge = root.queryBridgeWords("new", "and", root);
        System.out.println(bridge);
        //graph test
        try {
            Graph.CreateDirectedGraph(nodeList);
            Graph.showDirectedGraph();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //operation 4
        //according to new txt ---new bridge word and println
        String filename4 = "src/test/resources4.txt";
        String[] words4 = FileReadOneWord.readWords(filename4);
        String[] bridge4 = root.generateNewText(words4, root);
        //print the new text in a line
        for (String word : bridge4) {
            System.out.print(word + " ");
        }
        //operation 5
        //words1 and words2 min distance
        String rewords = Graph.calculateShortestPath("To", "and", nodeList, Graph.graph);
        System.out.println(rewords);

        List<List<Node>> pathList = new ArrayList<>();
        List<Node> path = new ArrayList<>();
        String rewords2 = Node.calculateAllShortestPath("To", "and", nodeList, path, pathList);
        System.out.println("All shortest path from To to and are: ");
        for (List<Node> p : pathList) {
            for (Node node : p) {
                System.out.print(node.name + " ");
            }
            System.out.println();
        }
        //operation 6
        //random walk
        String rewords3 = root.randomWalk("To", nodeList);
        System.out.println(rewords3);
    }
}


