package saidadeproduto

import base.ComposersService
import grails.plugin.springsecurity.SpringSecurityService
import lua.Settings
import lua.contador.ContadorService
import lua.entidades.cliente.Cliente
import lua.estoque.destino.Destino
import lua.estoque.estoque.Armazem
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.iva.Iva
import lua.estoque.produto.Produto
import lua.security.Utilizador
import lua.estoque.saidaDeProduto.SaidaDeProduto
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import java.sql.SQLException

class NewSaidaViewModel {
    def settings = Settings.findByDesigracao("settings")
    ContadorService contadorService
    SpringSecurityService springSecurityService
    boolean showDestino = false
    @Wire Label info
    String blue = "color:blue"
    String red = "color:red"
    private String filter
    private String codigo
    private ListModelList<ItemProduto> items
    private ListModelList<Produto> itemsProduto
    private ListModelList<Destino> destinos
    private ListModelList<Cliente> clientes
    ItemProduto selectedItemProduto
    ComposersService composersService
    SaidaDeProduto saida = new SaidaDeProduto()
    Destino selectedDestino = new Destino()
    Cliente selectedCliente = new Cliente()

    Cliente getSelectedCliente() {
        return selectedCliente
    }

    void setSelectedCliente(Cliente selectedCliente) {
        this.selectedCliente = selectedCliente
    }

    ListModelList<Cliente> getClientes() {
        if(clientes==null){
            clientes = new ListModelList<>(Cliente.all)
        }
        return clientes
    }

    ListModelList<Destino> getDestinos() {
        if(destinos == null){
            destinos = new ListModelList<>(Destino.all)
        }
        return destinos
    }

    String getCodigo() {
        return codigo
    }

    void setCodigo(String codigo) {
        this.codigo = codigo
    }

    boolean getShowDestino() {
        return showDestino
    }

    void setShowDestino(boolean showDestino) {
        this.showDestino = showDestino
    }


