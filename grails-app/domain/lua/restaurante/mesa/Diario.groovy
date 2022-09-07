package lua.restaurante.mesa

import lua.vendas.fatura.Vd

import java.sql.Time

/**
 * Diario
 * A domain class describes the data object and it's mapping to the database
 */
class Diario {
    String estado
    String numeroDoDiario
	/* Default (injected) attributes of GORM */
//	Long	id
//	Long	version
	
	/* Automatic timestamping of GORM */
	Date	dateCreated
	Date	dateClosed
	Date	lastUpdated
	
//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
	static	hasMany		= [vds:Vd]	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping 
	
    static	mapping = {
        vds lazy: false
    }
    
	static	constraints = {
        estado inList: ["aberto","fechado","pendente"]
        lastUpdated nullable: true
        vds nullable: true
        dateClosed nullable: true
    }
	
	/*
	 * Methods of the Domain Class
	 */
//	@Override	// Override toString for a nicer / more descriptive UI 
//	public String toString() {
//		return "${name}";
//	}


}
