package cotacao

import base.ComposersService

import lua.vendas.cotacao.Cotacao
import lua.BasicController

import lua.estoque.itemProduto.ItemProduto
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

import java.text.DecimalFormat


class ShowCotacaoComposer extends zk.grails.Composer {
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
    Cotacao cotacao = new Cotacao()
    Listbox lb_items
    Listfooter lf_total
    Grid gd_cotacao
    Grid gd_fornecedor = new Grid()
    @Wire
    Div vd_cliente




    def afterCompose = { window ->
          cotacao  = composersService.cotacao
        renderGridCotacao()
       }

    def renderGridCotacao(){
        String pattern = "###,###.00#"
        DecimalFormat df = new DecimalFormat(pattern)
        df.setMaximumFractionDigits(2)

        renderLBItemsProdutos()
        lb_nome.value = cotacao.cliente?.nome
        lb_id.value = cotacao.id


        Label label = new Label(df.format(cotacao.valor)+" MT")

        lf_total.appendChild(label)


    }

    def renderLBItemsProdutos () {
        itemsList()
        lb_items.getItems().clear()
        lb_items.setItemRenderer(new ListitemRenderer<ItemProduto>() {

            @Override
            void render(Listitem item, ItemProduto ip, int i) throws Exception {
                String pattern = "###,###.00#"
                DecimalFormat df = new DecimalFormat(pattern)
                df.setMaximumFractionDigits(2)

                new Listcell(ip.produto.codigo).setParent(item)
                new Listcell(ip.descricao).setParent(item)
                new Listcell(ip.qtd.toString()).setParent(item)
                new Listcell(df.format(ip.precoDeVenda.setScale(2,BigDecimal.ROUND_DOWN))+"").setParent(item)
                new Listcell(df.format(ip.qtd*ip.precoDeVenda.setScale(2,BigDecimal.ROUND_DOWN))+"").setParent(item)

                if (!ip.preco.equals(null)){
                    ip.preco.setScale(2,BigDecimal.ROUND_DOWN)
                    ip.preco.floatValue()
                    new Listcell(df.format(ip.preco.setScale(2,BigDecimal.ROUND_DOWN)).toString()).setParent(item)
                }
                else  new Listcell("").setParent(item)
                if(!ip.preco.equals(null)){

                    new Listcell(df.format(ip.preco.setScale(2,BigDecimal.ROUND_DOWN)*ip.qtd)+"").setParent(item)
                }else {
                    new Listcell("").setParent(item)
                }



                item.setValue(ip)


            }


        })
    }

    /*def getTotal(){
        def items = cotacao.itemsProduto

        BigDecimal total  = 0.00

        for (ItemProduto ip in items){
            if(!ip.preco.equals(null))
                total += ip.preco.setScale(2,BigDecimal.ROUND_DOWN)*ip.qtd


        }
        return total
    }*/

    def itemsList(){
        def itemsProdutos = new ArrayList<ItemProduto>()
        def items = cotacao.itemsProduto

        for(ItemProduto ip in items) {
            itemsProdutos.add(ip)
        }
        ListModelList lml = new ListModelList(itemsProdutos,true)
        lb_items.setModel(lml)
    }

    @Listen("onClick = button#bt_close")
    def close() {
        Executions.sendRedirect("/cotacao/newCotacao.gsp")

    }

    @Listen("onClick = button#bt_print")
    def static Imprimir(){
        Executions.sendRedirect("/cotacao/imprimir")

       /* def id = composersService.cotacao.id
        System.print(id)
        String reportName = ''
        reportName = "/cotacao"
        redirect(controller:CotacaoController,action:imprimir, params: [reportName:reportName,cotacaoId:id])*/

    }
}
