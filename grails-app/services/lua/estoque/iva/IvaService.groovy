package lua.estoque.iva

import grails.transaction.Transactional

/**
 * IvaService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class IvaService {
    Iva iva =  Iva.findByValido(true)


}
