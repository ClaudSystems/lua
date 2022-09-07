package produto

import base.EncryptionService
import estoque.EntradaDeProdutoService
import estoque.ProdutoService
import grails.transaction.Transactional
import lua.entidades.fornecedor.Fornecedor
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.produto.Produto
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Checkbox
import org.zkoss.zul.Combobox
import org.zkoss.zul.Doublebox
import org.zkoss.zul.Intbox
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listitem
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Textbox
import javax.swing.ListModel

@Transactional
class ProdutoFilterViewModel {
    EncryptionService encryptionService
    static boolean firstItemDone = false
    static id_item = 1
    String message
    String style
    @Wire
    Listbox lb_filter
    @Wire
    Combobox cb_fornecedores

    @Wire
    Listbox lb_items
    @Wire
    Textbox tb_cod
    @Wire
    Textbox tb_desc
    @Wire
    Intbox  ib_qt
    @Wire
    Doublebox  db_vu
    @Wire
    Checkbox cb_ck
    private  ListModel<Listitem> itemsDaEncomenda

    Item pickedItem
    ProdutoService produtoService
    EntradaDeProdutoService entradaDeProdutoService



    private String filter;
    private String fatura
    private ListModelList<Item> items
    private ArrayList<ItemProdutoComponent> itemsProdutoComponet = new ArrayList()
    private ListModelList<Produto> selectedItems

    public class Item {
        private String codigo
        private String nome
        private String descricao
        private String saldo

        public Item(String codigo, String nome, String descricao, String saldo) {
            this.codigo = codigo
            this.nome = nome
            this.descricao = descricao
            this.saldo = saldo


        }


        public String getDescricao() {
            return descricao
        }

        public String getCodigo() {
            return codigo
        }

        public String getNome() {
            return nome
        }

        public String getSaldo() {
            return saldo
        }
    }
    public class ItemProdutoComponent {
        private Textbox textbox_codigo
        private Textbox textbox_descricao
        private Intbox intbox_quantidade
        private Doublebox doublebox_valor

        public ItemProdutoComponent(Textbox codigo, Textbox descricao, Intbox quantidade, Doublebox valor) {
            this.textbox_codigo = codigo
            this.textbox_descricao = descricao
            this.intbox_quantidade = quantidade
            this.doublebox_valor = valor
        }


    }

    public String getFilter() {
        return filter
    }
    public String getFactura() {
        return fatura
    }

    public String getProduto() {
        return produto
    }

    public ListModelList<Item> getItems() {
        if (items == null) {
            items = new ListModelList<Item>(getAllItems())
        }
        return items;
    }

    public ListModelList<Item> getSelectedItems() {

        return selecteItems
    }

    @NotifyChange
    public void setFilter(String filter) {
        this.filter = filter
    }
    @NotifyChange
    public void setFactura(String fatura) {
        this.fatura = fatura
    }


    @NotifyChange("items")
    @Command
    public void doSearch() {

        items.clear();
        List<Item> allItems = getAllItems()
        if (filter == null || "".equals(filter)) {
            items.addAll(allItems)
        } else {
            for (Item item : allItems) {
                if (item.getDescricao().toLowerCase().indexOf(filter.toLowerCase()) >= 0 || item.getCodigo().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item.getNome().toLowerCase().indexOf(filter.toLowerCase()) >= 0) {
                    items.add(item)
                }
            }
        }
    }

    private List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>()
        def produtos = Produto.all
        for (Produto p in produtos) {
            items.add(new Item(p.codigo, p.nome, p.descricao, p.saldo + ""))

        }

