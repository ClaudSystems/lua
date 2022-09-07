package encomenda

import estoque.EncomendaService
import lua.estoque.produto.Produto
import org.zkoss.bind.annotation.Command
import org.zkoss.zhtml.Button
import org.zkoss.zk.ui.Executions

import org.zkoss.zk.ui.select.annotation.Listen

import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listitem
import org.zkoss.zul.ListitemRenderer
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Textbox
import org.zkoss.zul.Window


class ProdutoFilterComposer extends zk.grails.Composer {

    EncomendaService encomendaService
    Window win
    Button bt_close = new Button()
    public  Listbox lb_produtos
    Textbox tb_filter

    public String setectedProduto(){
        return lb_produtos.selectedItem.value
    }

    def afterCompose = { window ->
        renderLBProdutos()
    }

    def renderLBProdutos () {
        if (!tb_filter.value.empty){

            updatedList()
        }else
            encomendasList()

        lb_produtos.getItems() .clear()
        lb_produtos.setItemRenderer(new ListitemRenderer<Produto>() {

            @Override
            void render(Listitem item, Produto produto, int i) throws Exception {

                new Listcell(produto.codigo).setParent(item)
                new Listcell(produto.descricao).setParent(item)
                item.setValue(produto)


            }
        })
    }
    def encomendasList(){
        def produtos = new ArrayList<Produto>()
        def produtosDB = Produto.all

        for(Produto p in produtosDB) {

            produtos.add(p)

        }
        ListModelList lml = new ListModelList(produtos,true)
        lb_produtos.setModel(lml)
    }

    def updatedList(){
        def produtos = new ArrayList<Produto>()
        def produtosDB = Produto.withCriteria{

            like('descricao', '%'+tb_filter.value+'%')
            like('codigo', '%'+tb_filter.value+'%')
        }

        for(Produto p in produtosDB) {

            produtos.add(p)



        }
        ListModelList lml = new ListModelList(produtos,true)
        lb_produtos.setModel(lml)
    }


    @Listen("onOK = textbox#tb_filter")
    public void doSearch() {
        lb_produtos.children.clear()
        renderLBProdutos()
    }
    @Listen("onClick = listbox#lb_produtos")
    public void addProduto() {

       encomendaService.addItems(lb_produtos.selectedItem.value.toString())
       win.detach()
        Executions.sendRedirect("/encomenda/novaEncomenda.")


    }


    @Listen("onClick = button#bt_close")
    @Command('close')
    public void closeWin() {
        Messagebox.show("xxx")
         win.detach()
        Executions.sendRedirect("/encomenda/novaEncomenda.gsp")

        // session.setAttribute("entradadeproduto",url)
       // Executions.sendRedirect(url)


    }


    @Command
    public  void close(){
        Messagebox.show("dfsdf")
        Executions.sendRedirect("/cliente/index.gsp")

    }

}
