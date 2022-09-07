package precos

import grails.transaction.Transactional
import lua.vendas.precos.Preco
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox


@Transactional
class PrecosViewModel {
        @Wire Label info
    String blue = "color:blue"
    String red = "color:red"
    String message
    Preco preco
    private List<Preco> items

    List<Preco> getItems() {
        if(items.equals(null)){
            items = new ListModelList<Preco>(Preco.all)
        }
        return items
    }

    Preco getPreco() {

        if(preco.equals(null)){
            if(Preco.findByAtivo(true)){
                preco = Preco.findByAtivo(true)
            }else
            preco = new Preco()

        }
        return preco
    }

    void setPreco(Preco preco) {
        this.preco = preco
    }

    String getMessage() {
        return message
    }

    @Init init() {
        // initialzation code here
    }

    @Command
    static fecharEditor(){
        Executions.sendRedirect("/settings/index")
    }

    @Command
    @NotifyChange(["items","info"])
    def update(){
        if(preco.ativo){
            if(Preco.findByAtivo(true)) {
               // Messagebox.show("Somente um único congunto de preço pode estar ativo!", "Lua", 1, Messagebox.ERROR)
                info.value = "Somente um único congunto de preço pode estar ativo!"
                info.style = red
                return
            }
        }

       preco.save(flush: true)

       // Messagebox.show("Percentual dos preços atualizados com sucesso!", "Lua", 1, Messagebox.INFORMATION)
        info.value = "Percentual dos preços atualizados com sucesso!"
        info.style = blue
    }



}
