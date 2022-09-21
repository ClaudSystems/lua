package cliente

import grails.transaction.Transactional
import lua.entidades.cliente.Cliente

@Transactional
class ClienteService {

   List selectedClientes = new ArrayList()
    List clientes = Cliente.all

    public void addSelectedCliente (String codigo){
       def cliente =  Cliente.findAllByCodigo(codigo)
        selectedClientes.add(cliente)
    }

    public List getSelectedClientes (){
        return selectedClientes
    }

    public List getAll(){
       return clientes
    }


    List<Cliente> findAllByName(String nome) {
        def c = Cliente.createCriteria()
        def results = c.list {
            like("nome", "%" + nome + "%")

            maxResults(4)
            order("nome", "desc")
        }
        return results
    }


}
