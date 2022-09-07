package cotacao

import base.ComposersService
import grails.transaction.Transactional
import lua.vendas.cotacao.Cotacao
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import lua.estoque.itemProduto.ItemProduto


@Transactional
class ListItemsViewModel {


    @Wire    Listbox lb_items
    ComposersService composersService

    Item pickedItem
    private ListModelList<ItemProduto> selectedItems
    private ListModelList<Item> items



    private List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>()
        Cotacao cotacao = composersService.cotacao
        System.print(cotacao.id)
        def isp = ItemProduto.all
        for (ItemProduto x:isp){

            items.add(new Item(x.descricao,x.qtd,x.precoDeVenda))
        }
        return items;
    }

    public ListModelList<Item> getItems() {
        if (items == null) {
            items = new ListModelList<Item>(getAllItems())
        }
        return items;
    }

    public ListModelList<Item> getSelectedItems() {

        return selecteItems
    }

    public class Item {
        private  int qtd
        private BigDecimal preco
       private String descricao


        public Item(String descricao,int  qtd, BigDecimal preco) {
          this.qtd = qtd
            this.preco = preco
            this.descricao = descricao

        }
        public String getDescricao() {
            return descricao
        }
        public BigDecimal getPreco() {
            return preco
        }
        public int getQtd() {
            return qtd
        }


    }








}
