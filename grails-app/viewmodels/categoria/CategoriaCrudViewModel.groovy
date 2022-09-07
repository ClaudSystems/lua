package categoria

import grails.transaction.Transactional
import lua.ColorCodeGenerator
import lua.HibernateService
import lua.estoque.categoria.Categoria

import lua.estoque.produto.Produto
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.util.media.Media
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.event.UploadEvent
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Div
import org.zkoss.zul.Image
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Window

@Transactional
class CategoriaCrudViewModel {
    private ListModelList<Categoria> items
    private ListModelList<Produto> itemsProdutos
    private ListModelList<Produto> selectedCategoriaProdutoList = new ListModelList<Produto>()
    private Categoria selectedItem
    private Produto selectedProduto
    private Produto selectedCategoriaProduto
    private String filter
    private String filterProduto
    private String codigo
    private  String color
    private String codigoProduto
    def itemsToDb = new HashSet< Produto >()
    @Wire
    Div image
    @Wire Window win
    @Wire Label info
    String red = "color:red"
    String blue = "color:blue"
    boolean btsalvar = true



    Boolean getBtsalvar() {
        return btsalvar
    }



    void setBtsalvar(Boolean btsalvar) {
        this.btsalvar = btsalvar
    }

    @Command("upload")
    @NotifyChange(["selectedItem","image","info"])
    def uploadImage(@BindingParam("upEvent") UploadEvent event){
        image.children.clear()
        Media media = event.getMedia()
        if (media instanceof org.zkoss.image.Image) {
            if(media.width>128&&media.height>128){
              //  Messagebox.show(""+image+" a imagem deve ter no maximo px ", "Error", Messagebox.OK, Messagebox.ERROR)
                info.value = ""+image+" a imagem deve ter no maximo px "
                info.style="color:red"
                return
            }

            Image im = new Image()
            im.setContent((org.zkoss.image.Image) media)

            im.setParent(image)
            selectedItem.image = im

        } else {
           // Messagebox.show(""+image+" is not an image ", "Error", Messagebox.OK, Messagebox.ERROR)
            info.value = ""+image+" is not an image "
            info.style="color:red"
        }





    }

    Produto getSelectedCategoriaProduto() {
        return selectedCategoriaProduto
    }

    void setSelectedCategoriaProduto(Produto selectedCategoriaProduto) {
        this.selectedCategoriaProduto = selectedCategoriaProduto
    }

    String getFilterProduto() {
        return filterProduto
    }

    void setFilterProduto(String filterProduto) {
        this.filterProduto = filterProduto
    }

    String getCodigoProduto() {
        return codigoProduto
    }

