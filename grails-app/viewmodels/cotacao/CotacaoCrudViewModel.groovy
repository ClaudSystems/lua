package cotacao

import base.ComposersService
import grails.transaction.Transactional
import lua.entidades.cliente.Cliente
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.produto.Produto
import lua.vendas.cotacao.Cotacao
import org.zkoss.zul.ListModelList


@Transactional
class CotacaoCrudViewModel {

    private ListModelList<ItemProduto> items
    private String filter
/*    private ListModelList<Cliente> clientes
    private ListModelList<Produto> produtos*/
    private Cliente selectedCliente
    private Produto selectedProduto
    ComposersService composersService
    Cotacao cotacao = composersService.cotacao

    Cotacao getCotacao() {
        return cotacao
    }

    public ListModelList<ItemProduto> getItems() {

        if (items == null) {
            items = new ListModelList<ItemProduto>(cotacao.itemsProduto)
        }
        return items
    }



}
