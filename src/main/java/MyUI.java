import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyUI {
    private static Node[] nodeList;
     private static Node root;
    public static void main(String[] args) {
        //create a frame
        JFrame frame = new JFrame("樊强-2021111484-test1");
        //set default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window size
        frame.setSize(600, 300);
        frame.setVisible(true);

        //create a  panel
        JPanel panel = new JPanel();
        //布局管理器
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //create buttons and action listeners
        JButton button1 = new JButton("read txt and exchange to directed graph");
        button1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //read txt and exchange to directed graph
                //create a label
                //read txt and exchange to directed graph
                String filename = "/home/yinzi/Documents/Vscode_docu/Intellij_docu/software_lab1/src/test/resources.txt";
                String[] words = FileReadOneWord.readWords(filename);
                StringBuilder sb = new StringBuilder("<html>");
                //测试words是否读取成功
                for (String word : words) {
                    sb.append(word).append("<br/>");
                }
                nodeList = DirectedGraph.directGraph(words);
               // Node root = nodeList[0];
                sb.append("</html>");
                JLabel label = new JLabel(sb.toString());
                panel.add(label, gbc);

            }
        });
        //create buttons and action listeners show the directed graph
        JButton button2 = new JButton("show the directed graph");
        button2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){

                //show the directed graph
                try {
                    Graph.CreateDirectedGraph(nodeList);
                    Graph.showDirectedGraph();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        //create operation buttons3
        JButton button3 = new JButton("show bridge words");
        button3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //show bridge words
                root = nodeList[0];
                String bridge = root.queryBridgeWords("shy", "became", root);
                System.out.println(bridge);
            }
        });
        //create operation buttons4
        //according to new txt ---new bridge word and println
        JButton button4 = new JButton("according to new txt ---new bridge word and println");
        button4.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){

                String filename4 = "/home/yinzi/Documents/Vscode_docu/Intellij_docu/software_lab1/src/test/resources4.txt";
                String[] words4 = FileReadOneWord.readWords(filename4);
                String[] bridge4 = root.generateNewText(words4,root);
                //print the new text in a line
                for (String word : bridge4) {
                    System.out.print(word+" ");
                }
            }
        });

        //create operation button5
        //show the all min distance paths
        JButton button5 = new JButton("show the all min distance paths");
        button5.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                java.util.List<java.util.List<Node>> pathList = new ArrayList<>();
                java.util.List<Node> path = new ArrayList<>();
                String rewords2 = Node.calculateAllShortestPath("I","help",nodeList,path,pathList);
                System.out.println("All shortest path from To to and are: ");
                for (List<Node> p : pathList) {
                    for (Node node : p) {
                        System.out.print(node.name + " ");
                    }
                    System.out.println();
                }
            }
        });

        JButton button6= new JButton("random walk");
        button6.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String rewords3 = root.randomWalk("I",nodeList);
                System.out.println(rewords3);
            }
        });
        // Set alignment and add buttons with vertical strut for spacing
        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button1);
        panel.add(Box.createVerticalStrut(10)); // 10 pixels of vertical spacing

        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button2);
        panel.add(Box.createVerticalStrut(10));

        button3.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button3);
        panel.add(Box.createVerticalStrut(10));

        button4.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button4);
        panel.add(Box.createVerticalStrut(10));

        button5.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button5);
        panel.add(Box.createVerticalStrut(10));

        button6.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button6);
        frame.add(panel);

    }
}