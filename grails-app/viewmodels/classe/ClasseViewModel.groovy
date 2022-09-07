package classe

import grails.transaction.Transactional
import lua.entidades.cliente.Classe
import org.springframework.stereotype.Service
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList

@Transactional
@Service
class ClasseViewModel {

    String message
    @Wire  btnHello
    private boolean viewClasse
    private  Classe classe = new Classe()
    LinkedList<Classe> classes


    Classe getClasse() {
        return classe
    }

    void setClasse(Classe classe) {
        this.classe = classe
    }

    LinkedList<Classe> getClasses() {
        if(classes ==null){
            classes = new ListModelList<Classe>()
        }
        classes.clear()
        classes= Classe.all
        return classes
    }



    boolean getViewClasse() {
        return viewClasse
    }

    @Init init() {
        // initialzation code here
    }

    @NotifyChange(['message'])
    @Command clickMe() {
        message = "Clicked"
    }

}
