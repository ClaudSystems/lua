package estoque

import grails.transaction.Transactional
import lua.estoque.encomenda.Encomenda
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.produto.Produto

@Transactional
class EncomendaService {
    String myUrl
    String notaDe
    Encomenda encomenda = new  Encomenda()
    EntradaDeProduto entradaDeProduto = new EntradaDeProduto()
    def items = new ArrayList<ItemProduto>()
    def addItems(String produto) {
        Produto pro = Produto.findByDescricao(produto)
        ItemProduto itemProduto = new ItemProduto()
        itemProduto.produto=pro
        items.add(itemProduto)
    }

    def addItemsbyCodigo(String codigo) {
        Produto pro = Produto.findByCodigo(codigo)
        ItemProduto itemProduto = new ItemProduto()
        itemProduto.produto=pro
        items.add(itemProduto)
    }

    def getListProdutos(){
        return items
    }
    def removerItem(ItemProduto itemProduto){
        items.remove(itemProduto)
    }
    def removerItem(String codigo){
        for (ItemProduto ip in items){
            if (ip.produto.codigo.equals(codigo)){
                items.remove(ip)
                return ip
            }
        }

    }

    def getItemProduto (String codigo){
        for (ItemProduto ip in items){
            if (ip.produto.codigo.equals(codigo)){
               return ip
            }
        }
    }


}
