package lua.vendas.cotacao
import lua.entidades.cliente.Cliente
import lua.estoque.itemProduto.ItemProduto
import lua.security.Utilizador

/**
 * Cotacao
 * A domain class describes the data object and it's mapping to the database
 */
class Cotacao implements Serializable{
	private static final long serialVersionUID = 1
	Long	id
	BigDecimal valor = 0.0
	BigDecimal valorDoIva =0.0
	Utilizador utilizador
	Cliente cliente
	String numeroDaCotacao
	BigDecimal valorTotal = 0.0



	Date	dateCreated
	Date	lastUpdated
	

	static	hasMany		= [itemsProduto:ItemProduto]	// tells GORM to associate other domain objects for a 1-n or n-m mapping

	
    static	mapping = {
		autoTimestamp true
		id generator: 'increment'
        itemsProduto lazy: false
    }
    
	static	constraints = {
		cliente nullable: false
		utilizador nullable: true
		valor nullable: false
		itemsProduto nullable: false
        valorDoIva nullable: true
        numeroDaCotacao nullable: false , unique: true
    }
	
	/*
	 * Methods of the Domain Class
	 */
	@Override	// Override toString for a nicer / more descriptive UI
	public String toString() {
		return "${id}"
	}

	BigDecimal getValor() {
		def valor_ = 0
		if(itemsProduto!=null){
			itemsProduto.each {
				valor_ += it.subTotal
			}
		}
		return valor_
	}

	BigDecimal getValorDoIva() {
		def valor_do_iva = 0
		if(itemsProduto!=null){
			itemsProduto.each {
				valor_do_iva += it.valorTotalDoIva
			}
		}
		return valor_do_iva
	}

	BigDecimal getValorTotal() {
		return valor+valorDoIva
	}
}
