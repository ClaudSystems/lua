package mesa

import grails.transaction.Transactional
import lua.estoque.anexo.Anexo
import lua.restaurante.mesa.Localizacao
import lua.restaurante.mesa.Mesa
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.util.media.Media

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.event.UploadEvent
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Button
import org.zkoss.zul.Div
import org.zkoss.zul.Image
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox

@Transactional
class MesaCrudViewModel {

    private ListModelList<Mesa> items
    private Mesa selectedItem
    private Mesa deletedItem
    private Localizacao selectedLocal
    private String filter
    private String newLocal
    @Wire  Label info
    String blue = "color:blue"
    String red = "color:blue"
    private boolean btn_undelete
    private String numeroDaMesa
    private ListModelList<Localizacao> locais
    @Wire
    Div image = new Div()

    boolean getBtn_undelete() {
        return btn_undelete
    }

    String getNewLocal() {
        return newLocal
    }

    void setNewLocal(String newLocal) {
        this.newLocal = newLocal
    }

    @Command
    @NotifyChange(["locais","info"])
    def salvarNewLocal(){
        info.value = ""
        if(Localizacao.findByDescricaoDoLocal(newLocal)){
            info.value = "Este local já existe!"
            info.style = blue
            return
        }
        if (newLocal.empty){
            info.style = blue
            info.value = "Por favor preencha  o campo Novo Local!"
            return
        }
        Localizacao localizacao = new Localizacao()
        localizacao.descricaoDoLocal = newLocal
        localizacao.save(flush: true)
        if(Localizacao.findByDescricaoDoLocal(newLocal)){
            info.value = "O local " +newLocal+" foi salvo com sucesso!"
            locais.add(Localizacao.findByDescricaoDoLocal(newLocal))
        }

    }
    Localizacao getSelectedLocal() {
        return selectedLocal
    }

    void setSelectedLocal(Localizacao selectedLocal) {
        this.selectedLocal = selectedLocal
    }

    ListModelList<Localizacao> getLocais() {
        if(locais == null){
            locais = Localizacao.all
        }
        return locais
    }

    @Command("upload")
    @NotifyChange(["selectedItem"])
    def uploadImage(@BindingParam("upEvent") UploadEvent event){
        image.children.clear()
        Media media = event.getMedia()
        if (media instanceof org.zkoss.image.Image) {
            if(media.width>260&&media.height>260){
              //  Messagebox.show(""+image+" a iamgem deve ter no maximo 258px ", "Error", Messagebox.OK, Messagebox.ERROR)
                info.value = ""+image+" a iamgem deve ter no maximo 258px "
                return
            }

            Image im = new Image()
            im.setContent((org.zkoss.image.Image) media)
            im.setParent(image)
            selectedItem.image = im

        } else {
          //  Messagebox.show(""+image+" is not an image ", "Error", Messagebox.OK, Messagebox.ERROR)
            info.style = red
            info.value = ""+image+" is not an image "
        }
    }





    String getNumeroDaMesa() {
        return numeroDaMesa
    }

    void setNumeroDaMesa(String numeroDaMesa) {
        this.numeroDaMesa = numeroDaMesa
    }


    BigDecimal getPercentagem() {
        return percentagem
    }

    void setPercentagem(BigDecimal percentagem) {
        this.percentagem = percentagem
    }



    private Boolean btsalvar =false

    public  Boolean getBtsalvar() {
        return btsalvar
    }

    @NotifyChange(["selectedItem","selectedLocal","items"])
    @Command
    public void fecharEditor(){
        selectedItem = null
        selectedLocal = null
        info.value = ""
        btn_undelete= false

    }



    @NotifyChange(["selectedItem","btsalvar"])
    @Command
    public void addItem(){
        Mesa i = new Mesa()
        selectedItem = i
        btsalvar= true
    }


