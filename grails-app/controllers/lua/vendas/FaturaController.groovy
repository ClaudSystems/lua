package lua.vendas

import base.ComposersService
import lua.BasicController
import lua.SessionStorageService
import lua.vendas.fatura.Fatura

/**
 * FaturaController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class FaturaController extends BasicController{
	SessionStorageService sessionStorageService
	ComposersService composersService
	def faturas(){

	}

	def cotacao(){}
	def cotacaoCrud (){ }
	def editFatura (){
		def fa = sessionStorageService.fatura as Fatura
		String numero = ""
		System.println(fa.id)
		if(fa){
			numero= fa.numeroDaFatura
		}
		[numero: numero ]
	}
	def faturasAbertas(){ [faturas:Fatura.findByEstado("aberta")]	}

	def newFatura(){
		/*[id:composersService.faturaId ,userId:composersService.utilizadorId]*/
	}

	def printerFatura(){
		Fatura fa = sessionStorageService.getFatura() as Fatura
		String numero = ""
		if(fa){
			numero = fa.numeroDaFatura
		}
		[numero:numero]

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
