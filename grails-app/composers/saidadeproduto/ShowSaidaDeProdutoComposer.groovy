package saidadeproduto

import base.ComposersService
import lua.estoque.itemProduto.ItemProduto
import lua.estoque.saidaDeProduto.SaidaDeProduto
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


class ShowSaidaDeProdutoComposer extends zk.grails.Composer {

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
    SaidaDeProduto saidaDeProduto = new SaidaDeProduto()
    Listbox lb_items
    Listfooter lf_total
    Grid gd_saidaDeProduto
    Grid gd_fornecedor = new Grid()
    @Wire
    Div vd_cliente

    def afterCompose = { window ->
        saidaDeProduto  = composersService.saidaDeProduto
        lb_id.value = saidaDeProduto.toString()
        renderGridSaidaDeProduto()
    }

    def renderGridSaidaDeProduto(){
        renderLBItemsProdutos()
        lb_nome.value = saidaDeProduto.cliente?.nome



        Label label = new Label(saidaDeProduto.valor+" MT")

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
                if(!ip.precoDeVenda.equals(null)){
                    new Listcell(ip.precoDeVenda.setScale(2,BigDecimal.ROUND_DOWN)+"").setParent(item)
                } else {new Listcell("").setParent(item)}

                 if (!ip.precoDeVenda.equals(null)){
                    ip.precoDeVenda.setScale(2,BigDecimal.ROUND_DOWN)
                    ip.precoDeVenda.floatValue()
                    new Listcell(ip.precoDeVenda.setScale(2,BigDecimal.ROUND_DOWN)*ip.qtd+"").setParent(item)
                }
                else  new Listcell("").setParent(item)




                item.setValue(ip)


            }


        })
    }

    /*def getTotal(){
        def items = saidaDeProduto.itemsProduto

        BigDecimal total  = 0.00

        for (ItemProduto ip in items){
            if(!ip.preco.equals(null))
                total += ip.preco.setScale(2,BigDecimal.ROUND_DOWN)*ip.qtd


        }
        return total
    }*/

    def itemsList(){
        def itemsProdutos = new ArrayList<ItemProduto>()
        def items = saidaDeProduto.itemsProduto

        for(ItemProduto ip in items) {
            itemsProdutos.add(ip)
        }
        ListModelList lml = new ListModelList(itemsProdutos,true)
        lb_items.setModel(lml)
    }

    @Listen("onClick = button#bt_close")
    public void close() {
        Executions.sendRedirect("/saidaDeProduto/saidaDeProduto.gsp")

    }

    @Listen("onClick = button#bt_print")
    def Imprimir(){
        String reportName = '/saidaDeProduto'
        redirect(action: printReport, params: [reportName:reportName,saidaDeProdutoId:saidaDeProduto.id])

    }
}
