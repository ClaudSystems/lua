package fornecedor

import grails.transaction.Transactional
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import lua.entidades.fornecedor.Fornecedor



@Transactional
class FornecedorCrudViewModel {
    @Wire Label info
    String blue= "color:blue"
    String red= "color:red"
    private ListModelList<Fornecedor> fornecedores
    private Fornecedor selectedFornecedor
    private String filter

    private Boolean btsalvar =false

    public  Boolean getBtsalvar() {
        return btsalvar
    }

    @NotifyChange("selectedFornecedor")
    @Command
    public void fecharEditor(){
        selectedFornecedor = null
    }


    @NotifyChange("btsalvar")
    @Command
    public void removeBtSalvar(){
        btsalvar = false
    }

    @NotifyChange(["selectedFornecedor","btsalvar"])
    @Command
    public void addFornecedor(){
        Fornecedor c = new Fornecedor()
        selectedFornecedor = c
        btsalvar = true
    }


    @NotifyChange(["selectedFornecedor","fornecedores"])
    @Command
    public  void salvarFornecedor(){
        while (selectedFornecedor.codigo.equals(null)||selectedFornecedor.nome.equals(null)||selectedFornecedor.nuit.equals(null)){
           // Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.value = "Preecha os campos!"
            info.style = red
            return
        }
        if (Fornecedor.findAllByCodigo(selectedFornecedor.codigo)) {
           // Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
            info.value = "o valor do codigo ja existe!"
            info.style = red
            return
        }

        selectedFornecedor.save()
        fornecedores.add(selectedFornecedor)
      //  Messagebox.show("Fornecedor "+selectedFornecedor.nome+" foi criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Fornecedor "+selectedFornecedor.nome+" foi criado com sucesso!"
        selectedFornecedor = null

    }
    @NotifyChange(["selectedFornecedor","fornecedores"])
    @Command
    public  void updateFornecedor(){
        while (selectedFornecedor.codigo.equals(null)||selectedFornecedor.nome.equals(null)||selectedFornecedor.nuit.equals(null)){
          //  Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.value = "Preecha os campos!"
            return
        }
        Fornecedor c = Fornecedor.findById(selectedFornecedor.id)
        c.codigo = selectedFornecedor.codigo
        c.nome = selectedFornecedor.nome
        c.nuit = selectedFornecedor.nuit
        c.endereco = selectedFornecedor.endereco
        c.numTelefone= selectedFornecedor.numTelefone

        c.save()
        selectedFornecedor = null
       // Messagebox.show("Os dados do Fornecedor "+c.nome+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Os dados do Fornecedor "+c.nome+" foram actualizados com sucesso!"
    }




    public String getFilter() {
        return filter
    }

    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }

    public Fornecedor getSelectedFornecedor(){
        return  selectedFornecedor
    }
    public void setSelectedFornecedor (Fornecedor fornecedor){
        this.selectedFornecedor = fornecedor
    }


    public ListModelList<Fornecedor> getFornecedores() {
        if (fornecedores == null) {
            fornecedores = new ListModelList<Fornecedor>(Fornecedor.all)
        }
        return fornecedores
    }

    @NotifyChange("fornecedores")
    @Command
    public void doSearch() {

        fornecedores.clear()
        List<Fornecedor> allItems = Fornecedor.all
        if (filter == null || "".equals(filter)) {
            fornecedores.addAll(allItems);
        } else {
            for (Fornecedor c : allItems) {
                if (c.codigo.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.nome.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.mail?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0 ||
                        c.numTelefone?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0 ||
                        c.endereco?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0 ||
                        c.nuit.toLowerCase().indexOf(filter.toLowerCase()) >= 0) {
                    fornecedores.add(c)
                }
            }
        }
    }


    @Command
    @NotifyChange(["selectedFornecedor","fornecedores"])
    public void deleteFornecedor (){
        try {
            while (selectedFornecedor.codigo.equals(null)||selectedFornecedor.nome.equals(null)||selectedFornecedor.nuit.equals(null)){
              //  Messagebox.show("Selecione O Fornecedor que desja eliminar!", "Lua", 1, Messagebox.ERROR)
                info.value = "Selecione O Fornecedor que desja eliminar!"
                info.style = red
                return
            }
          /*  Messagebox.show("Tem certeza que deseja eliminar este Fornecedor?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        selectedFornecedor.delete flush: true
                        fornecedores.remove(selectedFornecedor)
                        selectedFornecedor = null
                    }


                }

            }

            );*/
            selectedFornecedor.delete flush: true
            fornecedores.remove(selectedFornecedor)
            selectedFornecedor = null

        }catch (Exception e){
           // Messagebox.show("Selecione um fonecedor!")
            info.value = "Selecione um fonecedor!"
            info.style = blue
        }


    }


    @Init
    init() {
        // initialzation code here
    }


}
