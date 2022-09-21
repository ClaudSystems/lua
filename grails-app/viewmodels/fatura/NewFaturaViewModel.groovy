package fatura

import base.ComposersService
import base.EncryptionService
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import lua.SessionStorageService
import lua.UtilizadorService
import lua.contador.ContadorService
import lua.entidades.cliente.Cliente
import lua.security.Utilizador
import lua.estoque.estoque.Armazem
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.iva.Iva
import lua.estoque.produto.Produto
import lua.vendas.fatura.Fatura
import org.zkoss.bind.annotation.BindingParam

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Button
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox



@Transactional
class NewFaturaViewModel  {
    String blue = "color:blue"
    ContadorService contadorService
    SessionStorageService sessionStorageService
    SpringSecurityService springSecurityService
    boolean viewCliente = false
    private String filter
    private String codigo
    private ListModelList<ItemProduto> items
    private ListModelList<Produto> itemsProduto
    private List<Cliente> clientes
    ItemProduto selectedItemProduto
    Fatura fatura
    Cliente cliente
    boolean labelInfo = false
    boolean bt_save = true
    @Wire Label info
    private BigDecimal totalIva
    private  Iva iva

    BigDecimal getTotalIva() {
        return totalIva
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
            itemProduto.setProduto(p)
            itemProduto.setDescricao(p.descricao)
            itemProduto.operacao = "fatura"
            itemProduto.setArmazem(Armazem.findByCodigo("A1"))
           itemProduto.precoDeVenda = p.precoDeVenda
           itemProduto.ivaAplicado = p.ivaAplicado
            itemProduto.iva = p.iva
            items.add(itemProduto)
        }

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
    @NotifyChange(["viewCliente","cliente","info"])
    def showCliente(){
        info.value=""
        if (viewCliente){
            viewCliente = false

        }
        else viewCliente = true
        if(cliente ==null){
            cliente = new Cliente()
        }
    }

