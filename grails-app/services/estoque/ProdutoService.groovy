package estoque

import grails.transaction.Transactional
import lua.estoque.encomenda.Encomenda
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.produto.Produto

@Transactional
class ProdutoService {
   String tipoDeMovimento


    List selectedProdutos = new ArrayList()

    public void addSelectedProduto(String codigo){
        def produto =  Produto.findByCodigo(codigo)
        selectedProdutos.add(produto)
    }

    public List getSelectedProdutos(){
        return selectedProdutos
    }

    def addItensProdutos (ItemProduto ip){
                ip.produto.saldo+= ip.qtd
                ip.save flash:true
                ip.produto.save flush: true
    }
    def removeItensProdutos (ItemProduto ip){
        ip.produto.saldo-= ip.qtd
        ip.save flash:true
        ip.produto.save flush: true
    }
    def removeItensProdutos (Encomenda encomenda){

        def intemPro = encomenda.itemsProduto
        for (ItemProduto ip in intemPro){
            Produto p = Produto.find(ip.produto)
             p.saldo-=ip.qtd
             p.save flash:true
        }

    }



}
