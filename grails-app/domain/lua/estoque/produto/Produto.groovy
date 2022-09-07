package lua.estoque.produto

import lua.estoque.categoria.Categoria
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.iva.Iva
import org.zkoss.zul.Image


class Produto implements Serializable {
    private static final long serialVersionUID = 1
    String codigo
    String nome
    String descricao
    Integer qtd
    Integer qtdItems = 0
    Integer saldo = 0
    Image image
    Categoria categoria
    String color
    BigDecimal precoDeVenda = 0.0
    BigDecimal valorDoIva = 0.0
    BigDecimal precoTotalDeVenda
    boolean  ivaAplicado = true
    boolean  pacote = false
    boolean  estocavel = true
    Iva iva
    static hasMany = [itemsProduto:ItemProduto]

    static mapping = {
        id generator: 'increment'
     //  unidade lazy: false
    }

    static constraints = {
        codigo nullable: false, unique: true
        nome nullable: true, unique: true
        descricao nullable: false
        itemsProduto nullable: true
        qtd null:true , display:false
        image nullable: true
        categoria nullable: true
        color nullable: true
        saldo nullable: true
         nullable: true
        precoDeVenda nullable: true
        valorDoIva nullable: true
        precoTotalDeVenda nullable: true
        qtdItems nullable: true
        iva nullable: true
    }

    public String toString() {
        return "${nome}"

    }

    Integer getQtd() {
        def sal = 0
        for(ItemProduto ip in itemsProduto){
            sal+= ip.qtd
        }
        qtd = sal
        return qtd
    }

    Integer getSaldo() {
       saldo = 0
        for(ItemProduto ip in itemsProduto){
            if(ip.estocavel){
                if(ip.operacao=="entrada"){
                    saldo+=ip.qtd
                }
                if(ip.operacao=="saida"){
                    saldo-=ip.qtd
                }
            }

        }
        return saldo
    }

    BigDecimal getPrecoTotalDeVenda() {
        if(ivaAplicado){
            return getValorDoIva()+getPrecoDeVenda()
        }else
        return precoDeVenda
    }


}
