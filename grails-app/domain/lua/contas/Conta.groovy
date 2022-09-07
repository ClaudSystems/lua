package lua.contas

import lua.entidades.cliente.Cliente
import lua.entidades.fornecedor.Fornecedor

/**
 * Conta
 * A domain class describes the data object and it's mapping to the database
 */
class Conta {


    Integer numeroDaConta
    String descricao
    String codigo
    String tipoDeConta
    String estatuto
    String natureza
    BigDecimal credito = 0.0
    BigDecimal debito = 0.0
    BigDecimal saldo = 0.0
    Cliente cliente
    Fornecedor fornecedor
    Conta conta



    def creditar(BigDecimal valor){
        if(tipoDeConta=='ativa'&&natureza=='devedora'){
            credito-=valor
        }
        if(tipoDeConta=='ativa'&&natureza=='credora'){
            credito+=valor
        }

        if(tipoDeConta=='passiva'&&natureza=='credora'){
            credito+=valor
        }
        if(tipoDeConta=='receita'&&natureza=='credora'){
            credito+=valor
        }
        if(tipoDeConta=='despesa'&&natureza=='devedora'){
            credito-=valor
        }
    }

    def debitar (BigDecimal valor){
        if(tipoDeConta=='ativa'&&natureza=='credora'){
            debito-=valor
        }
        if(tipoDeConta=='ativa'&&natureza=='devedora'){
            debito+= valor
        }
        if(tipoDeConta=='passiva'&&natureza=='credora'){
            debito-=valor
        }
        if(tipoDeConta=='receita'&&natureza=='credora'){
            debito-=valor
        }

        if(tipoDeConta=='despesa'&&natureza=='devedora'){
            debito+=valor
        }
    }

    String getEstatuto() {
       if(contas==null){
           return "movimento"
       }

        return "agregado"
    }
/* Default (injected) attributes of GORM */
//	Long	id
//	Long	version

    /* Automatic timestamping of GORM */
//	Date	dateCreated
//	Date	lastUpdated

	static	belongsTo	= [ClasseDeContas]	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
	static	hasMany		= [contas:Conta,transacoes:Transacao]	// tells GORM to associate other domain objects for a 1-n or n-m mapping
	static	mappedBy	= [transacoes: 'origem']	// specifies which property should be used in a mapping

    static	mapping = {
    }

    static	constraints = {
        tipoDeConta inList: ['ativa','passiva','receita','despesa']
        natureza inList: ['devedora','credora']
        estatuto inList: ['integrado','movimento']
        cliente nullable: true
        fornecedor nullable: true
        contas nullable: true
        transacoes nullable: true
        conta nullable: true

    }

    /*
     * Methods of the Domain Class
     */
//	@Override	// Override toString for a nicer / more descriptive UI
    public String toString() {
        return "${numeroDaConta}"
    }
}
