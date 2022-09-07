package lua.estoque

import base.ComposersService
import lua.BasicController

/**
 * RelatoriosController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class RelatoriosController extends  BasicController{
        ComposersService composersService

	def estoque (){}
	def vendas (){}
	def iva (){}


    def imprimirEntradas(){
        System.println(params.fim_e.value)
        System.println(params.inicio_e.value)

        String reportName = ''
        reportName = "/entrada_de_produtos"
        redirect(action: printReport, params: [reportExt:params.ext,reportName:reportName,data_inicio:params.inicio_e_value,data_fim:params.fim_e_value])
    }
    def imprimirSaidas(){
        System.println(params.fim_s.value)
        System.println(params.inicio_s.value)

        String reportName = ''
        reportName = "/saida_de_produtos"
        redirect(action: printReport, params: [reportExt:params.ext,reportName:reportName,data_inicio:params.inicio_s_value,data_fim:params.fim_s_value])
    }

    def imprimirIva(){
        System.println(params.fim.value)
        System.println(params.inicio.value)

        String reportName = ''
        reportName = "/iva"
        redirect(action: printReport, params: [reportExt:params.ext,reportName:reportName,data_inicio:params.inicio_value,data_fim:params.fim_value])
    }

    def imprimirVDs(){
        System.println(params.fim_v.value)
        System.println(params.inicio_v.value)

        String reportName = ''
        reportName = "/vds"
        redirect(action: printReport, params: [reportExt:params.ext,reportName:reportName,data_inicio:params.inicio_v_value,data_fim:params.fim_v_value])
    }
}
