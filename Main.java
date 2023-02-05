import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(args[0]);     // It takes input file
        File outputBST =new File(args[1]+ "_BST.txt");  // Creates 2 files to write avl and bst outputs separately
        File outputAVL =new File(args[1]+ "_AVL.txt");
        Scanner readFile1;
        Scanner readFile2;
        Tree tree1 = new Tree(false);       // Creates two trees, one of them is avl and the other is bst
        Tree tree2 = new Tree(true);
        try{
            FileWriter outputFileBST = new FileWriter(outputBST);
            readFile1 = new Scanner(inputFile);
            Node root1 = new Node(readFile1.nextLine());
            tree1.setRoot(root1);
            while (readFile1.hasNextLine()){    // While there is an upcoming input, it continues to read
                String[] inputs = readFile1.nextLine().split(" ");
                switch (inputs[0]) {    // By looking the inputs, it makes the operations
                    case "ADDNODE" -> tree1.addNode(new Node(inputs[1]), outputFileBST);
                    case "DELETE" -> tree1.deleteNode(inputs[1], outputFileBST);
                    case "SEND" -> tree1.search(inputs[1], inputs[2], outputFileBST);
                }
            }
            outputFileBST.close();

            readFile2 = new Scanner(inputFile);
            Node root2 = new Node(readFile2.nextLine());
            FileWriter outputFileAVL = new FileWriter(outputAVL);
            tree2.setRoot(root2);
            while (readFile2.hasNextLine()){
                String[] inputs = readFile2.nextLine().split(" ");
                switch (inputs[0]) {
                    case "ADDNODE" -> tree2.addNode(new Node(inputs[1]), outputFileAVL);
                    case "DELETE" -> tree2.deleteNode(inputs[1], outputFileAVL);
                    case "SEND" -> tree2.search(inputs[1], inputs[2], outputFileAVL);
                }
            }
            outputFileAVL.close();

        } catch (FileNotFoundException ex1){
            System.out.println(ex1.getMessage());
        }
    }
}
