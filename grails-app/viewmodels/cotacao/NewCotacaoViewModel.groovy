package cotacao

import base.ComposersService
import base.EncryptionService
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import lua.SessionStorageService
import lua.contador.ContadorService
import lua.entidades.cliente.Cliente
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.estoque.Armazem
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.iva.Iva
import lua.estoque.produto.Produto
import lua.security.Utilizador
import lua.vendas.cotacao.Cotacao
import lua.vendas.precos.Preco
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


@Transactional
class NewCotacaoViewModel {
    ComposersService composersService
    SessionStorageService sessionStorageService
    SpringSecurityService springSecurityService
    ContadorService contadorService
    boolean viewCliente = false
    boolean bt_salvar = false
    private String filter
    private String codigo
    private ListModelList<ItemProduto> items
    private ListModelList<Produto> itemsProduto
    private List<Cliente> clientes
    ItemProduto selectedItemProduto
    Cotacao cotacao = new Cotacao()
   Cliente cliente
    Preco preco = Preco.findByAtivo(true)
    boolean labelInfo = false
    boolean bt_salvar_cliente = false
    @Wire Label info
    String blue = "color:blue"
    String red = "color:red"
    String getCodigo() {
        return codigo
    }

    void setCodigo(String codigo) {
        this.codigo = codigo
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
    @NotifyChange(["viewCliente","cliente","bt_salvar_cliente"])
    def showCliente(){
        info.value=""
        if (viewCliente){
            viewCliente = false
        }
        else viewCliente = true
        if(cliente ==null){
            cliente = new Cliente()
            bt_salvar_cliente=true
        }else bt_salvar_cliente=false
    }

    void setCliente(Cliente cliente) {
        this.cliente = Cliente.findById(cliente.id)
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
        try {
            cotacao.delete(flush: true)
            info.value= "Tem certeza que deseja eliminar esta Cotação?"
            info.style = "color:red"
            wait(100)
            info.value= "A cotação for eliminada com sucesso!"

        }catch (Exception e ){
           // Messagebox.show("Selecione Um Cliente! "+e.message)
            info.value ="Erro na eliminação da cotação! "
            info.style = "color:red"
        }


    }

    @Command
    @NotifyChange(["items","info"])
    def alertDeleteCotacao(){

            info.value= "Tem certeza que deseja eliminar esta Cotação?"
            info.style = "color:red"
            wait(100)
            info.value= "Caso tenha certeza faça double click sobre a cotação em causa!"

    }

    @Command
    @NotifyChange(["cliente","clientes","labelInfo","cotacao","bt_salvar"])
    def createCotacao(){

        try {

            if(cliente.equals(null)){
               // Messagebox.show("Preencha os campos! ", "Lua", 1,  Messagebox.ERROR)
                info.value ="Preencha os campos! "
                info.style = "color:red"
                return
            }
            if(cotacao.itemsProduto?.empty){
              //  Messagebox.show("Seleccione pelo menos um item válido! ", "Lua", 1,  Messagebox.ERROR)
                info.value ="Seleccione pelo menos um item válido! "
                info.style = "color:red"
                return
            }

            System.println("__-----------------------------------__")

            cotacao.setCliente(Cliente.findByCodigo(cliente.codigo))
            if(cotacao.cliente.equals(null)){
               // Messagebox.show("Seleccione pelo menos cliente! ", "Lua", 1,  Messagebox.ERROR)
                info.value ="Seleccione pelo menos cliente! "
                info.style = "color:red"
                return
            }
            if(items.empty){
              //  Messagebox.show("Seleccione pelo menos um produto! ", "Lua", 1,  Messagebox.ERROR)
                info.value ="Seleccione pelo menos um produto! "
                info.style = "color:red"
                return
            }
            for(ItemProduto ip in cotacao.itemsProduto){
                System.println(ip.produto.nome)
                if(ip.produto.codigo.empty){
                  //  Messagebox.show("Elimine os items sem codigo valido!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "Elimine os items sem codigo valido!"
                    info.style = "color:red"
                    return
                }
                if(ip.qtd<=0){
                  //  Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!", "Lua", 1,  Messagebox.ERROR)
                    info.value="O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!"
                    info.style = "color:red"
                    return
                }
                if(ip.precoDeVenda<=0){
                   // Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!", "Lua", 1,  Messagebox.ERROR)
                    info.value="O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!"
                    info.style = "color:red"
                    return
                }

            }

            cotacao.numeroDaCotacao=contadorService.gerarNumeroDaCotacao()
            cotacao.cliente.save()
           cotacao.save (flush: true,failOnError: true)
            sessionStorageService.setCotacao(cotacao)

        } catch (Exception e ){
               //     Messagebox.show("Erro na gravação "+e.message, "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na gravação "+e.message
            info.style="color:red"
            return
        }
        def cot = Cotacao.find(cotacao)

        if(Cotacao.findById(cotacao?.id)){
            labelInfo=true
           // Messagebox.show("Os dados da Ccotação No. "+cot?.id+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
            info.style = "color:blue"
            info.value = "Os dados da Ccotação No. "+cotacao.numeroDaCotacao+" foram actualizados com sucesso!"
            composersService.cotacao=cotacao
        }else {
            info.style = "color:red"
            info.value = "Erro na gravação da cotação!"
        }
        bt_salvar = false

    }


    @NotifyChange(["cliente","clientes","info"])
    def createCliente(){

        try {
                cliente.save flush: true
                System.println("novo cliente criado com sucesso"+Cliente.findByCodigo(cliente.codigo))
            cotacao.save flush: true
        } catch (Exception e ){
           // Messagebox.show("Erro na gravação "+e.message, "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na gravação "+e.message
            info.style = red

            return
        }
        if (cotacao.id.equals(null)){
          //  Messagebox.show("Erro na gravação ", "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na gravação "
            info.style = red
            return
        }
       // Messagebox.show("Os dados Do Cliente No. "+cotacao.numeroDaCotacao+" foram criados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value="Os dados Do Cliente No. "+cotacao.numeroDaCotacao+" foram criados com sucesso!"
        info.style=blue

    }

    @Command
    @NotifyChange(["items","cotacao","info"])
    def addSelectedItem(@BindingParam("index") Long index){


        System.println("addSelectedItem: "+index)
        ItemProduto itemProduto = new ItemProduto()
        Produto p = Produto.findById(index)
        itemProduto.setProduto(p)
        itemProduto.operacao = "cotacao"
        itemProduto.setDescricao(p.descricao)
        itemProduto.ivaAplicado = p.ivaAplicado
        itemProduto.valorDoIva = p.valorDoIva
        if(p.pacote){
            itemProduto.precoDeVenda = p.precoDeVenda/p.qtdItems
        }else {
            itemProduto.precoDeVenda = p.precoDeVenda
        }
        itemProduto.qtd = 1
        itemProduto.estocavel = p.estocavel
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        itemProduto.iva = p.iva
         items.add(itemProduto)
        if(cotacao.itemsProduto==null){
            cotacao.itemsProduto = new ArrayList<>()
        }
        cotacao.itemsProduto.add(itemProduto)
      }

    @NotifyChange(["itemsProduto","info"])
    @Command
     void doSearch() {
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




    @Init
    init() {

        cotacao.utilizador =  Utilizador.findById(springSecurityService.principal?.id)
        cotacao.cliente= cliente
        cotacao.valor = 0.0
        cotacao.valorDoIva = 0.0



    }

    Cliente getCliente() {
        return cliente
    }

    Cotacao getCotacao() {
        return cotacao
    }

    void setCotacao(Cotacao cotacao) {
        this.cotacao = cotacao
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
       if (items == null) {
                items= new ListModelList<ItemProduto>()
        }
        return items
    }

    @Command
    @NotifyChange(["items","cotacao","info"])
    def addItem(){
        if(cliente.equals(null)){
          //  Messagebox.show("Selecione o cliente!", "Lua", 1,  Messagebox.ERROR)
            info.value = "Selecione o cliente!"
            return
        }


        ItemProduto itemProduto = new ItemProduto()
        Produto produto = new Produto()
        produto.codigo=""
        itemProduto.setProduto(produto)
        itemProduto.qtd = 1
        itemProduto.operacao = "cotacao"
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        items.add(itemProduto)
        updateCotacao()
        }

    @Command
    @NotifyChange(["items","cotacao","info"])
    void removeItem(@BindingParam("index") Integer index) {
        info.value=""
        System.println(index)
        items.remove(index)
       cotacao.itemsProduto=items
       }


    @Command
    def static fecharEditor(){
        Executions.sendRedirect("/cotacao/listCotacao")
    }

    @Command
    def  imprimir(){
        def cotacaoDb = Cotacao.findById(cotacao?.id)
        if(cotacaoDb){
            composersService.cotacaoId=cotacaoDb.id
            bt_salvar=false
            Executions.sendRedirect("/cotacao/printerCotacao/")
        }else

        {
            // Messagebox.show("Por favor salve a cotacao antes!", "Lua", 1,  Messagebox.INFORMATION)
            info.value="Por favor salve a cotacao antes!"
            info.style = "color:blue"
        }
    }

    @Command
    @NotifyChange(["items","info"])
    def showMe(){

        items.each {
            def produtoDB = Produto.findByCodigo(it.produto.codigo)
            if(!produtoDB){
             //   Messagebox.show("Codido do produto errado!", "Lua", 1, Messagebox.ERROR)
                info.value = "Codido do produto errado!"
                info.style = "color:red"
            }else {
                it.descricao=produtoDB.descricao
                it.precoDeVenda=produtoDB.precoDeVenda
            }


        }
    }


    @Command
    @NotifyChange(["items","codigo","info","cotacao"])
    def addItemByCode(){
        if(!codigo.empty){
            ItemProduto itemProduto = new ItemProduto()
            Produto p = Produto.findByCodigo(codigo)
            if (p.equals(null)){
                return
            }
            itemProduto.setProduto(p)
            itemProduto.operacao = "cotacao"
            itemProduto.setDescricao(p.descricao)
            itemProduto.ivaAplicado = p.ivaAplicado
            itemProduto.iva = p.iva
            if(p.pacote){
                itemProduto.precoDeVenda = p.precoDeVenda/p.qtdItems
            }else {
                itemProduto.precoDeVenda = p.precoDeVenda
            }

            itemProduto.qtd = 1
            itemProduto.estocavel = p.estocavel
            itemProduto.setArmazem(Armazem.findByCodigo("A1"))
            items.add(itemProduto)
            if(cotacao.itemsProduto==null){
                cotacao.itemsProduto = new ArrayList<>()
            }
            cotacao.itemsProduto.add(itemProduto)
        }
    }

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
    @NotifyChange(["viewCliente"])
    def hideCliente(){
        viewCliente = false
        info.value=""
    }

    @Command
    def    editarCotacao(){
        info.value=""
        System.println("o botão editarCotacao foi clicado")
        composersService.cotacaoId=cotacao.id
        if(cotacao?.id!=null){
            Executions.sendRedirect("/cotacao/edit")
        }else {
            info.value="Selecione uma cotação!"
            info.style ="color:red"
        }

    }

    @Command
    @NotifyChange(['cotacao'])
    def  updateCotacao(){
        for(ItemProduto ip in items){
            if(ip.ivaAplicado){
                ip.valorDoIva = ip.precoDeVenda*ip.produto?.iva?.percentualIva/100
            }
        }
        cotacao.itemsProduto = items
    }


}
