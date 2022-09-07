package diario

import base.ComposersService
import lua.contador.ContadorService
import lua.restaurante.mesa.Diario
import lua.vendas.fatura.Vd
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList

import java.sql.SQLDataException

class DiarioViewModel {
    def pickedItem
    ComposersService composersService
    Diario selectedItem
    @Wire Label info
    String blue="color:blue"
    String red = "color:red"
    ContadorService contadorService
    private ListModelList<Diario> items
    private boolean lb_items = true
    private boolean bt_remover = false
    private boolean bt_abrir = false

    @Command
    def imprimirDiario(){
        info.value = "Click o botão direito sobre imprimir para impressão do diário detalhado!"
        composersService.diarioId = selectedItem.id
        Executions.sendRedirect("/diario/imprimirDiario")
    }
    @Command
    def imprimirDiarioDetalhado(){
        composersService.diarioId = selectedItem.id
        Executions.sendRedirect("/diario/imprimirDiarioDetalhado")
    }

    boolean getBt_abrir() {
        return bt_abrir
    }

    boolean getBt_remover() {
        return bt_remover
    }

    boolean getLb_items() {
        return lb_items
    }

    ListModelList<Diario> getItems() {
        if(items==null){
            items = new ListModelList<>(Diario.all)
        }
        return items
    }

    @Command
    @NotifyChange(["selectedItem","items"])
    def addItem(){
        if(Diario.findByEstado("aberto")){
            info.value = "Por favor Feiche todos os diários antes de criar um novo!"
            info.style =red
            return
        }
        def diario = new Diario(estado: "aberto",numeroDoDiario:contadorService.gerarNumeroDoDiario()).save(flush: true)
        selectedItem = Diario.findById(diario.id)
        if(selectedItem!=null){
            info.value = "O diário com o Nº " +selectedItem.numeroDoDiario+" foi criado com sucesso!"
            info.style = blue

        }
        getItems()
    }

    @Command
    @NotifyChange(["items","selectedItem"])
    def fecharDiario(){

        for(Vd vd in selectedItem.vds){
            if(!vd.pago){
                info.value = "O VD Nº."+vd.numeroDeVd+" não foi paga!"
                info.style = red
                return
            }
        }
        selectedItem.estado = "fechado"
        try {
            selectedItem.save(flush: true)
            info.value = "Diário Nº."+selectedItem.numeroDoDiario+"Fechada com sucesso!"
            info.style = blue

        }catch (SQLDataException e){
            info.value = e.toString()
            info.style = red
        }

    }

    @Command
    void showIt() {
        System.print(pickedItem.id)
        composersService.vdId=pickedItem.id
        composersService.vd=pickedItem
        Executions.sendRedirect("/vd/editVd")

    }

    @Command
    @NotifyChange(["bt_remover"])
    def checkEstado(){
        info.value = ""
        if(selectedItem.estado =="fechado"){
            bt_remover = false

        }else {
            bt_remover = true

        }
    }
    @Command
    @NotifyChange(["items","selectedItem"])
    def abrirDiario(){
        if(selectedItem.estado =="fechado"){

            try {
                if(Diario.findByEstado("pendente")){
                    info.value ="Existe um diário pendente!"
                    info.style = blue
                    return 
                }
                selectedItem.estado = "pendente"

                selectedItem.save(flush: true)
                info.value = "O Diário "+selectedItem.numeroDoDiario+" foi reaberto!"
                info.style = blue
            }catch ( SQLDataException e){
                info.value = e.toString()
                info.style = red
            }

        }else {
            bt_remover = true
            info.value = "Este diário já esta aberto!"
            info.style = blue
        }
    }
}
