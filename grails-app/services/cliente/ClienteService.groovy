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





}
