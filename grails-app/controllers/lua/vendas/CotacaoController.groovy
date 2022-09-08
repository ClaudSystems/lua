package lua.vendas

import base.ComposersService
import lua.BasicController
import lua.SessionStorageService

/**
 * CotacaoController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class CotacaoController extends BasicController {

	ComposersService composersService
	SessionStorageService sessionStorageService
	def cotacao(){}
	def cotacaoCrud (){ }
	def editCotacao (){

		[id:composersService.cotacao.numeroDaCotacao ,userId:composersService.utilizadorId?.id]
	}
	def ListCotacao (){ }
	def listItems (){ }
	def showCotacao(){}
	def newCotacao(){}
	def printerCotacao(){
		def numero = sessionStorageService.getCotacao().numeroDaCotacao as String
		[numero:numero]
	}
/*	def Imprimir(){
		String reportName = '/cotacao'
		def
		redirect(action: printReport, params: [reportName:reportName,enomendaId:composersService.cotacao.id])
	}*/

	def Imprimir(){

		def id = composersService.getCotacaoId()
			System.print(id)
		String reportName = ''
		reportName = "cotacao"
		redirect(action: printReport, params: [reportName:reportName,cotacaoId:id])

	}
}
