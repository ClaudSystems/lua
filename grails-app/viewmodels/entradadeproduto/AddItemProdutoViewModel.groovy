package entradadeproduto

import base.ComposersService
import base.EncryptionService
import grails.transaction.Transactional
import lua.entidades.fornecedor.Fornecedor
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.estoque.Armazem
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.produto.Produto
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox

@Transactional
class AddItemProdutoViewModel {
    EncryptionService encryptionService
    ComposersService composersService
    boolean viewFornecedor = false
    protected  EntradaDeProduto entradaDeProduto = new EntradaDeProduto()
    private ListModelList<ItemProduto> items
    private String filter
    private List<Fornecedor> fornecedores
    private Fornecedor fornecedor = new Fornecedor()
    private Produto produto
    private ListModelList<Produto> produtos

    boolean getViewFornecedor() {
        return viewFornecedor
    }


    @Command
    @NotifyChange(["viewFornecedor","showFornecedor"])
    def showFornecedor(){
        if (viewFornecedor){
            viewFornecedor= false

        }
        else viewFornecedor = true
    }
    @Init
    init() {

        entradaDeProduto.utilizador = encryptionService.localUser
        entradaDeProduto.fornecedor= fornecedor

        // subTotal=0
    }

    @Command
    @NotifyChange("items")
    def addSelectedItem(@BindingParam("index") Long index){
        System.println("addSelectedItem: "+index)
        ItemProduto itemProduto = new ItemProduto()
        Produto p = Produto.findById(index)
        itemProduto.setProduto(p)
        itemProduto.descricao = p.descricao
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))
        itemProduto.setPreco(p.itemsProduto.last().preco)
        items.add(itemProduto)
        entradaDeProduto.addToItemsProduto(itemProduto)
    }

    @Command
    @NotifyChange("items")
    def addItem(){
        ItemProduto itemProduto = new ItemProduto()
        Produto produto = new Produto()
        produto.codigo=""
        itemProduto.setProduto(produto)
        itemProduto.setArmazem(Armazem.findByCodigo("A1"))

        items.add(itemProduto)
        entradaDeProduto.addToItemsProduto(itemProduto)
    }

    @Command
    @NotifyChange("items")
    public void removeItem(@BindingParam("index") Integer index) {
        System.println(index)
        items.remove(index)
    }

    @Command
    @NotifyChange (["items","produtos"])
    def createEntradas(){


        try {
            System.println(fornecedor.nome)
            entradaDeProduto.fornecedor = fornecedor
            Fornecedor f = Fornecedor.findByCodigo(fornecedor.codigo)
            if(f==null){
                fornecedor.save flush: true
            }

           for (ItemProduto ip in items){
               ip.save()
               Produto p = Produto.findByCodigo(ip.produto.codigo)
               p.saldo+= ip.qtd
               p.save()
              entradaDeProduto.valor+=ip.preco*ip.qtd
           }


            entradaDeProduto.save flush: true
            EntradaDeProduto ep = EntradaDeProduto.find(entradaDeProduto)
            composersService.entradaDeProduto = ep

            Messagebox.show("Os produtos  No.  foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        } catch (Exception e ){
            return Messagebox.show("ERRO!"+e.toString(), "Lua", 1, Messagebox.ERROR)
        }





        //Executions.sendRedirect("/entra/list")
    }




    ListModelList<ItemProduto> getItems() {
        if (items == null) {
           items= new ListModelList<ItemProduto>()
        }
        return items
    }

    void setItems(ListModelList<ItemProduto> items) {
        this.items = items
    }

    Produto getProduto() {
        return produto
    }

    void setProduto(Produto produto) {
        this.produto = produto
    }

    String getFilter() {
        return filter
    }

    void setFilter(String filter) {
        this.filter = filter
    }

    ListModelList<Produto> getProdutos() {
        if (produtos == null) {
            produtos = new ListModelList<Produto>(Produto.all)
        }
        return produtos
    }

    void setProdutos(ListModelList<Produto> produtos) {
        this.produtos = produtos
    }

    List<Fornecedor> getFornecedores() {
        if (fornecedores == null) {
            fornecedores = new ArrayList<Fornecedor>(Fornecedor.all)
        }
        return fornecedores
    }

    Fornecedor getFornecedor() {
        if(fornecedor==null){
            Fornecedor f = new Fornecedor()
            return  f
        }
        return fornecedor
    }

    void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor
    }

    @NotifyChange("produtos")
    @Command
    public void doSearch() {

        produtos.clear();
        List<Produto> allItems = Produto.all
        if (filter == null || "".equals(filter)) {
            produtos.addAll(allItems);
        } else {
            for (Produto p : allItems) {
                if (p.getDescricao().toLowerCase().indexOf(filter.toLowerCase()) >= 0 || p.getCodigo().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        p.getNome().toLowerCase().indexOf(filter.toLowerCase()) >= 0) {
                    produtos.add(p);
                }
            }
        }
    }
}
