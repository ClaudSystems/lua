package cliente


import org.zkoss.zhtml.Button
import lua.entidades.cliente.Cliente
import org.zkoss.zk.ui.Executions
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

     Textbox tb_codigo
        Textbox tb_nome
    Textbox tb_nuit
    Textbox tb_mail
    Intbox tb_telefone
    Textbox tb_endereco
    Button bt_salvar
    Button bt_update
    Button bt_add_cliente
    Button bt_clien
    Button bt_delite = new Button()

    Button bt_add_filter
    Grid gd_cliente
    Div dv_delite
    Div dv_salvar
    Div dv_filter
    static Cliente selectedCliente
    Label lb_info
    Listbox lb_categoiras
    ArrayList<Cliente> dados = new ArrayList()


    def afterCompose = { window ->
        renderLBClientes()
        bt_delite.setVisible(false)
        dv_delite.setVisible(false)
        dv_salvar.setVisible(true)



    }

    def CreateForm (Cliente cliente){



    }
    @Listen("onClick = button#bt_salvar")
    public void salvar() {
        try {
            Cliente cliente = new Cliente()


            cliente.codigo = tb_codigo?.value
            cliente.nome = tb_nome?.value
            cliente.nuit = tb_nuit?.value
            cliente.mail = tb_mail?.value
            cliente.numTelefone = tb_telefone?.value?.toString()
            cliente.endereco = tb_endereco?.value
            if (Cliente.findAllByCodigo(tb_codigo.value)) {
                Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
                return
            }

            cliente.save flush: true
            renderLBClientes()
            Messagebox.show(cliente.nome + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)

        }
        catch (Exception ex) {
            Messagebox.show(ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }


    def renderLBClientes () {
        lb_info.value=""
        clientesList()
        lb_clientes.getItems() .clear()
        lb_clientes.setItemRenderer(new ListitemRenderer<Cliente>() {

            @Override
            void render(Listitem item, Cliente cliente, int i) throws Exception {
                new Listcell(cliente?.codigo).setParent(item)
                new Listcell(cliente.nome).setParent(item)
                new Listcell(cliente.nuit).setParent(item)
                new Listcell(cliente.mail).setParent(item)
                new Listcell(cliente.numTelefone).setParent(item)
                new Listcell(cliente.endereco).setParent(item)
                item.setValue(cliente)


            }
        })
    }

    def clientesList(){
        def clientes = new ArrayList<Cliente>()
        def clientesDB = Cliente.all

        for(Cliente c in clientesDB) {

            clientes.add(c)

        }
        ListModelList lml = new ListModelList(clientes,true)
        lb_clientes.setModel(lml)
    }


    @Listen("onClick = button#bt_add_cliente")
    public  void openGrid (){
        dv_salvar.setVisible(true)
        dv_delite.setVisible(false)

        if(gd_cliente.visible){
            gd_cliente.setVisible(false)
        }
        else  {


            gd_cliente.setVisible(true)

        }

    }
    @Listen("onClick = button#bt_add_filter")
    public  void openFilter (){
        if(dv_filter.visible){
            dv_filter.setVisible(false)
        }
        else  dv_filter.setVisible(true)
    }

    @Listen("onDoubleClick = listbox#lb_clientes")
    public  void editarCliente (){
        dv_delite.setVisible(true)
        dv_salvar.setVisible(false)
        def id = lb_clientes.getSelectedItem().getValue().toString().split('-')

         def cliente = Cliente.findById(id[0].toLong())

        selectedCliente = cliente
        gd_cliente.setVisible(true)
        tb_codigo.setValue(cliente.codigo)
        tb_nome.value=cliente.nome
        tb_nuit.value= cliente.nuit
        tb_mail.value = cliente.mail
        tb_telefone.value=cliente.numTelefone.toInteger()
        tb_endereco.value=cliente.endereco

    }

    @Listen("onClick = button#bt_update")
    public void update(){
            Cliente cliente = Cliente.find(selectedCliente)
        if(!cliente.equals(null)){
            if (!tb_codigo.value.equals(cliente.codigo)) {
                if(Cliente.findAllByCodigo(tb_codigo.value)){
                    Messagebox.show("O valor de codigo deve ser unico!", "Lua", 1,  Messagebox.ERROR)
                    return
                }
            }

                cliente.codigo =tb_codigo?.value
                cliente.nome = tb_nome?.value
                cliente.nuit = tb_nuit?.value
                cliente.mail = tb_mail?.value
                cliente.numTelefone = tb_telefone?.value?.toString()
                cliente.endereco = tb_endereco?.value

                cliente.save()
                Messagebox.show(cliente.nome+" Actualizado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                renderLBClientes()


        }

    }
    @Listen("onClick = button#bt_delite")
    public  void delite(){
        Cliente cliente = Cliente.find(selectedCliente)
        cliente.delete flush: true

        Messagebox.show(cliente.nome+" Eliminado!", "Lua", 1,  Messagebox.INFORMATION)

    }
    @Listen("onClick = button#bt_add_filter")
    public  void getFilter(){
        Executions.sendRedirect("/cliente/filter.gsp")

    }


}
