package destino

import lua.estoque.destino.Destino
import org.zkoss.zhtml.Button
import org.zkoss.zul.Div
import org.zkoss.zul.Grid
import org.zkoss.zul.Label
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Textbox
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listitem
import org.zkoss.zul.ListitemRenderer
import org.zkoss.zk.ui.select.annotation.Listen


class IndexComposer extends zk.grails.Composer {
    /*
     String codigo
    String nome
    String descricao
      */

    Textbox tb_codigo
    Textbox tb_nome
    Textbox tb_descricao

    Button bt_salvar
    Button bt_update
    Button bt_add_destino
    Button bt_clien
    Button bt_delite = new Button()

    Button bt_add_filter
    Grid gd_destino
    Div dv_delite
    Div dv_salvar
    Div dv_filter
    static Destino selectedDestino
    Label lb_info
    Listbox lb_destinos
    ArrayList<Destino> dados = new ArrayList()


    def afterCompose = { window ->
       renderLBDestinos()
        bt_delite.setVisible(false)
        dv_delite.setVisible(false)
        dv_salvar.setVisible(true)



    }

    def CreateForm (Destino destino){



    }
    @Listen("onClick = button#bt_salvar")
    public void salvar() {
        try {
            Destino destino = new Destino()
            destino.codigo = tb_codigo?.value
            destino.nome = tb_nome?.value
            destino.descricao = tb_descricao?.value

            if (Destino.findAllByCodigo(tb_codigo.value)) {
                Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
                return
            }

            destino.save flush: true
            renderLBDestinos()
            Messagebox.show(destino.nome + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)

        }
        catch (Exception ex) {
            Messagebox.show(ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }


    def renderLBDestinos () {
        lb_info.value=""
        destinosList()
        lb_destinos.getItems() .clear()
        lb_destinos.setItemRenderer(new ListitemRenderer<Destino>() {

            @Override
            void render(Listitem item, Destino destino, int i) throws Exception {
                new Listcell(destino.codigo).setParent(item)
                new Listcell(destino.nome).setParent(item)
                new Listcell(destino.descricao).setParent(item)
               item.setValue(destino)


            }
        })
    }

    def destinosList(){
        def destinos = new ArrayList<Destino>()
        def destinosDB = Destino.all

        for(Destino d in destinosDB) {

            destinos.add(d)

        }
        ListModelList lml = new ListModelList(destinos,true)
        lb_destinos.setModel(lml)
    }


    @Listen("onClick = button#bt_add_destino")
    public  void openGrid (){
        dv_salvar.setVisible(true)
        dv_delite.setVisible(false)

        if(gd_destino.visible){
            gd_destino.setVisible(false)
        }
        else  {


            gd_destino.setVisible(true)

        }

    }
    @Listen("onClick = button#bt_add_filter")
    public  void openFilter (){
        if(dv_filter.visible){
            dv_filter.setVisible(false)
        }
        else  dv_filter.setVisible(true)
    }

    @Listen("onDoubleClick = listbox#lb_destinos")
    public  void editarDestino (){
        dv_delite.setVisible(true)
        dv_salvar.setVisible(false)
        def id = lb_destinos.getSelectedItem().getValue().toString().split('-')
        def destinosDd = Destino.all
       //def destino = Destino.first(sort:lb_destinos.getSelectedItem().getValue().toString() )

        for (Destino destino in destinosDd ){
            gd_destino.setVisible(true)

            if (destino.codigo.equals(id[0])){
                selectedDestino = destino
                gd_destino.setVisible(true)
                tb_codigo.setValue(destino.codigo)
                tb_nome.value=destino.nome
                tb_descricao.value= destino.descricao

            }
        }

    }

    @Listen("onClick = button#bt_update")
    public void update(){
        Destino destino = Destino.find(selectedDestino)
        if(!destino.equals(null)){
            if (!tb_codigo.value.equals(destino.codigo)) {
                if(Destino.findAllByCodigo(tb_codigo.value)){
                    Messagebox.show("O valor de codigo deve ser unico!", "Lua", 1,  Messagebox.ERROR)
                    return
                }
            }

            destino.codigo =tb_codigo?.value
            destino.nome = tb_nome.value
            destino.descricao = tb_descricao?.value

            destino.save()
            Messagebox.show(destino.nome+" Actualizado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
            renderLBDestinos()


        }

    }
    @Listen("onClick = button#bt_delite")
    public  void delite(){
        Destino destino = Destino.find(selectedDestino)
        destino.delete flush: true

        Messagebox.show(destino.nome+" Eliminado!", "Lua", 1,  Messagebox.INFORMATION)
        renderLBDestinos()
    }



}
