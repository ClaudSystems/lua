package lua.vendas

import base.ComposersService
import lua.BasicController

/**
 * ReciboController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class ReciboController extends BasicController {
	ComposersService composersService
	def Imprimir(){
		String id = composersService.faturaId
		System.print("imprimir id="+id)
		 String reportName = "fatura"
		redirect(action: printReport, params: [reportName:reportName,faturaId:id])

	}
}
