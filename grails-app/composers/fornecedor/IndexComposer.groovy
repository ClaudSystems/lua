package fornecedor

import lua.entidades.fornecedor.Fornecedor
import lua.entidades.fornecedor.Fornecedor
import org.zkoss.zhtml.Button
import org.zkoss.zul.Div
import org.zkoss.zul.Grid
import org.zkoss.zul.Intbox
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
      String nuit
      String mail
      String numTelefone
      String endereco
      */

    Textbox tb_codigo
    Textbox tb_nome
    Textbox tb_nuit
    Textbox tb_mail
    Intbox tb_telefone
    Textbox tb_endereco
    Button bt_salvar
    Button bt_update
    Button bt_add_fornecedor
    Button bt_clien
    Button bt_delite = new Button()

    Button bt_add_filter
    Grid gd_fornecedor
    Div dv_delite
    Div dv_salvar
    Div dv_filter
    static Fornecedor selectedFornecedor
    Label lb_info
    Listbox lb_fornecedores
    ArrayList<Fornecedor> dados = new ArrayList()


    def afterCompose = { window ->
        renderLBFornecedores()
        bt_delite.setVisible(false)
        dv_delite.setVisible(false)
        dv_salvar.setVisible(true)



    }

    def CreateForm (Fornecedor fornecedor){



    }
    @Listen("onClick = button#bt_salvar")
    public void salvar() {
        try {
            Fornecedor fornecedor = new Fornecedor()


            fornecedor.codigo = tb_codigo?.value
            fornecedor.nome = tb_nome?.value
            fornecedor.nuit = tb_nuit?.value
            fornecedor.mail = tb_mail?.value
            fornecedor.numTelefone = tb_telefone?.value?.toString()
            fornecedor.endereco = tb_endereco?.value
            if (Fornecedor.findAllByCodigo(tb_codigo.value)) {
                Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
                return
            }

            fornecedor.save flush: true
            renderLBFornecedores()
            Messagebox.show(fornecedor.nome + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)

        }
        catch (Exception ex) {
            Messagebox.show(ex.toString(), "Lua", 1, Messagebox.ERROR)
        }

    }


    def renderLBFornecedores () {
        lb_info.value=""
        fornecedoresList()
        lb_fornecedores.getItems() .clear()
        lb_fornecedores.setItemRenderer(new ListitemRenderer<Fornecedor>() {

            @Override
            void render(Listitem item, Fornecedor fornecedor, int i) throws Exception {
                new Listcell(fornecedor?.codigo).setParent(item)
                new Listcell(fornecedor.nome).setParent(item)
                new Listcell(fornecedor.nuit).setParent(item)
                new Listcell(fornecedor.mail).setParent(item)
                new Listcell(fornecedor.numTelefone).setParent(item)
                new Listcell(fornecedor.endereco).setParent(item)
                item.setValue(fornecedor)


            }
        })
    }

    def fornecedoresList(){
        def fornecedores = new ArrayList<Fornecedor>()
        def fornecedoresDB = Fornecedor.all

        for(Fornecedor c in fornecedoresDB) {

            fornecedores.add(c)

        }
        ListModelList lml = new ListModelList(fornecedores,true)
        lb_fornecedores.setModel(lml)
    }


    @Listen("onClick = button#bt_add_fornecedor")
    public  void openGrid (){
        dv_salvar.setVisible(true)
        dv_delite.setVisible(false)

        if(gd_fornecedor.visible){
            gd_fornecedor.setVisible(false)
        }
        else  {


            gd_fornecedor.setVisible(true)

        }

    }
    @Listen("onClick = button#bt_add_filter")
    public  void openFilter (){
        if(dv_filter.visible){
            dv_filter.setVisible(false)
        }
        else  dv_filter.setVisible(true)
    }

    @Listen("onDoubleClick = listbox#lb_fornecedores")
    public  void editarFornecedor (){
        dv_delite.setVisible(true)
        dv_salvar.setVisible(false)
       // def id = lb_fornecedores.getSelectedItem().getValue().toString().split('-')
       // System.println(lb_fornecedores.getSelectedItem().value)

        def fornecedor= Fornecedor.findByNome(lb_fornecedores.getSelectedItem().value.toString())
        selectedFornecedor = fornecedor
        gd_fornecedor.setVisible(true)
        tb_codigo.setValue(fornecedor.codigo)
        tb_nome.value=fornecedor.nome.toString()
        tb_nuit.value= fornecedor.nuit
        tb_mail.value = fornecedor.mail
        tb_telefone.value=fornecedor.numTelefone.toInteger()
        tb_endereco.value=fornecedor.endereco
       }

    @Listen("onClick = button#bt_update")
    public void update(){
        Fornecedor fornecedor = Fornecedor.find(selectedFornecedor)
        if(!fornecedor.equals(null)){
            if (!tb_codigo.value.equals(fornecedor.codigo)) {
                if(Fornecedor.findAllByCodigo(tb_codigo.value)){
                    Messagebox.show("O valor de codigo deve ser unico!", "Lua", 1,  Messagebox.ERROR)
                    return
                }
            }

            fornecedor.codigo =tb_codigo?.value
            fornecedor.nome = tb_nome?.value
            fornecedor.nuit = tb_nuit?.value
            fornecedor.mail = tb_mail?.value
            fornecedor.numTelefone = tb_telefone?.value?.toString()
            fornecedor.endereco = tb_endereco?.value

            fornecedor.save()
            Messagebox.show(fornecedor.nome+" Actualizado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
            renderLBFornecedores()


        }

    }
    @Listen("onClick = button#bt_delite")
    public  void delite(){
        Fornecedor fornecedor = Fornecedor.find(selectedFornecedor)
        fornecedor.delete flush: true

        Messagebox.show(fornecedor.nome+" Eliminado!", "Lua", 1,  Messagebox.INFORMATION)

    }



}
