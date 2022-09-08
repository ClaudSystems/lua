package cotacao

import base.ComposersService
import base.EncryptionService
import lua.SessionStorageService
import lua.UtilizadorService
import lua.entidades.cliente.Cliente
import lua.estoque.estoque.Armazem
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.iva.Iva
import lua.estoque.produto.Produto
import lua.vendas.cotacao.Cotacao
import lua.vendas.fatura.Fatura
import org.zkoss.bind.annotation.Command

import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.BindingParam

import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox

class EditCotacaoViewModel {
    EncryptionService encryptionService
    UtilizadorService utilizadorService
    SessionStorageService sessionStorageService
    boolean viewCliente = false
    boolean viewBtActualizar = false
    private String filter
    private ListModelList<ItemProduto> items
    private ListModelList<Produto> itemsProduto
    private List<Cliente> clientes
    ItemProduto selectedItemProduto
    ComposersService composersService
    Cotacao cotacao = new Cotacao()
    BigDecimal valor
    BigDecimal iva
    Cliente cliente
    @Wire Label info
    String red = "color:red"
    String blue = "color:blue"

    BigDecimal getValor() {
        valor=cotacao.valor
        return valor
    }

    void setValor(BigDecimal valor) {
        this.valor = valor
    }

    BigDecimal getIva() {
        iva=cotacao.valorDoIva
        return iva
    }

    void setIva(BigDecimal iva) {
        this.iva = iva
    }

    boolean getViewBtActualizar() {
        return viewBtActualizar
    }



