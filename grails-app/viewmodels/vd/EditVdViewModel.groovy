package vd

import base.ComposersService
import lua.Settings
import lua.SettingsService
import lua.entidades.cliente.Cliente
import lua.estoque.categoria.Categoria
import lua.estoque.estoque.Armazem
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.iva.Iva
import lua.estoque.produto.Produto
import lua.vendas.fatura.Vd
import lua.vendas.recibo.Cheque
import lua.vendas.recibo.FormaDePagamento
import lua.vendas.recibo.Recibo
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox

class EditVdViewModel {
    SettingsService settingsService
    @Wire Label info
    boolean  viewCheque = false
    String red = "color:red"
    String blue = "color:blue"
    boolean viewCliente = false
    boolean dvProdutosVisible = true
    boolean dvPagamentosVisible = false
    boolean viewBtActualizar
    private String filter
    boolean bt_salvar_pagamento = false
    private ListModelList<ItemProduto> items
    private ListModelList<Produto> itemsProduto
    private ListModelList<FormaDePagamento> formasDePagamento
    private selectedFormaDePagamento
    private List<Cliente> clientes
    ItemProduto selectedItemProduto
    ComposersService composersService
    Cheque cheque
    Vd vd
    BigDecimal subTotal = 0.0
    BigDecimal totalIva = 0.0
    Cliente cliente

    boolean getBt_salvar_pagamento() {
        return bt_salvar_pagamento
    }

    boolean getViewCheque() {
        return viewCheque
    }

    @Command
    @NotifyChange(["viewCheque"])
    def showCheque(){
        System.println(selectedFormaDePagamento)
        if (selectedFormaDePagamento.toString()=="Cheque"){
            System.println("Cheque=="+selectedFormaDePagamento)
            viewCheque = true
        }else {
            viewCheque= false
        }

    }
    ListModelList<FormaDePagamento> getFormasDePagamento() {
        if (formasDePagamento == null) {
            formasDePagamento = new ListModelList<FormaDePagamento>(FormaDePagamento.all)
        }
        return formasDePagamento
    }

    def getSelectedFormaDePagamento() {
        return selectedFormaDePagamento
    }

    void setSelectedFormaDePagamento(selectedFormaDePagamento) {
        this.selectedFormaDePagamento = selectedFormaDePagamento
    }

    boolean getViewBtActualizar() {
        return viewBtActualizar
    }

    BigDecimal getSubTotal() {
        return subTotal
    }



    @Command
    @NotifyChange("totalIva")
    void changeValue()
    {
        checked=!checked
        getTotalIva()
    }




    boolean getChecked()
    {
        return checked
    }

    void setChecked(Boolean checked)
    {
        this.checked=checked
    }

    boolean getViewCliente() {
        return viewCliente
    }

    void setViewCliente(boolean viewCliente) {
        this.viewCliente = viewCliente
    }

    @Command
    @NotifyChange(["viewCliente","showCliente"])
    def showCliente(){
        if (viewCliente){
            viewCliente= false

        }
        else viewCliente = true




    }

    void setCliente(Cliente cliente) {
        this.cliente = cliente
    }

    ListModelList<Produto> getItemsProduto() {
        if (itemsProduto == null) {
            itemsProduto = new ListModelList<Produto>(Produto.all)
        }
        return itemsProduto
    }

    void setItemsProduto(ListModelList<Produto> itemsProduto) {
        this.itemsProduto = itemsProduto
    }

    String getFilter() {
        return filter
    }

    void setFilter(String filter) {
        this.filter = filter
    }

    ItemProduto getSelectedItemProduto() {
        return selectedItemProduto
    }

    void setSelectedItemProduto(ItemProduto selectedItemProduto) {
        this.selectedItemProduto = selectedItemProduto
    }

    @Command
    @NotifyChange(["items","info"])
    def deleteVd(){
        try {


           /* Messagebox.show("Tem certeza que deseja eliminar esta Vd?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {

                    }
                }
            }
            );*/
            vd.delete flush: true
            items.removeAll()
            Executions.sendRedirect("/vd/listagemVD")

        }catch (Exception e ){
           // Messagebox.show("Selecione Um Cliente!")
            info.value= "Selecione Um Cliente!"
            info.style = red
        }


    }



