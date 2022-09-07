package encomenda

import estoque.EncomendaService
import lua.entidades.fornecedor.Fornecedor
import lua.estoque.encomenda.Encomenda
import lua.estoque.itemProduto.ItemProduto
import org.zkoss.zk.ui.Execution
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Button
import org.zkoss.zul.Column
import org.zkoss.zul.Div
import org.zkoss.zul.Grid
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listfooter
import org.zkoss.zul.Listitem
import org.zkoss.zul.ListitemRenderer


class ShowEncomendaComposer extends zk.grails.Composer {
    EncomendaService encomendaService
    Label lb_nome
    Label lb_nuit
    Label lb_endereco
    Label lb_tef
    Label lb_mail
    Label lb_id
    Label lb_ref_fatura
    Label lb_title
    Button bt_print
    Button bt_close
    @Wire
    Column cl1

    Encomenda selected
    Listbox lb_items
    Listfooter lf_total
    Grid gd_encomenda
    Grid gd_fornecedor = new Grid()
    @Wire
    Div dv_fornecedor




    def afterCompose = { window ->
        dv_fornecedor.setVisible(false)
        Encomenda encomenda = encomendaService.encomenda
        renderGridEncoemnda(encomenda)
        Fornecedor fornecedor = Fornecedor.findById(encomenda?.fornecedor?.id)
        if (fornecedor){
            dv_fornecedor.setVisible(true)

        }
        cl1.label = encomendaService.notaDe

    }

    def renderGridEncoemnda(Encomenda encomenda){

        renderLBItemsProdutos(encomenda)
       lb_nome.value = encomenda.fornecedor?.nome
        selected = encomenda
       lb_id.value = encomenda.id
        lb_ref_fatura.value = encomenda.refFactura
        lb_title.value = encomenda.tipo
        lb_nuit.value = encomenda.fornecedor?.nuit
        lb_mail.value = encomenda.fornecedor?.mail
        lb_endereco.value = encomenda.fornecedor?.endereco
        lb_tef.value=encomenda.fornecedor?.numTelefone

        Label label = new Label(getTotal(encomenda)+" MT")

        lf_total.appendChild(label)


    }

    def renderLBItemsProdutos (Encomenda encomenda) {
        itemsList(encomenda)
        lb_items.getItems().clear()
        lb_items.setItemRenderer(new ListitemRenderer<ItemProduto>() {

            @Override
            void render(Listitem item, ItemProduto ip, int i) throws Exception {
                new Listcell(ip.produto.codigo).setParent(item)
                new Listcell(ip.produto.descricao).setParent(item)
                new Listcell(ip.qtd.toString()).setParent(item)
                if (!ip.preco.equals(null)){
                    ip.preco.setScale(2,BigDecimal.ROUND_DOWN)
                    ip.preco.floatValue()
                    new Listcell(ip.preco.setScale(2,BigDecimal.ROUND_DOWN).toString()).setParent(item)
                }
                else  new Listcell("").setParent(item)
                if(!ip.preco.equals(null)){

                    new Listcell(ip.preco.setScale(2,BigDecimal.ROUND_DOWN)*ip.qtd+"").setParent(item)
                }else {
                    new Listcell("").setParent(item)
                }



                item.setValue(ip)


            }


        })
    }

    def getTotal(Encomenda encomenda){
        def items = encomenda.itemsProduto

        BigDecimal total  = 0.00

        for (ItemProduto ip in items){
            if(!ip.preco.equals(null))
            total += ip.preco.setScale(2,BigDecimal.ROUND_DOWN)*ip.qtd


        }
        return total
    }

    def itemsList(Encomenda encomenda){
        def itemsProdutos = new ArrayList<ItemProduto>()
        def items = encomenda.itemsProduto

        for(ItemProduto ip in items) {

            itemsProdutos.add(ip)

        }
        ListModelList lml = new ListModelList(itemsProdutos,true)
        lb_items.setModel(lml)
    }

    @Listen("onClick = button#bt_close")
    public void close() {
        Executions.sendRedirect(encomendaService.myUrl)

    }

    @Listen("onClick = button#bt_print")
    def Imprimir(){
        String reportName = '/encomenda'
        redirect(action: printReport, params: [reportName:reportName,encomendaId:selected.id,notade:encomendaService.notaDe])

    }
}
