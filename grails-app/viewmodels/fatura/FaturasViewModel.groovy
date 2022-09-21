package fatura


import cliente.ClienteService
import grails.transaction.Transactional
import lua.SessionStorageService
import lua.entidades.cliente.Cliente
import lua.estoque.itemProduto.ItemProduto
import lua.vendas.fatura.Fatura
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Messagebox

import javax.validation.constraints.Null


@Transactional
class FaturasViewModel {
    @Wire    Listbox lb_items
    Cliente cliente
    SessionStorageService sessionStorageService
    private String filter = ""
    private String id
    Fatura fatura
    private  boolean todas_faturas
   private ListModelList<Fatura> faturas
    private ListModelList<Cliente> clientes = new ListModelList<Cliente>()
    Fatura item = new Fatura()
    @Command
    @NotifyChange(["faturas","todas_faturas","info"])
    void setAllfaturas() {
        todas_faturas = !todas_faturas
        getFaturas()
    }
    boolean getTodas_faturas() {
        return todas_faturas
    }

    void setTodas_faturas(boolean todas_faturas) {
        this.todas_faturas = todas_faturas
    }

    ListModelList<Cliente> getClientes() {
        return clientes
    }

    Cliente getCliente() {
        return cliente
    }

    @NotifyChange(["faturas"])
    void setCliente(Cliente cliente) {
        this.cliente = cliente
    }

    void setFatura(Fatura fatura) {
        this.fatura = fatura
        sessionStorageService.setFatura(fatura)
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
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


    @NotifyChange(["faturas"])
    @Command
    ListModelList<Fatura> getFaturas() {

        if (faturas == null){
            faturas = new ListModelList<Fatura>()
        }
        faturas.clear()
        if(cliente){
            if(todas_faturas){
                faturas = Fatura.findAllByClienteAndCanceladoAndPago(cliente,false,true)
            }else
            faturas = Fatura.findAllByClienteAndCanceladoAndPago(cliente,false,false)
        }
       return faturas
    }


    static List<Cliente> findAllByName(String nome) {
        def c = Cliente.createCriteria()
        def results = c.list {
            like("nome", "%" + nome + "%")

            maxResults(4)
            order("nome", "desc")
        }
        return results
    }

    @NotifyChange(["faturas"])
    @Command
    void doSearch() {
        clientes.clear()
        List<Cliente> allItems = findAllByName(filter)
           clientes.addAll(allItems)
     }

    @Command
    void showIt() {
        Executions.sendRedirect("/fatura/edit")

    }
    /*@Command
    @NotifyChange(["item","itemList"])
    void viewItems(@BindingParam("id") Integer id) {
        item = Fatura.findById(id)
    }*/

    @Command
    static
    def  createNewItem(){

        Executions.sendRedirect("/fatura/new")
    }

    @Command
    @NotifyChange(["pickedItem"])
    def findItem(){
        def id_ = id as String
        def itemDB = Fatura.findByNumeroDaFatura(id_)

        if(itemDB.equals(null)){
            Messagebox.show("Não foi localizado a Fatura com o ID= "+id, "Lua", 1,  Messagebox.ERROR)
        }else {
            sessionStorageService.fatura=itemDB
            Executions.sendRedirect("/fatura/edit")
        }

    }



    @Init init() {

    }


}
