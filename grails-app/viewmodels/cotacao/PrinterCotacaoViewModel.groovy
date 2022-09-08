package cotacao

import lua.SessionStorageService
import lua.vendas.cotacao.Cotacao
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire

class PrinterCotacaoViewModel {

SessionStorageService sessionStorageService
private Cotacao cotacao
private String original = "Original"

    String getOriginal() {
        return original
    }

    void setOriginal(String original) {
        this.original = original
    }

    @Command
    def change(){
        if(original=="Original"){
            original ="Cópia"
        }else if(original=="Cópia"){
            original="Original"
        }
    }

    Cotacao getCotacao() {
        return cotacao
    }

    void setCotacao(Cotacao cotacao) {
        this.cotacao = cotacao
    }

    @Init init() {
        cotacao = sessionStorageService.getCotacao() as Cotacao
    }



}
