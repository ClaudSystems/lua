package vd

import base.ComposersService
import base.EncryptionService
import grails.plugin.springsecurity.SpringSecurityService
import lua.Settings
import lua.contador.ContadorService
import lua.entidades.cliente.Cliente
import lua.estoque.estoque.Armazem
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.iva.Iva
import lua.estoque.produto.Produto
import lua.security.Utilizador
import lua.vendas.fatura.Vd
import org.zkoss.bind.annotation.BindingParam


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

class NewVdViewModel {
    Settings settings = Settings.findByDesigracao("settings")
    @Wire Label info
    String blue="color:blue"
    String red = "color:red"
    SpringSecurityService springSecurityService
    ContadorService contadorService
    ComposersService composersService
    boolean viewCliente = false
    private String filter
    private String codigo
    private ListModelList<ItemProduto> items
    private ListModelList<Produto> itemsProduto
    private List<Cliente> clientes
    private  cliente = new Cliente()
    ItemProduto selectedItemProduto
    private Boolean btsalvar =false
    Vd vd = new Vd()

    Boolean getBtsalvar() {
        return btsalvar
    }

    @NotifyChange("btsalvar")
    @Command
    public void removeBtSalvar(){
        btsalvar = false
    }

    @NotifyChange(["cliente","clientes"])
    @Command
    public  void salvarCliente(){
        while (cliente.codigo.equals(null)||cliente.nome.equals(null)||cliente.nuit.equals(null)){
           // Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.style = red
            info.value = "Preecha os campos!"
            return
        }
        if (Cliente.findAllByCodigo(cliente.codigo)) {
           // Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
            info.value = "o valor do codigo ja existe!"
            info.style = red
            return
        }

        cliente.save()
        clientes.add(cliente)

       // Messagebox.show("Cliente "+cliente.nome+" foi criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Cliente "+cliente.nome+" foi criado com sucesso!"
        info.style = blue
        cliente=null
    }

    String getCodigo() {
        return codigo
    }

    void setCodigo(String codigo) {
        this.codigo = codigo
    }

    @Command
    @NotifyChange(["items","codigo"])
    def addItemByCode(){
        if(!codigo.empty){
            ItemProduto itemProduto = new ItemProduto()
            Produto p = Produto.findByCodigo(codigo)
            if (p.equals(null)){
                return
            }
            if(!settings.aceitarSaidasSemEstoque){
                if(p.saldo<=0){
                    info.value = "Não tem saldo suficiente!"
                    info.style = red
                    return
                }
            }
            itemProduto.setProduto(p)
            itemProduto.ivaAplicado = p.ivaAplicado
            itemProduto.qtd = 1
            if(settings.refletirSaidasEmEstoqueDeVds){
                itemProduto.operacao = "saida"
            }else {
                itemProduto.operacao = "vd"
            }

            itemProduto.setDescricao(p.descricao)
            itemProduto.setArmazem(Armazem.findByCodigo("A1"))
            itemProduto.precoDeVenda = p.precoDeVenda
            items.add(itemProduto)
            codigo=""
        }
        updateVd()
    }
    Cliente cliente = new Cliente(nome: "",codigo: "",)
    boolean labelInfo = false

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
    @NotifyChange(["viewCliente","cliente","info"])
    def showCliente(){

        if (viewCliente){
            viewCliente = false

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


           /* Messagebox.show("Tem certeza que deseja eliminar esta Cotação?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        vd.delete flush: true
                        items.removeAll()
                        Executions.sendRedirect("/vd/list")
                    }
                }
            }
            )*/
            vd.itemsProduto = null
            vd.delete flush: true
            items.removeAll()
            info.value = "O VD "+vd.numeroDeVd+" Foi Eliminado!"
            info.style = red
           // Executions.sendRedirect("/vd/list")

        }catch (Exception e ){
         //   Messagebox.show("Selecione Um Cliente! "+e.message)
            info.value = "Selecione Um Cliente! "+e.message
            info.style = red
        }


    }

