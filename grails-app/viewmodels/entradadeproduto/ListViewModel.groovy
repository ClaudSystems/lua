package entradadeproduto

import base.ComposersService
import base.EncryptionService
import grails.transaction.Transactional
import lua.UtilizadorService
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.itemProduto.ItemProduto
import lua.vendas.cotacao.Cotacao
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Messagebox


@Transactional
class ListViewModel {
    UtilizadorService utilizadorService

    @Wire Label info
    @Wire    Listbox lb_items
    ComposersService composersService
    private String filter
    Item pickedItem
    String red = "color:red"
    String blue = "color:blue"
    private ListModelList<EntradaDeProduto> selectedItems
    private ListModelList<Item> items
    List<ItemProduto> itemList = new ArrayList<ItemProduto>()
    EntradaDeProduto item = new EntradaDeProduto()

    EntradaDeProduto getItem() {
        return item
    }

    ListModelList<ItemProduto> getItemList() {
        List<ItemProduto> itemLists = new ArrayList<ItemProduto>()
        for (ItemProduto ip in item.itemsProduto){
            itemLists.add(ip)
        }
        return itemLists
    }

    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }
    public String getFilter() {
        return filter
    }

    private List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>()
        def entradas = EntradaDeProduto.all
        for (EntradaDeProduto x:entradas){
            items.add(new Item(x.id,x.fornecedor.nome, x.valor,x.valorDoIva, x.dateCreated))
        }
        return items
    }

    public ListModelList<Item> getItems() {
        if (items == null) {
            items = new ListModelList<Item>(getAllItems())
        }
        return items
    }

    public ListModelList<Item> getSelectedItems() {

        return selecteItems
    }

    public class Item {
        private  Long id
        private BigDecimal valor
        private BigDecimal valorIva
        private Date dateCreated
        private String nome


        public Item(Long id,String nome, BigDecimal valor,BigDecimal valorIva, Date dateCreated) {
            this.id = id
            this.nome = nome
            this.valor = valor
            this.valorIva = valorIva
            this.dateCreated = dateCreated

        }
        public Long getId() {
            return id
        }
        public BigDecimal getValor() {
            return valor
        }
        public Date getDateCreated() {
            return dateCreated
        }
        public String getNome() {
            return nome
        }

        BigDecimal getValorIva() {
            return valorIva
        }


    }


    @NotifyChange(["items","info"])
    @Command
    public void doSearch() {
    info.value = ""
        items.clear()
        List<Item> allItems = getAllItems()
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (Item item : allItems) {
                if (item.getNome().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||item.getId().toString().indexOf(filter) >= 0 || item.getValor().toString().indexOf(filter) >= 0 ||
                        item.getDateCreated().toString().indexOf(filter) >= 0) {
                    items.add(item)
                }
            }
        }
    }

    @Command
    public void showIt() {
        System.print(pickedItem.id)

        composersService.setEntradaId(pickedItem.id)
        Executions.sendRedirect("/entradadeproduto/edit")

    }
 /*   @Command
    public void viewItems() {
        Cotacao cotacao =  Cotacao.findById(pickedItem.id)
        composersService.setCotacao(cotacao)
        *//*  Executions.sendRedirect("/cotacao/listItems.gsp")*//*
    }*/

    @Command
     static def showNewEntrada(){
              Executions.sendRedirect("/entradaDeProduto/newEntrada")
    }

    @Command
    @NotifyChange(["item","itemList","info"])
    void viewItems(@BindingParam("id") Integer id) {
        info.value = ""
        item = EntradaDeProduto.findById(id)
        System.println("viewItems"+id)

    }

    @Command
    @NotifyChange(["items","entrada","info"])
    public void removeItem(@BindingParam("index") Integer index) {
        if(!utilizadorService.getAccess("entrada_produto_d")){
          //  Messagebox.show("Este utilizador não permissão para executar esta acção !", "Lua", 1,  Messagebox.ERROR)
            info.value = "Este utilizador não permissão para executar esta acção !"
            info.style = red
            return
        }

      /*  Messagebox.show("Tem certeza que deseja eliminar O Documento No." +index+ "?", "Execute?", Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION, new EventListener<Event>() {
            @Override
            public void onEvent(final Event evt) throws InterruptedException {
                if (Messagebox.ON_YES.equals(evt.getName())) {

                    EntradaDeProduto.findById(items[index].id).delete(flush: true)
                    items.remove(index)
                }
            }
        }
        )*/

        EntradaDeProduto.findById(items[index].id).delete(flush: true)
        items.remove(index)

    }


}
