public class BinaryTree {
    private static final class Node {

        public Node parent;
        public Node left;
        public Node right;
        public Integer element;

        public Node(Integer element) {
            this.element = element;
            parent = left = right = null;
        }
    }

    // Atributos
    private int count; //contagem do número de nodos
    private Node root; //referência para o nodo raiz

    public BinaryTree() {
        count = 0;
        root = null;
    }

    private Node searchNodeRef(Integer element, Node n) {
        if (element == null || n == null)
            return null;
        int c = n.element.compareTo(element);
        if (c==0)
            return n;
        if (c > 0) {
            return searchNodeRef(element, n.left);
        }
        else {
            return searchNodeRef(element, n.right);
        }
    }
}
