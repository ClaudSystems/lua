package entradadeproduto

import base.ComposersService
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.entradaDeProduto.EntradaDeProduto
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Button
import org.zkoss.zul.Div
import org.zkoss.zul.Grid
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listfooter
import org.zkoss.zul.Listitem
import org.zkoss.zul.ListitemRenderer


class ShowEntradaDeProdutoComposer extends zk.grails.Composer {
    ComposersService composersService
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
    EntradaDeProduto entradaDeProduto = new EntradaDeProduto()
    Listbox lb_items
    Listfooter lf_total
    Grid gd_entrada_de_produto
    Grid gd_fornecedor = new Grid()
    @Wire
    Div vd_cliente




    def afterCompose = { window ->
        entradaDeProduto  = composersService.entradaDeProduto
        renderGridEntradaDeProduto()
    }

    def renderGridEntradaDeProduto(){
        renderLBItemsProdutos()
        lb_nome.value = entradaDeProduto.fornecedor?.nome
        lb_id.value = entradaDeProduto.id


        Label label = new Label(entradaDeProduto.valor+" MT")

        lf_total.appendChild(label)


    }

    def renderLBItemsProdutos () {
        itemsList()
        lb_items.getItems().clear()
        lb_items.setItemRenderer(new ListitemRenderer<ItemProduto>() {

            @Override
            void render(Listitem item, ItemProduto ip, int i) throws Exception {
                new Listcell(ip.produto.codigo).setParent(item)
                new Listcell(ip.descricao).setParent(item)
                new Listcell(ip.qtd.toString()).setParent(item)
                new Listcell(ip.precoDeVenda.setScale(2,BigDecimal.ROUND_DOWN)+"").setParent(item)
                new Listcell(ip.qtd*ip.precoDeVenda.setScale(2,BigDecimal.ROUND_DOWN)+"").setParent(item)

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


    def itemsList(){
        def itemsProdutos = new ArrayList<ItemProduto>()
        def items = entradaDeProduto.itemsProduto

        for(ItemProduto ip in items) {
            itemsProdutos.add(ip)
        }
        ListModelList lml = new ListModelList(itemsProdutos,true)
        lb_items.setModel(lml)
    }

    @Listen("onClick = button#bt_close")
    public void close() {
        Executions.sendRedirect("/entradaDeProduto/entradaDeProduto.gsp")

    }

    @Listen("onClick = button#bt_print")
    def Imprimir(){
        String reportName = '/entradaDeProduto'
        redirect(action: printReport, params: [reportName:reportName,entradaDeProdutoId:entradaDeProduto.id])

    }
}
