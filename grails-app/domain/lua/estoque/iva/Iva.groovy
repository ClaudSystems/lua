package lua.estoque.iva

/**
 * Iva
 * A domain class describes the data object and it's mapping to the database
 */
class Iva implements Serializable{
    private static final long serialVersionUID = 1
    String descricao
   BigDecimal percentualIva
    boolean valido

	/* Default (injected) attributes of GORM */
//	Long	id
//	Long	version
	
	/* Automatic timestamping of GORM */
//	Date	dateCreated
//	Date	lastUpdated
	
//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
//	static	hasMany		= []	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping 
	
    static	mapping = {
    }
    
	static	constraints = {
        percentualIva nullable:false,min: 0.0, max: 100.0, scale: 2, unique: true
        valido unique: true

    }
	
	/*
	 * Methods of the Domain Class
	 */
//	@Override	// Override toString for a nicer / more descriptive UI 
//	public String toString() {
//		return "${name}";
//	}
}