    void setSelectedDestino(Destino selectedDestino) {
        this.selectedDestino = selectedDestino
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
    def deleteSaidaDeProduto(){

        try {

            items.removeAll()

        }catch (Exception e ){
            //  Messagebox.show("Selecione Um TipoDeSaida!")
            info.value = "Selecione Um TipoDeSaida!"+e.toString()
        }


    }

    @Command
    @NotifyChange(["info"])
    def createSaidaDeProduto(){
        try {
            saida.cancelado = false
            if(selectedCliente.nome!=null){
                saida.cliente = selectedCliente
            }
            saida.utilizador =  Utilizador.findById(springSecurityService.principal?.id)
            saida.numeroDeSaida = contadorService.gerarNumeroDeSaida()
            System.println("createSaidaDeProduto numero de saida ="+saida.numeroDeSaida)
            if(selectedDestino?.nome==null){
                // Messagebox.show("Selecione um TipoDeSaida!", "Lua", 1, Messagebox.ERROR)
                info.value = "Selecione um Destino!"
                info.style = red
                return
            }
            saida.destino = selectedDestino

            if(saida.itemsProduto?.empty){
                //   Messagebox.show("Seleccione pelo menos um item válido! ", "Lua", 1,  Messagebox.ERROR)
                info.value = "Seleccione pelo menos um item válido! "
                info.style = red
                return
            }
            if(items.empty){
                // Messagebox.show("Seleccione pelo menos um produto! ", "Lua", 1,  Messagebox.ERROR)

                info.value = "Seleccione pelo menos um produto! "
                info.style = red
                return
            }
            for(ItemProduto ip in items){
                if(ip.produto.codigo.empty){
                    //   Messagebox.show("Elimine os items sem codigo valido!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "Elimine os items sem codigo valido!"
                    return
                }
                if(ip.qtd<=0){
                    // Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!", "Lua", 1,  Messagebox.ERROR)
                    info.value= "O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!"
                    info.style = red
                    return
                }
                if(!settings.aceitarSaidasSemEstoque){
                    if(ip.qtd>ip.produto.saldo){
                       info.value = "O Item "+ip.produto.codigo+" Não tem saldo Suficiente!"
                        return
                    }
                }
               /* if(ip.precoDeCompra<=0){
                    // Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!"
                    info.style = red
                    return
                }*/
                ip.save()
            }
            System.println("createSaidaDeProduto Destino="+saida.destino.nome)

            saida.itemsProduto = items
            saida.save flush: true
            if(SaidaDeProduto.find(saida)){
                //   Messagebox.show("Os dados com a ref. No. "+saida.id+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                info.value = "Os dados com a ref. No. "+saida.numeroDeSaida+" foram gerados com sucesso!"
                info.style = blue

            }else
            //  Messagebox.show("Erro na gravação! ", "Lua", 1,  Messagebox.ERROR)
                info.value = "Erro na gravação! "
            info.style = red


        } catch (SQLException ex ){
            System.println(ex.toString())
            // Messagebox.show("Erro na gravação!! ", "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na gravação!"
        }

       // Executions.sendRedirect("/saidaDeProduto/list")
    }

    @Command
    @NotifyChange(["items","info"])
    def addSelectedItem(@BindingParam("index") Long index){
        info.value = ""
        System.println("addSelectedItem: "+index)
        ItemProduto itemProduto = new ItemProduto()
        Produto p = Produto.findById(index)
        if(!settings.aceitarSaidasSemEstoque){
            if(p.saldo<=0){
                info.value = "O Produto "+p.nome+" não tem saldo Suficiente!"
                info.style = blue
                return
            }
        }

        itemProduto.setProduto(p)
        itemProduto.precoDeVenda=p.precoDeVenda
        itemProduto.setDescricao(p.descricao)
        itemProduto.operacao ="saida"
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        items.add(itemProduto)

    }

    @Command
    @NotifyChange(["items","codigo","info"])
    def addSelectedItemByCode(){
        info.value = ""
        if(!codigo.empty){
            ItemProduto itemProduto = new ItemProduto()
            Produto p = Produto.findByCodigo(codigo)
            if (p.equals(null)){
                return
            }
            itemProduto.setProduto(p)
            itemProduto.setDescricao(p.descricao)
            itemProduto.operacao = "saida"
            itemProduto.setArmazem(Armazem.findByCodigo("A1"))
            // itemProduto.setPreco(p.itemsProduto.last().precoDeCompra)
            items.add(itemProduto)
            codigo=""
        }




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
                        c.descricao?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0
                ) {
                    itemsProduto.add(c)
                }
            }
        }
    }

    @Command
    def    editarSaidaDeProduto(){
        System.println("o botão editarSaidaDeProduto foi clicado")
        composersService.editarSaidaDeProduto = true
        Executions.sendRedirect("/saida/SaidaDeProduto")
    }


    @Init
    init() {


    }


    SaidaDeProduto getSaida() {
        return saida
    }

    void setSaida(SaidaDeProduto saida) {
        this.saida = saida
    }



    @NotifyChange(["items","saida","info"])
    BigDecimal getSubTotal() {
        info.value = ""
        // return composersService.saida.valor
        return subTotal
    }


    @NotifyChange("items")
    ListModelList<ItemProduto> getItems() {
        if (items == null) {
            items= new ListModelList<ItemProduto>()
        }
        return items
    }

    @Command
    @NotifyChange("items")
    def addItem(){
        ItemProduto itemProduto = new ItemProduto()
        Produto produto = new Produto()
        produto.codigo=""
        itemProduto.setProduto(produto)
        itemProduto.operacao = "saida"
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))

        items.add(itemProduto)
        items.add(itemProduto)


    }

    @Command
    @NotifyChange(["items","saida","info"])
    public void removeItem(@BindingParam("index") Integer index) {
        info.value= ""
        System.println(index)
        items.remove(index)
        updateSaida()
    }


    @Command
    @NotifyChange(["items","info"])
    def showMe(){
        info.value = ""
        items.each {
            def produtoDB = Produto.findByCodigo(it.produto.codigo)
            if(!produtoDB){
                // Messagebox.show("Codido do produto errado!"+selectedItemProduto, "Lua", 1, Messagebox.ERROR)
                info.value = "Codido do produto errado!"+selectedItemProduto

            }else {
                it.descricao=produtoDB.descricao

            }


        }
    }

    @Command
    def fecharEditor(){
        Executions.sendRedirect("/saidaDeProduto/saidaList")
    }

    @Command
    @NotifyChange(["saida"])
    def  updateSaida(){
        saida.setValor(0.0)
        saida.setValorDoIva(0.0)
        def totalIva = 0.0
        def subTotal = 0.0
        Iva iva = Iva.findByValido(true) as Iva
        System.println("iva = "+iva.percentualIva)
        for (ItemProduto ip in items){
            System.println("update saida ip.sub totoal="+ip.subTotal)
            System.println("update saida iva apliado="+ip.ivaAplicado)
            if(ip.ivaAplicado){
                ip.subTotal=ip.qtd*ip.precoDeCompra
                ip.valorDoIva+=ip.precoDeCompra*iva.percentualIva/100
                totalIva+=ip.valorDoIva
                System.println("ip.subTotal="+ip.subTotal)
                ip.valorDoIva = ip.subTotal*iva.percentualIva/100
                System.println("updateFatura ip.valorDoIva = "+ip.valorDoIva)
            }else {
                ip.valorDoIva=0.0
            }

            subTotal+=ip.subTotal
            totalIva+=ip.valorDoIva

        }
        System.println("upDateFatura subtotal "+subTotal)
        System.println("updatecotacao total iva= "+totalIva)
        saida.setValor(subTotal)
        saida.setValorDoIva(totalIva)
        System.println("upDateEntrada valorDoIva "+saida.valorDoIva)
        System.println("updatesaida saida.valor= "+saida.valor)
    }

    @Command
    def  imprimir(){

        if(SaidaDeProduto.findById(saida.id)){
            composersService.saidaDeProduto = saida
            Executions.sendRedirect("/saidaDeProduto/imprimir")
            saida=new SaidaDeProduto()
            selectedCliente = null
        }else
        //  Messagebox.show("Por favor Salve a fatura antes!", "Lua", 1,  Messagebox.INFORMATION)
        {
            info.style="color:blue"
            info.value ="Por favor Salve a fatura antes!"
        }
    }


}
