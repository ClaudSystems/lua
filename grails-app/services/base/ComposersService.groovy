package base


import lua.estoque.encomenda.Encomenda
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.saidaDeProduto.SaidaDeProduto
import lua.restaurante.mesa.Mesa
import lua.vendas.cotacao.Cotacao
import lua.vendas.fatura.Fatura
import lua.vendas.fatura.Vd
import lua.vendas.recibo.Recibo

/**
 * ComposersService
 * A service class encapsulates the core business logic of a Grails application
 */
class ComposersService {
    String relatorio_entradas_data_inicio
    String relatorio_entradas_data_fim
    boolean editarCotacao = false
    def clienteId
    Vd vd
    Mesa mesa
    def cotacaoId
    Cotacao cotacao
    def faturaId
    Fatura fatura
    def faturaNumber
    def vdId
    def utilizadorId
    def diarioId
    Encomenda encomenda
    Recibo recibo
    def reciboId
    def  entradaId
    SaidaDeProduto saidaDeProduto

    def serviceMethod() {

    }

}
