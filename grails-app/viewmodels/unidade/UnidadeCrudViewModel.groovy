package unidade

import lua.estoque.unidade.Unidade
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox

class UnidadeCrudViewModel {
    @Wire Label info
    String blue = "color:blue"
    String red = "color:red"
    private ListModelList<Unidade> items
    private Unidade selectedItem
    private String filter

    private Boolean btsalvar =false

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
        info.value= ""
        btsalvar = false
    }

    @NotifyChange(["selectedItem","btsalvar","info"])
    @Command
    public void addItem(){
        info.value = ""
        Unidade i = new Unidade()
        selectedItem = i
        btsalvar= true
    }


    @NotifyChange(["selectedItem","items","info"])
    @Command
    public  void salvarItem(){
        info.value = ""
        while (selectedItem.codigo.equals(null)||selectedItem.nome.equals(null)||selectedItem.numeroDeElementos.equals(null)){
           // Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.style = red
            info.value = "Preecha os campos!"
            return
        }
        if (Unidade.findAllByCodigo(selectedItem.codigo)) {
           // Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
            info.style = red
            info.value = "o valor do codigo ja existe!"
            return
        }

        selectedItem.save()
        items.add(selectedItem)

      //  Messagebox.show("Unidade "+selectedItem.nome+" foi criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Unidade "+selectedItem.nome+" foi criado com sucesso!"
        selectedItem=null
    }
    @NotifyChange(["selectedItem","items","info"])
    @Command
    public  void updateItem(){
        while (selectedItem.codigo.equals(null)||selectedItem.nome.equals(null)||selectedItem.numeroDeElementos.equals(null)){
          //  Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.style = red
            info.value = "Preecha os campos!"
            return
        }
        Unidade c = Unidade.findById(selectedItem.id)
        c.codigo = selectedItem.codigo
        c.nome = selectedItem.nome
        c.numeroDeElementos = selectedItem.numeroDeElementos



        c.save()
        selectedItem = null
       // Messagebox.show("Os dados do Unidade "+c.nome+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.style = red
        info.value = "Os dados do Unidade "+c.nome+" foram actualizados com sucesso!"
    }




    public String getFilter() {
        return filter
    }

    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }

    public Unidade getSelectedItem(){
        return  selectedItem
    }
    public void setSelectedItem (Unidade item){
        this.selectedItem = item
    }


    public ListModelList<Unidade> getItems() {
        if (items == null) {
            items = new ListModelList<Unidade>(Unidade.all)
        }
        return items
    }

    @NotifyChange(["items","info"])
    @Command
    public void doSearch() {
        info.value = ""
        items.clear()
        List<Unidade> allItems = Unidade.all
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (Unidade c : allItems) {
                if (c.codigo.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.nome.toLowerCase().indexOf(filter.toLowerCase()) >= 0

                ) {
                    items.add(c);
                }
            }
        }
    }


    @Command
    @NotifyChange(["selectedItem","items"])
    public void deleteItem (){
        try {

            while (selectedItem.codigo.equals(null)||selectedItem.nome.equals(null)||selectedItem.equals(null)){
               // Messagebox.show("Selecione O Unidade que desja eliminar!", "Lua", 1, Messagebox.ERROR)
                info.style = red
                info.value = "Selecione O Unidade que desja eliminar!"
                return
            }
           /* Messagebox.show("Tem certeza que deseja eliminar este Unidade?", "Execute?", Messagebox.YES | Messagebox.NO,
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
            );*/
            selectedItem.delete()
            items.remove(selectedItem)
            selectedItem = null
        }catch (Exception e ){
            Messagebox.show("Selecione Um Unidade!")
        }


    }



    @Init init() {
        // initialzation code here
    }


}
