package lua.restaurante.mesa

import base.ComposersService
import lua.BasicController

/**
 * DiarioController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class DiarioController extends BasicController{

	ComposersService composersService
	def diario(){}

	def imprimirDiario(){
		def id = composersService.diarioId
		System.print(id)
		String reportName = ''
		reportName = "mini_diario_de_vendas"
		redirect(action: printReport, params: [reportName:reportName,id:id])
	}

    def imprimirDiarioDetalhado(){
        def id = composersService.diarioId
        System.print(id)
        String reportName = ''
        reportName = "mini_diario_de_vendas_detalhado"
        redirect(action: printReport, params: [reportName:reportName,id:id])
    }

	def showDiario(){

	}
}
