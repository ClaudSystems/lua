package lua.restaurante.mesa

import grails.transaction.Transactional

/**
 * DiarioService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class DiarioService {

    Diario findByEstado(String estado){
        return Diario.findByEstado(estado)
    }

    def save(Diario diario){
        diario.save(flush: true)
    }
}
