package lua.vendas.fatura

import grails.transaction.Transactional
import lua.estoque.itemProduto.ItemProduto
import lua.restaurante.mesa.Diario
import lua.restaurante.mesa.Mesa
import lua.security.Utilizador
import org.zkoss.zul.ListModelList

/**
 * VdService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class VdService {

    def updateVd(Vd vd){

        def vdFrmDb = Vd.findById(vd.id)
        System.println('updateVd.vd='+vd.id)
        if(vdFrmDb!=null){
            System.println('vdFrmDb='+vdFrmDb.id)
          //  vdFrmDb.valorDoIva = vd.valorDoIva
            System.println(vdFrmDb.valorDoIva)
            //vdFrmDb.valor = vd.valor
            System.println(vdFrmDb.valor)
           // vdFrmDb.valorTotal = vd.valorTotal
            //vdFrmDb.itemsProduto = vd.itemsProduto
            vdFrmDb.label= vd.label
            System.println(vdFrmDb.valorTotal)
            System.println('utilizador'+vdFrmDb.utilizador)

           // System.println(vdFrmDb.cliente)
            System.println(vdFrmDb.label)
            vdFrmDb.pago = vd.pago
            System.println(vdFrmDb.pago)
            vdFrmDb.cancelado = vd.cancelado
            System.println(vdFrmDb.cancelado)
            vdFrmDb.ativo = vd.ativo
            System.println(vdFrmDb.ativo)
            vdFrmDb.condicoesDeVenda = vd.condicoesDeVenda
            System.println(vdFrmDb.condicoesDeVenda)
            vdFrmDb.formaDePagamento = vd.formaDePagamento
            System.println(vdFrmDb.formaDePagamento)
            vdFrmDb.cheque = vd.cheque
            System.println('vdFrmDb.cheque'+vdFrmDb.cheque)
           // vdFrmDb.diario = vd.diario
            System.println('vdFrmDb.dairio'+vdFrmDb.diario)
           // vdFrmDb.mesa = vd.mesa
            System.println(vdFrmDb.mesa)
            System.println('1')


            vd.save(flush: true)

        }
    }


    def saveVd (Vd vd){
        vd.save(flush: true)

        def vdDb = Vd.findById(vd.id)
        for(ItemProduto ip in vdDb.itemsProduto){
            ip.vd = vdDb
            ip.save(flush: true)
        }
    }
    Vd findById(Long id){
      return Vd.with {Vd.findById(id)}
    }

    ListModelList findAllWhere (Mesa selectedMesa, Diario diario1){
        return Vd.findAllWhere(mesa: selectedMesa,diario: diario1)
    }

    Utilizador getUser(Long id){
        return Utilizador.findById(id)
    }

    Vd findByNumeroDeVd(String numeroDoVd){
         Vd.findByNumeroDeVd(numeroDoVd)
    }

    Vd findByLabel(String label){
        return Vd.findByLabel(label)
    }


}
