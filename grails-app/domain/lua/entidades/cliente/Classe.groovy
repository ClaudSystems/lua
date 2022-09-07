package lua.entidades.cliente

import lua.estoque.produto.Produto

/**
 * Classe
 * A domain class describes the data object and it's mapping to the database
 */
class Classe {
    String nomeDaClasse
    BigDecimal percentualDeDesconto
	/* Default (injected) attributes of GORM */
//	Long	id
//	Long	version
	
	/* Automatic timestamping of GORM */
//	Date	dateCreated
//	Date	lastUpdated
	
//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
	static	hasMany		= [clientes:Cliente,produtos:Produto]	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping 
	
    static	mapping = {
    }
    
	static	constraints = {
        clientes nullable: true
        nomeDaClasse unique: true
        produtos nullable: true
        percentualDeDesconto   nullable:false,min: 0.0, max: 100.0, scale: 2
    }
	
	/*
	 * Methods of the Domain Class
	 */
//	@Override	// Override toString for a nicer / more descriptive UI 
//	public String toString() {
//		return "${name}";
//	}
}
