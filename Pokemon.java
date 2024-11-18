import java.util.List;
/***
 * Classe para armazenar informações sobre instâncias de pokemon.
 */
public class Pokemon {
    private int id;
    private String nome;
    private List<String> tipos;
    private int nivel;
    private int pontosCombate;

    public Pokemon(int id, String nome, List<String> tipos, int nivel, int pontosCombate) {
        this.id = id;
        this.nome = nome;
        this.tipos = tipos;
        this.nivel = nivel;
        this.pontosCombate = pontosCombate;
    }
    public int getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public List<String> getTipos() {
        return this.tipos;
    }

    public int getNivel() {
        return this.nivel;
    }

    public int getPontosCombate() {
        return this.pontosCombate;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "numero=" + id +
                ", nome='" + nome + '\'' +
                ", tipos=" + tipos +
                ", nivel=" + nivel +
                ", pontosCombate=" + pontosCombate +
                '}';
    }
}
