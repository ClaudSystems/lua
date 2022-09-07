package pos

import base.ComposersService
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import lua.Settings
import lua.SettingsService
import lua.contador.ContadorService
import lua.contas.Conta
import lua.entidades.cliente.Cliente
import lua.entidades.cliente.ClienteService
import lua.estoque.categoria.Categoria
import lua.estoque.categoria.CategoriaService
import lua.estoque.estoque.Armazem
import lua.estoque.estoque.ArmazemService
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.itemProduto.ItemProdutoService
import lua.estoque.iva.Iva
import lua.estoque.iva.IvaService
import lua.estoque.produto.Produto
import lua.estoque.produto.ProdutoService
import lua.restaurante.mesa.Diario
import lua.restaurante.mesa.DiarioService
import lua.restaurante.mesa.MesaService
import lua.security.Utilizador
import lua.security.UtilizadorService
import lua.vendas.fatura.VdService
import lua.vendas.recibo.Cheque
import lua.vendas.recibo.FormaDePagamento
import lua.vendas.recibo.Recibo
import org.zkoss.bind.annotation.BindingParam

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Button
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import lua.restaurante.mesa.Mesa
import lua.vendas.fatura.Vd
import org.zkoss.zul.Textbox

@Transactional
class PosViewModel {
    VdService vdService
    CategoriaService categoriaService
    IvaService ivaService
    ProdutoService produtoService
    ItemProdutoService itemProdutoService
    MesaService mesaService
    DiarioService diarioService
    ArmazemService armazemService
    ClienteService clienteService
    SettingsService settingsService
    private ListModelList<Categoria> categogos = new ListModelList<>()
    Diario diario
    ComposersService composersService
    SpringSecurityService springSecurityService
    ContadorService contadorService
    Recibo recibo
    boolean dvPagamentosVisible = false
    boolean dvProdutosVisible = false
    Recibo selectedRecibo
    boolean bt_salvar_recibo = true
    private ListModelList<Categoria> categorias
    private ListModelList<Produto> produtos
    private ListModelList<Produto> produtos1
    private ListModelList<Produto> produtos2
    private ListModelList<Produto> produtos3
    private ListModelList<Produto> produtos4
    private ListModelList<Mesa> mesas
    private ListModelList<ItemProduto> items
    private ListModelList<ItemProduto> vditems
    private ListModelList<Vd> vds
    private Mesa selectedMesa
    private String mesa_label
    private String label
    private boolean showMesas =true
    private boolean estado = true
    boolean  viewCheque = false
    private BigDecimal valorTotal
    private  String procuraNome
    Cheque cheque
    private Vd selectedVd = new Vd()
    @Wire  bt_estado = new Button()
   @Wire Label info
   @Wire Label lb_valor_total
   @Wire Button bt_diario
   boolean tb_label = false
    boolean bt_salvar = false
    boolean  bt_update = false
    boolean  lb_items = false
    boolean  lb_vd_items = false
    String red = "color:red"
    String blue= "color:blue"
    ItemProduto selectedItem
    ItemProduto selectedVdItem
    private selectedFormaDePagamento
    private ListModelList<Recibo> recibos

    String getProcuraNome() {
        return procuraNome
    }

    void setProcuraNome(String procuraNome) {
        this.procuraNome = procuraNome
    }

    BigDecimal getValorTotal() {
        return valorTotal
    }
    boolean getTb_label() {
        return tb_label
    }

    String getLabel() {
        return label
    }

    void setLabel(String label) {
        this.label = label
    }