    @NotifyChange(["selectedItem","items","info","selectedLocal"])
    @Command
    public  void salvarItem(){

        selectedItem.localizacao=selectedLocal
        while (selectedItem.numeroDaMesa.equals(null)||selectedItem.localizacao.equals(null)){
         //   Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.value = "Preecha os campos!"
            info.style = red
            return
        }
        if (Mesa.findByNumeroDaMesa(selectedItem.numeroDaMesa)) {
            info.value = "o valor do numeroDaMesa ja existe!"
            info.style = blue
            return
        }
        selectedItem.estado = "fechada"
        selectedItem.ativo=false
        try {
            selectedItem.save( flush: true)
        }catch (Exception e){
            System.println(e.toString())
            info.value = e.toString()
        }


        if(Mesa.findByNumeroDaMesa(selectedItem.numeroDaMesa)){
            items.add(selectedItem)
            info.value = "Mesa "+selectedItem.numeroDaMesa+" foi criado com sucesso!"
        }else {
            info.style="color:red"
            info.value = "Erro na gravação!"

            return
        }
       // Messagebox.show(, "Lua", 1,  Messagebox.INFORMATION)
        selectedItem= new Mesa()


    }
    @NotifyChange(["selectedItem","items","info","selectedLocal"])
    @Command
    public  void updateItem(){
        System.println(selectedItem.numeroDaMesa)
       // Mesa c = Mesa.findById(selectedItem.id)

        try {
            selectedItem.save(flush: true)

           /* c.numeroDaMesa = selectedItem.numeroDaMesa
            System.println("selectedItem.numeroDaMesa="+selectedItem.numeroDaMesa)
            c.localizacao = selectedLocal
            c.save(flush: true)

            selectedItem = null*/
            info.value = "Os dados da Mesa "+selectedItem.numeroDaMesa+" foram actualizados com sucesso!"
          //  Messagebox.show("Os dados da Mesa "+c.nome+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)

        }catch (Exception e){
           // Messagebox.show("Erro na atualização dos  dados da Mesa "+c.nome+"!"+e.toString(), "Lua", 1,  Messagebox.ERROR)
            info.value = "Erro na atualização dos  dados da Mesa "+c.numeroDaMesa+"!"+e.toString()
        }


    }




    public String getFilter() {
        return filter
    }

    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }

    public Mesa getSelectedItem(){
        return  selectedItem
    }
    public void setSelectedItem (Mesa item){
        this.selectedItem = item
    }


    public ListModelList<Mesa> getItems() {
        if (items == null) {
            items = new ListModelList<Mesa>(Mesa.all)
        }
        return items
    }

    @NotifyChange("items")
    @Command
    public void doSearch() {

        items.clear()
        List<Mesa> allItems = Mesa.all
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (Mesa c : allItems) {
                if (c.numeroDaMesa.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.localizacao.toString()?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0
                ) {
                    items.add(c)
                }
            }
        }
    }


    @Command
    @NotifyChange(["selectedItem","items","btn_undelete"])
    public void deleteItem (){
        deletedItem = selectedItem
        try {

            while (selectedItem.numeroDaMesa.equals(null)){
               // Messagebox.show("Selecione A Mesa que desja eliminar!", "Lua", 1, Messagebox.ERROR)
                info.value = "Selecione A Mesa que desja eliminar!"
                return
            }
            /*Messagebox.show("Tem certeza que deseja eliminar este Mesa?", "Execute?", Messagebox.YES | Messagebox.NO,
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
            def itemToDelete = Mesa.findById(selectedItem.id)
            itemToDelete.delete(flush: true)
            items.remove(selectedItem)
            selectedItem = null
            info.style="color:red"
            info.value = "A Mesa foi Remoido com sucesso!"
            btn_undelete=true

        }catch (Exception e ){
           // Messagebox.show("Selecione Um Mesa!"+e.toString())
            info.value = e.toString()
        }


    }

    @Command
    @NotifyChange(["btn_undelete"])
    def unDelete() {
        Mesa mesa = new Mesa()
        if(deletedItem!=null){
            mesa.numeroDaMesa = deletedItem.numeroDaMesa
            mesa.estado = deletedItem.estado
            mesa.localizacao = deletedItem.localizacao
        }

       mesa.save(flush: true)
        btn_undelete=false
        info.style="color:green"
        info.value = "O «A mesa foi reposta com sucesso"
    }

    @Init init() {
        // initialzation code here
        btn_undelete=false
    }

    @Command
    @NotifyChange(["selectedLocal","info"])
    def deleteLocal(){
        info.value = ""
        if(Mesa.findByLocalizacao(selectedLocal)){
            info.style="color:red"
            info.value = "Este local não pode ser removido, pois tem mesas!"
            return
        }
        if(selectedLocal!=null){
            selectedLocal.delete(flush: true)
            info.style = "color:red"
            info.value = "O Local foi eliminado!"
            locais.remove(selectedLocal)
        }
    }

    @Command
    @NotifyChange(["selectedItem","itemList","btsalvar","selectedLocal","locais","info"])
    void viewItems(@BindingParam("id") Integer id) {
        info.value =""
        selectedItem = Mesa.findById(id)
        image.children.clear()
        if(selectedItem.imagem){
            selectedItem.imagem.setParent(image)
        }

        btsalvar = false

    }

    @Command
    @NotifyChange(["percentagem","selectedItem","info"])
    void calcularPrecoDeVenda(@BindingParam("preco") BigDecimal preco) {
        info.value = ""
        selectedItem.precoDeVenda=preco

        if(percentagem>0){
            selectedItem.precoDeVenda=(preco*percentagem/100)+preco
        }
    }

    @Command
    @NotifyChange(["selectedItem","itemList","btsalvar","info"])
    def viewItemBynumeroDaMesa(){
        info.value = ""
        def item = Mesa.findByNumeroDaMesa(numeroDaMesa)
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


}
