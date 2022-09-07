package lua.periodo

class Periodo implements Serializable{
    private static final long serialVersionUID = 1
    String descricao
    Date inicio
    Date fim


    static constraints = {
    }
    static mapping = {
        id generator: 'increment'
    }

    public String toString() {
        return "${descricao}";

    }
}
