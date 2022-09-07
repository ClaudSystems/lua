package fatura

import lua.entidades.cliente.Cliente
import lua.vendas.fatura.Fatura
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listitem
import org.zkoss.zul.ListitemRenderer


class IndexFaturaComposer extends zk.grails.Composer {
    Listbox lb_faturas

    def afterCompose = { window ->
        // initialize components here
    }

    def renderLBFaturas () {
        lb_info.value=""
        listFaturas()
        lb_faturas.getItems() .clear()
        lb_faturas.setItemRenderer(new ListitemRenderer<Fatura>() {

            @Override
            void render(Listitem item, Fatura fatura, int i) throws Exception {
                new Listcell(fatura?.id+"").setParent(item)
                new Listcell(fatura.cliente.nome).setParent(item)
                new Listcell(fatura?.dateCreated+"").setParent(item)

                item.setValue(fatura)


            }
        })
    }

    def listFaturas(){
        def faturas = new ArrayList<Fatura>()
        def faturasDB = Fatura.all

        for(Fatura f in faturasDB) {

            faturas.add(f)

        }
        ListModelList lml = new ListModelList(clientes,true)
        lb_faturas.setModel(lml)
    }


}
