package lua.home

import base.EncryptionService
import grails.plugin.springsecurity.annotation.Secured
import lua.BasicController
import lua.HibernateService
import lua.estoque.produto.Produto
import lua.security.Utilizador

@Secured(['ROLE_ADMIN'])
class HomeController extends BasicController{
    EncryptionService encryptionService
    HibernateService hibernateService

    @Secured(['permitAll'])
    def index() {

    }
    @Secured(['permitAll'])
    def home() {
    }

    @Secured(['permitAll'])
    def contabilidade() {
    }
    def logout() {

        session.removeAttribute("user")
        encryptionService.localUser=null

    }



    def loginForm(){

    }

}
