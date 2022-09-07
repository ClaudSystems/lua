package lua.vendas

import base.ComposersService
import lua.BasicController
import lua.vendas.fatura.Fatura

/**
 * FaturaController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class FaturaController extends BasicController{

	ComposersService composersService
	def faturas(){}
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def faturas = Fatura.findAllByEstado("aberta")
        [faturas:faturas,total:faturas.size()]
    }
	def cotacao(){}
	def cotacaoCrud (){ }
	def editFatura (){
		def fa = Fatura.findById(composersService.faturaId)
		[id:fa.numeroDaFatura ,userId:composersService.utilizadorId]
	}
	def faturasAbertas(){ [faturas:Fatura.findByEstado("aberta")]	}
	def listFatura (){ }
	def listItems (){ }
	def showCotacao(){}
	def newFatura(){
		/*[id:composersService.faturaId ,userId:composersService.utilizadorId]*/
	}


	def Imprimir(){
		String id = composersService.fatura.id
		def numero = composersService.fatura.numeroDaFatura.split("/")
        String fn = numero[1]
		System.print(id)
        String		reportName = "fatura"
		redirect(action: printReport, params: [id:fn,reportName:reportName,faturaId:id])
	}
	def ImprimirDuplicado(){
		String id = composersService.fatura.id
        def numero = composersService.fatura.numeroDaFatura.split("/")
        String fn = numero[1]
		System.print(id)
		String		reportName = "fatura_duplicado"
		redirect(action: printReport, params: [id:fn,reportName:reportName,faturaId:id])

	}

    def ImprimirRecibo(){
        String id = composersService.reciboId
        System.print(id)
        String		reportName = "recibo"
        redirect(action: printReport, params: [reportName:reportName,reciboId:id])

    }

}
