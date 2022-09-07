package settings

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import lua.Settings
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label

class SettingsViewModel {
    @Wire Label info
   Settings  settings = Settings.findByDesigracao("settings")

    @Init init() {

    }

    @Command
    @NotifyChange(["info","settings"])
    def aceitarSaidasSemEstoque(){
        System.println("aceitar saidas sem estoque"+settings.aceitarSaidasSemEstoque)
        if(settings.aceitarSaidasSemEstoque){
            settings.aceitarSaidasSemEstoque = false
        }else {
            settings.aceitarSaidasSemEstoque = true
        }
        settings.save(flush: true)
        info.style="color:blue"
        info.value = "Configuração atualizada!"
    }

    @Command
    @NotifyChange(["info","settings"])
    def refletirSaidasEmEstoqueDeVds(){
        System.println("refletirSaidasEmEstoqueDeVds"+settings.refletirSaidasEmEstoqueDeVds)
        if(settings.refletirSaidasEmEstoqueDeVds){
            settings.refletirSaidasEmEstoqueDeVds = false
        }else {
            settings.refletirSaidasEmEstoqueDeVds = true
        }
        settings.save(flush: true)
        info.style="color:blue"
        info.value = "Configuração atualizada!"
    }

    @Command
    @NotifyChange(["info","settings"])
    def imprimirMiniVd(){
        System.println("refletirSaidasEmEstoqueDeVds"+settings.imprimirMiniVd)
        if(settings.imprimirMiniVd){
            settings.imprimirMiniVd = false
        }else {
            settings.imprimirMiniVd= true
        }
        settings.save(flush: true)
        info.style="color:blue"
        info.value = "Configuração atualizada!"
    }
}