        return items
    }


    @NotifyChange("lb_filter")
    @Command
    public void addSelectedtItem() {
        if (!firstItemDone){
           addFirstItem()
            return
        }
            Textbox tb_codigo = new Textbox()
            tb_codigo.value = pickedItem?.codigo
            Textbox tb_descricao = new Textbox()
            tb_descricao.value = pickedItem.descricao
            tb_descricao.width = "100%"
            Intbox ib_qtd = new Intbox()
            ib_qtd.setConstraint("no negative , no empty")
            Doublebox db_valor = new Doublebox()
            db_valor.width = "100%"
            ItemProdutoComponent ipc = new ItemProdutoComponent(tb_codigo,tb_descricao,ib_qtd,db_valor)


        ipc.textbox_codigo.value = pickedItem.codigo
        ipc.textbox_descricao.value = pickedItem.descricao
        Listitem listitem = new Listitem()
        Listcell listcell_codigo = new Listcell()
        listcell_codigo.appendChild(ipc.textbox_codigo)
        Listcell listcell_descricao = new Listcell()
        listcell_descricao.appendChild(ipc.textbox_descricao)
        Listcell listcell_quantidade = new Listcell()
        listcell_quantidade.appendChild(ipc.intbox_quantidade)
        Listcell listcell_valor = new Listcell()
        listcell_valor.appendChild(ipc.doublebox_valor)
        listitem.appendChild(listcell_codigo)
        listitem.appendChild(listcell_descricao)
        listitem.appendChild(listcell_quantidade)
        listitem.appendChild(listcell_valor)
        itemsProdutoComponet.add(ipc)
        lb_items.appendChild(listitem)
    }

    @Command
    public static void close() {
        Executions.sendRedirect("/home")

    }


    @Command
    public void changeSyte() {
        pickedItem.style = "color:red"
    }

    @Command
    public void addItem() {
        if(!itemsProdutoComponet.empty){
           String codigo = itemsProdutoComponet.last().textbox_codigo.value
            Produto produto = Produto.findByCodigo(codigo)
            if (produto) {
                itemsProdutoComponet.last().textbox_descricao.value = produto.descricao

            } else {Messagebox.show("codigo invalido!", "Lua", 1, Messagebox.ERROR)
                    itemsProdutoComponet.last().textbox_codigo.focus()
                return
            }

        }

        if(!itemsProdutoComponet.empty){
            itemsProdutoComponet.last().intbox_quantidade.setConstraint("no negative,no empty")
        }
                    1.upto(1, {
                Textbox textbox_codigo = new Textbox()

                textbox_codigo.addEventListener("onChange", new MyListener())
                Textbox textbox_descricao = new Textbox()
                textbox_descricao.width = "100%"
                Intbox intbox_quantidade = new Intbox()

                Doublebox doublebox_valor = new Doublebox()
                doublebox_valor.width = "100%"
            ItemProdutoComponent itemProduto = new ItemProdutoComponent(textbox_codigo,textbox_descricao,intbox_quantidade,doublebox_valor)
            Listitem listitem = new Listitem()
                Listcell listcell_codigo = new Listcell()
                listcell_codigo.appendChild(itemProduto.textbox_codigo)
                Listcell listcell_descricao = new Listcell()
                listcell_descricao.appendChild(itemProduto.textbox_descricao)
                Listcell listcell_quantidade = new Listcell()
                listcell_quantidade.appendChild(itemProduto.intbox_quantidade)
                Listcell listcell_valor = new Listcell()
                listcell_valor.appendChild(itemProduto.doublebox_valor)
                listitem.appendChild(listcell_codigo)
                listitem.appendChild(listcell_descricao)
                listitem.appendChild(listcell_quantidade)
                listitem.appendChild(listcell_valor)
                itemsProdutoComponet.add(itemProduto)
                lb_items.appendChild(listitem)
        })

    }

    @Command
    void addFirstItem(){
        def produto = Produto.findByCodigo(tb_cod?.value)
        def produtoS = Produto.findByCodigo(pickedItem?.codigo)

        if (produto) {
            tb_desc.value = produto.descricao
            ItemProdutoComponent itemProdutoComponent =  new ItemProdutoComponent(tb_cod,tb_desc,ib_qt,db_vu)
            itemsProdutoComponet.add(itemProdutoComponent)
            firstItemDone=true
            addItem()
        }else if (produtoS){
            tb_cod.value = produtoS.codigo
            tb_cod.addEventListener("onChange",new SearchListener())
            tb_desc.value = produtoS.descricao
            ItemProdutoComponent itemProdutoComponent =  new ItemProdutoComponent(tb_cod,tb_desc,ib_qt,db_vu)
            itemsProdutoComponet.add(itemProdutoComponent)
            firstItemDone=true
            addItem()
        } else {Messagebox.show("codigo invalido!", "Lua", 1, Messagebox.ERROR)
            tb_cod.focus()
        }
    }
    void addItemsAEncomenda(Listitem listitem){
        createFirstItem()

    }

   public class MyListener implements org.zkoss.zk.ui.event.EventListener {
        @Override
        void onEvent(Event event) throws Exception {
            def produto = Produto.findByCodigo(tb_cod?.value) as Produto
            if (!produto.equals(null)) {
                tb_desc.value = produto.descricao
                addItem()
            } else {Messagebox.show("codigo invalido!", "Lua", 1, Messagebox.ERROR)}


        }
    }
    public class SearchListener implements org.zkoss.zk.ui.event.EventListener {
        @Override
        void onEvent(Event event) throws Exception {
            doSearch()


        }
    }


    @Command
    saveItemsProduto (){
        try {
       EntradaDeProduto edp = new EntradaDeProduto()
        edp.utilizador = encryptionService.localUser

       if(itemsProdutoComponet.empty){
          return   Messagebox.show("Nenhum produto foi selecioonado!", "Lua", 1, Messagebox.ERROR)
       }


            for (int x = 0; x <= itemsProdutoComponet.size(); x++) {

                ItemProduto ip = new  ItemProduto()
                String codigo = itemsProdutoComponet[x]?.textbox_codigo?.value
                 Produto produto = Produto.findByCodigo(codigo)
                if(!produto.equals(null)){

                    ip.produto = produto
                    ip.produto.descricao=itemsProdutoComponet[x].textbox_descricao.value
                    ip.qtd = itemsProdutoComponet[x].intbox_quantidade.value
                    ip.preco=itemsProdutoComponet[x].doublebox_valor.value
                    edp.addToItemsProduto(ip)
                    produtoService.addItensProdutos(ip)

                }

            }
        if (edp == null) {
            notFound()
            return
        }

        if (edp.hasErrors()) {
            return
        }

            edp.save flush: true

       Messagebox.show("Nota de entrada Criado com suesso!", "Lua", 1, Messagebox.INFORMATION)

            entradaDeProdutoService.entradaDeProduto = edp
            entradaDeProdutoService.myUrl = "/produto/produtoFilter/entrada"
            Executions.sendRedirect("/entradaDeProduto/show.gsp")
        }catch (Exception e){

            Messagebox.show("ERRO!"+e.toString(), "Lua", 1, Messagebox.ERROR)
        }
    }

    @Command
    def renderCb_fornecedores() {
        def fornecedores = Fornecedor.all
        List list_fornecedores = new ArrayList()
        for (Fornecedor f : fornecedores) {
            list_fornecedores.add(f.nome)
        }
        ListModelList lm_fornecedores = new ListModelList(list_fornecedores)
        cb_fornecedores.setModel(lm_fornecedores)
    }



}
