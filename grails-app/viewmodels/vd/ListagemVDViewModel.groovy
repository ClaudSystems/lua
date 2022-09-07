package vd

import base.ComposersService
import lua.estoque.itemProduto.ItemProduto
import lua.vendas.fatura.Vd
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

class ListagemVDViewModel {

    @Wire    Listbox lb_items
    ComposersService composersService
    private String filter = ""
    private String id
    Item pickedItem
    // private ListModelList<Cotacao> selectedItems
    private ListModelList<Item> items
    Vd item = new Vd()
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

    Vd getItem() {
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
        def vds = Vd.all

        for (Vd x:vds){
            if (!x.equals(null)){
                items.add(new Item(x.id,x.cliente.nome, x.valor, x.dateCreated,x.numeroDeVd,x.valorDoIva,x.valorTotal))
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
        private BigDecimal valorDoIva
        private BigDecimal valorTotal
        private Date dateCreated
        private String nomeCliente
        private String numeroDeVd



        Item(Long id,String nome, BigDecimal valor, Date dateCreated,String numeroDeVd,BigDecimal valorDoIva,valorTotal) {
            this.id = id
            this.nomeCliente = nome
            this.valor = valor
            this.dateCreated = dateCreated
            this.numeroDeVd = numeroDeVd
            this.valorDoIva = valorDoIva
            this.valorTotal = valorTotal




        }

        BigDecimal getValorDoIva() {
            return valorDoIva
        }

        BigDecimal getValorTotal() {
            return valorTotal
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

        String getNumeroDeVd() {
            return numeroDeVd
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
                if (item.getNomeCliente().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||item.getId().toString().indexOf(filter) >= 0 || item.getValor().toString().indexOf(filter) >= 0 ||
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
        composersService.vdId=pickedItem.id
        Executions.sendRedirect("/vd/editVd")

    }
    @Command
    @NotifyChange(["item","itemList"])
    void viewItems(@BindingParam("id") Integer id) {
        item = Vd.findById(id)
        System.println("viewItems"+id)

    }

    @Command
    def  createNewItem(){
        composersService.vdId=null
        Executions.sendRedirect("/vd/newVd")
    }

    @Command
    @NotifyChange(["pickedItem"])
    def findItem(){
        def id_ = id as Long
        def itemDB = Vd.findById(id_)
        System.println("findItem id="+id)
        System.println("findItem itemDB="+itemDB)
        if(itemDB.equals(null)){
            Messagebox.show("Não foi localizado a Vd com o ID= "+id, "Lua", 1,  Messagebox.ERROR)
        }else {
            composersService.vdId=itemDB.id
            Executions.sendRedirect("/vd/editVd")
        }

    }



    @Init init() {
        getAllItems()
    }



}
