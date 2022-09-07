package lua.estoque.iva

import lua.BasicController

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional


/**
 * IvaController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
class IvaController extends BasicController  {

    static allowedMethods = [save: ["POST","PUT"], update: ["PUT","POST"], delete: "DELETE"]

	def index(Integer max) {
        updateCurrentAction('index')
        params.max = Math.min(max ?: 10, 100)
        respond Iva.list(params), model:[ivaInstanceCount: Iva.count()]
    }

	def list(Integer max) {
        updateCurrentAction('list')
        params.max = Math.min(max ?: 10, 100)
        respond Iva.list(params), model:[ivaInstanceCount: Iva.count()]
    }

    def show(Iva ivaInstance) {
        updateCurrentAction('show')
        respond ivaInstance
    }

    def create() {
        updateCurrentAction('create')
        respond new Iva(params)
    }

    @Transactional
    def save(Iva ivaInstance) {
        if (ivaInstance == null) {
            notFound()
            return
        }

        if (ivaInstance.hasErrors()) {
            respond ivaInstance.errors, view:'create'
            return
        }

        ivaInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'ivaInstance.label', default: 'Iva'), ivaInstance.id])
                redirect ivaInstance
            }
            '*' { respond ivaInstance, [status: CREATED] }
        }
    }

    def edit(Iva ivaInstance) {
        updateCurrentAction('edit')
        respond ivaInstance
    }

    @Transactional
    def update(Iva ivaInstance) {
        if (ivaInstance == null) {
            notFound()
            return
        }

        if (ivaInstance.hasErrors()) {
            respond ivaInstance.errors, view:'edit'
            return
        }

        ivaInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Iva.label', default: 'Iva'), ivaInstance.id])
                redirect ivaInstance
            }
            '*'{ respond ivaInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Iva ivaInstance) {

        if (ivaInstance == null) {
            notFound()
            return
        }

        ivaInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Iva.label', default: 'Iva'), ivaInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'ivaInstance.label', default: 'Iva'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
