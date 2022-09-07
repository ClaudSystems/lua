package encomenda

import estoque.EncomendaService
import lua.entidades.fornecedor.Fornecedor
import lua.estoque.encomenda.Encomenda
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.produto.Produto
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.*
import org.zkoss.zk.ui.select.annotation.Wire
class IndexComposer extends zk.grails.Composer {
    EncomendaService encomendaService
    @Wire
    Intbox ib_id
    @Wire
     Column cl_id
    Combobox cb_fornecedores
    Combobox cb_produtos
    private ListModelList<Object> items
    Textbox tb_descricao

    @Wire
    Intbox ib_qtd
    Doublebox db_valor
    Menuitem mi_remover
    Menuitem mi_add
    Grid gd_encomenda
    Grid gd_addItems
    def encomendasDB = Encomenda.all
    Textbox tb_filter


     Encomenda selectedEncomenda = new Encomenda()
    Label lb_info
    Listbox lb_encomendas
    Listbox lb_items
    ArrayList<Encomenda> dados = new ArrayList()


    def afterCompose = { window ->
        renderLBEncomendas()
        renderCb_fornecedores()

    }

    def renderCb_produtos (){
        def produtos = Produto.all
        List list_produtos = new ArrayList()
        for (Produto p : produtos) {
            list_produtos.add(p.toString())
        }
        ListModelList lm_produtos = new ListModelList(list_produtos)
        cb_produtos.setModel(lm_produtos)
    }

    def renderCb_fornecedores (){
        def fornecedores = Fornecedor.all
        List list_fornecedores = new ArrayList()
        for (Fornecedor f : fornecedores) {
            list_fornecedores.add(f.toString())
        }
        ListModelList lm_fornecedores = new ListModelList(list_fornecedores)
        cb_fornecedores.setModel(lm_fornecedores)
    }


    def renderLBEncomendas() {
        lb_info.value = ""
        encomendasList()
        lb_encomendas.getItems().clear()
               lb_encomendas.setItemRenderer(new ListitemRenderer<Encomenda>() {
            @Override
            void render(Listitem item, Encomenda encomenda, int i) throws Exception {
                new Listcell(encomenda.id.toString()).setParent(item)
                new Listcell(encomenda.descricao).setParent(item)
                new Listcell(encomenda.fornecedor?.nome).setParent(item)
                new Listcell(encomenda.estado).setParent(item)
                new Listcell(encomenda.tipo).setParent(item)
                item.setValue(encomenda)
            }
        })

    }


    def encomendasList() {
        def encomendas = new ArrayList<Encomenda>()
        for (Encomenda encomenda in encomendasDB) {

            encomendas.add(encomenda)

        }
        ListModelList lml = new ListModelList(encomendas, true)
        lb_encomendas.setModel(lml)
    }
    def renderLBItems() {
              itemsList()
        lb_items.getItems().clear()
        lb_items.setItemRenderer(new ListitemRenderer<ItemProduto>() {
            @Override
            void render(Listitem item, ItemProduto itemProduto, int i) throws Exception {
                new Listcell(itemProduto.produto.descricao).setParent(item)
                new Listcell(itemProduto.qtd.toString()).setParent(item)
                if(!itemProduto.preco.equals(null))
                new Listcell(itemProduto.preco.toString()).setParent(item)
                item.setValue(itemProduto)
            }
        })

    }


    def itemsList() {
        def items = new ArrayList<ItemProduto>()
        def itemsDB = selectedEncomenda.itemsProduto

        for (ItemProduto ip in itemsDB) {
            items.add(ip)
        }
        ListModelList lml = new ListModelList(items, true)
        lb_items.setModel(lml)
    }


    public void openGrid() {
       gd_encomenda.setVisible(true)
        gd_encomenda.focus()

    }


    @Listen("onDoubleClick = listbox#lb_encomendas")
    public void editarEncomenda() {

      openGrid()
        def str = lb_encomendas.selectedItem.value.toString()
        def values = str.split('-')
        def id = values[0].toInteger()
        selectedEncomenda = Encomenda.get(id)
        cb_fornecedores.selectedItem = null
        tb_descricao.value = ""
        for (Comboitem comboitem in cb_fornecedores.items){
            if (comboitem.value.equals(selectedEncomenda.fornecedor.toString())){
                cb_fornecedores.setSelectedItem(comboitem)
            }
        }
        tb_descricao.value = selectedEncomenda.descricao
        cl_id.label = "No. "+selectedEncomenda.id.toString()
      renderLBItems()
    }



