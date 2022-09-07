package iva

import base.ComposersService
import base.EncryptionService
import lua.entidades.cliente.Cliente
import lua.estoque.estoque.Armazem
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.iva.Iva
import lua.estoque.produto.Produto
import lua.vendas.cotacao.Cotacao
import lua.vendas.precos.Preco
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox

class IvaCrudViewModel {

    ComposersService composersService
    EncryptionService encryptionService
    boolean viewCliente = false
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
    @NotifyChange(["viewCliente"])
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
    @NotifyChange("items")
    def deleteCotacao(){
        try {


            Messagebox.show("Tem certeza que deseja eliminar esta Cotação?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        cotacao.delete flush: true
                        items.removeAll()
                        Executions.sendRedirect("/cotacao/list")
                    }
                }
            }
            )

        }catch (Exception e ){
            Messagebox.show("Selecione Um Cliente! "+e.message)
        }


    }

    @Command
    @NotifyChange(["cliente","clientes","labelInfo","cotacao"])
    def createCotacao(){

        try {
            System.println("items da cotacao")
            if(cliente.equals(null)){
                Messagebox.show("Seleccione pelo menos um cliente! ", "Lua", 1,  Messagebox.ERROR)
                return
            }
            if(cotacao.itemsProduto?.empty){
                Messagebox.show("Seleccione pelo menos um item válido! ", "Lua", 1,  Messagebox.ERROR)
                return
            }
            /* for (ItemProduto ip in cotacao.itemsProduto){

                 ip.save flush: true

                 System.println("Poduto description "+ip.produto.nome+"qtd:"+ip.qtd+"preço de venda 1 : "+ip.precoDeVenda)
             }*/
            System.println("__-----------------------------------__")

            cotacao.setCliente(Cliente.findByCodigo(cliente.codigo))
            if(cotacao.cliente.equals(null)){
                Messagebox.show("Seleccione pelo menos cliente! ", "Lua", 1,  Messagebox.ERROR)
                return
            }
            if(items.empty){
                Messagebox.show("Seleccione pelo menos um produto! ", "Lua", 1,  Messagebox.ERROR)
                return
            }
            for(ItemProduto ip in cotacao.itemsProduto){
                if(ip.produto.codigo.empty){
                    Messagebox.show("Elimine os items sem codigo valido!", "Lua", 1,  Messagebox.ERROR)
                    return
                }
                if(ip.qtd<=0){
                    Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!", "Lua", 1,  Messagebox.ERROR)
                    return
                }
                if(ip.precoDeVenda<=0){
                    Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!", "Lua", 1,  Messagebox.ERROR)
                    return
                }

            }

            cotacao.itemsProduto = items
            cotacao.cliente.save()
            cotacao.save flush: true


        } catch (Exception e ){
            Messagebox.show("Erro na gravação "+e.message, "Lua", 1,  Messagebox.ERROR)
            return
        }
        def cot = Cotacao.find(cotacao)

        if(Cotacao.find(cotacao)){
            labelInfo=true
            Messagebox.show("Os dados da Ccotação No. "+cot?.id+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        }


    }


    @NotifyChange(["cliente","clientes"])
    def createCliente(){

        try {

            cliente.save flush: true

            System.println("novo cliente criado com sucesso"+Cliente.findByCodigo(cliente.codigo))


            cotacao.save flush: true

        } catch (Exception e ){
            Messagebox.show("Erro na gravação "+e.message, "Lua", 1,  Messagebox.ERROR)
            return
        }
        if (cotacao.id.equals(null)){
            Messagebox.show("Erro na gravação ", "Lua", 1,  Messagebox.ERROR)
            return
        }
        Messagebox.show("Os dados Do Cliente No. "+cotacao.id+" foram criados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)




    }

    @Command
    @NotifyChange(["items","cotacao"])
    def addSelectedItem(@BindingParam("index") Long index){
        if(cliente.equals(null)){
            Messagebox.show("Selecione o cliente!", "Lua", 1,  Messagebox.ERROR)
            return
        }
        System.println("addSelectedItem: "+index)
        ItemProduto itemProduto = new ItemProduto()
        Produto p = Produto.findById(index)
        itemProduto.setProduto(p)
        itemProduto.setDescricao(p.descricao)

        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        def precodecompras = p.itemsProduto.precoDeCompra
        BigDecimal precodecompra=0.0
        for (BigDecimal preco in precodecompras){
            if(precodecompra<preco){
                precodecompra=preco
            }
        }

        items.add(itemProduto)
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
    def    editarCotacao(){
        System.println("o botão editarCotacao foi clicado")
        composersService.editarCotacao = true
        Executions.sendRedirect("/cotacao/Cotacao")
    }


    @Init
    init() {

        cotacao.utilizador = encryptionService.localUser
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


    @NotifyChange("items")
    ListModelList<ItemProduto> getItems() {

        if (items == null) {
            items= new ListModelList<ItemProduto>()
        }
        return items
    }

    @Command
    @NotifyChange(["items","cotacao"])
    def addItem(){
        if(cliente.equals(null)){
            Messagebox.show("Selecione o cliente!", "Lua", 1,  Messagebox.ERROR)
            return
        }


        ItemProduto itemProduto = new ItemProduto()
        Produto produto = new Produto()
        produto.codigo=""
        itemProduto.setProduto(produto)
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        Preco preco = Preco.findByAtivo(true)

        items.add(itemProduto)
    }

    @Command
    @NotifyChange(["items","cotacao"])
    void removeItem(@BindingParam("index") Integer index) {
        System.println(index)
        items.remove(index)
        updateCotacao()
    }

    @Command
    @NotifyChange(["cotacao"])
    def  updateCotacao(){
        cotacao.setValor(0.0)
        cotacao.setValorDoIva(0.0)
        def totalIva = 0.0
        def subTotal = 0.0
        Iva iva = Iva.findByValido(true) as Iva


        if(iva.equals(null)){
            Messagebox.show("Por favor Introduza o iva no sistema!", "Lua", 1,  Messagebox.ERROR)
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
        System.println("upDateFatura subtotal "+subTotal)
        System.println("updatecotacao total iva= "+totalIva)
        cotacao.setValor(subTotal)
        cotacao.setValorDoIva(totalIva)
        System.println("upDateFatura valorDoIva "+cotacao.valorDoIva)
        System.println("updatecotacao cotacao.valor= "+cotacao.valor)
    }




    @Command
    def static fecharEditor(){
        Executions.sendRedirect("/cotacao/listCotacao")
    }




    @Command
    @NotifyChange(["cotacao"])
    def alterarIva(@BindingParam("index") Integer index){
        items[index].ivaAplicado=!items[index].ivaAplicado
        updateCotacao()
    }

    @Command
    def  imprimir(){

        if(Cotacao.find(cotacao)){
            composersService.cotacaoId=cotacao.id
            Executions.sendRedirect("/cotacao/imprimir_cotacao")
        }else
            Messagebox.show("Por favor salve a cotacao antes!", "Lua", 1,  Messagebox.INFORMATION)
    }

    @Command
    @NotifyChange("items")
    def showMe(){

        items.each {
            def produtoDB = Produto.findByCodigo(it.produto.codigo)
            if(!produtoDB){
                Messagebox.show("Codido do produto errado!", "Lua", 1, Messagebox.ERROR)

            }else {
                it.descricao=produtoDB.descricao

            }


        }
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
            itemProduto.setArmazem(Armazem.findByCodigo("A1"))
            // itemProduto.setPreco(p.itemsProduto.last().precoDeCompra)
            items.add(itemProduto)
            codigo=""
        }

    }


}
