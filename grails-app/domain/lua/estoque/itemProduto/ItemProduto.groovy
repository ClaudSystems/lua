package lua.estoque.itemProduto

import lua.estoque.categoria.Categoria
import lua.estoque.encomenda.Encomenda
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.estoque.Armazem
import lua.estoque.iva.Iva
import lua.estoque.lote.Lote
import lua.estoque.produto.Produto
import lua.estoque.saidaDeProduto.SaidaDeProduto
import lua.vendas.cotacao.Cotacao
import lua.vendas.fatura.Fatura
import lua.vendas.fatura.Vd
import lua.vendas.precos.Preco


class ItemProduto implements Serializable{
    private static final long serialVersionUID = 1
      int qtd
    Produto produto
    Lote lote
    Armazem  armazem
    Categoria categoria
    BigDecimal precoDeCompra =0.0
   BigDecimal precoDeVenda = 0.0
    String descricao
    BigDecimal subTotal=0.0
    BigDecimal valorTotal=0.0
    BigDecimal valorDoIva = 0.0
    BigDecimal valorTotalDoIva = 0.0
    boolean ivaAplicado = true
    boolean estocavel = true
    Iva iva
    String operacao


    BigDecimal getValorTotal() {
        return subTotal+valorTotalDoIva
    }

    BigDecimal getSubTotal() {
        return precoDeVenda*qtd
    }

    BigDecimal getValorTotalDoIva() {
        return valorDoIva*qtd
    }
    static mapping = {
        id generator: 'increment'
    }


    static constraints = {
        produto nullable: false
        lote nullable: true
        armazem nullable: true
        categoria nullable: true
        qtd nullable: false
        categoria nullable: true
        precoDeCompra nullable: true
        precoDeVenda nullable:  true
        operacao inList: ["entrada","saida","vd","cotacao","fatura"], nullable: false
        iva nullable: true
       valorDoIva nullable: true
     }

    public String toString() {
        return "${descricao}"

    }

    BigDecimal getValorDoIva() {
        return valorDoIva
    }
}
