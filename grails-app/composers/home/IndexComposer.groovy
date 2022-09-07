package home

import lua.entidades.cliente.Cliente
import org.zkoss.zk.ui.Execution
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.Menuitem
import org.zkoss.zul.Messagebox


class IndexComposer extends zk.grails.Composer {
    Menuitem mi_cliente
    def afterCompose = { window ->
        // initialize components here
    }

    @Listen("onClick = menuitem#mi_cliente")
    public  void openClienteIndex(){
        Execution.sendRedirect("/cliente/index.gsp");

    }
}
