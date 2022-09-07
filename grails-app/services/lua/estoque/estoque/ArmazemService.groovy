package lua.estoque.estoque

import grails.transaction.Transactional

/**
 * ArmazemService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class ArmazemService {

   Armazem findByCodigo(){
       return Armazem.findByCodigo('A1')
   }
}
