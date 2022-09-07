package lua.estoque.categoria
import lua.estoque.produto.Produto
import org.zkoss.zul.Image


class Categoria implements Serializable{
    private static final long serialVersionUID = 1
    String codigo
   String nome
    Image image
    String color
    private List<Categoria> children = new LinkedList<Categoria>()


    static hasMany = [produtos:Produto]
    static mapping = {
        id generator: 'increment'
        produtos lazy: false

    }
    static constraints = {
        codigo nullable: false, unique: true
        nome nullable: false, unique: true
        produtos nullable: true
        image nullable: true
        color nullable: true

    }

    public String toString() {
        return "${nome}"

    }
}
