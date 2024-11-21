public class BinaryTree {
    private static final class Node {

        private Node parent;
        private Node left;
        private Node right;
        private Integer  element;
        private Integer height;


        public Node(Integer element) {
            this.element = element;
            parent = left = right = null;
            height = 1;
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

    public boolean isEmpty() {
        if (root == null){
            return true;
        }
        return false;
    }

    public void add(Integer element) {
        Node newNode = new Node(element);
        if (isEmpty()){
            root = newNode;
        }
        else{
            addAux(newNode, root);
        }
        count++;
        balanceTree(newNode);
    }

    private void addAux(Node newNode, Node aux) {
        if (newNode.element <= aux.element){
            if(aux.left == null){
                aux.left = newNode;
                newNode.parent = aux;
                updateHeight(aux);
            }
            else addAux(newNode, aux.left);
        }
        else {
            if(aux.right == null){
                aux.right = newNode;
                newNode.parent = aux;
                updateHeight(aux);
            }
            else addAux(newNode, aux.right);
        }
    }

    private void updateHeight(Node aux) {
        int leftHeight = 0;
        int rightHeight = 0;

        // Verifica se o nó à esquerda existe e, caso exista, usa sua altura
        if (aux.left != null) {
            leftHeight = aux.left.height;
        }

        // Verifica se o nó à direita existe e, caso exista, usa sua altura
        if (aux.right != null) {
            rightHeight = aux.right.height;
        }

        // Atualiza a altura do nó atual com o valor máximo entre os filhos + 1
        aux.height = 1 + Math.max(leftHeight, rightHeight);
    }

    private void balanceTree(Node node) {
        Node current = node;
        while (current != null) {
            updateHeight(current); // Atualiza a altura de cada nó enquanto sobe até a raiz
            int balanceFactor = getBalanceFactor(current); // Calcula o fator de balanceamento

            // Se o fator de balanceamento for maior que 1, ou menor que -1, a árvore está desbalanceada
            if (balanceFactor > 1) {
                if (getBalanceFactor(current.left) < 0) {
                    rotateLeft(current.left); // Rotação dupla (esquerda-direita)
                }
                rotateRight(current); // Rotação simples à direita
            }
            else if (balanceFactor < -1) {
                if (getBalanceFactor(current.right) > 0) {
                    rotateRight(current.right); // Rotação dupla (direita-esquerda)
                }
                rotateLeft(current); // Rotação simples à esquerda
            }

            current = current.parent; // Sobe até o nó pai
        }
    }

    private void rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        if (newRoot.left != null) {
            newRoot.left.parent = node;
        }
        newRoot.left = node;
        newRoot.parent = node.parent;
        if (node.parent == null) {
            root = newRoot;
        } else if (node == node.parent.left) {
            node.parent.left = newRoot;
        } else {
            node.parent.right = newRoot;
        }
        node.parent = newRoot;

        updateHeight(node);
        updateHeight(newRoot);
    }

    private void rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        if (newRoot.right != null) {
            newRoot.right.parent = node;
        }
        newRoot.right = node;
        newRoot.parent = node.parent;
        if (node.parent == null) {
            root = newRoot;
        } else if (node == node.parent.right) {
            node.parent.right = newRoot;
        } else {
            node.parent.left = newRoot;
        }
        node.parent = newRoot;

        updateHeight(node);
        updateHeight(newRoot);
    }


    private int getBalanceFactor(Node node) {
        int leftHeight = 0;
        int rightHeight = 0;

        if (node.left != null) {
            leftHeight = node.left.height;
        } else {
            leftHeight = 0;
        }

        if (node.right != null) {
            rightHeight = node.right.height;
        } else {
            rightHeight = 0;
        }

        return leftHeight - rightHeight;
    }

    //Método de romoção abaixo:

    public void remove(Integer element) {
        Node nodeToRemove = searchNodeRef(element, root);
        if (nodeToRemove == null) {
            System.out.println("Elemento não encontrado.");
            return; // Elemento não está na árvore
        }
        removeNode(nodeToRemove);
        count--;
    }

    private void removeNode(Node node) {
        // Caso 1: Nó é folha
        if (node.left == null && node.right == null) {
            if (node.parent == null) {
                root = null; // Árvore fica vazia
            } else {
                if (node.parent.left == node) {
                    node.parent.left = null;
                } else {
                    node.parent.right = null;
                }
            }
            balanceTree(node.parent);
        }
        // Caso 2: Nó tem apenas um filho
        else if (node.left == null || node.right == null) {
            Node child = (node.left != null) ? node.left : node.right;
            if (node.parent == null) {
                root = child;
            } else {
                if (node.parent.left == node) {
                    node.parent.left = child;
                } else {
                    node.parent.right = child;
                }
            }
            child.parent = node.parent;
            balanceTree(child.parent);
        }
        // Caso 3: Nó tem dois filhos
        else {
            Node successor = getMin(node.right);
            node.element = successor.element; // Substitui o valor do nó
            removeNode(successor); // Remove o sucessor
        }
    }

    private Node getMin(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }








    // Gera uma saida no formato DOT
    // Esta saida pode ser visualizada no GraphViz
    // Versoes online do GraphViz pode ser encontradas em
    // http://www.webgraphviz.com/
    // http://viz-js.com/
    // https://dreampuf.github.io/GraphvizOnline

    private void GeraConexoesDOT(Node nodo) {
        if (nodo == null) {
            return;
        }

        GeraConexoesDOT(nodo.left);
        //   "nodeA":esq -> "nodeB" [color="0.650 0.700 0.700"]
        if (nodo.left != null) {
            System.out.println("\"node" + nodo.element + "\":esq -> \"node" + nodo.left.element + "\" " + "\n");
        }

        GeraConexoesDOT(nodo.right);
        //   "nodeA":dir -> "nodeB";
        if (nodo.right != null) {
            System.out.println("\"node" + nodo.element + "\":dir -> \"node" + nodo.right.element + "\" " + "\n");
        }
        //"[label = " << nodo->hDir << "]" <<endl;
    }

    private void GeraNodosDOT(Node nodo) {
        if (nodo == null) {
            return;
        }
        GeraNodosDOT(nodo.left);
        //node10[label = "<esq> | 10 | <dir> "];
        System.out.println("node" + nodo.element + "[label = \"<esq> | " + nodo.element + " | <dir> \"]" + "\n");
        GeraNodosDOT(nodo.right);
    }

    public void GeraConexoesDOT() {
        GeraConexoesDOT(root);
    }

    public void GeraNodosDOT() {
        GeraNodosDOT(root);
    }


    public void GeraDOT() {
        System.out.println("digraph g { \nnode [shape = record,height=.1];\n" + "\n");

        GeraNodosDOT();
        System.out.println("");
        GeraConexoesDOT(root);
        System.out.println("}" + "\n");
    }
}
