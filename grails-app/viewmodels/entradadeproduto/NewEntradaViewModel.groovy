package entradadeproduto

import base.ComposersService
import base.EncryptionService
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import lua.contador.ContadorService
import lua.entidades.fornecedor.Fornecedor
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.estoque.Armazem
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.produto.Produto
import lua.security.Utilizador
import org.zkoss.bind.annotation.BindingParam
import lua.estoque.iva.Iva
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.select.annotation.Wire

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox
import sun.org.mozilla.javascript.internal.ast.Yield

import java.sql.SQLException


@Transactional
class NewEntradaViewModel {
    SpringSecurityService springSecurityService
    ContadorService contadorService
    boolean show_fornecedor = false
    @Wire Label info
    String blue = "color:blue"
    String red = "color:red"
    private String filter
    private String codigo
    private ListModelList<ItemProduto> items
    private ListModelList<Produto> itemsProduto
    private List<Fornecedor> fornecedores
    ItemProduto selectedItemProduto
    ComposersService composersService
    EntradaDeProduto entrada = new EntradaDeProduto()
    Fornecedor fornecedor = new Fornecedor()

    String getCodigo() {
        return codigo
    }

    void setCodigo(String codigo) {
        this.codigo = codigo
    }

    boolean getShow_fornecedor() {
        return show_fornecedor
    }

    void setShow_fornecedor(boolean show_fornecedor) {
        this.show_fornecedor = show_fornecedor
    }

    @Command
    @NotifyChange(["show_fornecedor"])
    def mostraForncedor() {
        show_fornecedor = !show_fornecedor
    }

    void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor
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
    def deleteEntradaDeProduto(){

        try {


           /* Messagebox.show("Tem certeza que deseja eliminar Items desta cotação?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {

                        items.removeAll()
                       // Executions.sendRedirect("/entrada/list")
                    }
                }
            }
            )*/
            items.removeAll()

        }catch (Exception e ){
          //  Messagebox.show("Selecione Um Fornecedor!")
            info.value = "Selecione Um Fornecedor!"
        }


    }

    @Command
    @NotifyChange(["info"])
    def createEntradaDeProduto(){
        try {
            entrada.utilizador =  Utilizador.findById(springSecurityService.principal?.id)
            entrada.numeroDeEntrada = contadorService.gerarNumeroDeEntrada()
            if(fornecedor.equals(null)){
               // Messagebox.show("Selecione um Fornecedor!", "Lua", 1, Messagebox.ERROR)
                info.value = "Selecione um Fornecedor!"
                info.style = red
                return
            }
            entrada.setFornecedor(Fornecedor.findById(fornecedor.id))

            if(entrada.itemsProduto?.empty){
             //   Messagebox.show("Seleccione pelo menos um item válido! ", "Lua", 1,  Messagebox.ERROR)
                info.value = "Seleccione pelo menos um item válido! "
                info.style = red
                return
            }
            if(items.empty){
               // Messagebox.show("Seleccione pelo menos um produto! ", "Lua", 1,  Messagebox.ERROR)
                info.value = "Seleccione pelo menos um produto! "
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
                    return
                }
                if(ip.precoDeCompra<=0){
                   // Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!"
                    info.style = red
                    return
                }
                ip.save()
            }
            System.println("createEntradaDeProduto fornecedor="+entrada.fornecedor.nome)

            entrada.itemsProduto = items
             entrada.fornecedor.save()
            entrada.save flush: true
            if(EntradaDeProduto.find(entrada)){
             //   Messagebox.show("Os dados com a ref. No. "+entrada.id+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                info.value = "Os dados com a ref. No. "+entrada.id+" foram actualizados com sucesso!"
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





        Executions.sendRedirect("/entradaDeProduto/list")
    }

    @Command
    @NotifyChange(["items","info"])
    def addSelectedItem(@BindingParam("index") Long index){
        info.value = ""
        System.println("addSelectedItem: "+index)
        ItemProduto itemProduto = new ItemProduto()
        Produto p = Produto.findById(index)
        itemProduto.setProduto(p)
        itemProduto.setDescricao(p.descricao)
        itemProduto.operacao ="entrada"
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
            itemProduto.operacao = "entrada"
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
    def    editarEntradaDeProduto(){
        System.println("o botão editarEntradaDeProduto foi clicado")
        composersService.editarEntradaDeProduto = true
        Executions.sendRedirect("/entrada/EntradaDeProduto")
    }


    @Init
    init() {

        System.println(entrada.utilizador)
        entrada.fornecedor= fornecedor

    }

    Fornecedor getFornecedor() {
        return fornecedor
    }

    EntradaDeProduto getEntrada() {
        return entrada
    }

    void setEntrada(EntradaDeProduto entrada) {
        this.entrada = entrada
    }

    List<Fornecedor> getfornecedores() {
        if (fornecedores == null) {
            fornecedores = new ArrayList<Fornecedor>(Fornecedor.all)
        }

        return fornecedores
    }

    @NotifyChange(["items","entrada","info"])
    BigDecimal getSubTotal() {
        info.value = ""
       // return composersService.entrada.valor
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
        itemProduto.operacao = "entrada"
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))

        items.add(itemProduto)
        items.add(itemProduto)


    }

    @Command
    @NotifyChange(["items","entrada","info"])
    public void removeItem(@BindingParam("index") Integer index) {
        info.value= ""
        System.println(index)
        items.remove(index)
        updateEntrada()
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
         Executions.sendRedirect("/entradaDeProduto/list")
    }

    @Command
    @NotifyChange(["entrada"])
    def  updateEntrada(){
        entrada.setValor(0.0)
        entrada.setValorDoIva(0.0)
        def totalIva = 0.0
        def subTotal = 0.0
        Iva iva = Iva.findByValido(true) as Iva
        System.println("iva = "+iva.percentualIva)
        for (ItemProduto ip in items){
            System.println("update entrada ip.sub totoal="+ip.subTotal)
            System.println("update entrada iva apliado="+ip.ivaAplicado)
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
        entrada.setValor(subTotal)
        entrada.setValorDoIva(totalIva)
        System.println("upDateEntrada valorDoIva "+entrada.valorDoIva)
        System.println("updateentrada entrada.valor= "+entrada.valor)
    }




}
