package lua.entidades.entidade

import lua.estoque.estoque.Armazem

class Entidade implements Serializable{
    private static final long serialVersionUID = 1
    String codigo
    String nome
    String nuit
    String mail
    String numTelefone
    String endereco
    String nomeDoBanco
    byte[] cm

    static hasMany = [armazens: Armazem]

    static mapping = {
        autoTimestamp true
        id generator: 'increment'
    }

    static constraints = {
        codigo nullable: false, unique: true
        nome nullable: false
        nuit nullable: false, unique: true
        mail nullable: true, email: true
        numTelefone nullable: true
        endereco nullable: true
        nomeDoBanco nullable: true

    }


    public String toString() {
        return "${nome}"

    }
}
