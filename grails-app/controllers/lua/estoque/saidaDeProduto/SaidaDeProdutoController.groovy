package lua.estoque.saidaDeProduto

import base.ComposersService
import lua.BasicController

/**
 * SaidaDeProdutoController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class SaidaDeProdutoController  extends  BasicController{
	ComposersService ComposersService
	def saidaDeProduto() {}
	def showSaidaDeProduto() {}
	def newSaida(){}
	def saidaList(){}
	def Imprimir(){
		String id = composersService.saidaDeProduto.id
		String fn = composersService.saidaDeProduto.numeroDeSaida.substring(0,4)+ composersService.saidaDeProduto.numeroDeSaida.substring(5,13)
		System.print(id)
		String		reportName = "saida"
		redirect(action: printReport, params: [id:fn,reportName:reportName,saidaId:id])
	}

}
