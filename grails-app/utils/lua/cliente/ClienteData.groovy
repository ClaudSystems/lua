package lua.cliente

import lua.entidades.cliente.Cliente



class ClienteData {
    ClienteFilter clienteFilter = new  ClienteFilter()

    private List<Cliente> clientes = new ArrayList<Cliente>()
    static void  addClientes (){
        def clientes = Cliente.all
        for (Cliente c in clientes){
            clientes.add(c)
        }
    }

    public List<Cliente> getAllClientes() {
        addClientes()
        return new ArrayList<Cliente>(clientes)
    }
    public Cliente[] getAllClientesArray() {
        return clientes.toArray(new Cliente[clientes.size()])
    }

    // This Method only used in "Data Filter" Demo
    public  List<Cliente> getFilterClientes(ClienteFilter clienteFilter) {
        List<Cliente> someclientes = new ArrayList<Cliente>()
        String cat = clienteFilter.getCodigo().toLowerCase()
        String nm = clienteFilter.getNome().toLowerCase()
        String nut = clienteFilter.getNuit().toLowerCase()

        for (Iterator<Cliente> i = clientes.iterator(); i.hasNext();) {
            Cliente tmp = i.next();
            if (tmp.getCodigo().toLowerCase().contains(cat) &&
                    tmp.getNome().toLowerCase().contains(nm)  &&
                    tmp.getNuit().toLowerCase().contains(nut)) {
                someclientes.add(tmp)
            }
        }
        return someclientes
    }

    // This Method only used in "Header and footer" Demo
    public List<Cliente> getClientesByCodigo(String category) {
        List<Cliente> someclientes = new ArrayList<Cliente>()
        for (Iterator<Cliente> i = clientes.iterator(); i.hasNext();) {
            Cliente tmp = i.next()
            if (tmp.getCodigo().equalsIgnoreCase(category)){
                someclientes.add(tmp)
            }
        }
        return someclientes

    }





}
