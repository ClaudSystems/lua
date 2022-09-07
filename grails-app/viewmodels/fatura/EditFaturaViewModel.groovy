package fatura

import base.ComposersService
import grails.transaction.Transactional
import lua.contador.ContadorService
import lua.contas.Conta
import lua.entidades.cliente.Cliente
import lua.estoque.estoque.Armazem
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.iva.Iva
import lua.estoque.produto.Produto
import lua.vendas.fatura.Fatura
import lua.vendas.recibo.Cheque
import lua.vendas.recibo.FormaDePagamento
import lua.vendas.recibo.Recibo
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox

@Transactional
class EditFaturaViewModel {
    boolean viewCliente = false
    boolean bt_salvar_recibo = true
    boolean  viewCheque = false
    boolean dvProdutosVisible = true
    boolean dvPagamentosVisible = false
    boolean viewBtActualizar
    private String filter
    private ListModelList<ItemProduto> items
    private ListModelList<Produto> itemsProduto
    private ListModelList<FormaDePagamento> formasDePagamento
    private selectedFormaDePagamento
    private List<Cliente> clientes
    private ListModelList<Recibo> recibos
    ItemProduto selectedItemProduto
    ComposersService composersService
    Fatura fatura = new Fatura()
    BigDecimal valor
    BigDecimal iva
    Cliente cliente
    Recibo recibo
    Recibo selectedRecibo
    Cheque cheque
    @Wire Label info
    ContadorService contadorService
    String inf="Para imprimir o duplicado, pressione a botão direito do mouse sobre o botão 'imprimir'"

    boolean getBt_salvar_recibo() {
        return bt_salvar_recibo
    }

    ListModelList<Recibo> getRecibos() {
        if(recibos==null){
            recibos = new ListModelList<Recibo>()
        }
        if(fatura.recibos!=null){
            for(Recibo recibo1 in fatura.recibos){
                recibos.add(recibo1)
            }
        }
        return recibos
    }

    Recibo getSelectedRecibo() {
        return selectedRecibo
    }

    void setSelectedRecibo(Recibo selectedRecibo) {
        this.selectedRecibo = selectedRecibo
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
    def invalidarFatura(){
        try {


           /* Messagebox.show("Tem certeza que deseja eliminar esta Fatura?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        fatura.delete flush: true
                        items.removeAll()
                        Executions.sendRedirect("/fatura/listFatura")
                    }
                }
            }
            )*/
            fatura.cancelado = true
            fatura.save flush: true
            info.value = "Esta fatura foi cancelado!"
            info.style = "color:red"
          //  items.removeAll()
          //  Executions.sendRedirect("/fatura/listFatura")

        }catch (Exception e ){
           // Messagebox.show("Selecione Um Cliente!")
            info.style="color:red"
            info.value = "Selecione Um Cliente!"
        }


    }

    @Command
    @NotifyChange(["fatura"])
    def confirmar (){
        info.value= "Caso tenha a certeza que pretende cancelar a fatura por favor faça o double ckicl sobre o butão cancelar!"
        info.style = "color:blue"
    }

    @Command
    @NotifyChange
    def updateFatura(){
        try {
            Fatura faturaDB =Fatura.findById(fatura.id)
            Cliente c = Cliente.findById(cliente.id)
            c.codigo = cliente.codigo
            c.nome = cliente.nome
            c.nuit = cliente.nuit
            c.endereco = cliente.endereco
            c.numTelefone= cliente.numTelefone
            c.save()
            faturaDB.cliente=c
            faturaDB.itemsProduto = fatura.itemsProduto
            for(ItemProduto ip in faturaDB.itemsProduto){
                if(ip.produto.codigo.empty){
                   // Messagebox.show("Elimine os items sem codigo valido!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "Elimine os items sem codigo valido!"
                    info.style = "color:red"
                    return
                }
                if(ip.qtd<=0){
                  //  Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!", "Lua", 1,  Messagebox.ERROR)
                    info.value = "O Item "+ip.produto.codigo+" deve ter pelo menus uma unidade!"
                    info.style = "color:red"
                    return
                }
                if(ip.precoDeVenda<=0){
                  //  Messagebox.show("O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!", "Lua", 1,  Messagebox.ERROR)
                   info.value = "O Item "+ip.produto.codigo+" deve ter pelo menus uma preço válido!"
                    return
                }
                ip.save()
            }

            faturaDB.valorDoIva=getTotalIva()
            faturaDB.save flush: true

        } catch (Exception e ){
          //  Messagebox.show("Erro na gravação da Fatura No. "+fatura.numeroDaFatura+":"+ e.toString(), "Lua", 1,  Messagebox.ERROR)
            info.value="Erro na gravação da Fatura No. "+fatura.numeroDaFatura+":"+ e.toString()
            info.style= "color:red"
            return
        }

       // Messagebox.show("Os dados da Fatura No. "+fatura.numeroDaFatura+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Os dados da Fatura No. "+fatura.numeroDaFatura+" foram actualizados com sucesso!"
    }


    @Command
    @NotifyChange("items")
    def addSelectedItem(@BindingParam("index") Long index){
        System.println("addSelectedItem"+index)
        ItemProduto itemProduto = new ItemProduto()
        Produto p = Produto.findById(index)
        itemProduto.setProduto(p)
        itemProduto.setDescricao(p.descricao)
        itemProduto.operacao = "fatura"
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))

