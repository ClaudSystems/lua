package lua.estoque.modelo

import lua.estoque.lote.Lote
import lua.estoque.marca.Marca

class Modelo implements Serializable{
    private static final long serialVersionUID = 1
    String descricao
    Marca marca

    static hasMany = [lotes:Lote]
    static mapping = {
        autoTimestamp true
        id generator: 'increment'
    }
    static constraints = {
        marca nullable: false
        descricao nullable: false

    }

    public String toString() {
        return "${descricao}";

    }
}
