public class Node {
    private String IP;
    private Node right;
    private Node left;
    public int height;

    public Node(){  // Node constructor
        IP = "";
        right = null;
        left = null;
        height = 0;
    }

    public Node(String IP){ // Setter and getter methods
        this.IP = IP;
        height = 0;
    }

    public String getIP(){
        return IP;
    }

    public void setIP(String IP){
        this.IP = IP;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

}
