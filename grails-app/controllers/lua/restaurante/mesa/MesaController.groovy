package lua.restaurante.mesa

import grails.plugin.springsecurity.annotation.Secured
import lua.BasicController

/**
 * MesaController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class MesaController extends BasicController {

	static scaffold = true
//	def index = { }
    @Secured(['ROLE_ADMIN'])

	def mesaCrud(){}
    def index(){}
}