    @Command
    @NotifyChange(["dvProdutosVisible","dvPagamentosVisible","bt_update"])
    def pagar(){
        if(selectedVd.pago){
            info.value="Esta VD ja foi paga na totalidade!"
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
        bt_update = false
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
    def getSelectedFormaDePagamento() {
        return selectedFormaDePagamento
    }

    void setSelectedFormaDePagamento(selectedFormaDePagamento) {
        this.selectedFormaDePagamento = selectedFormaDePagamento
    }
    private ListModelList<FormaDePagamento> formasDePagamento

    ListModelList<FormaDePagamento> getFormasDePagamento() {

        if (formasDePagamento == null) {
            formasDePagamento = new ListModelList<FormaDePagamento>(FormaDePagamento.all)
        }
        return formasDePagamento
    }
    boolean getEstado() {
        return estado
    }

    boolean getLb_items() {
        return lb_items
    }

    @Command
    @NotifyChange(['valorTotal',"selectedVd","items","lb_items","mesa_label","estado","bt_update","bt_salvar","selectedMesa","recibo",'tb_label'])
    def showVd(@BindingParam("id") Integer id){

        recibo =null
        bt_update = true
        bt_salvar = false
        estado=false
        info.value = ""
        selectedVd = vdService.findById(id)
        getItems()
        if (selectedVd!=null){
            tb_label= true
            getVditems()
        }else {
            getVditems()
            tb_label = false
        }

    }

    ListModelList<Vd> getVds() {
        vds = new ListModelList<>()
        Date date = new Date()

       if (selectedMesa!=null){
           vds=vdService.findAllWhere(selectedMesa,diario)
       }
        return vds
    }

    Vd getSelectedVd() {
        return selectedVd
    }


    @NotifyChange(["selectedVd","valorTotal","bt_salvar"])
    @Command
    def  updateVd(){
        valorTotal = 0.0
        if(selectedVd ==null){
            selectedVd = new Vd()
            bt_salvar = true
            bt_update = false
        }

        for (ItemProduto ip in items){
            if(ip.ivaAplicado){
                System.println('ip.ivaAplicado=true'+ip.ivaAplicado+ip.produto.nome)
                if(ip.qtd==0){
                    info.value = "Não tem QTD suficiente!"
                    info.style = red
                    return
                }
                ip.valorDoIva = ip.precoDeVenda*ivaService.iva.percentualIva/100
            }else {
                ip.valorDoIva = 0.0
            }
            valorTotal+= ip.precoDeVenda+ip.valorDoIva
        }

       // selectedVd.itemsProduto=items
        System.println('selectedVd.valorTotal='+selectedVd.valorTotal)
        System.println('selectedVd.valor='+selectedVd.valor)
        System.println('selectedVd.valorDoIva='+selectedVd.valorDoIva)

    }

    @NotifyChange(["selectedVd","valor","bt_salvar"])
    @Command
    def  updateVditems(){
        for (ItemProduto ip in selectedVd.itemsProduto){
            if(ip.ivaAplicado){
                ip.valorDoIva = ip.precoDeVenda*ivaService.iva.percentualIva/100
            }else {
                ip.valorDoIva=0.0
            }

        }
    }
    def  updateSelectedVd(){
        if(selectedVd.pago){
            info.value = "Esta VD já foi paga!"
            info.style = red
            return
        }
        if(selectedVd ==null){
            selectedVd = new Vd()
            bt_salvar = true
            bt_update = false
        }


      }

    @NotifyChange("vditems")
    ListModelList<ItemProduto> getItems() {
        if (items == null) {
            items= new ListModelList<ItemProduto>()
        }
        if(selectedVd?.itemsProduto!=null){
            items = new ListModelList<>()
            for (ItemProduto ip in selectedVd.itemsProduto){
                items.add(itemProdutoService.findById(ip.id))
            }
        }
        return items
    }

    ListModelList<ItemProduto> getVditems() {
        if (vditems == null) {
            vditems= new ListModelList<ItemProduto>()
        }
        if(selectedVd?.itemsProduto!=null){
            vditems = new ListModelList<>()
            for (ItemProduto ip in selectedVd.itemsProduto){
                vditems.add(itemProdutoService.findById(ip.id))
            }
        }

        return vditems
    }

    @Command
    @NotifyChange(["items","selectedVd","info",'valorTotal'])
    void removeItem(@BindingParam("index") Integer index) {
        if(selectedVd.pago){
            info.value = "Este VD já foi pago!"
            info.style = blue
            return
        }
        info.value = ""
        ItemProduto ip = items[index]
        items.remove(index)
        if(selectedVd.itemsProduto!=null){
            selectedVd.itemsProduto.remove(ip)
        }

        updateVd()
    }

    @Command
    @NotifyChange(["selectedVd","info"])
    void removeVdItem(@BindingParam("id") Integer id) {
        info.value = ""
        if(selectedVd.pago){
            info.value = "Este VD já foi pago!"
            info.style = blue
            return
        }
       ItemProduto itemProduto = itemProdutoService.findById(id)
        selectedVd.itemsProduto
    }

    @NotifyChange(["selectedVd","valor","info","bt_salvar","bt_update","selectedMesa",'valorTotal'])
    @Command
    def addProduto(@BindingParam("id") Integer id){
        System.println('addProduto')
        if(selectedVd?.pago){
            info.value = "Este VD Já foi pago!"
            info.style = blue
            return
        }
        info.value = ""
        if(vdService.findById(selectedVd?.id)){
            System.println("addSelectedItem: "+id)
            ItemProduto itemProduto = new ItemProduto()
            Produto p = produtoService.findById(id)
            if(!settingsService.settings.aceitarSaidasSemEstoque){
                if(p.saldo<=0){
                    info.value = "Não tem saldo suficiente!"
                    info.style = red
                    return
                }
            }
            itemProduto.setProduto(p)
            itemProduto.qtd = 1
            itemProduto.valorDoIva = p.valorDoIva
            itemProduto.estocavel = p.estocavel
            System.println('p.ivaAplicado='+p.ivaAplicado)
            itemProduto.setDescricao(p.descricao)
            itemProduto.ivaAplicado = p.ivaAplicado
            itemProduto.precoDeVenda = p.precoDeVenda
            System.println(itemProduto.precoDeVenda)
            itemProduto.setArmazem(armazemService.findByCodigo())
            if(settingsService.settings.refletirSaidasEmEstoqueDeVds){
                itemProduto.operacao = "saida"
            }else {
                itemProduto.operacao = "vd"
            }
            selectedVd.itemsProduto.add(itemProduto)
            System.println('itemProduto.ivaAplicado='+itemProduto.ivaAplicado)
            items.add(itemProduto)
           // updateSelectedVd()

        }else {
            selectedMesa.estado="aberta"
            System.println("addSelectedItem: "+id)
            ItemProduto itemProduto = new ItemProduto()
            Produto p = produtoService.findById(id)

            if(!settingsService.settings.aceitarSaidasSemEstoque){
                if(p.saldo<=0){
                    info.value = "Não tem saldo suficiente!"
                    info.style = red
                    return
                }
            }
            itemProduto.setProduto(p)
            itemProduto.qtd = 1
            itemProduto.iva = p.iva
            itemProduto.estocavel = p.estocavel

            itemProduto.setDescricao(p.descricao)
            itemProduto.ivaAplicado = p.ivaAplicado
            itemProduto.precoDeVenda = p.precoDeVenda
            System.println(itemProduto.precoDeVenda)
            itemProduto.setArmazem(armazemService.findByCodigo())
            if(settingsService.settings.refletirSaidasEmEstoqueDeVds){
                itemProduto.operacao = "saida"
            }else {
                itemProduto.operacao = "vd"
            }
            items.add(itemProduto)

            updateVd()
        }

    }


    String getMesa_label() {
        return mesa_label
    }

    @NotifyChange(["showMesas","selectedMesa","bt_estado","selectedVd","items","vds"])
    @Command
    def addSelectedMesa(){
        items.clear()
        selectedVd = null
       /* if(selectedMesa.estado=="aberta"||selectedMesa.estado=="pendente"){
          getVds()
        }*/
        System.println("addSelectedMesa selectedMesa="+selectedMesa)
        selectedMesa = mesaService.findById(selectedMesa.id)
        showMesas = true

    }

    @Command
    @NotifyChange(["showMesas","selectedMesa","items","estado","bt_update","dvProdutosVisible","selectedVd","dvPagamentosVisible"])
    def selecionarMesa(){

        if(diario==null){
            info.value = "Por favor, Abra um diario de vendas!"
            info.style = blue
            return
        }
        if(mesas==null||mesas.empty){
            info.value = "Registre as Mesas nos cadastros!"
            info.style = blue
            return
        }
        dvPagamentosVisible = false
        selectedVd = null
        dvProdutosVisible = true
        bt_update = false
        if(selectedMesa!=null){
            selectedMesa.estado = "aberta"
        }

            estado=true
        info.value = ""
        if(items!=null){
            items.clear()
        }

       selectedVd = null
        selectedMesa = null
        if(showMesas){
            showMesas=false
        }else
        showMesas = true
    }
    boolean getShowMesas() {
        return showMesas
    }

    ListModelList<Mesa> getMesas() {
        if(mesas==null){
            mesas = new ListModelList<>(Mesa.all)
        }
        return mesas
    }

    @NotifyChange(["showMesas","mesa_label",'tb_label'])
    Mesa getSelectedMesa() {

        showMesas = false
        if(selectedMesa!=null){
            mesa_label = selectedMesa.numeroDaMesa

        }
        return selectedMesa
    }

    void setSelectedMesa(Mesa selectedMesa) {
        this.selectedMesa = selectedMesa
    }

    ListModelList<Produto> getProdutos1() {
        if(produtos1 ==null){
            produtos1 = new ListModelList<Produto>()
        }
        return produtos1.sort()

    }
    ListModelList<Produto> getProdutos2() {
        if(produtos2 ==null){
            produtos2 = new ListModelList<Produto>()
        }
        return produtos2.sort()

    }

    ListModelList<Produto> getProdutos3() {
        if(produtos3 ==null){
            produtos3 = new ListModelList<Produto>()
        }
        return produtos3.sort()

    }

    ListModelList<Produto> getProdutos4() {
        if(produtos4 ==null){
            produtos4 = new ListModelList<Produto>()
        }
        return produtos4.sort()

    }
    ListModelList<Categoria> getCategorias() {
        if(categorias==null){
            categorias = new ListModelList<Categoria>(Categoria.all.sort())
        }
        return categorias.sort()
    }

    ListModelList<Produto> getProdutos() {
        if(produtos == null){
            produtos = new ListModelList<>()
        }
        return produtos
    }

    @Init init() {
        diario = diarioService.findByEstado('pendente')
        diario = diarioService.findByEstado("aberto")
        selectedVd = new Vd()
        selectedVd.cliente = clienteService.findByCodigo("pbc")
        mesa_label = "Selecionar Mesa"
        bt_update = false

    }

    @Command
    @NotifyChange(["diario"])
    def mudarDiario(){
        if(diario.estado =="pendente"){
            if(diarioService.findByEstado("aberto")){
                diario = diarioService.findByEstado("aberto")
                bt_diario.style = "background:#7fb3d5"
            }

        }else {
            if(diarioService.findByEstado("pendente")){
                diario = diarioService.findByEstado("pendente")
                bt_diario.style = "background:#f1948a"
            }

        }
    }

    @Command
    @NotifyChange(["produtos"])
    def showItems(@BindingParam("id") Integer id){
        produtos.clear()
        produtos1.clear()
        produtos2.clear()
        produtos3.clear()
      //  produtos4.clear()

        def categoriaDB = categoriaService.findById(id)
        categoriaDB.produtos.sort()
        int quarto = categoriaDB.produtos.size()/4
        for(int y=0; y<categoriaDB.produtos.size(); y++){
            if(categoriaDB.produtos[y]!=null){
                produtos.add(categoriaDB.produtos[y])
            }

            y++
            if(categoriaDB.produtos[y]!=null){
                produtos1.add(categoriaDB.produtos[y])
            }

            y++
            if(categoriaDB.produtos[y]!=null){
                produtos2.add(categoriaDB.produtos[y])
            }

            y++
            if(categoriaDB.produtos[y]!=null){
                produtos3.add(categoriaDB.produtos[y])
            }
           /* y++
            if(categoriaDB.produtos[y]!=null){
                produtos4.add(categoriaDB.produtos[y])
            }*/

        }

    }

    @Command
    @NotifyChange(["vds","info","bt_salvar","selectedMesa"])
    def createVd(){
        def id =(springSecurityService.principal?.id)
        System.println('user.id='+id)
        def user = vdService.getUser(id)
            selectedVd.utilizador = user
        if(selectedVd.utilizador.equals(null)){
            //  Messagebox.show("Seleccione um Utilizador! ", "Lua", 1,  Messagebox.ERROR)
            info.value = "Utilizador Inválido! "
            info.style=red
            return
        }
            selectedVd.itemsProduto = new ArrayList<ItemProduto>(items)
        try {

            selectedVd.diario = diario
            selectedVd.numeroDeVd = contadorService.gerarNumeroDeVd()
            System.println("listagem dos items da vd")

            if(selectedVd.itemsProduto==null){
                info.value = "Seleccione pelo menos um item válido! "
                info.style = red
                return
            }
            for (ItemProduto ip in selectedVd.itemsProduto){

                System.println("ip="+ip.produto.nome)
                if(!settingsService.settings.aceitarSaidasSemEstoque){
                    if(produto.saldo<ip.qtd){
                        info.value = ip.produto.nome+" Não tem saldo suficiente!"
                        info.style = red
                        return
                    }
                }

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
            System.println("__-----------------------------------__")

            selectedVd.setCliente(clienteService.findByCodigo("pbc"))
            if(selectedVd.cliente.equals(null)){
                //  Messagebox.show("Seleccione pelo menos cliente! ", "Lua", 1,  Messagebox.ERROR)
                info.value = "Seleccione pelo menos cliente! "
                info.style=red
                return
            }






            System.println(selectedVd.valorDoIva)
            System.println(selectedVd.valor)
            System.println(selectedVd.itemsProduto)
            System.println(selectedVd.dateCreated)
            System.println(selectedVd.cliente)
            //vd.setEstado("Aberto")
           selectedVd.pago = false

            clienteService.save(selectedVd.cliente)
            selectedVd.mesa = selectedMesa
            vdService.saveVd(selectedVd)
          // selectedVd.save flush: true
            System.println("vd.save flash:true")

        } catch (Exception e ){
            // Messagebox.show("Erro na gravação "+e.message, "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na gravação "+e.message
            info.style = red
            return
        }
        def vdDB = vdService.findById(selectedVd.id)
        def diarioDb = Diario.findById(diario.id)
        System.println(vdDB)
        if(vdService.findById(selectedVd.id)){
            // Messagebox.show("Os dados da Vd No. "+vdDB.id+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
            info.style = blue
            info.value = "Os dados da Vd No. "+vdDB.numeroDeVd+" foram criados com sucesso!"
            bt_salvar = false
            selectedMesa.estado="aberta"
            if(diarioDb.vds==null){
                diarioDb.vds = new ArrayList<Vd>()
            }
            diarioDb.vds.add(vdDB)
            diarioService.save(diarioDb)

        }
        selectedVd = vdDB

    }

    @Command
    @NotifyChange(["selectedVd","items","vds","selectedMesa",'tb_label'])
    def updateMesa(){

            addSelectedMesa()
            vds.clear()
        items.clear()
        getSelectedVd()
        getVds()
    }

    @Command
    @NotifyChange(["selectedVd","info"])
    def atualizar(){
        System.println('items'+items)
        if(selectedVd.pago){
            info.value = 'Esta VD ja foi paga!'
            return

        }

        try {
            System.println('atualizar.itemsProduto='+selectedVd.itemsProduto)

            System.println('atualizar.itemsProduto.atualizados='+selectedVd.itemsProduto)
            vdService.updateVd(selectedVd)
            info.value = "Dados atualizados com sucesso!"
            info.style=blue
        }catch ( Exception e){
            info.value= e.toString()
        }

    }

    @Command
    @NotifyChange(['selectedVd','info',"recibos","bt_salvar_recibo"])
    def createRecibo(){
        if(selectedVd.pago){
            info.value="Este VD Já foi paga na totalidade!"
            info.style="color:red"
            return
        }
        if(selectedFormaDePagamento==null){
            info.value="Por favor, Selecione a Forma de Pagamento!"
            info.style="color:red"
            return
        }
        if(recibo.valor<selectedVd.valor ){
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
            /*if(viewCheque){
                cheque.save(flush: true)
                def chequeDB = Cheque.find(cheque)
                if(chequeDB){
                    recibo.cheque=chequeDB
                    *//*  recibo.fatura= fatura
                      recibo.save(flush: true)*//*
                    System.println("cheque salvo com sucesso")

                }

                // Messagebox.show("Recibo No."+reciboDB.id+" criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                *//* info.style="color:blue"
                 info.value="Recibo No."+reciboDB.id+" criado com sucesso!"*//*
            }
            *//*recibo.fatura=fatura*//*
            recibo.save(flush: true)
            System.println("createRecibo, recibo.selectedVd"+selectedVd.numeroDeVd)
            System.println("createRecibo, recibo.valor="+recibo.valor)


            def reciboDB = Recibo.findById(recibo.id)
            def vdDB  = Vd.findById(selectedVd.id)
            if(reciboDB==null){
                info.value="Erro na gravação do recibo!"
                info.style="color:red"
            }else
            {
                recibos.add(reciboDB)
                if(reciboDB.vds==null){
                    Set<Vd> vds = new HashSet<Vd>()
                    vds.add(vdDB)
                    reciboDB.vds=vds
                }else {
                    reciboDB.vds.add(vdDB)
                }
                reciboDB.save(flush: true)
                System.println("createRecibo, recibo salvo"+reciboDB.id)
                info.value="O recibo foi gerado com sucesso!"
                info.style="color:blue"
                if(vdDB){
                    if(vdDB.recibos==null){
                        Set<Recibo> recibos = new HashSet<Recibo>()
                        recibos.add(recibo)
                        vdDB.recibos=recibos

                    }else {
                        vdDB.recibos.add(recibo)
                    }
                    if(vdDB.valorEmDivida>0){
                        if(vdDB.cliente.conta==null){

                            Conta conta = new Conta()
                            conta.numeroDaConta = contadorService.gerarNumeroDaConta()
                            conta.tipoDeConta= "ativs"
                            conta.natureza = "credora"

                        }
                    }
                    vdDB.save(flush: true)
                    // Messagebox.show("Recibo No."+reciboDB.id+" criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                    info.style="color:blue"
                    info.value="Recibo No."+reciboDB.numeroDoRecibo+" criado com sucesso!"

                }



            }
*/
            selectedVd.formaDePagamento = selectedFormaDePagamento
            if(viewCheque){
                selectedVd.cheque = cheque
            }
            selectedVd.pago = true
            atualizar()
            info.style="color:blue"
            info.value="VD Pago!"

        }catch (Exception e){
            System.println(e.toString())
            // Messagebox.show("Erro na gravação do recibo!", "Lua", 1,  Messagebox.ERROR)
            info.style="color:red"
            info.value="Erro na gravação do recibo!"
        }
        bt_salvar_recibo=true
    }

    @Command
    def imprimirVd(@BindingParam("index") Integer index){
        if(selectedVd.pago){
            System.println("imprimirRecibo index = "+selectedVd.id)
            composersService.vdId = selectedVd.id
            Executions.sendRedirect("/vd/ImprimirVdMini")
        }else {
            info.value = "Esta VD ainda não foi paga"
            info.style = red
        }

    }

    @Command
    def sair(){
        Executions.sendRedirect("/home/")
    }
    @Command
    def refresh(){

        Executions.sendRedirect("/pos/pos")
    }

    @Command
    def imprimirPagamento(@BindingParam("index") Integer index){
            if(selectedVd?.itemsProduto==null){
                info.value = "Selecione pelo menos uma VD!"
                info.style = blue
                return
            }
        if(!selectedVd.pago){
            System.println("imprimirRecibo index = "+selectedVd.id)
            composersService.vdId = selectedVd.id
            Executions.sendRedirect("/vd/ImprimirPagamento")
        }else {
            info.value = "Esta VD Já foi Paga"
            info.style = red
        }

    }

    @Command
    @NotifyChange(['selectedVd'])
    def searchVdByLabel(){
        Calendar cal = Calendar.getInstance()
        int year = cal.get(Calendar.YEAR)
        def numero = year+"/"+label
        Vd vd = vdService.findByNumeroDeVd(numero)
        System.println(vd)
            System.println('searchVdByLabel='+label)
        if (vd!=null){
            selectedVd = vd
            System.println(selectedVd.numeroDeVd)
        }else {
            info.value= "VD Não localizado!"
        }

    }

    @Command
    @NotifyChange(['categorias'])
    def hideCategorias(){


    }

  /*  @Command
    def imprimirNovoVd(){

        if(Recibo.findById(recibo.id)){
            composersService.reciboId = recibo.id
            Executions.sendRedirect("/vd/imprimirVd")
        }else {
            //  Messagebox.show("Por favor, salve o recibo !", "Lua", 1,  Messagebox.ERROR)
            info.value= "Por favor, salve o recibo !"
            info.style = "color:blue"
        }

    }*/
    @Command
    @NotifyChange(['selectedVd'])
    def searchByLabel (){
        info.value = ''
       def newVd = vdService.findByLabel(procuraNome)
        if(newVd!=null){
            selectedVd = vdService.findByLabel(procuraNome)
        }else {
            info.value = "Não existe VD com o  nome :"+procuraNome+ ''
        }

    }
}