    void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto
    }

    String getCodigo() {
        return codigo
    }

    void setCodigo(String codigo) {
        this.codigo = codigo
    }


    BigDecimal getPercentagem() {
        return percentagem
    }

    void setPercentagem(BigDecimal percentagem) {
        this.percentagem = percentagem
    }



    @Command
    @NotifyChange(["selectedCategoriaProdutoList","btsalvar","info"])
    ListModelList<Produto> getSelectedCategoriaProdutoList() {
        info.value = ""
        image.children.clear()
        if(!selectedItem.image.equals(null)){
            selectedItem.image.setParent(image)
        }
        btsalvar = false

       return  selectedCategoriaProdutoList

    }




    @NotifyChange(["selectedItem","btsalvar","info"])
    @Command
     void fecharEditor(){
        info.value = ""
        selectedItem = null
       btsalvar = true
        image.children.clear()
    }



    @NotifyChange(["selectedItem","btsalvar","info"])
    @Command
    public void addItem(){
        info.value=""
        Categoria i = new Categoria()
        selectedItem = i
        image.children.clear()
        btsalvar= true
    }


    @NotifyChange(["selectedItem","items","info"])
    @Command
    void salvarItem(){

        // create random object - reuse this as often as possible
       String colorCode = ColorCodeGenerator.colorCode
        System.out.println(colorCode)
        String color = 'background-color:'+colorCode
        System.println('color='+color)
        selectedItem.setColor(color)
        info.value=""
        while (selectedItem.codigo.equals(null)||selectedItem.nome.equals(null)){
          //  Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.value="Preecha os campos!"
            info.style="color:red"
            return
        }
        if (Categoria.findAllByCodigo(selectedItem.codigo)) {
           // Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
            info.value = "o valor do codigo ja existe!"
            info.style = "color:red"
            return
        }


        selectedItem.save( flush: true)

        def savedItem = Categoria.findByCodigo(selectedItem.codigo)
        if(savedItem.equals(null)){
           // Messagebox.show("Categoria "+selectedItem.nome+" teve Erro na gravação!", "Lua", 1,  Messagebox.ERROR)
            info.value = "Categoria "+selectedItem.nome+" teve Erro na gravação!"
            info.style= "color:red"

        }else
        {
          //  Messagebox.show("Categoria "+savedItem?.nome+" foi criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
            info.value = "Categoria "+savedItem?.nome+" foi criado com sucesso!"
            items.add(selectedItem)
        }
        Executions.sendRedirect("/categoria/categoriaCrud")
    }
    @NotifyChange(["selectedItem","items"])
    @Command
   void updateItem(){

        Categoria c = Categoria.findById(selectedItem.id)
        String colorCode = ColorCodeGenerator.colorCode
        System.out.println(colorCode)
        String color = 'background-color:'+colorCode
        System.println('color='+color)
        selectedItem.setColor(color)

          try {

           c.codigo = selectedItem.codigo
            c.nome = selectedItem.nome
            c.image = selectedItem.image
              c.color = selectedItem.color
            c.save()
            c.setProdutos(selectedItem.produtos)

              for(Produto p:selectedItem.produtos){
                  p.setCategoria(c)
                  p.merge()

              }

              c.merge(flush: true)

           // Messagebox.show("Os dados da Categoria "+c.nome+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
              info.value = "Os dados da Categoria "+c.nome+" foram actualizados com sucesso!"
              info.style = blue

        }catch (Exception e){
          //  Messagebox.show("Erro na atualização dos  dados da categoria "+e.toString()+"!", "Lua", 1,  Messagebox.ERROR)
              info.value="Erro na atualização dos  dados da categoria "+e.toString()+"!"
              info.style=red

        }


    }

    Produto getSelectedProduto() {
        return selectedProduto
    }

    void setSelectedProduto(Produto selectedProduto) {
        this.selectedProduto = selectedProduto
    }

    public String getFilter() {
        return filter
    }

    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }

    public Categoria getSelectedItem(){

        return  selectedItem
    }
    public void setSelectedItem (Categoria item){
        this.selectedItem = item
    }


    public ListModelList<Categoria> getItems() {
        if (items == null) {
            items = new ListModelList<Categoria>(Categoria.all)
        }
        return items
    }

    public ListModelList<Produto> getItemsProdutos() {
        if (itemsProdutos == null) {
            itemsProdutos = new ListModelList<Produto>(Produto.all)
        }
        return itemsProdutos
    }

    @NotifyChange(["items","info"])
    @Command
    public void doSearch() {
        info.value=""
        items.clear()
        ListModelList<Categoria> allItems = Categoria.all
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (Categoria c : allItems) {
                if (c.codigo.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.nome.toLowerCase().indexOf(filter.toLowerCase()) >= 0
                ) {
                    items.add(c)
                }
            }
        }
    }

    @NotifyChange(["itemsProdutos","info"])
    @Command
    public void doSearchProduto() {
        info.value=""
        itemsProdutos.clear()
        ListModelList<Produto> allItems = Produto.all
        if (filterProduto == null || "".equals(filterProduto)) {
            itemsProdutos.addAll(allItems)
        } else {
            for (Produto c : allItems) {
                if (c.codigo.toLowerCase().indexOf(filterProduto.toLowerCase()) >= 0 ||
                        c.nome.toLowerCase().indexOf(filterProduto.toLowerCase()) >= 0||
                        c.descricao.toLowerCase().indexOf(filterProduto.toLowerCase()) >= 0
                ) {
                    itemsProdutos.add(c)
                }
            }
        }
    }


    @Command
    @NotifyChange(["selectedItem","items","info"])
    public void deleteItem (){
        info.value=""
        try {
          //  hibernateService.cleanSession()
            Categoria categoriaDB = Categoria.findById(selectedItem.id)
            while (selectedItem.codigo.equals(null)||selectedItem.nome.equals(null)||selectedItem.equals(null)){
               // Messagebox.show("Selecione a Categoria que desja eliminar!", "Lua", 1, Messagebox.ERROR)
                info.value="Selecione a Categoria que desja eliminar!"
                info.style=red
                return
            }
           /* Messagebox.show("Tem certeza que deseja eliminar este Categoria?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {

                    }
                }
            }
            )*/
            categoriaDB.delete(flush: true)
            info.value = 'Categoria '+ categoriaDB.nome+' eliminada com sucesso!'
        }catch (Exception e ){
           System.println(e.toString())
            info.println(e.toString())
            info.value =  'Erro! '+e.toString()
        }


    }

    @Command
    @NotifyChange(["info"])
    def showHelp(){
        info.value="Para eliminar o Item faça double Click!"
    }


    @Init init() {

    }



    @Command
    @NotifyChange(["selectedItem","newItemList","btsalvar"])
    void viewItems(@BindingParam("id") Integer id) {

        selectedItem = Categoria.findById(id)
        image.children.clear()
        if(!selectedItem.image.equals(null)){
            selectedItem.image.setParent(image)
        }

        getNewItemList()
        btsalvar = false
        image.children.clear()

    }

    @Command
    @NotifyChange(["selectedItem","selectedCategoriaProdutoList","info"])
    void addSelectedProduto(@BindingParam("index") Integer index) {
        info.value=""
            if(selectedItem.produtos.equals(null)){
                info.value="Selecione uma categoria!"
                info.style= blue
               // Messagebox.show("Selecione uma categoria!", "Lua", 1,  Messagebox.INFORMATION)
            }
            selectedItem?.produtos?.add(itemsProdutos[index])



    }

    @Command
    @NotifyChange(["selectedItem","selectedCategoriaProduto","info"])
    void removeSelectedProduto(@BindingParam("index") Integer index) {

        info.value=""
        selectedItem.produtos.remove(selectedCategoriaProduto)
        def prod = Produto.find(selectedCategoriaProduto)
        prod.categoria = null
        prod.save()
        getSelectedCategoriaProdutoList()
    }


    @Command
    @NotifyChange(["selectedItem","itemList","btsalvar","info"])
    def viewItemByCodigo(){
        info.value=""
        def item = Categoria.findByCodigo(codigo)
        if(item.equals(null)){
            return
        }
        selectedItem = item
        image.children.clear()
        if(!selectedItem.image.equals(null)){
            selectedItem.image.setParent(image)
        }

        btsalvar = false
    }


    @Command
    @NotifyChange(["items","itemList","btsalvar","info"])
    def addItemByCodigoProduto(){
        info.value=""
        def item = Produto.findByCodigo(codigo)
        if(item.equals(null)){
            return
        }else if(selectedItem){
            selectedItem.addToProdutos(item)

        }
        btsalvar = false
    }

    @Command
    @NotifyChange(["selectedItem","info"])
    def updateCategoria(){
        info.value=""
        updateItem()
    }

    @Command
    @NotifyChange(["selectedCategoriaProdutoList","btsalvar","info"])
    def showCategoryItems() {
        info.value=""
        getSelectedCategoriaProdutoList()
    }

    @Command
    @NotifyChange(["selectedCategoriaProdutoList","selectedItem","info"])
    def removeItem(){
        info.value=""
        selectedItem.produtos.remove(selectedCategoriaProduto)
        Produto p = Produto.find(selectedCategoriaProduto)
        p.categoria=null
        p.save(flush: true)


    }
}
