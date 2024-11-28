public class App {

    private AVLdex pokemons;

    public App() {
        this.pokemons = new AVLdex();
    }

    public void executar() {
        pokemons.carregarCsv("pokemons.csv");
        System.out.println(pokemons.buscarPorTipoENivel("Grama", 5));
    }
    
}
