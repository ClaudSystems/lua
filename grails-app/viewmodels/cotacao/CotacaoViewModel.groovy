package cotacao

import base.ComposersService
import base.EncryptionService
import estoque.ProdutoService
import grails.transaction.Transactional
import lua.entidades.cliente.Cliente
import lua.estoque.estoque.Armazem
import lua.vendas.cotacao.Cotacao
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.produto.Produto
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Combobox
import org.zkoss.zul.Doublebox
import org.zkoss.zul.Intbox
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listitem
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Textbox
import org.zkoss.zul.Window

import javax.swing.ListModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


@Transactional
class CotacaoViewModel {
    EncryptionService encryptionService
    Cotacao cotacao = new Cotacao()
    Cliente selectedCliente
    BigDecimal subtotal = 0
      static boolean firstItemDone = false
      static boolean editarCotacao = false
    static id_item = 1
    String message
    String style
    @Wire    Listbox lb_filter
    @Wire    Window win
    @Wire    Combobox cb_clientes
    @Wire    Listbox lb_items
    @Wire    Textbox tb_cod
    @Wire    Textbox tb_desc
    @Wire    Intbox ib_qt
    @Wire    Doublebox db_vu
    @Wire    Label lb_sub_total
    @Wire    Label lb_st
    @Wire    Label lb_iva
    @Wire    Label lb_total


    private ListModel<Listitem> itemsDaEncomenda

    Item pickedItem
    ProdutoService produtoService
    ComposersService composersService


    private String filter
    private String fatura
    private ListModelList<Item> items
    private ArrayList<ItemProdutoComponent> itemsProdutoComponet = new ArrayList()
    private ListModelList<Produto> selectedItems

    @Command
    @NotifyChange("selectedCliente")
    def renderSelectedCliente(){
        selectedCliente=Cliente.findByNome(cb_clientes.selectedItem.value.toString())
        System.println("renderSelectedCliente: "+selectedCliente.nome)
    }

    Cliente getSelectedCliente() {
        return selectedCliente
    }

    void setSelectedCliente(Cliente selectedCliente) {
        this.selectedCliente = selectedCliente
    }

