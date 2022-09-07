package lua.estoque.itemProduto

import grails.transaction.Transactional

/**
 * ItemProdutoService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class ItemProdutoService {

   ItemProduto findById(Long id){
      return ItemProduto.findById(id)
   }
}
