package lua.vendas

import lua.BasicController
import lua.estoque.produto.Produto

/**
 * PosController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class PosController extends BasicController {
def posCrud (){}
    def pos(){

        def produtos = Produto.findAll()

        respond   model: [produtos: produtos]
    }

    def listagemDeVds(){
    }

    def editPosVd(){}
    def newPosVd(){}
}
