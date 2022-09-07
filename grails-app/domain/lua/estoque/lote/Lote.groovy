package lua.estoque.lote

import lua.estoque.itemProduto.ItemProduto
import lua.estoque.modelo.Modelo
import lua.estoque.unidade.Unidade
import lua.estoque.produto.Produto

class Lote implements Serializable{
    private static final long serialVersionUID = 1

    String codigo
    Produto produto
    Modelo modelo
    Unidade unidade
    Date validade


    static  hasMany = [itemsProduto:ItemProduto]
    static mapping = {
        id generator: 'increment'
    }
    static constraints = {
        codigo nullable: false, unique: true
        produto nullable: false
        modelo nullable: true
        unidade nullable: true
        validade nullable: true


    }

    public String toString() {
        return "${codigo}";

    }
}
