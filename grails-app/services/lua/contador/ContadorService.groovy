package lua.contador

import lua.contas.Conta
import lua.estoque.entradaDeProduto.EntradaDeProduto
import lua.estoque.saidaDeProduto.SaidaDeProduto
import lua.restaurante.mesa.Diario
import lua.vendas.cotacao.Cotacao
import lua.vendas.fatura.Fatura
import lua.vendas.fatura.Vd
import lua.vendas.recibo.Recibo

/**
 * ContadorService
 * A service class encapsulates the core business logic of a Grails application
 */

class ContadorService {

    String gerarNumeroDaFatura(){
        Integer num = 0
        Date date = new Date()
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        int year = cal.get(Calendar.YEAR)

        def faturas = Fatura.all
        if(faturas==null){

            return year+"/"+1
        }else{
            for(Fatura fatura in faturas){

                def numero = fatura.numeroDaFatura.split("/")
                Integer sub = numero[1].toInteger()
                Integer ano = numero[0].toInteger()
                if(ano==year){
                    if(sub>=num){
                        num = sub
                    }
                }

            }


            num++

            return year+"/"+num

        }

    }

    String gerarNumeroDaCotacao() {
        Integer num = 0
        Date date = new Date()
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        int year = cal.get(Calendar.YEAR)

        def cotacoes = Cotacao.all
        if(cotacoes==null){

            return year+"/"+1
        }else{
            for(Cotacao cotacao in cotacoes){

                def numero = cotacao.numeroDaCotacao.split("/")
                Integer sub = numero[1].toInteger()
                Integer ano = numero[0].toInteger()
                if(ano==year){
                    if(sub>=num){
                        num = sub
                    }
                }

            }


            num++

            return year+"/"+num

        }


    }

    String gerarNumeroDeVd() {
        Integer num = 0
        Date date = new Date()
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        int year = cal.get(Calendar.YEAR)

        def vds = Vd.all
        if(vds==null){
            return year+"/"+1
        }else{
            for(Vd vd in vds){
                def numero = vd.numeroDeVd.split("/")
                Integer sub = numero[1].toInteger()
                Integer ano = numero[0].toInteger()
                if(ano==year){
                    if(sub>=num){
                        num = sub
                    }
                }

            }


            num++
            return year+"/"+num

        }


    }

    String gerarNumeroDoRecibo(){
        Integer num = 0
        Date date = new Date()
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        int year = cal.get(Calendar.YEAR)

        def recibos = Recibo.all
        if(recibos==null){

            return year+"/"+1
        }else{
            for(Recibo recibo in recibos){

                def numero = recibo.numeroDoRecibo.split("/")
                Integer sub = numero[1].toInteger()
                Integer ano = numero[0].toInteger()
                if(ano==year){
                    if(sub>=num){
                        num = sub
                    }
                }

            }


            num++
           // String formattedNumber = String.format("%08d", num)
           // System.println("formattedNumber"+formattedNumber)
            return year+"/"+num

        }


    }
    String gerarNumeroDeSaida(){
        Integer num = 0
        Date date = new Date()
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        int year = cal.get(Calendar.YEAR)

        def saidas = SaidaDeProduto.all
        if(saidas==null){
            return year+"/"+1
        }else{
            for(SaidaDeProduto sdp in saidas){
                def numero = sdp.numeroDeSaida.split("/")
                Integer sub = numero[1].toInteger()
                Integer ano = numero[0].toInteger()
                if(ano==year){
                    if(sub>=num){
                        num = sub
                    }
                }

            }


            num++
            return year+"/"+num

        }


    }
    String gerarNumeroDeEntrada(){
        Integer num = 0
        Date date = new Date()
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        int year = cal.get(Calendar.YEAR)

        def entradas = EntradaDeProduto.all
        if(entradas==null){
            return year+"/"+1
        }else{
            for(EntradaDeProduto edp in entradas){
                def numero = edp.numeroDeEntrada.split("/")
                Integer sub = numero[1].toInteger()
                Integer ano = numero[0].toInteger()
                if(ano==year){
                    if(sub>=num){
                        num = sub
                    }
                }

            }
            num++
            return year+"/"+num
        }


    }
    String gerarNumeroDoDiario(){
        Integer num = 0
        Date date = new Date()
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        int year = cal.get(Calendar.YEAR)

        def diarios = Diario.all
        if(diarios==null){
            return year+"/"+1
        }else{
            for(Diario edp in diarios){
                def numero = edp.numeroDoDiario.split("/")
                Integer sub = numero[1].toInteger()
                Integer ano = numero[0].toInteger()
                if(ano==year){
                    if(sub>=num){
                        num = sub
                    }
                }

            }
            num++
            return year+"/"+num
        }


    }
    String gerarNumeroDaConta(){
        Integer num = 0
        Date date = new Date()
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        int year = cal.get(Calendar.YEAR)
        int mes = cal.get(Calendar.MONTH)
        int dia = cal.get(Calendar.DAY_OF_MONTH)
        int total = year+mes+dia+num

        def contas = Conta.all
        if(contas==null){
            String formattedNumber = String.format("%09d", total+1)
            System.println(formattedNumber)
            return formattedNumber
        }else{
            for(Conta conta in contas){

                Integer numero = conta.numeroDaConta.toInteger()

                    if(numero>=num){
                        num = numero
                    }


            }


            num++
            String formattedNumber = String.format("%09d", num)
            System.println("formattedNumber"+formattedNumber)
            return formattedNumber

        }


    }

}
