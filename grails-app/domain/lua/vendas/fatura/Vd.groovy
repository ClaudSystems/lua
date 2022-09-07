package lua.vendas.fatura

import lua.entidades.cliente.Cliente
import lua.estoque.itemProduto.ItemProduto
import lua.restaurante.mesa.Diario
import lua.restaurante.mesa.Mesa
import lua.security.Utilizador
import lua.vendas.recibo.Cheque


/**
 * Vd
 * A domain class describes the data object and it's mapping to the database
 */

class Vd implements Serializable {

    private static final long serialVersionUID = 1
    Long	id
    BigDecimal valor = 0.0
    BigDecimal valorDoIva = 0.0
    BigDecimal valorTotal = 0.0
    Utilizador utilizador
    Cliente cliente
    String label
    boolean pago = false
    boolean  cancelado= false
    boolean  ativo= true
    String condicoesDeVenda
    String numeroDeVd
    Date	dateCreated
    Date	lastUpdated
    String formaDePagamento
    Cheque cheque

//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= [cliente:Cliente]	// tells GORM to associate another domain object as an owner in a 1-1 mapping
    static	hasMany		= [ itemsProduto:ItemProduto]	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping

    static	mapping = {
        autoTimestamp true
        id generator: 'increment'
       itemsProduto lazy: false

    }

    static	constraints = {
        cliente nullable: true
        valor nullable: false
        valorDoIva nullable: true
       condicoesDeVenda nullable: true
        ativo nullable:true
        cheque nullable: true
        formaDePagamento nullable: true
        label nullable: true

    }

    /*
     * Methods of the Domain Class
     */
    @Override	// Override toString for a nicer / more descriptive UI
    public String toString() {
        return "${id}"
    }




    BigDecimal getValorTotal() {
       def valo =  getValor()
       def valorIv =  getValorDoIva()

        return valo+valorIv
    }


}