    @Command
    @NotifyChange(["info","vd"])
    def updateVd(){
        try {
            System.println(cliente.nome)
            Cliente c = Cliente.findById(cliente.id)
            c.codigo = cliente.codigo
            c.nome = cliente.nome
            c.nuit = cliente.nuit
            c.endereco = cliente.endereco
            c.numTelefone= cliente.numTelefone
            c.save()
            for(ItemProduto ip in vd.itemsProduto){
                if(!settingsService.settings.aceitarSaidasSemEstoque){
                    if(produto.saldo<ip.qtd){
                        info.value = ip.produto.nome+" Não tem saldo suficiente!"
                        info.style = red
                        return
                    }
                }
                if(ip.produto.codigo.empty){
                    // Messagebox.show("Elimine os items sem codigo valido!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "Elimine os items sem codigo valido!"
                    info.style = red
                    return
                }
                if(ip.qtd<=0){
                   // Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!"
                    info.style = red
                    return
                }
                if(ip.precoDeVenda<=0){
                  //  Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!"
                    info.style = red
                    return
                }
                ip.save()
            }
            // vd.cliente.save()
            vd.valor=subTotal+totalIva
            vd.valorDoIva=getTotalIva()
            vd.save flush: true

        } catch (Exception e ){
          //  Messagebox.show("Erro na gravação da Vd No. "+vd.id+":"+ e.toString(), "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na gravação da Vd No. "+vd.numeroDeVd+":"+ e.toString()
            info.style = red
            return
        }

      //  Messagebox.show("Os dados da Vd No. "+vd.id+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Os dados da Vd No. "+vd.id+" foram actualizados com sucesso!"
        info.style = blue



        // Executions.sendRedirect("/vd/edit")
    }

    @Command
    @NotifyChange(["items","info","vd","subTotal","totalIva","total"])
    def addSelectedItem(@BindingParam("index") Long index){
        System.println("addSelectedItem"+index)
        ItemProduto itemProduto = new ItemProduto()
        Produto p = Produto.findById(index)
        if(!settingsService.settings.aceitarSaidasSemEstoque){
            if(p.saldo<=0){
                info.value = "Não tem saldo suficiente!"
                info.style = red
                return
            }
        }
        itemProduto.setProduto(p)
        if(settingsService.settings.refletirSaidasEmEstoqueDeVds){
            itemProduto.operacao = "saida"
        }else {
            itemProduto.operacao = "vd"
        }
        itemProduto.setProduto(p)
        itemProduto.setDescricao(p.descricao)
        itemProduto.ivaAplicado = p.ivaAplicado
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        itemProduto.qtd = 1
        itemProduto.precoDeVenda = p.precoDeVenda
        items.add(itemProduto)
        vd.addToItemsProduto(itemProduto)
        calcularSubTotal()
    }

    @NotifyChange(["itemsProduto","info"])
    @Command
    public void doSearch() {
    info.value = ""
        itemsProduto.clear()
        List<Produto> allItems = Produto.all
        if (filter == null || "".equals(filter)) {
            itemsProduto.addAll(allItems)
        } else {
            for (Produto c : allItems) {
                if (c.codigo.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.nome.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.precoDeVenda.toString().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.valorDoIva.toString().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.precoTotalDeVenda.toString().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.descricao?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0
                ) {
                    itemsProduto.add(c)
                }
            }
        }
    }




    @Init
    init() {

        vd = Vd.findById(composersService.vdId)
        cliente = vd.cliente
        subTotal=vd.valor
        totalIva =vd.valorDoIva



    }

    Cliente getCliente() {
        return cliente    }

    Vd getVd() {
        return vd
    }

    void setVd(Vd vd) {
        this.vd = vd
    }



    List<Cliente> getClientes() {
        if (clientes == null) {
            clientes = new ArrayList<Cliente>(Cliente.all)
        }

        return clientes
    }


    @NotifyChange("items")
    ListModelList<ItemProduto> getItems() {
        if (vd.equals(null)){
            return
        }
        if (items == null) {
            if (!(vd==null))
                items=vd.itemsProduto
        }
        return items
    }

    @Command
    @NotifyChange(["items","subTotal","totalIva","total"])
    def addItem(){
        ItemProduto itemProduto = new ItemProduto()
        Produto produto = new Produto()
        produto.codigo=""
        itemProduto.setProduto(produto)
        itemProduto.ivaAplicado = produto.ivaAplicado
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        itemProduto.qtd = 1
        items.add(itemProduto)
        vd.addToItemsProduto(itemProduto)
        calcularSubTotal()
    }

    @Command
    @NotifyChange(["items","subTotal","totalIva","total"])
    public void removeItem(@BindingParam("index") Integer index) {
        System.println(index)
        items.remove(index)
        calcularSubTotal()
    }


    @Command
    @NotifyChange("items")
    def showMe(){

        items.each {
            if(!Produto.findByCodigo(it.produto.codigo)){
                Messagebox.show("Codido do produto errado!"+selectedItemProduto, "Lua", 1, Messagebox.ERROR)

            }

        }
    }

