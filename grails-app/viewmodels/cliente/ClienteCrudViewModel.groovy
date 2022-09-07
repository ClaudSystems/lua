package cliente

import grails.transaction.Transactional
import lua.entidades.cliente.Cliente
import lua.entidades.entidade.Entidade
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener

@Transactional
class ClienteCrudViewModel {
    @Wire Label info
    private ListModelList<Cliente> clientes
    private Cliente selectedCliente
    private String filter
    String blue = "color:blue"
    String red = "color:red"

    private Boolean btsalvar =false

      public  Boolean getBtsalvar() {
        return btsalvar
    }

    @NotifyChange(["selectedCliente","info"])
    @Command
    public void fecharEditor(){
        info.value=""
        selectedCliente = null
    }

    @NotifyChange(["btsalvar","info"])
    @Command
    public void removeBtSalvar(){
        info.value = ""
        btsalvar = false
    }

    @NotifyChange(["selectedCliente","btsalvar","info"])
    @Command
    public void addCliente(){
        info.value=""
        Cliente c = new Cliente()
           selectedCliente = c
        btsalvar= true

    }


    @NotifyChange(["selectedCliente","clientes","info"])
    @Command
    public  void salvarCliente(){
        info.value = ""
        while (selectedCliente.codigo.equals(null)||selectedCliente.nome.equals(null)||selectedCliente.nuit.equals(null)){
          //  Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.value ="Preecha os campos!"
            return
        }
        if (Cliente.findAllByCodigo(selectedCliente.codigo)) {
            info.value="o valor do codigo ja existe!"
           // Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
            return
        }
        def entidade = Entidade.findByCodigo('pikas')
        selectedCliente.entidade = entidade
        selectedCliente.save()
        clientes.add(selectedCliente)

        // Messagebox.show("Cliente "+selectedCliente.nome+" foi criado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value = "Cliente "+selectedCliente.nome+" foi criado com sucesso!"
        info.style=blue
        selectedCliente=null
    }
    @NotifyChange(["selectedCliente","clientes","info"])
    @Command
    public  void updateCliente(){


        while (selectedCliente.codigo.equals(null)||selectedCliente.nome.equals(null)||selectedCliente.nuit.equals(null)){
            //Messagebox.show("Preecha os campos!", "Lua", 1, Messagebox.ERROR)
            info.value= "Preecha os campos!"
            info.style = red
            return
        }
        Cliente c = Cliente.findById(selectedCliente.id)
        c.codigo = selectedCliente.codigo
        c.nome = selectedCliente.nome
        c.nuit = selectedCliente.nuit
        c.endereco = selectedCliente.endereco
        c.numTelefone= selectedCliente.numTelefone

        c.save()
        selectedCliente = null
      //  Messagebox.show("Os dados do Cliente "+c.nome+" foram actualizados com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
        info.value= "Os dados do Cliente "+c.nome+" foram actualizados com sucesso!"
    }

    public String getFilter() {
        return filter
    }

    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }

    public Cliente getSelectedCliente(){
        return  selectedCliente
    }
    public void setSelectedCliente (Cliente cliente){
        this.selectedCliente = cliente
    }


    public ListModelList<Cliente> getClientes() {
        if (clientes == null) {
            clientes = new ListModelList<Cliente>(Cliente.all)
        }
        return clientes
    }

    @NotifyChange(["clientes","info"])
    @Command
    public void doSearch() {
        info.value = ""

        clientes.clear()
        List<Cliente> allItems = Cliente.all
        if (filter == null || "".equals(filter)) {
            clientes.addAll(allItems)
        } else {
            for (Cliente c : allItems) {
                if (c.codigo.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.nome.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        c.mail?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0 ||
                        c.numTelefone?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0 ||
                        c.endereco?.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0 ||
                        c.nuit.toLowerCase().indexOf(filter.toLowerCase()) >= 0) {
                    clientes.add(c);
                }
            }
        }
    }


    @Command
    @NotifyChange(["selectedCliente","clientes","info"])
    public void deleteCliente (){
        info.value =""
        try {

            while (selectedCliente.codigo.equals(null)||selectedCliente.nome.equals(null)||selectedCliente.nuit.equals(null)||selectedCliente.equals(null)){
              //  Messagebox.show("Selecione O Cliente que desja eliminar!", "Lua", 1, Messagebox.ERROR)
                info.value ="Selecione O Cliente que desja eliminar!"
                info.style = red
                return
            }
           /* Messagebox.show("Tem certeza que deseja eliminar este Cliente?", "Execute?", Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        selectedCliente.delete()
                        clientes.remove(selectedCliente)
                        selectedCliente = null
                    } else {
                        return
                    }
                }
            }
            );*/
            selectedCliente.delete()
            clientes.remove(selectedCliente)
            selectedCliente = null
        }catch (Exception e ){
            Messagebox.show("Selecione Um Cliente!")
            info.value = "Selecione Um Cliente!"+e.toString()
        }


    }



    @Init init() {
        // initialzation code here
    }



}
