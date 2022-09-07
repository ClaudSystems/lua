package lua.vendas.fatura

import base.ComposersService
import lua.BasicController

/**
 * VdController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class VdController extends BasicController{
	def index(){

	}
	ComposersService composersService
	def newVd(){

    }

	def listagemVD(){

    }
	def ImprimirVd(){
		def id = composersService.vdId
		System.print(id)
		String reportName = ''
		reportName = "vd"
		redirect(action: printReport, params: [reportName:reportName,vdId:id])

	}
	def ImprimirVdMini(){
		def id = composersService.vdId
		System.print(id)
		String reportName = ''
		reportName = "mini_vd"
		redirect(action: printReport, params: [reportName:reportName,vdId:id])

	}

	def ImprimirPagamento(){
		def id = composersService.vdId
		System.print(id)
		String reportName = ''
		reportName = "mini_extrato_pagamento"
		redirect(action: printReport, params: [reportName:reportName,vdId:id])
	}
    def editVd(){
        def id = Vd.findById(composersService.vdId)
		[id:id.numeroDeVd]
	}
}
