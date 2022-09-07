package relatorios

import lua.estoque.RelatoriosController
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Execution
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Datebox
import lua.estoque.RelatoriosController
import base.ComposersService
class EntradasDeProdutoViewModel {
    ComposersService composersService
@Wire Datebox db_inicio
@Wire Datebox db_fim

    @Command
    def  print(){
        if (!(db_fim.value.equals(null)||db_inicio.value.equals(null))){
            System.println(db_inicio.value)
            System.println(db_fim.value)
            composersService.relatorio_entradas_data_inicio = db_inicio.value
            composersService.relatorio_entradas_data_fim = db_fim.value
            Executions.sendRedirect("/relatorios/imprimir")
        }

    }


}
