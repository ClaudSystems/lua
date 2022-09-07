package lua.estoque.estoque


import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import lua.BasicController

/**
 * ArmazemController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
class ArmazemController extends BasicController {

    def armazemCrud (){

    }
}
