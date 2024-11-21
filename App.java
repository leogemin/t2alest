import java.util.List;

public class App {
    public static void main(String[] args) {

//        String caminhoCSV = "pokemons.csv";
//        List<Pokemon> pokemons = PokemonUtils.lerPokemonsDoCSV(caminhoCSV);
//        for (Pokemon pokemon : pokemons) {
//            System.out.println(pokemon);
//
//        }

        BinaryTree tree = new BinaryTree();
        tree.add(50);
        tree.add(40);
        tree.add(30);
        tree.add(20);
        tree.add(10);

        tree.remove(20);
        tree.remove(50);

        System.out.println("---------------------");
        tree.GeraDOT();
    }
}
