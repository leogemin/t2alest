public class App {

    private BinaryTree pokemons;

    public App() {
        this.pokemons = new BinaryTree();
    }

    public void executar() {
        pokemons.carregarCsv("pokemons.csv");
        System.out.println(pokemons.buscarPorTipoENivel("Grama", 5));
    }
    
}
