package lua.restaurante.mesa

import grails.transaction.Transactional

/**
 * MesaService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class MesaService {

    Mesa findById(Long id){
        return Mesa.findById(id)
    }
}
