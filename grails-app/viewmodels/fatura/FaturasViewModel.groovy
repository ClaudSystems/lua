package fatura

import base.ComposersService
import grails.transaction.Transactional
import lua.estoque.itemProduto.ItemProduto
import lua.vendas.cotacao.Cotacao
import lua.vendas.fatura.Fatura
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Messagebox


@Transactional
class FaturasViewModel {

    @Wire    Listbox lb_items
    ComposersService composersService
    private String filter = ""
    private String id
    Item pickedItem
    private ListModelList<Cotacao> selectedItems
    private ListModelList<Item> items
    Fatura item = new Fatura()
    List<ItemProduto> itemList = new ArrayList<ItemProduto>()

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    Item getPickedItem() {
        return pickedItem
    }

    void setPickedItem(Item pickedItem) {
        this.pickedItem = pickedItem
    }

    ListModelList<ItemProduto> getItemList() {
        List<ItemProduto> itemLists = new ArrayList<ItemProduto>()
        for (ItemProduto ip in item.itemsProduto){
            itemLists.add(ip)
        }
        return itemLists
    }

    Fatura getItem() {
        return item
    }



    @NotifyChange
    void setFilter(String filter) {
        this.filter = filter
    }
    String getFilter() {
        return filter
    }

    private List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>()
        def faturas = Fatura.all

        for (Fatura x:faturas){
            if (!x.equals(null)){
                items.add(new Item(x.id,x.cliente.nome, x.valor, x.dateCreated,x.numeroDaFatura,x.estado))
            }

        }
        return items
    }

    ListModelList<Item> getItems() {
        if (items == null) {
            items = new ListModelList<Item>(getAllItems())
        }
        return items
    }

    ListModelList<Item> getSelectedItems() {

        return selecteItems
    }

    class Item {
        private  Long id
        private BigDecimal valor
        private Date dateCreated
        private String nomeCliente
        private String numeroDaFatura
        private String estado


        Item(Long id,String nome, BigDecimal valor, Date dateCreated,String numeroDaFatura,String estado) {
            this.id = id
            this.nomeCliente = nome
            this.valor = valor
            this.dateCreated = dateCreated
            this.numeroDaFatura = numeroDaFatura
            this.estado = estado
        }
        Long getId() {
            return id
        }
        BigDecimal getValor() {
            return valor
        }
        Date getDateCreated() {
            return dateCreated
        }
        String getNomeCliente() {
            return nomeCliente
        }

        String getNumeroDaFatura() {
            return numeroDaFatura
        }
    }


    @NotifyChange("items")
    @Command
    void doSearch() {

        items.clear()
        List<Item> allItems = getAllItems()
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (Item item : allItems) {
                if (item.getNomeCliente().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item.numeroDaFatura.toString().indexOf(filter) >= 0 ||
                        item.estado.toString().indexOf(filter) >= 0 ||
                        item.getValor().toString().indexOf(filter) >= 0 ||
                        item.getDateCreated().toString().indexOf(filter) >= 0) {
                    items.add(item)
                }
                System.println(item.dateCreated)
            }
        }
    }

    @Command
    void showIt() {
        System.print(pickedItem.id)
        composersService.faturaId=pickedItem.id
        Executions.sendRedirect("/fatura/edit")

    }
    @Command
    @NotifyChange(["item","itemList"])
    void viewItems(@BindingParam("id") Integer id) {
        item = Fatura.findById(id)
        System.println("viewItems"+id)

    }

    @Command
    def  createNewItem(){
        composersService.faturaId=null
        Executions.sendRedirect("/fatura/new")
    }

    @Command
    @NotifyChange(["pickedItem"])
    def findItem(){
        def id_ = id as Long
        def itemDB = Fatura.findById(id_)
        System.println("findItem id="+id)
        System.println("findItem itemDB="+itemDB)
        if(itemDB.equals(null)){
            Messagebox.show("Não foi localizado a Fatura com o ID= "+id, "Lua", 1,  Messagebox.ERROR)
        }else {
            composersService.faturaId=itemDB.id
            Executions.sendRedirect("/fatura/edit")
        }

    }



    @Init init() {
        getAllItems()
    }


}
