package lua.estoque.categoria

import grails.transaction.Transactional

/**
 * CategoriaService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class CategoriaService {

    Categoria findById (Long id){
        return Categoria.findById(id)
    }
}
