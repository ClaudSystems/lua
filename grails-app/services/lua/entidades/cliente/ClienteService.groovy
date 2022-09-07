package lua.entidades.cliente

import grails.transaction.Transactional

/**
 * ClienteService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class ClienteService {

    Cliente findByCodigo(String codigo){
        return Cliente.findByCodigo(codigo)
    }

    def save(Cliente cliente){
        cliente.save(flush: true)
    }
}
