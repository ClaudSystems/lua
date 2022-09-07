package lua

import grails.transaction.Transactional
import lua.estoque.categoria.Categoria
import lua.estoque.produto.Produto

/**
 * CategoriaService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class CategoriaService {
    Categoria produtoCategoriesRoot = new Categoria()

    /**
     * Retrieve all cars in the car store.
     * @return all cars.
     */
    public List<Produto> findAll(){
        return Produto.all
    }

    /**
     * Store or modify a car in car store.
     */
    void store(Produto produto){
        produto.save()
    }

    /**
     * Store or modify a inventory item in car store.
     */
   /* void store(InventoryItem inventoryItem){

    }*/

    /**
     * Order cars.
     */
    /*void order(List<OrderItem> orderItems){

    }*/

    /**
     * Retrieve the root of car categories.
     */
    Categoria getProdutoCategoriesRoot(){

    }

    /**
     * Count cars by filter.
     */
    int countByFilter(String filter){
            return Categoria.findAllByNome(filter).size()
    }

    /**
     * Query cars by filter.
     */
    List<Produto> queryByFilter(String filter){
        return Categoria.findByNome(filter).produtos
    }
}
