import java.util.ArrayList;
import java.util.List;

public class AVLdex {
    private static final class Node {
        private Node parent;
        private Node left;
        private Node right;
        private Pokemon element;
        private int height;

        public Node(Pokemon element) {
            this.element = element;
            this.parent = this.left = this.right = null;
            this.height = 1;
        }
    }

    private int count;
    private Node root;

    public AVLdex() {
        count = 0;
        root = null;
    }

    /**
     * Métodos de busca
     */

    private Node searchNodeRef(int id, Node n) {
        if (n == null) return null;

        int c = Integer.compare(id, n.element.getId());
        if (c == 0) return n;
        if (c < 0) {
            return searchNodeRef(id, n.left);
        } else {
            return searchNodeRef(id, n.right);
        }
    }
    //Funcionalidade 2
    public Pokemon searchByPokedexNumber(int id) {
        Node node = searchNodeRef(id, root);
        if (node != null) {
            return node.element;
        }
        return null;
    }

    // Funcionalidade 4:
    public ArrayList<Pokemon> buscarPorTipoENivel(String tipo, int nivel) {
        ArrayList<Pokemon> resultado = new ArrayList<>();
        buscaLinear(root, tipo, nivel, resultado);
        return resultado;
    }

    private void buscaLinear(Node node, String tipo, int nivel, List<Pokemon> resultado) {
        if (node != null) {
            Pokemon aux = node.element;
            if (aux.getTipos().contains(tipo) && aux.getNivel() == nivel) {
                resultado.add(aux);
            }

            buscaLinear(node.left, tipo, nivel, resultado);
            buscaLinear(node.right, tipo, nivel, resultado);
        }
    }

    /**
     * Método para verificar se a AVL está vazia
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Métodos para adição de Pokemon na AVL
     */
    public void carregarCsv(String caminho) {
        List<Pokemon> tmp = PokemonUtils.lerPokemonsDoCSV(caminho);
        for(Pokemon p : tmp) {
            add(p);
        }
    }
    public void add(Pokemon pokemon) {
        Node newNode = new Node(pokemon);
        if (isEmpty()) {
            root = newNode;
        } else {
            addAux(newNode, root);
        }
        count++;
        balanceTree(newNode);
    }

    private void addAux(Node newNode, Node aux) {
        if (newNode.element.getId() <= aux.element.getId()) {
            if (aux.left == null) {
                aux.left = newNode;
                newNode.parent = aux;
                updateHeight(aux);
            } else {
                addAux(newNode, aux.left);
            }
        } else {
            if (aux.right == null) {
                aux.right = newNode;
                newNode.parent = aux;
                updateHeight(aux);
            } else {
                addAux(newNode, aux.right);
            }
        }
    }

    /**
     * Métodos para remoção de Pokemon na AVL
     */
    public void remove(int id) {
        Node nodeToRemove = searchNodeRef(id, root);
        if (nodeToRemove == null) return;

        removeAux(nodeToRemove);
        count--;
    }

    private void removeAux(Node node) {
        if (node.left == null && node.right == null) {
            if (node.parent == null) {
                root = null;
            } else if (node == node.parent.left) {
                node.parent.left = null;
            } else {
                node.parent.right = null;
            }
        } else if (node.left != null && node.right == null) {
            replaceNode(node, node.left);
        } else if (node.left == null && node.right != null) {
            replaceNode(node, node.right);
        } else {
            Node successor = findMin(node.right);
            node.element = successor.element;
            removeAux(successor);
        }

        balanceTree(node.parent);
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Métodos auxiliares para remover e adicionar Pokemon na AVL
     */

    private void replaceNode(Node oldNode, Node newNode) {
        if (oldNode.parent == null) {
            root = newNode;
        } else if (oldNode == oldNode.parent.left) {
            oldNode.parent.left = newNode;
        } else {
            oldNode.parent.right = newNode;
        }

        if (newNode != null) {
            newNode.parent = oldNode.parent;
        }
    }

    private void updateHeight(Node node) {
        int leftHeight = (node.left == null) ? 0 : node.left.height;
        int rightHeight = (node.right == null) ? 0 : node.right.height;
        node.height = 1 + Math.max(leftHeight, rightHeight);
    }

    private void balanceTree(Node node) {
        Node current = node;
        while (current != null) {
            updateHeight(current);
            int balanceFactor = getBalanceFactor(current);

            if (balanceFactor > 1) {
                if (getBalanceFactor(current.left) < 0) {
                    rotateLeft(current.left);
                }
                rotateRight(current);
            } else if (balanceFactor < -1) {
                if (getBalanceFactor(current.right) > 0) {
                    rotateRight(current.right);
                }
                rotateLeft(current);
            }

            current = current.parent;
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
        int leftHeight = (node.left == null) ? 0 : node.left.height;
        int rightHeight = (node.right == null) ? 0 : node.right.height;
        return leftHeight - rightHeight;
    }

    /**
     * Funcionalidade 3: Listagem por pontos de combate
     */

    public ArrayList<Pokemon> listarPokemonsOrdenadosPorPontosDeCombate() {

        ArrayList<Pokemon> lista = new ArrayList<>();
        ordemCrescente(root, lista);

        // Passo 2: Encontrar o maior valor de pontos de combate
        int maiorPontosDeCombate = 0;
        for (Pokemon p : lista) {
            maiorPontosDeCombate = Math.max(maiorPontosDeCombate, p.getPontosCombate());
        }

        // Passo 3: Criar as "listasAux" para ordenar
        ArrayList<ArrayList<Pokemon>> listaAux = new ArrayList<>(maiorPontosDeCombate + 1);
        for (int i = 0; i <= maiorPontosDeCombate; i++) {
            listaAux.add(new ArrayList<>());
        }

        // Passo 4: Distribuir os Pokémon nas listasAux
        for (Pokemon p : lista) {
            listaAux.get(p.getPontosCombate()).add(p);
        }

        // Passo 5: Recoletar os Pokémon ordenados
        ArrayList<Pokemon> ordenados = new ArrayList<>();
        for (int i = maiorPontosDeCombate; i >= 0; i--) { // Ordem decrescente
            ordenados.addAll(listaAux.get(i));
        }

        return ordenados; // Retorna a lista ordenada
    }

    // Método auxiliar para percorrer a AVL em ordem crescente
    private void ordemCrescente(Node node, ArrayList<Pokemon> lista) {
        if (node != null) {
            ordemCrescente(node.left, lista);  // Subárvore esquerda
            lista.add(node.element);          // Adiciona o Pokémon atual
            ordemCrescente(node.right, lista); // Subárvore direita
        }
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