        items.add(itemProduto)
        fatura.addToItemsProduto(itemProduto)
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
    def    editarFatura(){
        System.println("o botão editarFatura foi clicado")
        composersService.editarFatura = true
        Executions.sendRedirect("/fatura/edit")
    }


    @Init
    @NotifyChange(["inf"])
    init() {


        task.scheduledExecutionTime()
        fatura = Fatura.findById(composersService.faturaId)
        cliente= fatura.cliente
           }

    Cliente getCliente() {
        return cliente    }

    @NotifyChange(["inf"])
    Fatura getFatura() {
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
        if (fatura.equals(null)){
            return
        }

        if (items == null) {
            if (!(fatura==null))
                items=fatura.itemsProduto

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
        itemProduto.operacao = "fatura"
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))

        items.add(itemProduto)
        fatura.addToItemsProduto(itemProduto)
    }

    @Command
    @NotifyChange(["items","subTotal","totalIva","total","viewBtActualizarc"])
    public void removeItem(@BindingParam("index") Integer index) {
        System.println(index)
        items.remove(index)
        calcularSubTotal()
        viewBtActualizar=true
    }


    @Command
    @NotifyChange(["items","info"])
    def showMe(){

        items.each {
            if(!Produto.findByCodigo(it.produto.codigo)){
               // Messagebox.show("Codido do produto errado!"+selectedItemProduto, "Lua", 1, Messagebox.ERROR)
                info.style="color:red"
                info.value ="Codido do produto errado!"+selectedItemProduto
            }

        }
    }

    @Command
    def static fecharEditor(){
        Executions.sendRedirect("/fatura/list")
    }

    @Command
    @NotifyChange(["info"])
    def  imprimir(){
        if(fatura.cancelado){
            info.value = "Esta Fatura foi cancelada!"
            return
        }
        composersService.fatura=fatura
        Executions.sendRedirect("/fatura/imprimir")
        info.value="Para imprimir o duplicado pressione o botão direito"
    }
    @Command
    def  imprimirDuplicado(){
        if(fatura.cancelado){
            info.value = "Esta Fatura foi cancelada!"
            return
        }
        composersService.faturaId=fatura.id
        composersService.faturaNumber=fatura.numeroDaFatura
        Executions.sendRedirect("/fatura/imprimirDuplicado")
    }

    @Command
    @NotifyChange(["iva","valor","fatura","viewBtActualizar"])
    def alterarIva(@BindingParam("index") Integer index){
        // System.println(items[index].ivaAplicado)
        items[index].ivaAplicado=!items[index].ivaAplicado
        getTotalIva()
        calcularSubTotal()
        // System.println(items[index].ivaAplicado)
    }

    @NotifyChange(["fatura"])
    BigDecimal getTotalIva(){
        Iva iva = Iva.findByValido(true) as Iva
        BigDecimal vi = 0.0
        for (ItemProduto ip in fatura.itemsProduto){
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
        fatura.valorDoIva = vi
        return vi
    }


    @Command
    @NotifyChange(["viewBtActualizar","fatura","iva","valor","info"])
    def calcularSubTotal(){
        info.value=""
        fatura.setValor(0.0)
        fatura.setValorDoIva(0.0)
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
                System.println("updateFatura ip.valorDoIva = "+ip.valorDoIva)
            }else {
                ip.valorDoIva=0.0
                ip.subTotal = ip.precoDeVenda*ip.qtd
            }

            subTotal+=ip.subTotal

        }
        viewBtActualizar=true
        System.println("upDateFatura subtotal "+subTotal)
        System.println("updatefatura total iva= "+totalIva)
        fatura.setValor(subTotal)
        fatura.setValorDoIva(totalIva)
        System.println("upDateFatura valorDoIva "+fatura.valorDoIva)
        System.println("updatefatura fatura.valor= "+fatura.valor)
    }

    @Command
    @NotifyChange(["dvProdutosVisible","dvPagamentosVisible"])
    def pagar(){
        if(fatura.pago){
            info.value="Esta Fatura ja foi paga na totalidade!"
            info.style="color:blue"
        }
        recibo = new Recibo()
        cheque = new Cheque()
        if(dvPagamentosVisible){
            dvPagamentosVisible=false
            dvProdutosVisible = true
        }else {
            dvPagamentosVisible = true
            dvProdutosVisible = false
        }

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

    @Command
    @NotifyChange(['fatura','info',"recibos","bt_salvar_recibo"])
    def createRecibo(){

        if(fatura.pago){
            info.value="Esta Fatura Já foi paga na totalidade!"
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
            if(viewCheque){
                cheque.save(flush: true)
                def chequeDB = Cheque.find(cheque)
                if(chequeDB){
                    recibo.cheque=chequeDB
                  /*  recibo.fatura= fatura
                    recibo.save(flush: true)*/
                    System.println("cheque salvo com sucesso")

                }

               // Messagebox.show("Recibo No."+reciboDB.id+" criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
               /* info.style="color:blue"
                info.value="Recibo No."+reciboDB.id+" criado com sucesso!"*/
            }
                /*recibo.fatura=fatura*/
            recibo.save(flush: true)
                System.println("createRecibo, recibo.fatura"+fatura.numeroDaFatura)
                System.println("createRecibo, recibo.valor"+recibo.valor)


                def reciboDB = Recibo.find(recibo)
                def faturaDB  = Fatura.findById(fatura.id)
            if(reciboDB==null){
                info.value="Erro na gravação do recibo!"
                info.style="color:red"
            }else
            {
                recibos.add(reciboDB)
                    if(reciboDB.faturas==null){
                        Set<Fatura> faturas = new HashSet<Fatura>()
                        faturas.add(faturaDB)
                        reciboDB.faturas=faturas
                    }else {
                        reciboDB.faturas.add(faturaDB)
                    }
                    reciboDB.save(flush: true)
                    System.println("createRecibo, recibo salvo"+reciboDB.id)
                    info.value="O recibo foi gerado com sucesso!"
                    info.style="color:blue"
                    if(faturaDB){
                        if(faturaDB.recibos==null){
                            Set<Recibo> recibos = new HashSet<Recibo>()
                            recibos.add(recibo)
                            faturaDB.recibos=recibos

                        }else {
                            faturaDB.recibos.add(recibo)
                        }
                        if(faturaDB.valorEmDivida>0){
                            if(faturaDB.cliente.conta==null){

                                Conta conta = new Conta()
                                conta.numeroDaConta = contadorService.gerarNumeroDaConta()
                                conta.tipoDeConta= "ativs"
                                conta.natureza = "credora"

                            }
                        }
                        faturaDB.save(flush: true)
                       // Messagebox.show("Recibo No."+reciboDB.id+" criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                        info.style="color:blue"
                        info.value="Recibo No."+reciboDB.numeroDoRecibo+" criado com sucesso!"

                    }



                }




        }catch (Exception e){
            System.println(e.toString())
           // Messagebox.show("Erro na gravação do recibo!", "Lua", 1,  Messagebox.ERROR)
            info.style="color:red"
            info.value="Erro na gravação do recibo!"
        }
        bt_salvar_recibo=false
    }

    @Command
    def imprimirRecibo(@BindingParam("index") Integer index){
       /* System.println("imprimirRecibo index = "+index)
        composersService.reciboId = index
        Executions.sendRedirect("/fatura/imprimirRecibo")*/
        def reciboDB = Recibo.findById(index)
        if(reciboDB!=null){
            composersService.reciboId = reciboDB.id
            Executions.sendRedirect("/fatura/imprimirRecibo")
        }else {
            //  Messagebox.show("Por favor, salve o recibo !", "Lua", 1,  Messagebox.ERROR)
            info.value= "Erro na impressão!"
            info.style = "color:blue"
        }
    }
    @Command
    def imprimirNovoRecibo(){

        if(Recibo.findById(recibo.id)){
            composersService.reciboId = recibo.id
            Executions.sendRedirect("/fatura/imprimirRecibo")
        }else {
          //  Messagebox.show("Por favor, salve o recibo !", "Lua", 1,  Messagebox.ERROR)
            info.value= "Por favor, salve o recibo !"
            info.style = "color:blue"
        }

    }
    BigDecimal getValor() {
        valor=fatura.valor
        return valor
    }

    void setValor(BigDecimal valor) {
        this.valor = valor
    }

    BigDecimal getIva() {
        iva = fatura.valorDoIva
        return iva
    }

    void setIva(BigDecimal iva) {
        this.iva = iva
    }

    Recibo getRecibo() {
        return recibo
    }

    void setRecibo(Recibo recibo) {
        this.recibo = recibo
    }

    Cheque getCheque() {
        return cheque
    }

    void setCheque(Cheque cheque) {
        this.cheque = cheque
    }

    boolean getViewCheque() {
        return viewCheque
    }

    void setViewCheque(boolean viewCheque) {
        this.viewCheque = viewCheque
    }

    def timer = new Timer()



    def task = timer.runAfter(10000) {
        System.println("task one")
        inf = ""
    }
}
