package lua.estoque.saidaDeProduto

import lua.entidades.cliente.Cliente
import lua.estoque.destino.Destino
import lua.estoque.itemProduto.ItemProduto
import lua.security.Utilizador

class SaidaDeProduto implements Serializable{
    private static final long serialVersionUID = 1
    Long	id
    String numeroDeSaida
    Utilizador utilizador
    Destino destino
    Date	dateCreated
    Date	lastUpdated
    Cliente cliente
    BigDecimal valor
    BigDecimal valorDoIva
    boolean  cancelado = false
    static hasMany = [itemsProduto:ItemProduto]
    static mapping = {
        id generator: 'increment'
    }
    static constraints = {
        utilizador nullable: false
        destino nullable: false
        cliente nullable: true
        valor nullable:  true
    }

    public String toString() {
        return "${id}"
    }
}
