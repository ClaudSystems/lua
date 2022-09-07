package pos

import grails.transaction.Transactional
import lua.entidades.fornecedor.Fornecedor
import lua.estoque.categoria.Categoria
import lua.estoque.produto.Produto
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zul.Div
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listitem
import org.zkoss.zul.ListitemRenderer
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Window

import org.zkoss.zk.ui.select.annotation.Wire

@Transactional
class PosCrudViewModel {


    @Wire  btnClose
    @Wire  Window win
    ListModelList categorias
       Produto selectedProduto = new Produto()
    Categoria selectedCategoria = new Categoria()
    @Wire Div imageCategoria
    @Wire Div imageProduto
    @Wire Listbox lb_categorias
    Categoria getSelectedCategoria() {
        return selectedCategoria
    }

    void setSelectedCategoria(Categoria selectedCategoria) {
        this.selectedCategoria = selectedCategoria
    }

    Produto getSelectedProduto() {
            return selectedProduto
    }

    void setSelectedProduto(Produto selectedProduto) {
        this.selectedProduto = selectedProduto
    }




    ListModelList getCategorias() {
        if(categorias==null ){
            categorias = Categoria.all
        }
        return categorias
    }

    @Init init() {

    }

    @Command
    closeWin (){
        win.detach()
        Executions.sendRedirect("/home/index")
    }


    @Command
    @NotifyChange (["selectedCategoria"])
     def  getAllProdutos (){
        selectedCategoria = new Categoria(codigo: "all",nome: "all")

        def all = Produto.findAll()
        selectedCategoria.produtos= all

    }

    def renderLBCategorias () {
        categoriaList()

        lb_categorias.getItems().clear()
        lb_categorias.setItemRenderer(new ListitemRenderer<Categoria>() {

            @Override
            void render(Listitem item, Categoria categoria, int i) throws Exception {
                new Listcell(categoria.codigo).setParent(item)
                new Listcell(categoria.nome).setParent(item)


                item.setValue(categoria)
            }
        })
    }

    def categoriaList(){
        def categorias = new ArrayList<Categoria>()
        def categoriasDB = Categoria.all

        for(Categoria c in categoriasDB) {

            categorias.add(c)

        }
        ListModelList lml = new ListModelList(lotes,true)
        lb_categorias.setModel(lml)
    }



}