    @Listen("onClick = button#bt_update")
    public void update() {
        Encomenda encomenda = Encomenda.find(selectedEncomenda)
        if (!encomenda.equals(null)) {
            encomenda.descricao = tb_descricao?.value
            encomenda.fornecedor = Fornecedor.findByNome(cb_fornecedores.selectedItem.value.toString())
            encomenda.save()
            /*Messagebox.show(encomenda.descricao + " Actualizado com sucesso!", "Lua", 1, Messagebox.INFORMATION)*/
            renderLBEncomendas()


        }

    }

    @Listen("onClick = button#bt_delite")
    public void delite() {
        try {
            def encomenda = Encomenda.get(selectedEncomenda.id)
            encomenda.delete flush: true

            Messagebox.show("Ecomenda No. "+encomenda.id + " Eliminado!", "Lua", 1, Messagebox.INFORMATION)
            renderLBEncomendas()
            cb_fornecedores.children.clear()
            tb_descricao.value=""
        }catch (Exception e){
            Messagebox.show("Esta encomenda ja nao existe!", "Lua", 1, Messagebox.ERROR)
        }


    }


    @Listen("onClick = tab#tb_encomenda")
    public void openEncomenda() {

    }
    @Listen("onClick = menuitem#mi_remover")
    public void removerItem() {
       selectedEncomenda.itemsProduto.remove(lb_items.selectedItem.value)
        selectedEncomenda.save()
        renderLBItems()
    }
    @Listen("onClick = menuitem#mi_add")
    public void addItem() {

        renderCb_produtos()
        gd_addItems.setVisible(true)

    }


    @Listen("onOK = combobox#cb_produtos")
    public  void filtrarProdutos(){
        //renderCb_produtos()
        cb_produtos.getChildren().clear()
        def searchResults = Produto.withCriteria {
            like('descricao',  '%' + cb_produtos.value + '%')
            like('codigo', '%'+ cb_produtos.value + '%')

        }

        ListModelList lm_produtos = new ListModelList(searchResults)
        cb_produtos.setModel(lm_produtos)

    }
    @Listen("onCancel = combobox#cb_produtos")
    public  void repor(){
        renderCb_produtos()

    }


    @Listen("onClick = button#bt_add")
    public  void add(){
        ItemProduto itemProduto = new ItemProduto()
        itemProduto.preco=db_valor.value
        itemProduto.qtd = ib_qtd.value
        def produto = Produto.findAllByDescricao(cb_produtos.selectedItem.value.toString())
        itemProduto.produto = produto[0]
        selectedEncomenda.addToItemsProduto(itemProduto)
        selectedEncomenda.save()
        gd_addItems.setVisible(false)
        renderLBItems()
    }

    @Listen("onClick = button#bt_show")
    public  void show(){
        encomendaService.encomenda = selectedEncomenda

        session.setAttribute("encomenda", selectedEncomenda)
        Executions.sendRedirect("/encomenda/showEncomenda.gsp")

    }

    @Listen("onOK = intbox#ib_id")
    public  void getEncomenda(){
      try {
          openGrid()

          selectedEncomenda = Encomenda.get(ib_id.value)
          cb_fornecedores.selectedItem = null
          tb_descricao.value = ""
          for (Comboitem comboitem in cb_fornecedores.items){
              if (comboitem.value.equals(selectedEncomenda.fornecedor.toString())){
                  cb_fornecedores.setSelectedItem(comboitem)
              }
          }
          tb_descricao.value = selectedEncomenda.descricao
          cl_id.label = "No. "+selectedEncomenda.id.toString()
          renderLBItems()
      } catch ( Exception e){
          Messagebox.show("Encomenda nao encontrada!", "Lua", 1,  Messagebox.ERROR)
      }


    }

    @Listen("onOK = textbox#tb_filter")
    public  void filtrar(){

        def c = Encomenda.createCriteria()
        encomendasDB = c.list {
            ilike("descricao", "%"+tb_filter.value+"%")

            maxResults(10)
            order("descricao", "desc")
        }
        renderLBEncomendas()
    }
    @Listen("onClick = button#bt_close")
    public  void close(){
        gd_encomenda.setVisible(false)

    }






}
