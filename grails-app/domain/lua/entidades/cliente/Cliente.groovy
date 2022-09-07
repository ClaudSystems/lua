package lua.entidades.cliente

import lua.contas.Conta
import lua.entidades.entidade.Entidade
import lua.estoque.encomenda.Encomenda
import lua.estoque.saidaDeProduto.SaidaDeProduto
import lua.vendas.cotacao.Cotacao
import lua.vendas.fatura.Fatura
import lua.vendas.recibo.Recibo


class Cliente implements Serializable{
    private static final long serialVersionUID = 1
    String codigo
    String nome
    String nuit
    String mail
    String numTelefone
    String endereco
    Classe classe
    Conta conta

    static hasMany = [saidasDeProdutos:SaidaDeProduto, encomendas:Encomenda, faturas:Fatura, cotacoes:Cotacao, recibos:Recibo]
    static mapping = {
        autoTimestamp true
        id generator: 'increment'
    }

    static constraints = {
        codigo nullable: false, unique: true
        nome nullable: false, unique: true
        nuit nullable: true, unique: true
        mail nullable: true, email: true
        numTelefone nullable: true
        endereco nullable: true
        recibos nullable:  true
        classe nullable: false
        conta nullable: true
    }


    String toString() {
        return "${nome}"

    }



}
