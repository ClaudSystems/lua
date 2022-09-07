package home

import grails.transaction.Transactional
import lua.estoque.produto.Produto
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList

@Transactional
class IndexViewModel {
    private ListModelList<Produto> produtos
    int size
    int saldoMin = 10
    String lbSaldoMin = ""
    String getLbSaldoMin() {
        return "Saldo<"+saldoMin
    }
    Wire info
    int getSaldoMin() {
        return saldoMin
    }

    void setSaldoMin(int saldoMin) {
        this.saldoMin = saldoMin
    }

    @Command
    @NotifyChange(["produtos","saldoMin","size","lbSaldoMin"])
    ListModelList<Produto> getProdutos() {

        if(produtos == null){
            produtos = new ListModelList<>(Produto.findAllBySaldoLessThan(saldoMin))
        }
        return produtos

    }

    int getSize() {
        return produtos.size()
    }



    @NotifyChange(["produtos","saldoMin","size"])
    @Command
    public void doSearch() {

        produtos.clear()
        List<Produto> allItems = Produto.all
        if (saldoMin == 0 || "".equals(saldoMin.toString())) {
            items.addAll(allItems)
        } else {
            for (Produto produto : allItems) {
                if ( produto.saldo<=saldoMin
                ) {
                    produtos.add(produto)
                }
            }
        }
    }

    @Command
    def showNovaCotacao(){
        Executions.sendRedirect("/cotacao/newCotacao")
    }
    @Command
    def showNovaFatura(){
        Executions.sendRedirect("/fatura/new")
    }

    @Command
    def showClientes(){
        Executions.sendRedirect("/cliente/clientes")

    }

    @Command
    def showCotacoes(){
        Executions.sendRedirect("/cotacao/listCotacao")
    }
    @Command
    def showFaturas(){
        Executions.sendRedirect("/fatura/list")
    }

    @Command
    def showPosRestaurante(){
        Executions.sendRedirect("/pos/pos")
    }

    @Command
    def showVDs(){
        Executions.sendRedirect("/vd/listagemVD")
    }
    @Command
    def showNovaVD(){
        Executions.sendRedirect("/vd/newVd")
    }
    @Command
    def showDiario(){
        Executions.sendRedirect("/diario/diario")
    }
    @Command
    def showEntradas(){
        Executions.sendRedirect("/entradaDeProduto/list")
    }

    @Command
    def showSaidas(){
        Executions.sendRedirect("/saidaDeProduto/saidaList")
    }

    @Command
    def showArmazens(){
        Executions.sendRedirect("/armazem/index")
    }

    @Command
    def showNewEntrada(){
        Executions.sendRedirect("/entradaDeProduto/newEntrada")
    }
    @Command
    def showDestino(){
        Executions.sendRedirect("/destino/index")
    }
    @Command
    def showProduto(){
        Executions.sendRedirect("/produto/index")
    }

    @Command
    def showUnidades (){
        Executions.sendRedirect("/unidade/index")
    }
    @Command
    def showModelos (){
        Executions.sendRedirect("/modelo/index")
    }

    @Command
    def showLotes (){
        Executions.sendRedirect("/lote/index")
    }

    @Command
    def showMesas(){
        Executions.sendRedirect("/mesa/index")
    }

    @Command
    def showClasses(){
        Executions.sendRedirect("/classe/index")
    }

    @Command
    def showCategorias(){
        Executions.sendRedirect("/categoria/categoriaCrud")
    }


}
