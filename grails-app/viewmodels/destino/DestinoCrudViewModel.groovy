package destino

import grails.transaction.Transactional
import lua.estoque.destino.Destino
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.Messagebox
import org.zkoss.bind.annotation.BindingParam


@Transactional
class DestinoCrudViewModel {
    private List<Destino> items
    private List<Destino> pickedItemSet
    private Destino selectedItem
    private String filter
    @Wire Label info


    private Boolean btsalvar =false
    private Boolean allSelected =false



    @Command
    @NotifyChange(["items","pickedItemSet","selectedItem","info"])
    public  void deliteSelectedItems(){
        info.value = ""
        for(Destino des in pickedItemSet){
                des.delete flush: true
                items.remove(des)
        }
        selectedItem = null
        pickedItemSet.clear()
    }

    @Command
    @NotifyChange(["items","pickedItemSet","selectedItem","info"])
    public void excluirItems(){
        info.value = ""
        for(Destino des in pickedItemSet){
            des.estado = "excluido"
            des.save flush: true
            items.remove(des)
        }
        selectedItem = null
        pickedItemSet.clear()

    }

    @Command
    @NotifyChange(["selectedItem","btsalvar","info"])
    public void editSelelctedItem(@BindingParam("index") Integer index){
        info.value = ""
        selectedItem=items[index]
        btsalvar=false
    }

    public List  getPickedItemSet() {
        return pickedItemSet
    }

    public  void setPickedItemSet(List pickedItemSet) {
        this.pickedItemSet = pickedItemSet
    }

    public  Boolean getBtsalvar() {
        return btsalvar
    }

    public boolean getAllSelected(){
        return allSelected
    }


    @Command
    @NotifyChange(["pickedItemSet","info"])
    public void pick(@BindingParam("checked") boolean isPicked, @BindingParam("picked")Destino item){
        info.value = ""
        if (isPicked){
            pickedItemSet.add(item)
            }else{
            pickedItemSet.remove(item)

        }
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
        Destino i = new Destino()
        selectedItem = i
        btsalvar= true
    }


    @NotifyChange(["selectedItem","items","info"])
    @Command
    public  void salvarItem(){
        info.value = ""
        while (selectedItem.codigo.equals(null)||selectedItem.nome.equals(null)||selectedItem.descricao.equals(null)){
           // Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.value= "Preecha os campos!"
            return
        }
        if (Destino.findAllByCodigo(selectedItem.codigo)) {
           // Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
            info.value= "o valor do codigo ja existe!"
            return
        }

        selectedItem.save()
        selectedItem.estado="ativo"
        items.add(selectedItem)

      //  Messagebox.show("Destino "+selectedItem.nome+" foi criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Destino "+selectedItem.nome+" foi criado com sucesso!"
        selectedItem=null
    }
    @NotifyChange(["selectedItem","items","info"])
    @Command
    public  void updateItem(){
        info.value = ""
        while (selectedItem.codigo.equals(null)||selectedItem.nome.equals(null)||selectedItem.descricao.equals(null)){
           // Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.value = "Preecha os campos!"
            return
        }
        Destino c = Destino.findById(selectedItem.id)
        c.codigo = selectedItem.codigo
        c.nome = selectedItem.nome
        c.descricao = selectedItem.descricao



        c.save()
        selectedItem = null
       // Messagebox.show("Os dados do Destino "+c.nome+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Os dados do Destino "+c.nome+" foram actualizados com sucesso!"
        info.style = "color:blue"
    }




    public String getFilter() {
        return filter
    }

    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }

    public Destino getSelectedItem(){
        return  selectedItem
    }
    public void setSelectedItem (Destino item){
        this.selectedItem = item
    }




    public List<Destino> getItems() {

        if (items == null) {
            items = new ArrayList<Destino>(Destino.all)

        }


        return items
    }

    @NotifyChange(["items","pickedItemSet","info"])
    @Command
    public void doSearch() {
        info.value = ""
        items.clear()
        List<Destino> allItems = Destino.all
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (Destino c : allItems) {
                if (c.codigo.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.nome.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.descricao?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0 
                       ) {
                    items.add(c);
                }
            }
        }
    }

/*

    @Command
    @NotifyChange(["selectedItem","items","pickedItemSet"])
    public void deleteItem (){
        try {

            while (selectedItem.codigo.equals(null)||selectedItem.nome.equals(null)||selectedItem.equals(null)){
                Messagebox.show("Selecione O Destino que desja eliminar!", "Lua", 1, Messagebox.ERROR)
                return
            }
            Messagebox.show("Tem certeza que deseja eliminar este Destino?", "Execute?", Messagebox.YES | Messagebox.NO,
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
            );

        }catch (Exception e ){
            Messagebox.show("Selecione Um Destino!")
        }


    }
*/



    @Init init() {
        // initialzation code here
    }


}
