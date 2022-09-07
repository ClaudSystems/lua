package encomenda

import estoque.EncomendaService
import lua.entidades.fornecedor.Fornecedor
import lua.estoque.encomenda.Encomenda
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.produto.Produto
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.Session
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.*

class NovaEncomendaComposer extends zk.grails.Composer {
    Window win
    Textbox tb_codigo_produto
    Textbox tb_descricao
    ArrayList<MyData> dados = new ArrayList()
    EncomendaService encomendaService
    Button bt_filter_produto
    Listbox lb_items
    Listbox lb_encomendas
    Combobox cb_fornecedores
    def afterCompose = { window ->
        renderListBox()
        renderCb_fornecedores()
        renderLBFornecedores()
    }

    def renderListBox() {
        itemsList()
        lb_items.setItemRenderer(new ListitemRenderer<ItemProduto>() {
            @Override
            void render(Listitem item, ItemProduto ip, int i) throws Exception {
                new Listcell(ip.produto.codigo).setParent(item)
                Listcell cell0 = new Listcell()
                Listcell cell1 = new Listcell()
                Listcell cell2 = new Listcell()
                Listcell cell3 = new Listcell()

                Textbox tb_descricao = new Textbox()

                tb_descricao.value = ip.produto.descricao
                tb_descricao.width = "100%"
                Intbox ib_qtd = new Intbox()

                Doublebox db_valor = new Doublebox()
                db_valor.width = "100%"
                db_valor.setFormat("#,##0.##")
                Checkbox ckb_select = new Checkbox()

                MyData myData = new MyData()
                myData.setCodigo(ip.produto.codigo)
                myData.setTb_descricao(tb_descricao)
                myData.setIb_qtd(ib_qtd)
                myData.setDb_valor(db_valor)
                myData.setCkb_select(ckb_select)
                dados.add(myData)

                cell0.appendChild(tb_descricao)
                cell1.appendChild(ib_qtd)
                cell2.appendChild(db_valor)
                cell3.appendChild(ckb_select)


                item.appendChild(cell0)
                item.appendChild(cell1)
                item.appendChild(cell2)
                item.appendChild(cell3)


                item.setValue(ip)


            }
        })
    }

    def itemsList() {

        def itemsPro = encomendaService.getItems()
        ListModelList lml = new ListModelList(itemsPro, true)
        lb_items.setModel(lml)
    }


    def renderLBFornecedores() {
        encomendasList()

        lb_encomendas.getItems().clear()
        lb_encomendas.setItemRenderer(new ListitemRenderer<Encomenda>() {

            @Override
            void render(Listitem item, Encomenda encomenda, int i) throws Exception {
                new Listcell(encomenda.id.toString()).setParent(item)
                new Listcell(encomenda.descricao).setParent(item)
                new Listcell(encomenda.estado).setParent(item)

                item.setValue(encomenda)
            }
        })
    }

    def encomendasList() {
        def encomendas = new ArrayList<Encomenda>()
        def encomendasDB = Encomenda.all

        for (Encomenda f in encomendasDB) {

            encomendas.add(f)

        }
        ListModelList lml = new ListModelList(encomendas, true)
        lb_encomendas.setModel(lml)
    }

    @Listen("onClick = button#bt_filter_produto")
    public void openFilterProduto() {
        String url = "/produto/produtoFilter.gsp"
        encomendaService.setMyUrl("/encomenda/novaEncomenda.gsp")
        Executions.sendRedirect(url)

    }

    @Listen("onClick = button#bt_close")
    public void closeWin() {
        win.detach()
        Executions.sendRedirect("/home/index.gsp")
    }

    @Listen("onClick = menuitem#mi_remover")
    public void removerItems() {
        try {
            for (MyData myData in dados) {
                if (myData.ckb_select.checked) {
                    encomendaService.removerItem(myData.codigo)
                }
            }
        } catch (Exception e) {

        }
        renderListBox()
    }
   //
    @Listen("onClick = menuitem#mi_remover_todos")
    public void removerTodosItems() {
        try {
           encomendaService.items.clear()
        } catch (Exception e) {

        }
        renderListBox()
    }


    @Listen("onOK = textbox#tb_codigo_produto")
    public void addProduto() {
        if (Produto.findByCodigo(tb_codigo_produto?.value)) {
            encomendaService.addItemsbyCodigo(tb_codigo_produto.value)
            renderListBox()
        } else Messagebox.show("codigo invalido!", "Lua", 1, Messagebox.ERROR)
    }

    @Listen("onClick= textbox#tb_codigo_produto")
    public void removeValue() {
        tb_codigo_produto.value = ""
    }

    def save() {
        Encomenda encomenda = new Encomenda()
        encomenda.fornecedor = Fornecedor.findByNome(cb_fornecedores.selectedItem.value.toString())
        encomenda.estado = "Aberto"

        if (!tb_descricao.value.empty) {
            encomenda.descricao = tb_descricao.value
        }
        def itemsPro = encomendaService.getItems()
        for (MyData myData in dados) {
           Messagebox.show(myData.ib_qtd.value.toString())
            if (myData.ib_qtd.value.equals(null)) {
                Messagebox.show("O valor das quantidades e invalido!", "Lua", 1, Messagebox.ERROR)
                return
            }
        }
        for (int x = 0; x < itemsPro.size(); x++) {
            itemsPro[x].preco = dados[x].db_valor.value
            itemsPro[x].qtd = dados[x].ib_qtd.value
            itemsPro[x].produto.descricao = dados[x].tb_descricao.value
            encomenda.addToItemsProduto(itemsPro[x])
        }
        encomenda.save flush: true
        Messagebox.show("Encomenda No. " + encomenda.id + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)
        encomendaService.encomenda = encomenda
        encomendaService.notaDe = "Nota de Encomenda"
        session.setAttribute("encomenda", encomenda)
        Executions.sendRedirect("/encomenda/showEncomenda.gsp")

    }

    @Listen("onClick= button#bt_salvar")
    public void salvarEncomenda() {
        save()
    }

    @Listen("onClick= textbox#tb_descricao")
    public void clean() {
        tb_descricao.value = ""
    }

    def renderCb_fornecedores() {
        def fornecedores = Fornecedor.all
        List list_fornecedores = new ArrayList()
        for (Fornecedor f : fornecedores) {
            list_fornecedores.add(f.nome)
        }
        ListModelList lm_fornecedores = new ListModelList(list_fornecedores)
        cb_fornecedores.setModel(lm_fornecedores)
    }


    @Listen("onOK = combobox#cb_fornecedores")
    public void filtrarFornecedores() {
        //renderCb_produtos()
        cb_fornecedores.getChildren().clear()
        def searchResults = Fornecedor.withCriteria {
            like('nome', '%' + cb_fornecedores.value + '%')
            like('codigo', '%' + cb_fornecedores.value + '%')

        }

        ListModelList lm_fornecedores = new ListModelList(searchResults)
        cb_fornecedores.setModel(lm_fornecedores)

    }

    @Listen("onCancel = combobox#cb_fornecedores")
    public void repor() {
        renderCb_fornecedores()
    }


    class MyData {
        String codigo
        Textbox tb_descricao
        Intbox ib_qtd
        Doublebox db_valor
        Checkbox ckb_select

    }


}
