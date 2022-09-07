package lua.vendas.fatura

import lua.entidades.cliente.Cliente
import lua.estoque.itemProduto.ItemProduto
import lua.security.Utilizador
import lua.vendas.recibo.Recibo

/**
 * Factura
 * A domain class describes the data object and it's mapping to the database
 */
class Fatura implements Serializable{

	private static final long serialVersionUID = 1
	Long	id
   	String numeroDaFatura
	BigDecimal valor = 0.0
	BigDecimal valorTotal = 0.0
	BigDecimal valorTotalPago = 0.0
	BigDecimal valorEmDivida = 0.0
	BigDecimal valorDoIva = 0.0
	Utilizador utilizador
	Cliente cliente
	boolean pago = false
	boolean  cancelado= false
    boolean entregue = false
	String estado
    String condicoesDeVenda

	Date	dateCreated
	Date	dataPrevistaDePagamento
	Date	lastUpdated

//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= [cliente:Cliente]	// tells GORM to associate another domain object as an owner in a 1-1 mapping
	static	hasMany		= [recibos:Recibo,itemsProduto:ItemProduto]	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping 
	
    static	mapping = {
		autoTimestamp true
		id generator: 'increment'
    }
    
	static	constraints = {
		cliente nullable: false
		recibos nullable: true
		valor nullable: false
		valorDoIva nullable: true
        estado nullable: false, inList: ['aberta', 'pendente','fechada','em_pagamento','cancelada']
        condicoesDeVenda nullable: true
		dataPrevistaDePagamento nullable: true
        numeroDaFatura unique: true , nullable: false
		valorTotalPago nullable: true
        valorEmDivida nullable: true
        valorTotal nullable: true
    }
	
	/*
	 * Methods of the Domain Class
	 */
	@Override	// Override toString for a nicer / more descriptive UI
	public String toString() {
		return "${id}"
	}

    boolean getPago() {
      return valorEmDivida>=0

    }

    BigDecimal getValorTotalPago() {
        def valorPago=0.0
        if(recibos){
            for(int x=0; x<recibos.size();x++ ){
                valorPago+=recibos[x].valor
            }
        }
        return valorPago
    }

    BigDecimal getValorEmDivida() {
        BigDecimal divida = valorTotal-valorTotalPago
        return       -(divida)
    }

    BigDecimal getValorTotal() {
        return valor+valorDoIva
    }

    String getEstado() {
        Date date = new Date()
        if (cancelado){
            return "cancelada"
        }
        if(dataPrevistaDePagamento>date&&valorEmDivida<0){
           return  "pendente"
        }
        if(pago){
           return  "fechada"
        }
        if(valorTotalPago>0){
           return   "em_pagamento"

        }else

        return  "aberta"
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



}
