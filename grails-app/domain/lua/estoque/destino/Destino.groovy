package lua.estoque.destino

class Destino implements Serializable{
    private static final long serialVersionUID = 1

    String codigo
    String nome
    String descricao
    String estado

    static constraints = {
        descricao nullable: true
        codigo nullable: false
        nome nullable: false
        estado nullable: true , inList: ["excluido","ativo"]
    }

    static mapping = {
        autoTimestamp true
        id generator: 'increment'
    }
    public String toString() {
        return "${nome}"
    }


}
