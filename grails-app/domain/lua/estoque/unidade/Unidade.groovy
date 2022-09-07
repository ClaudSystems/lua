package lua.estoque.unidade

import lua.estoque.lote.Lote

class Unidade implements Serializable{
    private static final long serialVersionUID = 1
    String codigo
    String nome
    int numeroDeElementos

    static  hasMany = [lotes:Lote]
    static constraints = {
        nome nullable: false
        numeroDeElementos nullable:false

}

static mapping = {
    id generator: 'increment'
}

public String toString() {
    return "${nome}"

}
}
