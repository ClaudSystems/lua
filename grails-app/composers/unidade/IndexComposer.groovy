package unidade

import lua.estoque.unidade.Unidade
import org.zkoss.zhtml.Button
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.Div
import org.zkoss.zul.Grid
import org.zkoss.zul.Intbox
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listitem
import org.zkoss.zul.ListitemRenderer
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Textbox


class IndexComposer extends zk.grails.Composer {

    Textbox tb_codigo
    Textbox tb_nome
    Intbox tb_numero
    Button bt_salvar
    Button bt_update
    Button bt_add_unidade
    Button bt_clien
    Button bt_delite = new Button()

  
    Grid gd_unidade
    Div dv_delite
    Div dv_salvar
    Div dv_filter
    static Unidade selectedUnidade
    Label lb_info
    Listbox lb_unidades
    ArrayList<Unidade> dados = new ArrayList()


    def afterCompose = { window ->
        renderLBUnidades()
        bt_delite.setVisible(false)
        dv_delite.setVisible(false)
        dv_salvar.setVisible(true)



    }

    def CreateForm (Unidade unidade){



    }
    @Listen("onClick = button#bt_salvar")
    public void salvar() {

        try {
            Unidade unidade = new Unidade()
            unidade.codigo = tb_codigo?.value
            unidade.nome = tb_nome?.value

           unidade.numeroDeElementos = tb_numero.value
           
            if (Unidade.findAllByCodigo(tb_codigo.value)) {
                Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
                return
            }
            unidade.save flush: true
            renderLBUnidades()
            Messagebox.show(unidade.nome + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)

        }


        catch (Exception ex) {
            Messagebox.show("Erro! "+ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }


    def renderLBUnidades () {
        lb_info.value=""
        clientesList()
        lb_unidades.getItems().clear()
        lb_unidades.setItemRenderer(new ListitemRenderer<Unidade>() {

            @Override
            void render(Listitem item, Unidade unidade, int i) throws Exception {
                new Listcell(unidade.codigo).setParent(item)
                new Listcell(unidade.nome).setParent(item)
                new Listcell(unidade.numeroDeElementos.toString()).setParent(item)
                item.setValue(unidade)


            }
        })
    }

    def clientesList(){
        def unidades = new ArrayList<Unidade>()
        def unidadesDB = Unidade.all

        for(Unidade c in unidadesDB) {

            unidades.add(c)

        }
        ListModelList lml = new ListModelList(unidades,true)
        lb_unidades.setModel(lml)
    }


    @Listen("onClick = button#bt_add_unidade")
    public  void openGrid (){
        dv_salvar.setVisible(true)
        dv_delite.setVisible(false)

        if(gd_unidade.visible){
            gd_unidade.setVisible(false)
        }
        else  {


            gd_unidade.setVisible(true)

        }

    }


    @Listen("onDoubleClick = listbox#lb_unidades")
    public  void editarUnidade (){
        dv_delite.setVisible(true)
        dv_salvar.setVisible(false)
        def id = lb_unidades.getSelectedItem().getValue().toString().split('-')
        def clientesDd = Unidade.all
        //  def unidade = Unidade.first(sort:lb_unidades.getSelectedItem().getValue().toString() )

        for (Unidade unidade in clientesDd ){
            gd_unidade.setVisible(true)

            if (unidade.id.equals(id[0].toLong())){
                selectedUnidade = unidade
                gd_unidade.setVisible(true)
                tb_codigo.setValue(unidade.codigo)
                tb_nome.value=unidade.nome
                tb_numero.value=unidade.numeroDeElementos.toInteger()
               
            }
        }

    }

    @Listen("onClick = button#bt_update")
    public void update(){


        try {
            Unidade unidade = Unidade.find(selectedUnidade)
            if(!unidade.equals(null)){
                if (!tb_codigo.value.equals(unidade.codigo)) {
                    if(Unidade.findAllByCodigo(tb_codigo.value)){
                        Messagebox.show("O valor de codigo deve ser unico!", "Lua", 1,  Messagebox.ERROR)
                        return
                    }
                }

                unidade.codigo =tb_codigo?.value
                unidade.nome = tb_nome?.value
                unidade.numeroDeElementos = tb_numero?.value

                unidade.save()
                Messagebox.show(unidade.nome+" Actualizado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                renderLBUnidades()
            }
        } catch ( Exception ex) {
            Messagebox.show("Erro! "+ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }
    @Listen("onClick = button#bt_delite")
    public  void delite(){
        Unidade unidade = Unidade.find(selectedUnidade)
        unidade.delete flush: true
        renderLBUnidades()
        Messagebox.show(unidade.nome+" Eliminado!", "Lua", 1,  Messagebox.INFORMATION)

    }

}
