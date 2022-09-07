package lote

import lua.estoque.lote.Lote
import lua.estoque.marca.Marca
import lua.estoque.modelo.Modelo
import lua.estoque.produto.Produto
import lua.estoque.unidade.Unidade
import org.zkoss.zhtml.Button
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.WireVariable
import org.zkoss.zul.Combobox
import org.zkoss.zul.Comboitem
import org.zkoss.zul.Datebox
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
  /*
   String codigo
    Produto produto
    Modelo modelo
    Unidade unidade
    Marca marca
    Date validade
    int qtd
    */
    Textbox tb_codigo_produto
    @Wire
    Textbox tb_filtro_codigo
    Textbox tb_nome_marca
    Textbox tb_nome_unidade
    Textbox tb_nome_produto
    Textbox tb_descricao_produto
    Textbox tb_descricao_modelo
    Textbox tb_codigo_lote
    Textbox tb_codigo_unidade
    Intbox ib_qtd
    Intbox ib_qtd_unidade
    Datebox db_validade
    Button bt_add_modelo
    Button bt_add_marca
    Button bt_salvar_lote
    Button bt_salvar_marca
    Button bt_update
    Button bt_add_lote
    Button bt_add_produto
    Button bt_add_uniade
    Button bt_clien
    Button bt_delite = new Button()
    @Wire
    Combobox cb_produtos
    @Wire
    Combobox cb_modelos
    @Wire
    Combobox cb_unidades
    @Wire
    Combobox cb_marcas
    @Wire
    Combobox cb_marcas_modelo
    @Wire
    Label lb_info_add
    @Wire
    Div dv_direito
    @Wire
    Grid gd_lote
    @Wire
    Grid gd_unidade
    @Wire
    Grid gd_produto
    Grid gd_modelo
    Grid gd_marca
    Div dv_delite
    Div dv_salvar
    Div dv_filter
    static Lote selectedLote
    Label lb_info
    Listbox lb_lotes
    ArrayList<Lote> dados = new ArrayList()


    def afterCompose = { window ->
        renderLBLotes()
        bt_delite.setVisible(false)
        dv_delite.setVisible(false)
        dv_salvar.setVisible(true)
        renderCb_marcas()
        renderCb_marcas_modelo()
        renderCb_modelos()
        renderCb_produtos()
        renderCb_unidades()
    }

    def renderCb_marcas (){
        def marcas = Marca.all
        for (Marca m : marcas) {
            cb_marcas?.appendItem(m.toString())
        }
    }
    def renderCb_marcas_modelo (){
        def marcas = Marca.all
        List list_marcas = new ArrayList()
        for (Marca m : marcas) {
            list_marcas.add(m.toString())
        }
        ListModelList lm_marcas = new ListModelList(list_marcas)
        cb_marcas_modelo.setModel(lm_marcas)
    }
    def renderCb_modelos(){
        def modelos = Modelo.all
        List list_modelos = new ArrayList()
        for (Modelo m : modelos) {
            list_modelos.add(m.toString())
        }
        ListModelList lm_modelos = new ListModelList(list_modelos)
        cb_modelos.setModel(lm_modelos)
    }
    def renderCb_unidades (){
        def unidades = Unidade.all
        List list_unidades = new ArrayList()
        for (Unidade u : unidades) {
            list_unidades.add(u.nome)
        }
        ListModelList lm_unidades = new ListModelList(list_unidades)
        cb_unidades.setModel(lm_unidades)
    }
    def renderCb_produtos (){
        def produtos = Produto.all
        List list_produtos = new ArrayList()
        for (Produto p : produtos) {
            list_produtos.add(p.toString())
        }
        ListModelList lm_produtos = new ListModelList(list_produtos)
        cb_produtos.setModel(lm_produtos)
    }


    @Listen("onOK = combobox#cb_produtos")
    public  void filtrarProdutos(){
        //renderCb_produtos()
        cb_produtos.getChildren().clear()
        def searchResults = Produto.withCriteria {
                like('descricao',  '%' + cb_produtos.value + '%')
                like('codigo', '%'+ cb_produtos.value + '%')

        }

        ListModelList lm_produtos = new ListModelList(searchResults)
        cb_produtos.setModel(lm_produtos)

    }
    @Listen("onCancel = combobox#cb_produtos")
    public  void repor(){
        renderCb_produtos()
    }

    def CreateForm (Lote lote){

    }
    @Listen("onClick = button#bt_salvar_lote")
    public void salvarLote() {

        Lote lote = new Lote()
        try {
            lote.codigo = tb_codigo_lote?.value
            lote.validade =db_validade?.value
            def produto = Produto.findByNome(cb_produtos.selectedItem.value?.toString())
            lote.produto = produto
            if (cb_modelos.selectedItem?.value){
                def modelo = Modelo.findByDescricao(cb_modelos.selectedItem.value?.toString())
                lote.modelo=modelo
            }

            if (cb_unidades.selectedItem?.value){
                def unidade = Unidade.findByNome(cb_unidades.selectedItem.value?.toString())
                lote.unidade=unidade
            }
                   if (Lote.findAllByCodigo(tb_codigo_lote.value)) {
                Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
                return
            }



            lote.save flush: true

            renderLBLotes()
            if (Lote.find(lote)){
                Messagebox.show(lote.codigo + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)
            }else           Messagebox.show("Erro na gravacao! ", "Lua", 1, Messagebox.INFORMATION)

        }catch (Exception ex) {
            Messagebox.show("Erro! "+ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }


    def renderLBLotes () {
        lb_info.value=""
        if (tb_filtro_codigo.value){
            lotesListFilter()
        }else { lotesList()}

        lb_lotes.getItems().clear()
        lb_lotes.setItemRenderer(new ListitemRenderer<Lote>() {

            @Override
            void render(Listitem item, Lote lote, int i) throws Exception {
                new Listcell(lote.codigo).setParent(item)
                new Listcell(lote.produto.descricao).setParent(item)
                new Listcell(lote.modelo?.descricao).setParent(item)
                new Listcell(lote.unidade?.nome).setParent(item)
                new Listcell(lote.validade.toString()).setParent(item)

                item.setValue(lote)
            }
        })
    }

    def lotesList(){
        def lotes = new ArrayList<Lote>()
        def lotesDB = Lote.all

        for(Lote c in lotesDB) {

            lotes.add(c)

        }
        ListModelList lml = new ListModelList(lotes,true)
        lb_lotes.setModel(lml)
    }
    def lotesListFilter(){
        def lotes = new ArrayList<Lote>()

        def lotesDB = Lote.withCriteria(uniqueResult: false) {

            like('codigo', '%'+tb_filtro_codigo.value+'%')
        }


        for(Lote l in lotesDB) {

            lotes.add(l)

        }
        ListModelList lml = new ListModelList(lotes,true)
        lb_lotes.setModel(lml)
    }


    @Listen("onClick = button#bt_add_lote")
    public  void openGrid (){

        dv_salvar.setVisible(true)
        dv_delite.setVisible(false)

        if(gd_lote.visible){
            gd_lote.setVisible(false)
            gd_modelo.setVisible(false)
            gd_produto.setVisible(false)
            gd_marca.setVisible(false)
            lb_info_add.setVisible(false)
        }
        else  {


            gd_lote.setVisible(true)

        }

    }
    @Listen("onClick = button#bt_add_unidade")
    public  void openGridUnidade (){

        dv_salvar.setVisible(true)
        dv_delite.setVisible(false)

        if(gd_unidade.visible){
            gd_unidade.setVisible(false)
            gd_modelo.setVisible(false)
            gd_produto.setVisible(false)
            lb_info_add.setVisible(false)
        }
        else  {
            lb_info_add.setVisible(true)
            lb_info_add.value="Nova Unidade"
            gd_unidade.setVisible(true)
            gd_modelo.setVisible(false)
            gd_produto.setVisible(false)


        }

    }


    @Listen("onDoubleClick = listbox#lb_lotes")
    public  void editarLote (){
        cb_modelos.selectedItem=null
        cb_unidades.selectedItem=null
        cb_produtos.selectedItem=null
        dv_delite.setVisible(true)
        dv_salvar.setVisible(false)
        Lote selectedL = Lote.findByCodigo(lb_lotes.selectedItem.value.toString())
        def lotesDd = Lote.all
        //  def lote = Lote.first(sort:lb_lotes.getSelectedItem().getValue().toString() )

        for (Lote lote in lotesDd ){
            gd_lote.setVisible(true)

            if (lote.equals(selectedL)){
                selectedLote = lote
                gd_lote.setVisible(true)
                tb_codigo_lote.setValue(lote.codigo)
                db_validade.setValue(lote.validade)

                for (Comboitem comboitem in cb_modelos.items){
                    if (comboitem.value.equals(lote.modelo?.descricao)){
                        cb_modelos.setSelectedItem(comboitem)
                    }
                }
                for (Comboitem comboitem in cb_unidades.items){
                    if (comboitem.value.equals(lote.unidade?.nome)){
                        cb_unidades.setSelectedItem(comboitem)
                    }
                }
                for (Comboitem comboitem in cb_produtos.items){
                    if (comboitem.value.equals(lote.produto?.nome)){
                        cb_produtos.setSelectedItem(comboitem)
                    }
                }
            }
        }
        gd_lote.focus()
    }

    @Listen("onClick = button#bt_update")
    public void updateLote(){


        try {
            Lote lote = Lote.find(selectedLote)
            if(!lote.equals(null)){
                if (!tb_codigo_lote.value.equals(lote.codigo)) {
                    if(Lote.findAllByCodigo(tb_codigo_lote.value)){
                        Messagebox.show("O valor de codigo deve ser unico!", "Lua", 1,  Messagebox.ERROR)
                        return
                    }
                }
                lote.codigo = tb_codigo_lote?.value
                lote.validade =db_validade?.value
                def produto = Produto.findByDescricao(cb_produtos.selectedItem.value?.toString())
                if (cb_modelos.selectedItem?.value){
                    def modelo = Modelo.findByDescricao(cb_modelos.selectedItem.value?.toString())
                    lote.modelo=modelo
                }

                if (cb_unidades.selectedItem?.value){
                    def unidade = Unidade.findByNome(cb_unidades.selectedItem.value?.toString())
                    lote.unidade=unidade
                }
                lote.produto=produto
                lote.save flush: true
                Messagebox.show(lote.codigo+" Actualizado com sucesso!", "Lua", 1,  Messagebox.INFORMATION)
                renderLBLotes()
            }
        } catch ( Exception ex) {
            Messagebox.show("Erro! "+ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }
    @Listen("onClick = button#bt_delite")
    public  void delite(){
        Lote lote = Lote.find(selectedLote)
        lote.delete flush: true
        renderLBLotes()
        Messagebox.show(lote.codigo+" Eliminado!", "Lua", 1,  Messagebox.INFORMATION)

    }

    @Listen("onClick = button#bt_add_produto")
    public  void openProdutoGd(){
        gd_modelo.setVisible(false)
        gd_marca.setVisible(false)
        gd_unidade.setVisible(false)
        if (gd_produto.visible){
            gd_produto.setVisible(false)

            lb_info_add.setVisible(false)
        }else {
            gd_modelo.setVisible(false)
            gd_produto.setVisible(true)
            lb_info_add.setVisible(true)
            lb_info_add.value="Novo Produto"
        }

    }

    @Listen("onClick = button#bt_add_modelo")
    public  void openGdModelo(){
        gd_produto.setVisible(false)
        gd_marca.setVisible(false)
        gd_unidade.setVisible(false)
        if (!gd_modelo.visible){
            gd_modelo.setVisible(true)
            lb_info_add.setVisible(true)
        }else {

            gd_modelo.setVisible(false)
            lb_info_add.setVisible(false)
            lb_info_add.value="Novo Modelo"
        }

    }


    @Listen("onClick = button#bt_salvar_produto")
    public void salvarProduto() {
        try {
            Produto produto = new Produto()
            produto.codigo = tb_codigo_produto?.value
            produto.nome = tb_nome_produto?.value
            produto.descricao = tb_descricao_produto?.value

            if (Produto.findAllByCodigo(tb_codigo_produto.value)) {
                Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
                return
            }
            produto.save flush: true
            Messagebox.show(produto.nome + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)
            renderCb_produtos()
           openProdutoGd()

        }


        catch (Exception ex) {
            Messagebox.show("Erro! "+ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }

    @Listen("onClick = button#bt_salvar_unidade")
    public void salvarUnidade() {
        try {
            Unidade unidade = new Unidade()
            unidade.codigo = tb_codigo_unidade?.value
            unidade.nome = tb_nome_unidade?.value
            unidade.numeroDeElementos = ib_qtd_unidade?.value

            if (Unidade.findAllByCodigo(tb_codigo_unidade.value)) {
                Messagebox.show("o valor do codigo ja existe!", "Lua", 1, Messagebox.ERROR)
                return
            }
            unidade.save flush: true
            Messagebox.show(unidade.nome + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)
            renderCb_unidades()


        }


        catch (Exception ex) {
            Messagebox.show("Erro! "+ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }

    @Listen("onClick = button#bt_add_modelo")
    public  void openModeloGd(){
            gd_produto.setVisible(false)
        gd_marca.setVisible(false)
        if (gd_modelo.visible){
            gd_modelo.setVisible(true)
            lb_info_add.setVisible(true)
            lb_info_add.value="Novo Modelo"
        }else {
            gd_modelo.setVisible(false)
            lb_info_add.setVisible(false)

        }

    }

    @Listen("onClick = button#bt_add_marca")
    public  void openMarcaGd(){
            gd_modelo.setVisible(false)
        gd_produto.setVisible(false)
        if (gd_marca.visible){
            gd_marca.setVisible(false)
            lb_info_add.setVisible(false)
        }else {
            gd_marca.setVisible(true)
            lb_info_add.setVisible(true)
            lb_info_add.value="Nova Marca"
        }

    }

    @Listen("onClick = button#bt_salvar_modelo")
    public void salvarModelo() {
        try {
            Modelo modelo = new Modelo()
            modelo.descricao = tb_descricao_modelo?.value
            def marcas = Marca.findAllByNome(cb_marcas_modelo.selectedItem?.value.toString())
            Marca marca = Marca.get(marcas.id)
            modelo.marca =marca

            if (Modelo.findAllByDescricao(tb_descricao_modelo.value)) {
                Messagebox.show("Este modelo ja existe!", "Lua", 1, Messagebox.ERROR)
                return
            }
            modelo.save flush: true
            Messagebox.show(modelo.descricao + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)
            renderCb_modelos()
            //openProdutoGd()

        }


        catch (Exception ex) {
            Messagebox.show("Erro! "+ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }

    @Listen("onClick = button#bt_salvar_marca")
    public void salvarMarca() {
        try {
            Marca marca = new Marca()
            marca.setNome(tb_nome_marca.value.toString())

            if (Marca.findAllByNome(tb_nome_marca.value)) {
                Messagebox.show("Esta marca ja existe!", "Lua", 1, Messagebox.ERROR)
                gd_modelo.setVisible(true)
                gd_marca.setVisible(false)
                lb_info_add.value="Novo Modelo"
                return
            }
            marca.save flush: true
            Messagebox.show(marca.nome + " Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)
            renderCb_marcas()
            renderCb_marcas_modelo()
            gd_modelo.setVisible(true)
            gd_marca.setVisible(false)
            lb_info_add.value="Novo Modelo"
            //openProdutoGd()

        }


        catch (Exception ex) {
            Messagebox.show("Erro! "+ex.toString(), "Lua", 1, Messagebox.INFORMATION)
        }

    }


    @Listen("onOK = textbox#tb_filtro_codigo")
    public void filtrarLb(){
        lotesListFilter()
        renderLBLotes()
    }

}
