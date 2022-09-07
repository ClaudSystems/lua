package lua.estoque.produto

import grails.transaction.Transactional

/**
 * ProdutoService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class ProdutoService {

    Produto findById (Long id){
        return Produto.findById(id)
    }
}
