package lua.entidades.cliente

import grails.plugin.springsecurity.annotation.Secured
import lua.BasicController
import lua.UtilizadorService
import lua.security.Utilizador
import lua.security.UtilizadorRoleGroup

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])

class ClienteController extends BasicController{
  def utilizadorService
    String role = "cliente"

   /*def filter(){

   }
    def index(){


    }*/
  @Secured(['ROLE_ADMIN'])
    def ClienteCrud(){

    }
  def clientes(){

  }
}
