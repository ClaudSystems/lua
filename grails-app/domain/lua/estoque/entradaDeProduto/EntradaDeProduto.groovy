package lua.estoque.entradaDeProduto

import lua.entidades.fornecedor.Fornecedor
import lua.estoque.itemProduto.ItemProduto
import lua.security.Utilizador

class EntradaDeProduto implements Serializable {
    private static final long serialVersionUID = 1
    Long	id
    String numeroDeEntrada
    BigDecimal valor
    BigDecimal valorDoIva
    Utilizador utilizador
    Date	dateCreated
    Date	lastUpdated
    Fornecedor fornecedor

    static hasMany = [itemsProduto:ItemProduto]
    static mapping = {
        id generator: 'increment'
    }
    static constraints = {
        utilizador nullable: false
        fornecedor nullable: true
        valorDoIva nullable: true

       }


    public String toString() {
        return "${id}"

    }
}