    @Command
    @NotifyChange(["cotacao","info"])
    void changeValue()
    {
        info.value =""
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
    @NotifyChange(["viewCliente","showCliente","info"])
    def showCliente(){
        info.value = ""
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
    def deleteCotacao(){
        info.value = ""
        try {
            if(!utilizadorService.getAccess("cotacao_d")){
               // Messagebox.show("Este utilizador não permissão para executar esta acção !", "Lua", 1,  Messagebox.ERROR)
                info.value="Este utilizador não permissão para executar esta acção !"
                info.style=red
                return
            }

           /* Messagebox.show("Tem certeza que deseja eliminar esta Cotação?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        cotacao.delete flush: true
                        items.removeAll()
                        Executions.sendRedirect("/cotacao/listCotacao")
                    }
                }
            }
            )*/
            cotacao.delete flush: true
            items.removeAll()
            Executions.sendRedirect("/cotacao/listCotacao")
        }catch (Exception e ){
          //  Messagebox.show("Selecione Um Cliente!")
            info.value = "Selecione Um Cliente!"
            info.style = blue
        }


    }

    @Command
    @NotifyChange(["info"])
    def showHelp(){
        info.value = "Double click to delete Item!"
    }
    @Command
    def createFatura(){

        try {


            Messagebox.show("Tem certeza que deseja gerar uma nova fatura com esta Cotação?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        Fatura fatura = new Fatura()
                        fatura.utilizador = encryptionService.localUser
                        fatura.cliente=cotacao.cliente
                        System.println("createFatura cliente="+fatura.cliente)
                        fatura.valorDoIva=cotacao.valorDoIva
                        System.println("createFatura valorDoIva="+fatura.valorDoIva)
                        fatura.utilizador=cotacao.utilizador
                        System.println("createFatura utilizador="+fatura.utilizador)
                        fatura.itemsProduto=cotacao.itemsProduto
                        System.println("createFatura itemsProduto="+fatura.itemsProduto)
                        fatura.valor=cotacao.valor
                        System.println("createFatura valor="+fatura.valor)

                        fatura.save flush: true
                        def faturaDb = Fatura.find(fatura)
                        if(!faturaDb.equals(null)){
                            Messagebox.show("Os dados da Cotação No. "+cotacao.id+" Geraram uma nova fatura! No."+faturaDb.id, "Lua", 1,  Messagebox.INFORMATION)
                            composersService.faturaId=faturaDb
                        }else{
                            Messagebox.show("Erro na geração da nova fatura", "Lua", 1,  Messagebox.ERROR)
                        }



                       // Executions.sendRedirect("/fatura/editFatura")
                    }
                }
            }
            )
            Fatura fatura = new Fatura()
            fatura.utilizador = encryptionService.localUser
            fatura.cliente=cotacao.cliente
            System.println("createFatura cliente="+fatura.cliente)
            fatura.valorDoIva=cotacao.valorDoIva
            System.println("createFatura valorDoIva="+fatura.valorDoIva)
            fatura.utilizador=cotacao.utilizador
            System.println("createFatura utilizador="+fatura.utilizador)
            fatura.itemsProduto=cotacao.itemsProduto
            System.println("createFatura itemsProduto="+fatura.itemsProduto)
            fatura.valor=cotacao.valor
            System.println("createFatura valor="+fatura.valor)

            fatura.save flush: true
            def faturaDb = Fatura.find(fatura)
            if(!faturaDb.equals(null)) {
              //  Messagebox.show("Os dados da Cotação No. " + cotacao.id + " Geraram uma nova fatura! No." + faturaDb.id, "Lua", 1, Messagebox.INFORMATION)
                info.value = "Os dados da Cotação No. " + cotacao.id + " Geraram uma nova fatura! No." + faturaDb.id
                composersService.faturaId = faturaId
            }else {
                info.value = "Erro na geração da Fatura"
                info.style = red

            }
        }catch (Exception e ){
           // Messagebox.show("Selecione Um Cliente!")
            info.value = "Erro na geração da Fatura"+e.toString()
            info.style = red
        }


    }

    @Command
    @NotifyChange(["info","cotacao"])
    def updateCotacao(){


        try {
            System.println(cliente.nome)
            Cliente c = Cliente.findById(cliente.id)
            c.codigo = cliente.codigo
            c.nome = cliente.nome
            c.nuit = cliente.nuit
            c.endereco = cliente.endereco
            c.numTelefone= cliente.numTelefone
            c.save()
            for(ItemProduto ip in cotacao.itemsProduto){
                if(ip.produto.codigo.empty){
                   // Messagebox.show("Elimine os items sem codigo valido!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "Elimine os items sem codigo valido!"
                    return
                }
                if(ip.qtd<=0){
                   // Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!"
                    return
                }
                if(ip.precoDeVenda<=0){
                  //  Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!"
                    return
                }
                ip.save()
            }
            cotacao.valorDoIva=getTotalIva()
            cotacao.merge(failOnError: true,flush: true)

        } catch (Exception e ){
          //  Messagebox.show("Erro na gravação da Cotação No. "+cotacao.id+":"+ e.toString(), "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na gravação da Cotação No. "+cotacao.id+":"+ e.toString()
            return
        }

      //  Messagebox.show("Os dados da Cotação No. "+cotacao.id+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Os dados da Cotação No. "+cotacao.id+" foram actualizados com sucesso!"
    }

    @Command
    @NotifyChange(["items","iva","valor","cotacao"])
    def addSelectedItem(@BindingParam("index") Long index){
        System.println("addSelectedItem"+index)
        ItemProduto itemProduto = new ItemProduto()
        Produto p = Produto.findById(index)
        itemProduto.setProduto(p)
        itemProduto.setDescricao(p.descricao)

        itemProduto.setArmazem(Armazem.findByCodigo("A1"))

        items.add(itemProduto)
        cotacao.addToItemsProduto(itemProduto)
    }

    @NotifyChange("itemsProduto")
    @Command
    public void doSearch() {

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
        def    editarCotacao(){
        System.println("o botão editarCotacao foi clicado")
        composersService.editarCotacao = true
        Executions.sendRedirect("/cotacao/Cotacao")
    }


    @Init
      init() {
        cotacao=Cotacao.findById(composersService.cotacao.id)
        cliente = Cliente.findById(composersService.clienteId)
        sessionStorageService.setCotacao(cotacao)
    }

    Cliente getCliente() {


        return cliente

    }

    Cotacao getCotacao() {
        return cotacao
    }

    void setCotacao(Cotacao cotacao) {
        this.cotacao = cotacao
        sessionStorageService.cotacao = cotacao
    }



    List<Cliente> getClientes() {
        if (clientes == null) {
            clientes = new ArrayList<Cliente>(Cliente.all)
        }
        return clientes
    }


    @NotifyChange(["items","info"])
    ListModelList<ItemProduto> getItems() {
        info.value = ""
       if(items==null){
           items = new ListModelList<>()
           for (ItemProduto ip in cotacao.itemsProduto){
               items.add(ItemProduto.findById(ip.id))
           }
       }

        return items
    }

    @Command
    @NotifyChange(["items","info"])
    def addItem(){
        info.value = ""
        ItemProduto itemProduto = new ItemProduto()
        Produto produto = new Produto()
        produto.codigo=""
        itemProduto.setProduto(produto)
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        itemProduto.precoDeVenda= produto.precoDeVenda
        items.add(itemProduto)
        cotacao.addToItemsProduto(itemProduto)
    }

    @Command
    @NotifyChange(["items","cotacao","valor","iva","info"])
    public void removeItem(@BindingParam("index") Integer index) {
        info.value = ""
        System.println(index)
        items.remove(index)
        cotacao.itemsProduto.remove(index)
        calcularSubTotal()
    }


    @Command
    @NotifyChange(["items","viewBtActualizar","info"])
    def showMe(){
        info.value = ""
        items.each {
            if(!Produto.findByCodigo(it.produto.codigo)){
               // Messagebox.show("Codido do produto errado!"+selectedItemProduto, "Lua", 1, Messagebox.ERROR)
                info.value= "Codido do produto errado!"+selectedItemProduto
                info.style = red

            }

        }
        viewBtActualizar=true
    }

    @Command
    def static fecharEditor(){
        Executions.sendRedirect("/cotacao/listCotacao")
    }

    @Command
    def  imprimir(){
        System.println(cotacao.id)
        composersService.cotacaoId=cotacao.id
      //  Executions.sendRedirect("/cotacao/showCotacao")
        Executions.sendRedirect("/cotacao/printerCotacao/")
    }

    @Command
    @NotifyChange(["valor","iva","cotacao","viewBtActualizar","info"])
    def alterarIva(@BindingParam("index") Integer index){
        // System.println(items[index].ivaAplicado)
        items[index].ivaAplicado=!items[index].ivaAplicado
        getTotalIva()
        calcularSubTotal()
        // System.println(items[index].ivaAplicado)
    }

    @NotifyChange(["cotacao","info"])
    BigDecimal getTotalIva(){
        info.value = ""
        Iva iva = Iva.findByValido(true) as Iva
        BigDecimal vi = 0.0
        for (ItemProduto ip in cotacao.itemsProduto){
            if(ip.ivaAplicado){
                System.println("calcular iva valido")

                if(ip?.subTotal!=null){
                    System.println("sub total nao nulo . iva calculado")
                    vi+=ip?.subTotal*iva?.percentualIva/100
                    ip.valorDoIva=vi
                }

            }else ip.valorDoIva=0.0
        }
        System.println("iva = "+vi)
        cotacao.valorDoIva = vi
        return vi
    }

    @Command
    @NotifyChange(["viewBtActualizar","cotacao","iva","valor","info"])
    def calcularSubTotal(){
        info.value = ""
        cotacao.setValor(0.0)
        cotacao.setValorDoIva(0.0)
        def totalIva = 0.0
        def subTotal = 0.0
        Iva iva = Iva.findByValido(true) as Iva
        if(iva.equals(null)){
           // Messagebox.show("Por favor Introduza o iva no sistema!", "Lua", 1,  Messagebox.ERROR)
            info.value = "Por favor Introduza o iva no sistema!"
            info.style = red
        }
        System.println("iva = "+iva.percentualIva)
        for (ItemProduto ip in items){
            if(ip.ivaAplicado){
                ip.subTotal = ip.precoDeVenda*ip.qtd
                totalIva+=ip.subTotal*iva.percentualIva/100
                System.println("ip.subTotal="+ip.subTotal)
                ip.valorDoIva = ip.subTotal*iva.percentualIva/100
                System.println("updateFatura ip.valorDoIva = "+ip.valorDoIva)
            }else {
                ip.valorDoIva=0.0
            }

            subTotal+=ip.subTotal

        }
        viewBtActualizar=true
        System.println("upDateFatura subtotal "+subTotal)
        System.println("updatecotacao total iva= "+totalIva)
        cotacao.setValor(subTotal)
        cotacao.setValorDoIva(totalIva)
        System.println("upDateFatura valorDoIva "+cotacao.valorDoIva)
        System.println("updatecotacao cotacao.valor= "+cotacao.valor)
    }


}
