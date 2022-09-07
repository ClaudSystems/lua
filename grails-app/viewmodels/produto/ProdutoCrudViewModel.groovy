package produto

import grails.transaction.Transactional
import lua.estoque.iva.IvaService
import org.zkoss.bind.BindContext
import org.zkoss.zk.ui.event.UploadEvent
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.iva.Iva
import lua.estoque.produto.Produto
import lua.estoque.anexo.Anexo
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Button

import org.zkoss.zul.Div
import org.zkoss.zul.Image
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.image.AImage
import org.zkoss.bind.annotation.Command


@Transactional
class ProdutoCrudViewModel {

    IvaService ivaService
    private ListModelList<Produto> items
    private Produto selectedItem
    private String filter
    private String codigo
    @Wire Button bt_update
    @Wire Button bt_eliminar
    BigDecimal percentagem = 0.0
    @Wire
    Div image
    Anexo anexo
    @Wire Label info
    @Wire Button bt_iva
    @Wire Button bt_iva_aplicado
    @Wire Button bt_pacote
    @Wire Button bt_estocavel
    String blue ="color:blue"
    String red = "color:red"
    private boolean ivaIncluido = true
    private boolean ivaAplicado = true
    private boolean pacote = false
    private boolean estocavel = true
    private AImage myImage
    private Div image
    private Iva iva = new Iva()

    Iva getIva() {
        iva = Iva.findByValido(true)
        return iva
    }

    boolean getEstocavel() {
        return estocavel
    }

    void setEstocavel(boolean estocavel) {
        this.estocavel = estocavel
    }

    boolean getPacote() {
        return pacote
    }

    void setPacote(boolean pacote) {
        this.pacote = pacote
    }

    boolean getIvaAplicado() {

        return ivaAplicado
    }

    void setIvaAplicado(boolean ivaAplicado) {
        this.ivaAplicado = ivaAplicado
    }

    void setMyImage(AImage myImage) {
        this.myImage = myImage
    }

    AImage getMyImage() {
        return myImage
    }

    boolean getIvaIncluido() {
        return ivaIncluido
    }

    void setIvaIncluido(boolean ivaIncluido) {
        this.ivaIncluido = ivaIncluido
    }

    @Command
    @NotifyChange(["estocavel"])
    def estocavel(){
        if(estocavel){
            estocavel = false
            bt_estocavel.style = "background: #FF5050;color:#FFFFFF"
        }else {
            estocavel = true
            bt_estocavel.style = "background:#759167;color:#FFFFFF"
        }
    }

    @Command
    @NotifyChange(["ivaAplicado"])
    def aplicarIva(){
        if(ivaAplicado){
            ivaAplicado = false
            bt_iva_aplicado.style = "background: #FF5050;color:#FFFFFF"
        }else {
           ivaAplicado = true
            bt_iva_aplicado.style = "background:#759167;color:#FFFFFF"
        }
    }

    @Command
    @NotifyChange(["ivaIncluido"])
    def incluirIva(){
        if(ivaIncluido){
            ivaIncluido = false
            bt_iva.style = "background: #FF5050;color:#FFFFFF"
        }else {
            ivaIncluido = true
            bt_iva.style = "background:#759167;color:#FFFFFF"
        }
    }

    @Command
    @NotifyChange(["pacote"])
    def aplicarPacote(){
        if(pacote){
            pacote = false
            bt_pacote.style = "background: #FF5050;color:#FFFFFF"
            bt_pacote.style
        }else {
            pacote = true
            bt_pacote.style = "background:#759167;color:#FFFFFF"
        }
    }

    @Command
    @NotifyChange("myImage")
    def upload(BindContext ctx) {
        UploadEvent event = (UploadEvent)ctx.getTriggerEvent()
        System.out.println("uploading")
    }
  /*  @Command
    public void doDownload() {
        if (media != null)
            Filedownload.save(media)
        else
            Messagebox.show("First Drop Your File")

    }*/


    String getCodigo() {
        return codigo
    }

    void setCodigo(String codigo) {
        this.codigo = codigo
    }
    List<ItemProduto> itemList = new ArrayList<ItemProduto>()

    BigDecimal getPercentagem() {
        return percentagem
    }

    void setPercentagem(BigDecimal percentagem) {
        this.percentagem = percentagem
    }

    ListModelList<ItemProduto> getItemList() {
        List<ItemProduto> itemLists = new ArrayList<ItemProduto>()
        for (ItemProduto ip in selectedItem?.itemsProduto){
            itemLists.add(ip)
        }
        return itemLists
    }


    private Boolean btsalvar =false

    public  Boolean getBtsalvar() {
        return btsalvar
    }

    @NotifyChange("selectedItem")
    @Command
    public void fecharEditor(){
        selectedItem = null
    }



    @NotifyChange(["selectedItem","btsalvar"])
    @Command
    public void addItem(){
        Produto i = new Produto()
        selectedItem = i
        btsalvar= true
    }


