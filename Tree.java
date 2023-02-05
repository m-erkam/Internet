import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Tree {     // Constructor for tree
    private Node root;
    public boolean isAvl;      // A data field to adjust the avl property

    public Tree(boolean avl){
        this.root = new Node();
        isAvl = avl;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int nodeHeight(Node node){
        if(node == null){
            return -1;
        }else{
            return node.height;
        }
    }

    private Node findMin(Node rootSubtree){     // Find min method traverses to the leftmost child and returns it
        if(rootSubtree.getLeft() == null){
            return rootSubtree;
        }else{
            return findMin(rootSubtree.getLeft());
        }
    }

    public Node addNode(Node newNode, FileWriter file) throws IOException {     // Add method
        return addNode(newNode, root, file);
    }

    private Node addNode(Node newNode, Node place, FileWriter file) throws IOException {
        if(place == null){  // If it reaches null node, it places the new node
            return newNode;
        }

        if(newNode.getIP().compareTo(place.getIP()) < 0){   // It traverses by comparing its ips and decides which child to go
            file.write(place.getIP() + ": New node being added with IP:" + newNode.getIP() + "\n");
            place.setLeft(addNode(newNode, place.getLeft(), file));

        }else if(newNode.getIP().compareTo(place.getIP()) > 0){
            file.write(place.getIP() + ": New node being added with IP:" + newNode.getIP() + "\n");
            place.setRight(addNode(newNode, place.getRight(), file));

        }
        if(isAvl){  // If the tree is avl, it balances it
            return balance(place, file);
        }else{
            return place;
        }
    }

    public Node deleteNode(String IP, FileWriter file) throws IOException { // Delete method to remove the node
        return deleteNode(IP, root, root, false, file);
    }


    private Node deleteNode(String IP, Node node, Node parent, boolean non_leaf_deleted, FileWriter file) throws IOException {
        if(node == null){
            return node;
        }

        if(IP.compareTo(node.getIP()) < 0){     // To find the node to be deleted, it compares the ips and traverses the tree
            node.setLeft(deleteNode(IP, node.getLeft(), node, non_leaf_deleted, file));

        }else if(IP.compareTo(node.getIP()) > 0){
            node.setRight(deleteNode(IP, node.getRight(), node, non_leaf_deleted, file));

        }else if(node.getLeft() != null && node.getRight() != null && node != root){    // If the node has two childs
            file.write(parent.getIP() + ": Non Leaf Node Deleted; removed: " + node.getIP() + " replaced: "
                    + findMin(node.getRight()).getIP() + "\n");     // It prints the log, and changes it with right subtree's leftmost child
            non_leaf_deleted = true;
            node.setIP(findMin(node.getRight()).getIP());           // And it deletes the changed node

            node.setRight(deleteNode(node.getIP(), node.getRight(), parent, non_leaf_deleted, file));


        }else if(node.getRight() != null && node != root){  // If node has one child, it changes only its one child and prints log
            if(!non_leaf_deleted){
                file.write(parent.getIP() + ": Node with single child Deleted: " + node.getIP() + "\n");
            }
            node = node.getRight();

        }else if(node.getLeft() != null && node != root){
            if(!non_leaf_deleted){
                file.write(parent.getIP() + ": Node with single child Deleted: " + node.getIP() + "\n");
            }
            node = node.getLeft();

        }else{
            if(!non_leaf_deleted && node != root){      // If the node has no child, it directly deletes it
                file.write(parent.getIP() + ": Leaf Node Deleted: " + node.getIP() + "\n");
            }
            node = null;
        }

        parent = node;
        if(isAvl){      // If the tree is avl, it checks the avl property
            return balance(node, file);
        }else{

            return node;
        }

    }

    private Node balance(Node node, FileWriter file) throws IOException { // Balance method to keep the avl property
        if(node == null){
            return node;
        }

        if(nodeHeight(node.getLeft()) - nodeHeight(node.getRight()) > 1){   // It compares the heights of subtrees and finds the place where the balance is broken
            if(nodeHeight(node.getLeft().getLeft()) >= nodeHeight(node.getLeft().getRight())){
                node = rightRotation(node);
                file.write("Rebalancing: right rotation" + "\n");   // If it has its left subtrees left imbalance it rotates it to the right

            }else{
                node = leftRightRotation(node);     // If it has its left subtrees right imbalance it applies left right rotation
                file.write("Rebalancing: left-right rotation"+ "\n");
            }

        }else if(nodeHeight(node.getRight()) - nodeHeight(node.getLeft()) > 1){     // If it has its right subtrees right imbalance it rotates it to the left
            if(nodeHeight(node.getRight().getRight()) >= nodeHeight(node.getRight().getLeft())){
                node = leftRotation(node);
                file.write("Rebalancing: left rotation" + "\n");
            }else{
                node = rightLeftRotation(node);     // If it has its right subtrees left imbalance it applies right left rotation
                file.write("Rebalancing: right-left rotation"+ "\n");
            }
        }
        node.height = Math.max(nodeHeight(node.getRight()), nodeHeight(node.getLeft())) + 1; // And arranges the heights
        return node;
    }

    private Node rightRotation(Node node2){     // In right rotation, it takes the node which is the root of unbalanced subtree
        Node node1 = node2.getLeft();           // And sets the right child of this node to itself
        node2.setLeft(node1.getRight());
        node1.setRight(node2);
        node2.height = Math.max(nodeHeight(node2.getRight()), nodeHeight(node2.getLeft())) + 1;
        node1.height = Math.max(nodeHeight(node1.getRight()), nodeHeight(node1.getLeft())) + 1;
        if(node2 == root){
            root = node1;
        }
        return node1;
    }

    private Node leftRotation(Node node1){      // Similar process, but towards the left
        Node node2 = node1.getRight();
        node1.setRight(node2.getLeft());
        node2.setLeft(node1);
        node1.height = Math.max(nodeHeight(node1.getRight()), nodeHeight(node1.getLeft())) + 1;
        node2.height = Math.max(nodeHeight(node2.getRight()), nodeHeight(node2.getLeft())) + 1;
        if(node1 == root){
            root = node2;
        }

        return node2;
    }

    private Node leftRightRotation(Node node3){     // It first applies left, then right rotatiton
        node3.setLeft(leftRotation(node3.getLeft()));
        return rightRotation(node3);
    }

    private Node rightLeftRotation(Node node1){     // It first applies right, then left rotatiton
        node1.setRight(rightRotation(node1.getRight()));
        return leftRotation(node1);
    }

    private ArrayList<String> path(String IP, Node traversing, ArrayList<String> path){ // To help the send method, path
                                                                                        // method finds the path from the root to the node
        int comparedVal = IP.compareTo(traversing.getIP());

        if(comparedVal != 0){
            path.add(traversing.getIP());
            if(comparedVal > 0){
                path(IP, traversing.getRight(), path);
            }else{
                path(IP, traversing.getLeft(), path);
            }
        }else{
            path.add(traversing.getIP());
        }
        return path;
    }

    public void search(String senderIP, String receiverIP, FileWriter file) throws IOException {
        file.write(senderIP + ": Sending message to: "+ receiverIP + "\n");
        ArrayList<String> path1 = new ArrayList<>();
        ArrayList<String> path2 = new ArrayList<>();
        ArrayList<String> pathSender = path(senderIP, root, path1);     // In this method, it first find the paths
        ArrayList<String> pathReceiver = path(receiverIP, root, path2);

        int size1 = pathSender.size();
        int size2 = pathReceiver.size();

        int startingIndex = 0;


        if(!(Math.min(size1, size2) < 2)){          // Then according the their first common root, it changes the starting index
            for (int i = 1; i < Math.min(size1, size2); i++) {
                if(!pathSender.get(i).equals(pathReceiver.get(i))){
                    startingIndex = i-1;
                    break;
                }else if(i == Math.min(size1,size2)-1 && size2 > size1){
                    startingIndex = i;
                }else if(i == Math.min(size1,size2)-1 && size2 < size1){
                    startingIndex = i;
                }
            }
        }

        for (int i = 0; i < size1 - startingIndex - 1; i++) {   // And it logs the nodes from the sender path until the reach the common root
            if(i >= size1-1){
                break;
            }
            String lastIP = path1.get(size1-i-1);
            String previousIP = path1.get(size1-i-2);
            if(!(previousIP.equals(receiverIP))){
                file.write(previousIP +
                        ": Transmission from: "+ lastIP +
                        " receiver: "+ receiverIP +
                        " sender:" + senderIP + "\n");
            }
        }

        for (int i = startingIndex; i < size2; i++) {       // Then logs from the common root to the receiver
            if(i >= size2-1){                               // It handles the edge cases time to time
                if(path2.get(i).equals(path2.get(size2-1))){
                    file.write(receiverIP + ": Received message from: "+ senderIP + "\n");
                    break;
                }
                break;
            }

            String firstIP = path2.get(i);
            String nextIP = path2.get(i+1);
            if(receiverIP.equals(nextIP)){
                file.write(receiverIP + ": Received message from: "+ senderIP + "\n");
                break;
            }
            file.write(nextIP +
                    ": Transmission from: "+ firstIP +
                    " receiver: "+ receiverIP +
                    " sender:" + senderIP + "\n");
        }
    }
}