    @Init
    init() {
        if(!composersService.cotacao.equals(null)){
            cotacao = Cotacao.find(composersService.cotacao)
            System.println("init"+cotacao.id)
        }

    }




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
        this.filter = filter;
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
            items.addAll(allItems);
        } else {
            for (Item item : allItems) {
                if (item.getDescricao().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item.getCodigo().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item.getId().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item.getNome().toLowerCase().indexOf(filter.toLowerCase()) >= 0) {
                    items.add(item)
                }
            }
        }
    }

    private List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>();
        def produtos = Produto.all
        for (Produto p in produtos) {
            items.add(new Item(p.codigo, p.nome, p.descricao, p.saldo + ""))

        }

        return items;
    }


    @NotifyChange("lb_filter")
    @Command
    public void addSelectedtItem() {
        if (!firstItemDone) {
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
        ItemProdutoComponent ipc = new ItemProdutoComponent(tb_codigo, tb_descricao, ib_qtd, db_valor)


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
        Executions.sendRedirect("/cotacao/list")

    }



    @Command
    public void addItem() {
        if (!itemsProdutoComponet.empty) {
            String codigo = itemsProdutoComponet.last().textbox_codigo.value
            Produto produto = Produto.findByCodigo(codigo)
            if (produto) {
                itemsProdutoComponet.last().textbox_descricao.value = produto.descricao

            } else {
                Messagebox.show("codigo invalido!", "Lua", 1, Messagebox.ERROR)
                itemsProdutoComponet.last().textbox_codigo.focus()
                return
            }

        }

        if (!itemsProdutoComponet.empty) {
            itemsProdutoComponet.last().intbox_quantidade.setConstraint("no negative,no empty,no zero")
        }
        1.upto(1, {
            Listitem listitem = new Listitem()
            Textbox textbox_codigo = new Textbox()
            Label lb_st = new Label()
            textbox_codigo.addEventListener("onChange", new MyListener())
            Textbox textbox_descricao = new Textbox()
            textbox_descricao.width = "100%"
            Intbox intbox_quantidade = new Intbox()

            Doublebox doublebox_valor = new Doublebox()

             doublebox_valor.width = "100%"
          //  doublebox_valor.setConstraint("no negative,no empty")
            doublebox_valor.setFormat("#,##0.##")
            doublebox_valor.addEventListener("onChange", new STCalculatorListener(listitem,doublebox_valor,intbox_quantidade))
            ItemProdutoComponent itemProduto = new ItemProdutoComponent(textbox_codigo, textbox_descricao, intbox_quantidade, doublebox_valor)

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
    void addFirstItem() {

        def produto = Produto.findByCodigo(tb_cod?.value)
        def produtoS = Produto.findByCodigo(pickedItem?.codigo)

        if (produto) {
            tb_desc.value = produto.descricao
            ItemProdutoComponent itemProdutoComponent = new ItemProdutoComponent(tb_cod, tb_desc, ib_qt, db_vu)
            itemsProdutoComponet.add(itemProdutoComponent)
            firstItemDone = true
            addItem()
        } else if (produtoS) {
            tb_cod.value = produtoS.codigo
            tb_cod.addEventListener("onChange", new SearchListener())
            tb_desc.value = produtoS.descricao
            ItemProdutoComponent itemProdutoComponent = new ItemProdutoComponent(tb_cod, tb_desc, ib_qt, db_vu)
            itemsProdutoComponet.add(itemProdutoComponent)
            firstItemDone = true
            addItem()
        } else {
            Messagebox.show("codigo invalido!", "Lua", 1, Messagebox.ERROR)
            tb_cod.focus()
        }
    }

    void addItemsAEncomenda(Listitem listitem) {
        createFirstItem()

    }

    public class MyListener implements org.zkoss.zk.ui.event.EventListener {
        @Override
        void onEvent(Event event) throws Exception {
            def produto = Produto.findByCodigo(tb_cod?.value) as Produto
            if (!produto.equals(null)) {
                tb_desc.value = produto.descricao
                addItem()
            } else {
                Messagebox.show("codigo invalido!", "Lua", 1, Messagebox.ERROR)
            }


        }
    }

    public class STCalculatorListener implements org.zkoss.zk.ui.event.EventListener {
        Doublebox db
        Intbox ib
      Listitem li
        STCalculatorListener(Listitem li, Doublebox db, Intbox ib){
            this.li= li
            this.ib = ib
            this.db = db
        }

        @Override
        void onEvent(Event event) throws Exception {
            String pattern = "####,####.00#"
            DecimalFormat df = new DecimalFormat(pattern)
            df.setMaximumFractionDigits(2)



           BigDecimal value = (db.value*ib.value)
            value.setScale(2,BigDecimal.ROUND_HALF_DOWN)
            subtotal+=value
           Listcell lc = new Listcell()
            Label label = new Label()
            label.value=df.format(value.setScale(2,BigDecimal.ROUND_HALF_DOWN))
            lb_st.value= df.format((subtotal).setScale(2,BigDecimal.ROUND_HALF_DOWN))

            lb_iva.value=df.format( (subtotal*0.17).setScale(2,BigDecimal.ROUND_HALF_DOWN))

            lb_total.value= df.format((subtotal*1.17).setScale(2,BigDecimal.ROUND_HALF_DOWN))
            lc.appendChild(label)
        li.appendChild(lc)


        }
    }

    public class SearchListener implements org.zkoss.zk.ui.event.EventListener {
        @Override
        void onEvent(Event event) throws Exception {
            doSearch()


        }
    }


    @Command
    saveItemsProduto() {

        if (itemsProdutoComponet.empty) {
            return Messagebox.show("Nenhum produto foi selecioonado!", "Lua", 1, Messagebox.ERROR)
        }
        if (!cb_clientes.selectedItem) {
            return Messagebox.show("Nenhum cliente foi selecioonado!", "Lua", 1, Messagebox.ERROR)
        }

        try {


            cotacao.utilizador = encryptionService.localUser
            cotacao.cliente = Cliente.findByNome(cb_clientes.selectedItem.value.toString())
            BigDecimal valor = 0
            for (int x = 0; x <= itemsProdutoComponet.size(); x++) {

                ItemProduto ip = new ItemProduto()
                String codigo = itemsProdutoComponet[x]?.textbox_codigo?.value
                Produto produto = Produto.findByCodigo(codigo)
                if (!produto.equals(null)) {
                    ip.armazem = Armazem.findById(1)
                    ip.produto = produto
                    ip.descricao = itemsProdutoComponet[x].textbox_descricao.value
                    ip.qtd = itemsProdutoComponet[x].intbox_quantidade.value
                    ip.precoDeVenda = itemsProdutoComponet[x].doublebox_valor.value.toBigDecimal()
                    cotacao.addToItemsProduto(ip)
                    /*produtoService.addItensProdutos(ip)*/
                    valor += ip.precoDeVenda * ip.qtd
                    ip.save()


                }

            }
            cotacao.setValor(valor)
            cotacao.save(flush: true)



            composersService.cotacao = null
            composersService.cotacao = cotacao
            Cotacao cotacaoDB = Cotacao.find(cotacao)
            if (cotacaoDB.equals(null)) {
                Messagebox.show("Erro na gravacao!", "Lua", 1, Messagebox.ERROR)
                win.detach()
                Executions.sendRedirect("/cotacao/cotacao.gsp")
                return
            }
            composersService.setCotacao(cotacaoDB)
            Executions.sendRedirect("/cotacao/showCotacao.gsp")
            win.detach()
        } catch (Exception e) {

            Messagebox.show("ERRO!" + e.toString(), "Lua", 1, Messagebox.ERROR)
        }
    }

    @Command
    def renderCb_clientes() {
        def clientes = Cliente.all
        List list_clientes = new ArrayList()
        for (Cliente c : clientes) {
            list_clientes.add(c.nome)
        }
        ListModelList lm_clientes = new ListModelList(list_clientes)
        cb_clientes.setModel(lm_clientes)
     }
    @Command
    def calculateSB() {
        String pattern = "####,####.00#"
        DecimalFormat df = new DecimalFormat(pattern)
        df.setMaximumFractionDigits(2)


        while (db_vu.value.equals(null)||ib_qt.value.equals(null)){
            return
        }
        subtotal = db_vu?.value * ib_qt?.value
        lb_sub_total.value = df.format(subtotal.setScale(2,BigDecimal.ROUND_HALF_DOWN))
        lb_st.value=df.format(subtotal.setScale(2,BigDecimal.ROUND_HALF_DOWN))
        lb_iva.value =df.format((subtotal*0.17).setScale(2,BigDecimal.ROUND_HALF_DOWN))
        lb_total.value= df.format( (subtotal*0.17+subtotal).setScale(2,BigDecimal.ROUND_HALF_DOWN))
    }
}