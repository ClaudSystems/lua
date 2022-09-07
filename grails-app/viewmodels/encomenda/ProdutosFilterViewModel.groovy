package encomenda

import grails.transaction.Transactional
import org.zkoss.zk.grails.*
import lua.entidades.cliente.Cliente
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Grid
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Messagebox

@Transactional
class ProdutosFilterViewModel {

    String message
    @Wire   Listbox lb_filter
    @Wire    Grid gd_encomenda
    String filter
    private ListModelList<Item> items
    @Init
    init() {

    }



     class Item {
        private String name

        Item(String name) {
            this.name = name
        }

         String getName() {
            return name
        }
    }

     String getFilter() {
        return filter
    }

     ListModelList<Item> getItems() {
        if(items == null) {
            items = new ListModelList<Item>(getAllItems())
        }
        return items
    }

    @NotifyChange
     void setFilter(String filter) {
        this.filter = filter
    }

    @NotifyChange("items")
    @Command
     void doSearch() {
        System.out.println()
        items.clear()
        List<Item> allItems = getAllItems()
        if(filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for(Item item : allItems) {
                if(item.getName().toLowerCase().indexOf(filter.toLowerCase()) >= 0) {
                    items.add(item)
                }
            }
        }
    }

    private List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>()
        def clientes = Cliente.all
        for (Cliente c in clientes){
            items.add(new Item(c.nome))
        }

        return items
    }
    @NotifyChange
    @Command
      void sendMessage(){
        items.each { Messagebox.show(it.name)}
    }


}