    @Command
    @NotifyChange(["cliente","clientes","vd","labelInfo","info"])
    def createVd(){
        info.value = ""
        try {
            vd.numeroDeVd = contadorService.gerarNumeroDeVd()

            System.println("listagem dos items da vd")
            if(cliente.equals(null)){
              //  Messagebox.show("Seleccione pelo menos um cliente! ", "Lua", 1,  Messagebox.ERROR)
                info.value = "Seleccione pelo menos um cliente! "
                info.style = blue
                return
            }
            if(vd.itemsProduto?.empty){
              //  Messagebox.show("Seleccione pelo menos um item válido! ", "Lua", 1,  Messagebox.ERROR)
                info.value = "Seleccione pelo menos um item válido! "
                info.style = red
                return
            }
            for (ItemProduto ip in vd.itemsProduto){
                System.println(ip)
                if(ip.qtd==0||ip.precoDeVenda==0||ip.subTotal==0){
                    info.value = ip.produto.nome+" tem dados Inválidos!"
                    info.style = red
                    return
                }
                if(!settings.aceitarSaidasSemEstoque){
                    if(produto.saldo<ip.qtd){
                        info.value = ip.produto.nome+" Não tem saldo suficiente!"
                        info.style = red
                        return
                    }
                }

            }
            System.println("__-----------------------------------__")

            vd.setCliente(Cliente.findByCodigo(cliente.codigo))
            if(vd.cliente.equals(null)){
              //  Messagebox.show("Seleccione pelo menos cliente! ", "Lua", 1,  Messagebox.ERROR)
                info.value = "Seleccione pelo menos cliente! "
                info.style=red
                return
            }
            if(vd.utilizador.equals(null)){
              //  Messagebox.show("Seleccione um Utilizador! ", "Lua", 1,  Messagebox.ERROR)
                info.value = "Utilizador Inválido! "
                info.style=red
                return
            }
            if(items.empty){
               // Messagebox.show("Seleccione pelo menos um produto! ", "Lua", 1,  Messagebox.ERROR)
                info.value = "Seleccione pelo menos um produto! "
                info.style = red
                return
            }
            for(ItemProduto ip in vd.itemsProduto){
                if(ip.produto.codigo.empty){
                  //  Messagebox.show("Elimine os items sem codigo valido!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "Elimine os items sem codigo valido!"
                    info.style = red
                    return
                }
                if(ip.qtd<=0){
                   // Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!"
                    return
                }
                if(ip.precoDeVenda<=0){
                    info.value = "O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!"
                    info.style = red
                  //  Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!", "Lua", 1,  Messagebox.ERROR)
                    return
                }

            }

            vd.itemsProduto = items

            System.println(vd.valorDoIva)
            System.println(vd.valor)
            System.println(vd.itemsProduto)
            System.println(vd.dateCreated)
            System.println(vd.cliente)
            //vd.setEstado("Aberto")
            vd.pago = true

            vd.cliente.save()

            vd.save flush: true
            System.println("vd.save flash:true")
            def vdDB = Vd.findById(vd.id)
            System.println(vdDB)
            if(Vd?.findById(vd?.id)){
                labelInfo=true
                // Messagebox.show("Os dados da Vd No. "+vdDB.id+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                info.value = "Os dados da Vd No. "+vdDB.numeroDeVd+" foram criados com sucesso!"
                info.style = blue
            }
        } catch (Exception e ){
            // Messagebox.show("Erro na gravação "+e.message, "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na gravação "+e.message
            info.style = red

        }



    }


    @NotifyChange(["cliente","clientes"])
    def createCliente(){

        try {

            cliente.save flush: true

            System.println("novo cliente criado com sucesso"+Cliente.findByCodigo(cliente.codigo))


            vd.save flush: true

        } catch (Exception e ){
          //  Messagebox.show("Erro na gravação "+e.message, "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na gravação "+e.message
            info.style = red
            return
        }
        if (vd.id.equals(null)){
          //  Messagebox.show("Erro na gravação ", "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na gravação "
            info.style = red
            return
        }
      //  Messagebox.show("Os dados Do Cliente No. "+vd.id+" foram criados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Os dados Do Cliente No. "+vd.numeroDeVd+" foram criados com sucesso!"
        info.style = blue
    }

    @Command
    @NotifyChange(["items","vd","info"])
    def addSelectedItem(@BindingParam("index") Long index){
        if(cliente.equals(null)){
           // Messagebox.show("Selecione o cliente!", "Lua", 1,  Messagebox.ERROR)
            info.value = "Selecione o cliente!"
            info.style = blue
            return
        }
        System.println("addSelectedItem: "+index)
        ItemProduto itemProduto = new ItemProduto()
        Produto p = Produto.findById(index)
        if(!settings.aceitarSaidasSemEstoque){
            if(p.saldo<=0){
                info.value = "Não tem saldo suficiente!"
                info.style = red
                return
            }
        }
        itemProduto.setProduto(p)
        if(settings.refletirSaidasEmEstoqueDeVds){
            itemProduto.operacao = "saida"
        }else {
            itemProduto.operacao = "vd"
        }

        itemProduto.setDescricao(p.descricao)
        itemProduto.precoDeVenda = p.precoDeVenda
        itemProduto.ivaAplicado = p.ivaAplicado
        itemProduto.qtd = 1
        System.println(itemProduto.precoDeVenda)
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        items.add(itemProduto)
        updateVd()
    }



