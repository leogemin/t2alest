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
        root = removeAux(root, element);
    }

    private Node removeAux(Node node, Integer element) {
        if (node == null) {
            return null; // Elemento não encontrado
        }

        if (element < node.element) {
            node.left = removeAux(node.left, element); // Continua buscando na subárvore esquerda
        } else if (element > node.element) {
            node.right = removeAux(node.right, element); // Continua buscando na subárvore direita
        } else {
            // Encontramos o nó a ser removido
            if (node.left == null && node.right == null) {
                // Caso 1: Nó folha
                return null;
            } else if (node.left == null) {
                // Caso 2: Apenas um filho (direita)
                Node temp = node.right;
                temp.parent = node.parent; // Atualiza o pai
                return temp;
            } else if (node.right == null) {
                // Caso 2: Apenas um filho (esquerda)
                Node temp = node.left;
                temp.parent = node.parent; // Atualiza o pai
                return temp;
            } else {
                // Caso 3: Dois filhos
                Node successor = getMin(node.right); // Menor valor na subárvore direita
                node.element = successor.element; // Substitui o valor do nó pelo do sucessor
                node.right = removeAux(node.right, successor.element); // Remove o sucessor
            }
        }

        // Atualiza a altura do nó atual após a remoção
        updateHeight(node);

        // Balanceia a árvore a partir deste nó
        return balanceTreeAfterRemoval(node); // Ajusta o balanceamento
    }


    private Node balanceTreeAfterRemoval(Node node) {
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) { // Desbalanceado para a esquerda
            if (getBalanceFactor(node.left) < 0) {
                // Rotação dupla (esquerda-direita)
                rotateLeft(node.left);
            }
            // Rotação simples à direita
            rotateRight(node);
        } else if (balanceFactor < -1) { // Desbalanceado para a direita
            if (getBalanceFactor(node.right) > 0) {
                // Rotação dupla (direita-esquerda)
                rotateRight(node.right);
            }
            // Rotação simples à esquerda
            rotateLeft(node);
        }

        return node; // Retorna o nó balanceado
    }


    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
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
