package lua.estoque.encomenda

import lua.entidades.fornecedor.Fornecedor
import lua.estoque.itemProduto.ItemProduto

class Encomenda implements Serializable{
    private static final long serialVersionUID = 1
    String descricao
    Fornecedor fornecedor
    String estado
    String tipo
    String refFactura

    Long	id
    Date	dateCreated
    Date	lastUpdated

    static hasMany = [itemsProduto:ItemProduto]

    static mapping = {
        autoTimestamp true
        id generator: 'increment'
    }

    static constraints = {
        estado inList: ["Aberto","Pendente","Fechado"]
        tipo inList: ["entrada","saida","encomenda","cotacao","fatura"]
        descricao nullable: true
        fornecedor nullable: true
        itemsProduto nullable: false
        refFactura nullable: true
      }

    public String toString() {
        return "${id}"

    }
}