    @NotifyChange(["selectedItem","items","info"])
    @Command
    public  void salvarItem(){
        try {
            while (selectedItem.codigo.equals(null)||selectedItem.nome.equals(null)||selectedItem.descricao.equals(null)){
                //  Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
                info.value = "Preecha os campos!"
                info.style=red
                return
            }
            if (Produto.findAllByCodigo(selectedItem.codigo)) {
                // Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
                info.value ="o valor do codigo ja existe!"
                return
            }
            if (Produto.findAllByNome(selectedItem.nome)) {
                // Messagebox.show("Já existe um produto com o nome !"+selectedItem.nome, "Lua", 1, Messagebox.ERROR)
                info.value = "Já existe um produto com o nome !"
                info.style = red
                return
            }
            if(ivaIncluido){
                while (getIva().equals(null)){
                    info.value = "Indruza i IVA no Systema !"
                    info.style = blue
                    return
                }
                def valor = selectedItem.precoDeVenda
                def dividendo = iva.percentualIva/100+1
                selectedItem.precoDeVenda = valor/dividendo
                System.println("salvarItem ivaIncluido valor="+selectedItem.precoDeVenda)
            }
            selectedItem.ivaAplicado=ivaAplicado
            if(selectedItem.ivaAplicado){
                selectedItem.valorDoIva = selectedItem.precoDeVenda*ivaService.iva.percentualIva/100
            }else {
                selectedItem.valorDoIva = 0.0
            }
            selectedItem.pacote = pacote
            selectedItem.estocavel = estocavel
            selectedItem.iva = ivaService.iva

            selectedItem.save( flush: true)
            items.add(selectedItem)

            // Messagebox.show("Produto "+selectedItem.nome+" foi criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
            info.value ="Produto "+selectedItem.nome+" foi criado com sucesso!"
            info.style = blue
            bt_update.label= "Atualizado com sucesso!"
            bt_update.style="background:#4294FF"
            selectedItem= new Produto()
        }catch (Exception e){
            info.value = e.toString()

        }


    }
    @NotifyChange(["selectedItem","items","info"])
    @Command
    public  void updateItem(){
        Produto c = Produto.findById(selectedItem.id)
        try {

            c.codigo = selectedItem.codigo
            c.nome = selectedItem.nome
            c.descricao = selectedItem.descricao
            c.pacote = pacote
            c.ivaAplicado = ivaAplicado
            c.estocavel = estocavel

            if(ivaIncluido){
                def valor = selectedItem.precoDeVenda
                def dividendo = iva.percentualIva/100+1
                System.println("updateItem dividendo="+dividendo)
                c.precoDeVenda = valor/dividendo
                System.println("updateItem c.precoDeVenda="+c.precoDeVenda)
            }else {
                c.precoDeVenda = selectedItem.precoDeVenda
            }

            c.image = selectedItem.image



            c.save(flush: true)

           // selectedItem = null
                 //     Messagebox.show("Os dados do Produto "+c.nome+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                info.value = "Os dados do Produto "+c.nome+" foram actualizados com sucesso!"
            info.style= blue
            bt_update.label="Atualizado!"
            bt_update.style="color:black;background:#A6D3E2"



        }catch (Exception e){
          //  Messagebox.show("Erro na atualização dos  dados do Produto "+c.nome+"!"+e.toString(), "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na atualização dos  dados do Produto "+c.nome+"!"+e.toString()
            info.style = red
        }


    }




    public String getFilter() {
        return filter
    }

    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }

    public Produto getSelectedItem(){
        return  selectedItem
    }
    public void setSelectedItem (Produto item){
        this.selectedItem = item
    }


    public ListModelList<Produto> getItems() {
        if (items == null) {
            items = new ListModelList<Produto>(Produto.all)
        }
        return items
    }

    @NotifyChange(["items","info"])
    @Command
    public void doSearch() {
        info.value = ""
        items.clear()
        List<Produto> allItems = Produto.all
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (Produto c : allItems) {
                if (c.codigo.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.nome.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.descricao?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0
                ) {
                    items.add(c)
                }
            }
        }
    }


    @Command
    @NotifyChange(["selectedItem","items","info"])
    public void deleteItem (){
        info.value=""
        try {

            while (selectedItem.codigo.equals(null)||selectedItem.nome.equals(null)||selectedItem.equals(null)){
              //  Messagebox.show("Selecione O Produto que desja eliminar!", "Lua", 1, Messagebox.ERROR)
                info.value = "Selecione O Produto que desja eliminar!"
                info.style= red
                return
            }
           /* Messagebox.show("Tem certeza que deseja eliminar este Produto?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        selectedItem.delete()
                        items.remove(selectedItem)
                        selectedItem = null
                    }
                }
            }
            )*/
            selectedItem.delete()
            items.remove(selectedItem)
           // selectedItem = null
            info.value = "O produto foi eliminado !"
            bt_eliminar.label="O produto foi eliminado !"


        }catch (Exception e ){
            //Messagebox.show("Selecione Um Produto!")
            info.value="Selecione Um Produto!"+e.toString()

        }


    }



    @Init init() {
        // initialzation code here
        getIva()

    }

    @Command
    def showAlert(){
        bt_eliminar.label ="Faça double click para eliminar!"

    }

    @Command
    @NotifyChange(["selectedItem","itemList","btsalvar","info","ivaAplicado",'ivaIncluido'])
    void viewItems(@BindingParam("id") Integer id) {
        bt_update.label="Atualizar"
        bt_eliminar.label = "Eliminar"
        bt_eliminar.style="color:white;background:#800000"
        bt_update.style="background:#0000A0;color:white"
        info.value=""
        selectedItem = Produto.findById(id)
        if(selectedItem.valorDoIva>0){
            ivaIncluido = true
        }else {
            ivaIncluido = false
        }
        ivaAplicado = selectedItem.ivaAplicado
      image?.children?.clear()
        if(selectedItem.image){
            selectedItem.image.setParent(image)
        }


        btsalvar = false

    }



    @Command
    @NotifyChange(["selectedItem","itemList","btsalvar","info"])
    def viewItemByCodigo(){
        info.value=""
        def item = Produto.findByCodigo(codigo)
        if(item.equals(null)){
            return
        }
        selectedItem = item
        image.children.clear()
        if(selectedItem.image){
            selectedItem.image.setParent(image)
        }

        btsalvar = false
    }

    @Command
    @NotifyChange(['selectedItem'])
    def addNomeToDescricao(){
        selectedItem.descricao = selectedItem.nome
    }
}
