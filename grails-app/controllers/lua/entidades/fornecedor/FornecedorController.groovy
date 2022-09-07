package lua.entidades.fornecedor

import grails.plugin.springsecurity.annotation.Secured
import lua.BasicController


class FornecedorController  extends BasicController{
    @Secured(['ROLE_ADMIN'])
    def index() { }
    @Secured(['ROLE_ADMIN'])
    def produtosFilter(){}
    @Secured(['ROLE_ADMIN'])
    def fornecedorCrud(){}
}
