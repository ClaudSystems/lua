package lua.estoque.trasferenciaDeProduto

import lua.estoque.produto.Produto
import lua.estoque.estoque.Armazem
class TransferenciaDeProduto implements Serializable {
    private static final long serialVersionUID = 1
    Armazem armazemFonte
    Armazem armazemDestino
    Produto produto

    static constraints = {
        armazemFonte nullable: false
        armazemDestino nullable: false
        produto nullable: false
    }
    static mapping = {
        id generator: 'increment'
    }

    public String toString() {
        return "${id}";

    }
}
