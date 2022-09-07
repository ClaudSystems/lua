package saidadeproduto

import base.ComposersService
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.saidaDeProduto.SaidaDeProduto
import lua.security.Utilizador
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
@Transactional
class SaidaListViewModel {
    SpringSecurityService springSecurityService
    @Wire Label info
    @Wire    Listbox lb_items
    ComposersService composersService
    private String filter
    SaidaDeProduto selectedSaida
    String red = "color:red"
    String blue = "color:blue"

    private ListModelList<SaidaDeProduto> items
    private ListModelList<ItemProduto> selectedItems

    @Command
    @NotifyChange(["selectedItems"])
    ListModelList<ItemProduto> getSelectedItems() {
       if(selectedItems==null){
           selectedItems = new ListModelList<>()
       }
        selectedItems.clear()
            selectedItems = new ListModelList<>()
        if(selectedSaida!=null){
            selectedSaida = SaidaDeProduto.findById(selectedSaida.id)
            for(ItemProduto ip:selectedSaida.itemsProduto){
                selectedItems.add(ip)
            }
        }
        return selectedItems
    }

    SaidaDeProduto getSelectedSaida() {
        return selectedSaida
    }

    void setSelectedSaida(SaidaDeProduto selectedSaida) {
        this.selectedSaida = selectedSaida
    }




    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }
    public String getFilter() {
        return filter
    }



    ListModelList<SaidaDeProduto> getItems() {
        if (items == null) {
            items = new ListModelList<SaidaDeProduto>(SaidaDeProduto.all)
        }
        return items
    }


    @NotifyChange(["items","info"])
    @Command
    public void doSearch() {
        info.value = ""
        items.clear()
        List<SaidaDeProduto> allItems = SaidaDeProduto.all
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (SaidaDeProduto item : allItems) {
                if (item.getNome().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item.getId().toString().indexOf(filter) >= 0 ||
                        item.getValor().toString().indexOf(filter) >= 0 ||
                        item.destino.toString().indexOf(filter) >= 0 ||
                        item.cliente.nome.indexOf(filter) >= 0 ||
                        item.utilizador.username.indexOf(filter) >= 0 ||
                        item.getDateCreated().toString().indexOf(filter) >= 0) {
                    items.add(item)
                }
            }
        }
    }

    @Command
    public void showIt() {
        System.print(pickedItem.id)

        composersService.setEntradaId(selectedSaida.id)
        Executions.sendRedirect("/entradadeproduto/edit")

    }

    @Command
    static def showNewSaida(){
        Executions.sendRedirect("/saidaDeProduto/newSaida")
    }



    @Command
    @NotifyChange(["items","selectedSaida","info"])
    @Secured(['entrada_produto_d'])
    public void removeItem(@BindingParam('id') Integer id ) {
       selectedSaida = SaidaDeProduto.findById(id)
            if(selectedSaida.cancelado){
                selectedSaida.cancelado=false
            }else {
                selectedSaida.cancelado = true
            }
            selectedSaida.save(flush: true)
            info.value= selectedSaida.numeroDeSaida+" Foi atualizado!"
        info.style = blue

    }



}
