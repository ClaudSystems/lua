package lua.estoque

import lua.BasicController
import lua.estoque.encomenda.Encomenda

class EncomendaController extends BasicController {
def encomendaService
    def index(Integer max) {
        updateCurrentAction('list')
        params.max = Math.min(max ?: 10, 100)
        respond Encomenda.list(params), model:[encomendaInstanceCount: Encomenda.count()]
    }
    def novaEncomenda() {}
    def produtoFilter() {}
    def showEncomenda() {}


    def Imprimir(){
        String reportName = '/encomenda'
       def   encomenda =  session.getAttribute("encomenda")as Encomenda
        redirect(action: printReport, params: [reportName:reportName,enomendaId:encomenda.id,notade:encomendaService.notaDe])
    }

    def show (){

    }

}
