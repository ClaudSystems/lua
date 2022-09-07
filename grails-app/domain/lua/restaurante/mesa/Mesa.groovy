package lua.restaurante.mesa

import lua.vendas.fatura.Vd
import org.zkoss.zul.Image

/**
 * Mesa
 * A domain class describes the data object and it's mapping to the database
 */
class Mesa {
    String numeroDaMesa
    Localizacao localizacao
    String estado
    Image imagem
    boolean ativo = false

	/* Default (injected) attributes of GORM */
//	Long	id
//	Long	version
	
	/* Automatic timestamping of GORM */
//	Date	dateCreated
//	Date	lastUpdated
	
//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
	static	hasMany		= [vds:Vd]	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping 
	
    static	mapping = {
        vds lazy: false
    }
    
	static	constraints = {
        estado inList: ['nova','aberta','paga','pendente']
        vds nullable: true
        imagem nullable: true
        numeroDaMesa unique: true


    }

    String getEstado() {
        if(vds==null){
            return "nova"
        }else
        if(vds.empty){
            return "vazia"
        }else
        {
            for(Vd vd in vds){
                if(vd.pago){
                   return "pago"
                }
                if(!vd.pago&&vd.ativo){
                    return "aberta"
                }
            }
        }
        return estado
    }
/*
	 * Methods of the Domain Class
	 */
//	@Override	// Override toString for a nicer / more descriptive UI 
//	public String toString() {
//		return "${name}";
//	}
}
