package lua.estoque.estoque

import lua.entidades.entidade.Entidade
import lua.periodo.Periodo
import lua.estoque.produto.Produto

class Estoque implements Serializable{
    private static final long serialVersionUID = 1

    String codigo
    String nome
    Periodo periodo
    Entidade entidade
    boolean fechado

    static hasMany = [produtos:Produto]
    static belongsTo = Produto
    static mapping = {
        id generator: 'increment'
    }
    static constraints = {
        codigo nullable: false, unique: true
        nome nullable: false, unique:  true
        periodo nullable: true, unique: true
        entidade nullable: true
        produtos nullable: true

    }

    public String toString() {
        return "${id+"-"+nome}"

    }
}
