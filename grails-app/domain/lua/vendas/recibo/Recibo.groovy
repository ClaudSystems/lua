package lua.vendas.recibo

import lua.vendas.fatura.Fatura
import lua.vendas.fatura.Vd

/**
 * Recibo
 * A domain class describes the data object and it's mapping to the database
 */
class Recibo implements Serializable{
	private static final long serialVersionUID = 1
	Long	id
	String numeroDoRecibo
	BigDecimal valor
	String formaDePagamento
	String descricao
	Cheque cheque
//	Long	version
	
	/* Automatic timestamping of GORM */
	Date	dateCreated
	Date	dataDePagamento
	Date	lastUpdated
	
	static	belongsTo	= [Fatura]	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
	//static	belongsTo	= [Vd]	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= [cliente:Cliente]	// tells GORM to associate another domain object as an owner in a 1-1 mapping
	static	hasMany		= [faturas:Fatura]	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping 
	
    static	mapping = {
		autoTimestamp true
		id generator: 'increment'
    }
    
	static	constraints = {
		descricao nullable: true
		cheque nullable: true
		faturas nullable: true
    }
	
	/*
	 * Methods of the Domain Class
	 */
	@Override	// Override toString for a nicer / more descriptive UI
	public String toString() {
		return "${id}"
	}
}
