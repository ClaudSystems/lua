package cotacao

import base.ComposersService
import grails.transaction.Transactional
import lua.UtilizadorService
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.produto.Produto
import lua.vendas.cotacao.Cotacao
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
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
class ListCotacaoViewModel {
    UtilizadorService utilizadorService
    @Wire    Listbox lb_items
    ComposersService composersService
    private String filter = ""
    private String id
    Cotacao selectedCotacao = new Cotacao()
    private Cotacao item = new Cotacao()
    Item pickedItem
    private ListModelList<Item> items
    List<ItemProduto> itemList = new ArrayList<ItemProduto>()
    @Wire Label info
    Item getPickedItem() {
        return pickedItem
    }

    void setPickedItem(Item pickedItem) {
        this.pickedItem = pickedItem
    }

    class Item {
        private  Long id
        private BigDecimal valor
        private Date dateCreated
        private String cliente
        private String numeroDaCotacao


        Item(Long id,String nome, BigDecimal valor, Date dateCreated,String numeroDaCotacao) {
            this.id = id
            this.cliente = nome
            this.valor = valor
            this.dateCreated = dateCreated
            this.numeroDaCotacao= numeroDaCotacao

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

        String getCliente() {
            return cliente
        }

        String getNumeroDaCotacao() {
            return numeroDaCotacao
        }
    }
    Cotacao getSelectedCotacao() {
        return selectedCotacao
    }

    void setSelectedCotacao(Cotacao selectedCotacao) {
        this.selectedCotacao = selectedCotacao
    }

    ListModelList<ItemProduto> getItemList() {
        List<ItemProduto> itemLists = new ArrayList<ItemProduto>()
        for (ItemProduto ip in item.itemsProduto){
            itemLists.add(ip)
        }
        return itemLists
    }

    @NotifyChange
    void setFilter(String filter) {
        this.filter = filter
    }
    String getFilter() {
        return filter
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    @Command
    def getItemById(){
    // Messagebox.show("Selected Item="+id)
        info.value = ""

    }


    ListModelList<Item> getItems() {
        if (items == null) {
            items = new ListModelList<Item>(getAllItems())
        }
        return items
    }

    private List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>()
        def cotacoes = Cotacao.all

        for (Cotacao x:cotacoes){
            if (!x.equals(null)){
                items.add(new Item(x.id,x.cliente.nome, x.valor, x.dateCreated,x.numeroDaCotacao))
            }
        }
        return items
    }


    @NotifyChange(["items","info"])
    @Command
    void doSearch() {
        info.value = ""
        items.clear()
        List<Item> allItems = getAllItems()
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (Item item : allItems) {
                if (item.cliente.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||item.getId().toString().indexOf(filter) >= 0 ||
                        item.getValor().toString().indexOf(filter) >= 0 ||
                        item.numeroDaCotacao.toString().indexOf(filter) >= 0 ||
                        item.getDateCreated().toString().indexOf(filter) >= 0) {
                    items.add(item)
                }
                System.println(item.dateCreated)
            }
        }
    }

    @Command
    void showIt(@BindingParam("id") Integer id) {
        Cotacao cotacaoDb = Cotacao.findById(id)
        composersService.cotacao= cotacaoDb
        composersService.clienteId=cotacaoDb.cliente.id
        Executions.sendRedirect("/cotacao/edit")

    }

    @Command
    @NotifyChange(["pickedItem","info"])
    def findItem(){
        def id_ = id as Long
        def itemDB = Cotacao.findById(id_)
        System.println("findItem id="+id)
        System.println("findItem itemDB="+itemDB)
        if(itemDB.equals(null)){
           // Messagebox.show("Não foi localizado a cotação com o ID= "+id, "Lua", 1,  Messagebox.ERROR)
            info.value= "Não foi localizado a cotação com o ID= "+id
            info.style = "color:red"
        }else {
            composersService.cotacaoId=itemDB.id
            Executions.sendRedirect("/cotacao/edit")
        }

    }

    @Command
    @NotifyChange(["item","itemList"])
    void viewItems(@BindingParam("id") Integer id) {
        item = Cotacao.findById(id)
        System.println("viewItems"+id)
    }

    @Command
    static  def createNewCotacao(){
        Executions.sendRedirect("/cotacao/new")
    }


    @Init init() {

    }

    @Command
    @NotifyChange(["items","info"])
    public void removeItem(@BindingParam("index") Integer index) {
        if(!utilizadorService.getAccess("cotacao_d")){
          //  Messagebox.show("Este utilizador não permissão para executar esta acção !", "Lua", 1,  Messagebox.ERROR)
            info.value = "Este utilizador não permissão para executar esta acção !"
            info.style = "color:red"
            return
        }

       /* Messagebox.show("Tem certeza que deseja eliminar a cotacao No." +items[index].id+ "?", "Execute?", Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION, new EventListener<Event>() {
            @Override
            public void onEvent(final Event evt) throws InterruptedException {
                if (Messagebox.ON_YES.equals(evt.getName())) {

                    Cotacao.findById(items[index].id).delete(flush: true)
                    items.remove(index)
                }
            }
        }
        )*/
        info.value= "Sem  a tem certeza que deseja eliminar a cotacao com ID." +items[index].id+ "faça double click sobre a cotação para eliminar!"


    }

    @Command
    @NotifyChange(["items","info"])
    public void deleteItem(@BindingParam("index") Integer index) {
        if(!utilizadorService.getAccess("cotacao_d")){
            //  Messagebox.show("Este utilizador não permissão para executar esta acção !", "Lua", 1,  Messagebox.ERROR)
            info.value = "Este utilizador não permissão para executar esta acção !"
            info.style = "color:red"
            return
        }
        info.value= "Sem  a tem certeza que deseja eliminar a cotacao com ID." +items[index].id+ "faça double click sobre a cotação para eliminar!"
        Cotacao.findById(items[index].id).delete(flush: true)
        items.remove(index)

    }
}
