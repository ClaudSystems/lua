package produto

import lua.estoque.produto.Produto
import org.zkoss.zhtml.Button
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.Div
import org.zkoss.zul.Grid
import org.zkoss.zul.Intbox
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listitem
import org.zkoss.zul.ListitemRenderer
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Textbox


class IndexComposer extends zk.grails.Composer {

   /* String codigo
    String nome
    String descricao
    int saldo*/

    Textbox tb_codigo
    Textbox tb_nome
    Textbox tb_descricao
    Button bt_salvar
    Button bt_update
    Button bt_add_unidade
    Button bt_clien
    Button bt_delite = new Button()


    Grid gd_produto
    Div dv_delite
    Div dv_salvar
    Div dv_filter
    static Produto selectedProduto
    Label lb_info
    Listbox lb_produtos
    ArrayList<Produto> dados = new ArrayList()


    def afterCompose = { window ->
        renderLBProdutos()
        bt_delite.setVisible(false)
        dv_delite.setVisible(false)
        dv_salvar.setVisible(true)



    }

    def CreateForm (Produto produto){
    }
    @Listen("onClick = button#bt_salvar")
    public void salvar() {

        try {
            Produto produto = new Produto()
            produto.codigo = tb_codigo?.value
            produto.nome = tb_nome?.value
            produto.descricao = tb_descricao?.value

            if (Produto.findAllByCodigo(tb_codigo.value)) {
                Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
                return
            }
            produto.save flush: true
            renderLBProdutos()
            Messagebox.show(produto.nome + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)
            tb_codigo.focus()

        }


        catch (Exception ex) {
            Messagebox.show("Erro! "+ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }


    def renderLBProdutos () {
        clientesList()
        lb_produtos.getItems().clear()
        lb_produtos.setItemRenderer(new ListitemRenderer<Produto>() {

            @Override
            void render(Listitem item, Produto produto, int i) throws Exception {
                new Listcell(produto.codigo).setParent(item)
                new Listcell(produto.nome).setParent(item)
                new Listcell(produto.descricao).setParent(item)
                new Listcell(produto.saldo.toString()).setParent(item)
                item.setValue(produto)


            }
        })
    }

    def clientesList(){
        def produtos = new ArrayList<Produto>()
        def produtosDB = Produto.all

        for(Produto c in produtosDB) {

            produtos.add(c)

        }
        ListModelList lml = new ListModelList(produtos,true)
        lb_produtos.setModel(lml)
    }


    @Listen("onClick = button#bt_add_produto")
    public  void openGrid (){
        dv_salvar.setVisible(true)
        dv_delite.setVisible(false)

        if(gd_produto.visible){
            gd_produto.setVisible(false)
        }
        else  {


            gd_produto.setVisible(true)

        }

    }


    @Listen("onDoubleClick = listbox#lb_produtos; onOK = listbox#lb_produtos")
    public  void editarUnidade (){
        gd_produto.focus()
        dv_delite.setVisible(true)
        dv_salvar.setVisible(false)

        def str = lb_produtos.getSelectedItem().getValue().toString()
        System.println(str[0])
        def produto = Produto.findById(str[0])
        selectedProduto = produto
        tb_codigo.setValue(produto.codigo)
        tb_nome.value=produto.nome
        tb_descricao.value=produto.descricao


    }

    @Listen("onClick = button#bt_update")
    public void update(){
        try {
            Produto produto = Produto.find(selectedProduto)
            if(!produto.equals(null)){
                if (!tb_codigo.value.equals(produto.codigo)) {
                    if(Produto.findAllByCodigo(tb_codigo.value)){
                        Messagebox.show("O valor de codigo deve ser unico!", "Lua", 1,  Messagebox.ERROR)
                        return
                    }
                }

                produto.codigo =tb_codigo?.value
                produto.nome = tb_nome?.value
                produto.descricao = tb_descricao?.value


                produto.save()
                Messagebox.show(produto.nome+" Actualizado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                renderLBProdutos()
            }
        } catch ( Exception ex) {
            Messagebox.show("Erro! "+ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }
    @Listen("onClick = button#bt_delite")
    public  void delite(){
        Produto produto = Produto.find(selectedProduto)
        produto.delete flush: true
    renderLBProdutos()
        Messagebox.show(produto.nome+" Eliminado!", "Lua", 1,  Messagebox.INFORMATION)

    }


    public  void cleanFilds(){
        tb_codigo.value=null
        tb_nome.value=null
        tb_descricao.value=null
    }
}
