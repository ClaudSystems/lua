package pos

import base.ComposersService
import lua.estoque.itemProduto.ItemProduto
import lua.restaurante.mesa.Diario
import lua.restaurante.mesa.DiarioService
import lua.restaurante.mesa.Mesa
import lua.restaurante.mesa.MesaService
import lua.vendas.fatura.Vd
import lua.vendas.fatura.VdService
import lua.vendas.recibo.FormaDePagamento
import lua.vendas.recibo.Recibo
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox

class ListagemDeVdsViewModel {
    @Wire Label info
    Vd vd
    VdService vdService
    private ListModelList<Vd> vds
    MesaService mesaService
    private String mesa_label
    Diario diario
    Recibo recibo
    private Mesa selectedMesa
    private ListModelList<Mesa> mesas
    DiarioService diarioService
    @Wire    Listbox lb_items
    ComposersService composersService
    private String filter = ""
    private String id
    private ListModelList<Item> items
    Vd item = new Vd()
    private selectedFormaDePagamento
    private ListModelList<FormaDePagamento> formasDePagamento

    def getSelectedFormaDePagamento() {
        return selectedFormaDePagamento
    }

    void setSelectedFormaDePagamento(selectedFormaDePagamento) {
        this.selectedFormaDePagamento = selectedFormaDePagamento
    }

    ListModelList<FormaDePagamento> getFormasDePagamento() {

        if (formasDePagamento == null) {
            formasDePagamento = new ListModelList<FormaDePagamento>(FormaDePagamento.all)
        }
        return formasDePagamento
    }
    Vd getVd() {
        return vd
    }

    Mesa getSelectedMesa() {
        return selectedMesa
    }

    void setSelectedMesa(Mesa selectedMesa) {
        this.selectedMesa = selectedMesa
    }

    ListModelList<Mesa> getMesas() {
        if(mesas==null){
            mesas = new ListModelList<>(Mesa.all)
        }
        return mesas
    }
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

    @NotifyChange("mesas")
    @Command
    void doSearch() {
        mesas.clear()
        List<Mesa> allItems = Mesa.all
        if (filter == null || "".equals(filter)) {
            mesas.addAll(allItems)
        } else {
            for (Mesa item : allItems) {
                if (item.numeroDaMesa.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item.localizacao.descricaoDoLocal.toString().indexOf(filter) >= 0 ||
                        item.estado.indexOf(filter) >= 0) {
                    mesas.add(item)
                }
            }
        }
    }

    @Command
    @NotifyChange(['vd'])
    void showVd(@BindingParam("id") Integer id) {
        System.println('showVd='+id)
        composersService.vdId=id
        vd = Vd.findById(id)
        composersService.mesa = selectedMesa
        Executions.sendRedirect("/pos/editPosVd")
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
            info.value = "Não foi localizado a Vd com o ID= "+id
        }else {
            composersService.vdId=itemDB.id
            Executions.sendRedirect("/pos/editPosVd")
        }

    }



    @Init init() {
        diario = diarioService.findByEstado('pendente')
        diario = diarioService.findByEstado("aberto")

    }

    @NotifyChange(["showMesas","selectedMesa","vds","selectedVd","items"])
    @Command
    def addSelectedMesa(){
        selectedMesa = mesaService.findById(selectedMesa.id)
        composersService.mesa = selectedMesa
        getVds()
    }


    ListModelList<Vd> getVds() {
        vds = new ListModelList<>()
            vds=diario.vds
        if(selectedMesa!=null){
            vds = selectedMesa.vds
        }
        return vds
    }

    @Command
    def static addItem(){
        Executions.sendRedirect("/pos/newPosVd")
    }

    @Command
    def static sair(){
        Executions.sendRedirect("/home/")
    }

    @Command
    @NotifyChange(['selectedVd','info',"recibos","bt_salvar_recibo"])
    def createRecibo(){
         recibo = new Recibo()
        if(vd.pago){
            info.value="Este VD Já foi paga na totalidade!"
            info.style="color:red"
            return
        }
        if(selectedFormaDePagamento==null){
            info.value="Por favor, Selecione a Forma de Pagamento!"
            info.style="color:red"
            return
        }
        if(recibo.valor<vd.valor ){
            info.value="O Valor pago não cobre o valor em dívida!"
            info.style="color:red"
            return
        }
        info.value=""
        System.println("createRecibo, viewCheque="+viewCheque)
        recibo.formaDePagamento = selectedFormaDePagamento.toString()
        System.println("selectedRecibo, recibo.formaDePagamento="+recibo.formaDePagamento)
        recibo.numeroDoRecibo=contadorService.gerarNumeroDoRecibo()
        System.println("selectedRecibo, recibo.numeroDoRecibo"+recibo.numeroDoRecibo)
        cheque.valorDoCheque=recibo.valor
        try {
            vd.pago = true
            info.style="color:blue"
            info.value="VD Pago!"
            vd.save(flush: true)
        }catch (Exception e){
            System.println(e.toString())
            // Messagebox.show("Erro na gravação do recibo!", "Lua", 1,  Messagebox.ERROR)
            info.style="color:red"
            info.value="Erro na gravação do recibo!"
        }

    }
}
