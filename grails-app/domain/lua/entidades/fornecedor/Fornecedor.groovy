package lua.entidades.fornecedor

class Fornecedor implements Serializable {
    private static final long serialVersionUID = 1

    String codigo
    String nome
    String nuit
    String mail
    String numTelefone
    String endereco


    static mapping = {
        autoTimestamp true
        id generator: 'increment'
    }

    static constraints = {
        codigo nullable: false, unique: true
        nome nullable: false
        nuit nullable: true, unique: false
        mail nullable: true, email: true
        numTelefone nullable: true
        endereco nullable: true
    }


    public String toString() {
        return "${nome}";

    }

}