    @NotifyChange("itemsProduto")
    @Command
    void doSearch() {
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


    @Init
    init() {

        vd.utilizador =(Utilizador.findById(springSecurityService.principal?.id))
        vd.cliente= cliente
        vd.valorDoIva = 0.0
        vd.valor = 0.0
    }

    @Command
    @NotifyChange("cliente")
    Cliente getCliente() {
        return  Cliente?.findById(cliente?.id)
    }

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

        if (items == null) {
            items= new ListModelList<ItemProduto>()
        }
        return items
    }

    @Command
    @NotifyChange(["items","vd","info"])
    def addItem(){
        ItemProduto itemProduto = new ItemProduto()
        Produto produto = new Produto()
        produto.codigo=""

        if(!settings.aceitarSaidasSemEstoque){
            if(produto.saldo<=0){
                info.value = "Não tem saldo suficiente!"
                info.style = red
                return
            }
        }
        itemProduto.setProduto(produto)
        if(settings.refletirSaidasEmEstoqueDeVds){
            itemProduto.operacao = "saida"
        }else {
            itemProduto.operacao = "vd"
        }
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        itemProduto.precoDeVenda = produto.precoDeVenda

        items.add(itemProduto)
    }

    @Command
    @NotifyChange(["items","vd"])
    void removeItem(@BindingParam("index") Integer index) {
        System.println(index)
        items.remove(index)
        updateVd()
    }

    @Command
    @NotifyChange(["vd","info"])
    def  updateVd(){
        vd.setValor(0.0)
        vd.setValorDoIva(0.0)
        def totalIva = 0.0
        def subTotal = 0.0
        Iva iva = Iva.findByValido(true) as Iva
        if(iva.equals(null)){
         //  Messagebox.show("Por favor Introduza o iva no sistema!", "Lua", 1,  Messagebox.ERROR)
            info.value = "Por favor Introduza o iva no sistema!"
            info.style = red
        }
        System.println("iva = "+iva.percentualIva)
        for (ItemProduto ip in items){
            if(ip.ivaAplicado){
                if(ip.qtd==0){
                    info.value = "Não tem QTD suficiente!"
                    info.style = red
                    return
                }
                ip.subTotal = ip.precoDeVenda*ip.qtd
                totalIva+=ip.subTotal*iva.percentualIva/100
                System.println("ip.subTotal="+ip.subTotal)
                ip.valorDoIva = ip.subTotal*iva.percentualIva/100
                System.println("updateVd ip.valorDoIva = "+ip.valorDoIva)
            }else {
                ip.valorDoIva=0.0
            }
            subTotal+=ip.subTotal
        }
        System.println("upDateVd subtotal "+subTotal)
        System.println("updateVd total iva= "+totalIva)
        vd.setValor(subTotal)
        vd.setValorDoIva(totalIva)
        System.println("upDateVd valorDoIva "+vd.valorDoIva)
        System.println("updateVd vd.valor= "+vd.valor)
    }




    @Command
    def static fecharEditor(){
        Executions.sendRedirect("/vd/listagemVD")
    }




    @Command
    @NotifyChange(["vd"])
    def alterarIva(@BindingParam("index") Integer index){
        items[index].ivaAplicado=!items[index].ivaAplicado
        updateVd()
    }

    @Command
    @NotifyChange(["info"])
    def  imprimir(){
        btsalvar = false
        if(Vd?.findById(vd?.id)){
            composersService.vdId = vd.id
                if(settings.imprimirMiniVd){
                    Executions.sendRedirect("/vd/ImprimirPagamento")
                }else {
                    Executions.sendRedirect("/vd/imprimirVd")
                }

        }else
          //  Messagebox.show("Por favor Salve a vd antes!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Por favor Salve a vd antes!"
        info.style = blue



    }


    @Command
    @NotifyChange(["info","items"])
    def showMe(){

        items.each {
            if(!Produto.findByCodigo(it.produto.codigo)){
              //  Messagebox.show("Codido do produto errado!"+selectedItemProduto, "Lua", 1, Messagebox.ERROR)
                info.style = red
                info.value = "Codido do produto errado!"+selectedItemProduto

            }else {

            }


        }
    }

    @Command
    def    editarFatura(){
        info.value=""
        System.println("o botão editarFatura foi clicado")
        composersService.faturaId=fatura.id
        if(fatura.id!=null){
            Executions.sendRedirect("/fatura/edit")
        }else {
            info.value="Selecione uma fatura!"
            info.style ="color:red"
        }

    }

    @Command
    def    editarVd(){
        btsalvar = false
        info.value=""
        System.println("o botão editarVd foi clicado")
        composersService.vdId=vd.id
        composersService.vd = vd
        if(vd.id!=null){
            Executions.sendRedirect("/vd/editVd")
        }else {
            info.value="Selecione uma VD!"
            info.style ="color:red"
        }

    }
}