    @Command
    def static fecharEditor(){
        Executions.sendRedirect("/vd/listVd")
    }

    @Command
    def  imprimir(){
        composersService.vdId=vd.id
        //  Executions.sendRedirect("/vd/showVd")
        if(settingsService.settings.imprimirMiniVd){
            Executions.sendRedirect("/vd/ImprimirPagamento")
        }else {
            Executions.sendRedirect("/vd/imprimirVd")
        }

    }

    @Command
    @NotifyChange(["totalIva","subTotal","total","vd","viewBtActualizar"])
    def alterarIva(@BindingParam("index") Integer index){
        // System.println(items[index].ivaAplicado)
        items[index].ivaAplicado=!items[index].ivaAplicado
        getTotalIva()
        calcularSubTotal()
        // System.println(items[index].ivaAplicado)
    }

    @NotifyChange(["totalIva","subTotal","total","vd"])
    BigDecimal getTotalIva(){
        Iva iva = Iva.findByValido(true) as Iva
        BigDecimal vi = 0.0
        for (ItemProduto ip in vd.itemsProduto){
            if(ip.ivaAplicado){
                System.println("calcular iva valido")

                if(!ip?.subTotal?.equals(null)){
                    System.println("sub total nao nulo . iva calculado")
                    vi+=ip?.subTotal*iva?.percentualIva/100
                    ip.valorDoIva=vi
                }

            }else ip.valorDoIva=0.0
        }
        System.println("iva = "+vi)
        vd.valorDoIva = vi
        totalIva = vi
        return vi
    }

  /*  @Command
    @NotifyChange(["subTotal","totalIva","total", "viewBtActualizar","vd"])
    def calcularSubTotal(){
        def sb = 0.0

        for (ItemProduto ip in items){
            if(!ip.produto.equals(null)){
                if(!ip.precoDeVenda.equals(null)){
                    sb+=ip?.precoDeVenda*ip?.qtd
                }

            }

        }
        subTotal=sb
        viewBtActualizar=true

    }*/

    @Command
    @NotifyChange(["viewBtActualizar","fatura","iva","valor","info"])
    def calcularSubTotal(){
        info.value=""
        vd.setValor(0.0)
        vd.setValorDoIva(0.0)
        def totalIva = 0.0
        def subTotal = 0.0
        Iva iva = Iva.findByValido(true) as Iva
        if(iva.equals(null)){
            // Messagebox.show("Por favor Introduza o iva no sistema!", "Lua", 1,  Messagebox.ERROR)
            info.value = "Por favor Introduza o iva no sistema!"
        }
        System.println("iva = "+iva.percentualIva)
        for (ItemProduto ip in items){
            if(ip.ivaAplicado){
                ip.subTotal = ip.precoDeVenda*ip.qtd
                totalIva+=ip.subTotal*iva.percentualIva/100
                System.println("ip.subTotal="+ip.subTotal)
                ip.valorDoIva = ip.subTotal*iva.percentualIva/100
                System.println("updateVD ip.valorDoIva = "+ip.valorDoIva)
            }else {
                ip.valorDoIva=0.0
                ip.subTotal = ip.precoDeVenda*ip.qtd
            }

            subTotal+=ip.subTotal

        }
        viewBtActualizar=true
        System.println("upDateFatura subtotal "+subTotal)
        System.println("updatefatura total iva= "+totalIva)
        vd.setValor(subTotal)
        vd.setValorDoIva(totalIva)
        System.println("upDateFatura valorDoIva "+vd.valorDoIva)
        System.println("updatefatura fatura.valor= "+vd.valor)
    }

  /*  @Command
    @NotifyChange(["dvProdutosVisible","dvPagamentosVisible"])
    def pagar(){

        if(dvPagamentosVisible){
            dvPagamentosVisible = false
            dvProdutosVisible = true
        }else {
            dvProdutosVisible = false
            dvPagamentosVisible = true
        }
    }*/

    @Command
    @NotifyChange(["dvProdutosVisible","dvPagamentosVisible","bt_update"])
    def pagar(){
        if(vd.pago){
            info.value="Esta VD ja foi paga na totalidade!"
            info.style="color:blue"
        }
        cheque = new Cheque()
        if(dvPagamentosVisible){
            dvPagamentosVisible=false
            dvProdutosVisible = true
        }else {
            dvPagamentosVisible = true
            dvProdutosVisible = false
        }

    }

    ListModelList<Categoria> getCategorias() {
        if(categorias==null){
            categorias = new ListModelList<Categoria>(Categoria.all.sort())
        }
        return categorias.sort()
    }
}
