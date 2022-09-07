package lua.estoque.marca

import lua.estoque.modelo.Modelo

class Marca implements Serializable {
    private static final long serialVersionUID = 1
    String nome

    static hasMany = [modelos:Modelo]
    static mapping = {
        autoTimestamp true
        id generator: 'increment'
    }

    static constraints = {
    }

    public String toString() {
        return "${nome}";
    }
}
