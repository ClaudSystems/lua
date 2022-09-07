package modelo

import grails.transaction.Transactional
import lua.estoque.marca.Marca
import lua.estoque.modelo.Modelo

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox


@Transactional
class ModeloCrudViewModel {
    @Wire Label info
    String blue = "color:blue"
    String red = "color:red"
    private ListModelList<Modelo> items
    private ListModelList<Marca> marcas
    private Modelo selectedItem
    private String selectedMarca
    private String filter
    private boolean divmarca = false
    private Boolean btsalvar =false

    String getSelectedMarca() {
        return selectedMarca
    }

    void setSelectedMarca(String selectedMarca) {
        this.selectedMarca = selectedMarca
    }

    boolean getDivmarca() {
        return divmarca
    }

    @NotifyChange(["divmarca","info"])
    @Command
    public void showMarca(){
        info.value = ""
        if(divmarca){
            divmarca=false
        }else {
            divmarca = true
        }

    }



    public  Boolean getBtsalvar() {
        return btsalvar
    }

    @NotifyChange(["selectedItem","info"])
    @Command
    public void fecharEditor(){
        info.value = ""
        selectedItem = null
    }

    @NotifyChange(["btsalvar","info"])
    @Command
    public void removeBtSalvar(){
        info.value = ""
        btsalvar = false
    }

    @NotifyChange(["selectedItem","btsalvar","info"])
    @Command
    public void addItem(){
        info.value = ""
        Modelo i = new Modelo()
        selectedItem = i
        btsalvar= true
    }


    @NotifyChange(["selectedItem","items","info"])
    @Command
    public  void salvarItem(){
        while (selectedItem.descricao.equals(null)&&selectedItem.marca.equals(null)){
          //  Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.style = red
            info.value = "Preecha os campos!"
            return
        }
        if (Modelo.findByDescricao(selectedItem.descricao)) {
          //  Messagebox.show("o valor da descricao ja existe!", "Lua", 1, Messagebox.ERROR)
            info.style = red
            info.value = "o valor da descricao ja existe!"
            return
        }

        selectedItem.save flush: true

        if(Modelo.findByDescricao(selectedItem.descricao)){
           // Messagebox.show("Modelo "+selectedItem.descricao+" foi criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
            info.value = "Modelo "+selectedItem.descricao+" foi criado com sucesso!"
            info.style = blue
            items.add(selectedItem)
            selectedItem=null
        }else {
         //   Messagebox.show("Modelo "+selectedItem.descricao+" Erro!", "Lua", 1,  Messagebox.ERROR)
            info.value = "Modelo "+selectedItem.descricao+" Erro!"
            info.style = red
            return
        }

    }
    @NotifyChange(["selectedItem","items","info"])
    @Command
    public  void salvarMarca(){
        while (selectedMarca.empty){
           // Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.value = "Preecha os campos!"
            info.style = red
            return
        }
        if (Marca.findByNome(selectedMarca)) {
          //  Messagebox.show("o valor da Marca ja existe!", "Lua", 1, Messagebox.ERROR)
            info.value = "o valor da Marca ja existe!"
            info.style = red
            return
        }
        Marca marca = new Marca(nome: selectedMarca)
        marca.save flush: true

        if(Marca.findByNome(selectedMarca)){
           // Messagebox.show("Marca "+selectedMarca+" foi criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
            info.value = "Marca "+selectedMarca+" foi criado com sucesso!"
            info.style = blue
            marcas.add(marca)
            selectedMarca=null
        }else {
           // Messagebox.show("Marca "+selectedMarca+" Erro na grava��o!", "Lua", 1,  Messagebox.ERROR)
            info.value = "Marca "+selectedMarca+" Erro na gravação!"
            info.style = red
        }

    }
    @NotifyChange(["selectedItem","items","info"])
    @Command
    public  void updateItem(){
        while (selectedItem.descricao.equals(null)&&selectedItem.marca.equals(null)){
           // Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.value = "Preecha os campos!"
            info.style = red
            return
        }
        Modelo c = Modelo.findById(selectedItem.id)
        c.descricao = selectedItem.descricao
        c.marca= selectedItem.marca



        c.save()
        selectedItem = null
      //  Messagebox.show("Os dados do Modelo "+c.descricao+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Os dados do Modelo "+c.descricao+" foram actualizados com sucesso!"
        info.style= blue
    }




    public String getFilter() {
        return filter
    }

    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }

    public Modelo getSelectedItem(){
        return  selectedItem
    }
    public void setSelectedItem (Modelo item){
        this.selectedItem = item
    }


    public ListModelList<Modelo> getItems() {
        if (items == null) {
            items = new ListModelList<Modelo>(Modelo.all)
        }
        return items
    }
    public ListModelList<Marca> getMarcas() {
        if (marcas == null) {
            marcas = new ListModelList<Marca>(Marca.all)
        }
        return marcas
    }

    @NotifyChange(["items","info"])
    @Command
    public void doSearch() {
        info.value = ""
        items.clear()
        List<Modelo> allItems = Modelo.all
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (Modelo c : allItems) {
                if (c.marca.nome.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.descricao.toLowerCase().indexOf(filter.toLowerCase()) >= 0

                ) {
                    items.add(c)
                }
            }
        }
    }


    @Command
    @NotifyChange(["selectedItem","items","info"])
    public void deleteItem (){
        info.value = ""
        try {

            while (selectedItem.marca.equals(null)||selectedItem.descricao.equals(null)||selectedItem.equals(null)){
                //Messagebox.show("Selecione O Modelo que desja eliminar!", "Lua", 1, Messagebox.ERROR)
                info.value = "Selecione O Modelo que desja eliminar!"
                info.style = red
                return
            }
           /* Messagebox.show("Tem certeza que deseja eliminar este Modelo?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        selectedItem.delete()
                        items.remove(selectedItem)
                        selectedItem = null
                    } else {
                        return
                    }
                }
            }
            )*/
            selectedItem.delete()
            items.remove(selectedItem)
            selectedItem = null
            info.value = "Item eliminado!"
            info.style = red
        }catch (Exception e ){
           // Messagebox.show("Selecione Um Modelo!")
            info.value = "Selecione Um Modelo!"+e.toString()
            info.style = blue

        }


    }



    @Init init() {
        // initialzation code here
    }


}