    @Command
    @NotifyChange(["cliente","info"])
    def updateCliente(){
        info.value = ""
        try {
            def clienteDb = Cliente.findById(cliente.id)
            clienteDb.codigo = cliente.codigo
            clienteDb.numTelefone=cliente.numTelefone
            clienteDb.nome = cliente.nome
            clienteDb.mail = cliente.mail
            clienteDb.endereco = cliente.endereco
            clienteDb.classe = cliente.classe
            clienteDb.nuit=cliente.nuit
            clienteDb.save(flush: true)
            info.style = "color:blue"
            info.value = "Dados do cliente "+cliente.nome+" foram atualizados com sucesso!"
        }catch (Exception e){
            info.style = "color:red"
            info.value = "Erro Na Gravação! "+e.toString()
        }
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
    def deleteFatura(){
        try {


           /* Messagebox.show("Tem certeza que deseja eliminar esta Fatura", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        fatura.delete flush: true
                        items.removeAll()
                        Executions.sendRedirect("/fatura/list")
                    }
                }
            }
            )*/

        }catch (Exception e ){
         //   Messagebox.show("Selecione Um Cliente! "+e.message)

            info.value = "Selecione Um Cliente! "+e.message
            info.style = "color:red"
        }


    }

    @Command
    @NotifyChange(["cliente","clientes","fatura","labelInfo","info","bt_save"])
    def createFatura(){
        fatura.utilizador =  Utilizador.findById(springSecurityService.principal?.id)
        Date date = new Date()
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        cal.add(Calendar.MONTH,(1))
        fatura.dataPrevistaDePagamento =cal.getTime()
        System.println("listagem dos items da fatura")
        if(cliente.equals(null)){
           // Messagebox.show("Seleccione pelo menos um cliente! ", "Lua", 1,  Messagebox.ERROR)
            info.style="color:red"
            info.value="Seleccione pelo menos um cliente! "
            return
        }
        if(fatura.itemsProduto?.empty){
           // Messagebox.show("Seleccione pelo menos um item válido! ", "Lua", 1,  Messagebox.ERROR)
            info.style="color:red"
            info.value="Seleccione pelo menos um item válido! "
            return
        }
        for (ItemProduto ip in fatura.itemsProduto){
            System.println(ip)
        }
        System.println("__-----------------------------------__")

        fatura.setCliente(Cliente.findByCodigo(cliente.codigo))
        if(fatura.cliente.equals(null)){
          //  Messagebox.show("Seleccione pelo menos cliente! ", "Lua", 1,  Messagebox.ERROR)
            info.style="color:red"
            info.value="Seleccione pelo menos cliente! "
            return
        }
        if(fatura.utilizador.equals(null)){
           // Messagebox.show("Seleccione um Utilizador! ", "Lua", 1,  Messagebox.ERROR)
            info.style="color:red"
            info.value="Seleccione um Utilizador! "
            return
        }
        if(items.empty){
          //  Messagebox.show("Seleccione pelo menos um produto! ", "Lua", 1,  Messagebox.ERROR)
            info.style="color:red"
            info.value="Seleccione pelo menos um produto! "
            return
        }
        for(ItemProduto ip in fatura.itemsProduto){
            if(ip.produto.codigo.empty){
             //   Messagebox.show("Elimine os items sem codigo valido!", "Lua", 1,  Messagebox.ERROR)
                info.style="color:red"
                info.value="Elimine os items sem codigo valido!"
                return
            }
            if(ip.qtd<=0){
              //  Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!", "Lua", 1,  Messagebox.ERROR)
                info.style="color:red"
                info.value="O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!"
                return
            }
            if(ip.precoDeVenda<=0){
              //  Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!", "Lua", 1,  Messagebox.ERROR)
                info.style="color:red"
                info.value="O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!"
                return

            }

        }
        try {


            fatura.itemsProduto = items
            System.println(fatura.valorDoIva)
            System.println(fatura.valor)
            System.println(fatura.itemsProduto)
            System.println(fatura.dateCreated)
            System.println(fatura.cliente)
            System.println(fatura.numeroDaFatura)
           /* fatura.setEstado("aberta")*/
            fatura.cliente.save()
            fatura.numeroDaFatura=contadorService.gerarNumeroDaFatura()
            System.println("numero fa fatura = "+fatura.numeroDaFatura)
            fatura.save flush: false
            info.value="Os dados da Fatura No. "+fatura.numeroDaFatura+" foram actualizados com sucesso!"
            info.style = blue

        } catch (Exception e ){
           // Messagebox.show("Erro na gravação "+e.message, "Lua", 1,  Messagebox.ERROR)
            info.style="color:red"
            info.value="Erro na gravação "+e.message
            bt_save= true
        }

     /*   if(Fatura.find(fatura)){
           // Messagebox.show("Os dados da Fatura No. "+fa?.id+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
            info.style="color:blue"
            fa.save(flush: true)
            info.value="Os dados da Fatura No. "+fatura.numeroDaFatura+" foram actualizados com sucesso!"
            bt_save=false
        }else {
            info.style="color:red"
            info.value="Erro na gravação "
            bt_save= true

        }*/


        //composersService.faturaId=fa.id

      //  Executions.sendRedirect("/fatura/fatura")
    }

/*
    @NotifyChange(["cliente","clientes"])
    def createCliente(){

        try {

            cliente.save flush: true

            System.println("novo cliente criado com sucesso"+Cliente.findByCodigo(cliente.codigo))


            fatura.save flush: true

        } catch (Exception e ){
            Messagebox.show("Erro na gravação "+e.message, "Lua", 1,  Messagebox.ERROR)
            return
        }
        if (fatura.id.equals(null)){
            Messagebox.show("Erro na gravação ", "Lua", 1,  Messagebox.ERROR)
            return
        }
        Messagebox.show("Os dados Do Cliente No. "+fatura.id+" foram criados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)

    }*/
    @Command
    @NotifyChange(["cliente","clientes","info"])
    def salvarCliente(){
        while (cliente.codigo.equals(null)||cliente.nome.equals(null)||cliente.nuit.equals(null)){
            //  Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.style = "color:red"
            info.value = "Preecha os campos!"
            return
        }
        if (Cliente.findAllByCodigo(cliente.codigo)) {
            // Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
            info.style="color:red"
            info.value = "o valor do codigo ja existe!"
            return
        }

        cliente.save()
        clientes.add(cliente)

        //   Messagebox.show("Cliente "+cliente.nome+" foi criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.style="color:blue"
        info.value="Cliente "+cliente.nome+" foi criado com sucesso!"
        cliente=null
    }

    @Command
    @NotifyChange(["items","fatura"])
    def addSelectedItem(@BindingParam("index") Long index){
        ItemProduto itemProduto = new ItemProduto()
        Produto p = Produto.findById(index)
        itemProduto.setProduto(p)
        itemProduto.operacao = "fatura"
        itemProduto.setDescricao(p.descricao)
        itemProduto.precoDeVenda = p.precoDeVenda
        itemProduto.ivaAplicado = p.ivaAplicado
        itemProduto.iva = p.iva
        itemProduto.qtd = 1
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        items.add(itemProduto)
        updateFatura()
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

    @Command
    def    editarFatura(){
        info.value=""
        System.println("o botão editarFatura foi clicado")
        sessionStorageService.fatura = fatura
        if(fatura.id!=null){
            Executions.sendRedirect("/fatura/edit")
        }else {
            info.value="Selecione uma fatura!"
            info.style ="color:red"
        }

    }


    @Init
    init() {

        if(fatura==null){
            fatura = new Fatura()
        }

        fatura.cliente= cliente
        fatura.valorDoIva = 0.0
        fatura.valor = 0.0
        iva = Iva.findByValido(true)
      }

    Cliente getCliente() {
        return Cliente.findById(cliente?.id)
    }

    Fatura getFatura() {
        fatura.getValorTotal()
        fatura.getValor()
        return fatura
    }

    void setFatura(Fatura fatura) {
        this.fatura = fatura
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
    @NotifyChange(["fatura"])
    def addItem(){
        ItemProduto itemProduto = new ItemProduto()
        Produto produto = new Produto()
        produto.codigo=""
        itemProduto.setProduto(produto)
        itemProduto.qtd =1
        itemProduto.operacao = "fatura"
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        itemProduto.precoDeVenda = produto.precoDeVenda
        items.add(itemProduto)
        fatura.addToItemsProduto(itemProduto)

    }

    @Command
    @NotifyChange(["items","fatura"])
    void removeItem(@BindingParam("index") Integer index) {
        items.remove(index)
       updateFatura()
    }

    @Command
    @NotifyChange(["fatura","valorDoIva","valor","items","totalIva"])
    def  updateFatura(){
        totalIva = 0.0
        if(fatura.itemsProduto==null){
            fatura.itemsProduto = new LinkedHashSet<>()
        }
        fatura.itemsProduto.clear()
        for(ItemProduto ip in items){
            if(ip.ivaAplicado){
                ip.valorDoIva = ip.precoDeVenda*iva.percentualIva/100
            }else {
                ip.valorDoIva = 0.0
            }
            fatura.itemsProduto.add(ip)

        }
        System.println(totalIva)
    }




    @Command
    def static fecharEditor(){
        Executions.sendRedirect("/fatura/list")
    }




    @Command
    @NotifyChange(["fatura"])
    def alterarIva(@BindingParam("index") Integer index){
        items[index].ivaAplicado=!items[index].ivaAplicado
        System.println(items[index].ivaAplicado)
        updateFatura()
    }

    @Command
    def  imprimir(){
         if(fatura.id!=null){
             sessionStorageService.fatura = fatura
             Executions.sendRedirect("/fatura/printerFatura")
            /*fatura=new Fatura()
             cliente = null*/
         }else
      //  Messagebox.show("Por favor Salve a fatura antes!", "Lua", 1,  Messagebox.INFORMATION)
         {
             info.style="color:blue"
             info.value ="Por favor Salve a fatura antes!"
         }
    }

    @Command
    @NotifyChange("items")
    def showMe(){
        items.each {
            Produto produtoDB = Produto.findById(it.id)
            if(produtoDB==null){
               // Messagebox.show("Codido do produto errado!"+selectedItemProduto, "Lua", 1, Messagebox.ERROR)
                info.style="color:blue"
                info.value ="Codido do produto errado!"

            }
            else {
                it.precoDeVenda= produtoDB.precoDeVenda
            }

        }
    }

    String getCodigo() {
        return codigo
    }

    void setCodigo(String codigo) {
        this.codigo = codigo
    }
}
