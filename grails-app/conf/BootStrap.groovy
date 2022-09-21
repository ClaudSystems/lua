import base.EncryptionService
import lua.Settings
import lua.entidades.cliente.Classe
import lua.entidades.cliente.Cliente
import lua.entidades.entidade.Entidade
import lua.estoque.estoque.Armazem
import lua.estoque.destino.Destino
import lua.security.RoleGroupRole
import lua.security.UtilizadorRoleGroup
import lua.vendas.recibo.FormaDePagamento
import lua.security.Utilizador
import lua.security.Role
import lua.security.UtilizadorRole
import lua.security.RoleGroup



class BootStrap {
    EncryptionService encryptionService
    def init = { servletContext ->

        if(!Role.findByAuthority('cliente')){ def role1 = new Role(authority:"cliente").save (flush: true) }
        if(!Role.findByAuthority('entidade')){ def role5 = new Role(authority:"entidade").save (flush: true) }
        if(!Role.findByAuthority('fornecedor')){ def role9 = new Role(authority:"fornecedor").save (flush: true) }
        if(!Role.findByAuthority('categoria')){ def role13 = new Role(authority:"categoria").save (flush: true) }
        if(!Role.findByAuthority('destino')){ def role20 = new Role(authority:"destino").save (flush: true) }
        if(!Role.findByAuthority('encomenda')){ def role24 = new Role(authority:"encomenda").save (flush: true) }
        if(!Role.findByAuthority('entrada_produto')){ def role27 = new Role(authority:"entrada_produto").save (flush: true) }
        if(!Role.findByAuthority('estoque')){ def role31 = new Role(authority:"estoque").save (flush: true) }
        if(!Role.findByAuthority('item_produto')){ def role35 = new Role(authority:"item_produto").save (flush: true) }
        if(!Role.findByAuthority('iva')){ def role39 = new Role(authority:"iva").save (flush: true) }
        if(!Role.findByAuthority('lote')){ def role43 = new Role(authority:"lote").save (flush: true) }
        if(!Role.findByAuthority('marca')){ def role47 = new Role(authority:"marca").save (flush: true) }
        if(!Role.findByAuthority('modelo')){ def role51 = new Role(authority:"modelo").save (flush: true) }
        if(!Role.findByAuthority('produto')){ def role55 = new Role(authority:"produto").save (flush: true) }
        if(!Role.findByAuthority('saida_produto')){ def role59 = new Role(authority:"saida_produto").save (flush: true) }
        if(!Role.findByAuthority('transferenciae_produto')){ def role63 = new Role(authority:"transferenciae_produto").save (flush: true) }
        if(!Role.findByAuthority('unidade')){ def role68 = new Role(authority:"unidade").save (flush: true) }
        if(!Role.findByAuthority('periodo')){ def role72 = new Role(authority:"periodo").save (flush: true) }
        if(!Role.findByAuthority('role_group')){ def role76 = new Role(authority:"role_group").save (flush: true) }
        if(!Role.findByAuthority('role')){ def role80 = new Role(authority:"role").save (flush: true) }
        if(!Role.findByAuthority('utilizador')){ def role84 = new Role(authority:"utilizador").save (flush: true) }
        if(!Role.findByAuthority('cotacao')){ def role88 = new Role(authority:"cotacao").save (flush: true) }
        if(!Role.findByAuthority('recibo')){ def role96 = new Role(authority:"recibo").save (flush: true) }
        if(!Role.findByAuthority('cheque')){ def role0 = new Role(authority:"cheque").save (flush: true) }
        if(!Role.findByAuthority('settings')){ def role0 = new Role(authority:"settings").save (flush: true) }
        if(!Role.findByAuthority('relatorios')){ def role0 = new Role(authority:"relatorios").save (flush: true) }
        if(!Role.findByAuthority('fatura')){ def role0 = new Role(authority:"fatura").save (flush: true) }
        if(!Role.findByAuthority('precos')){ def role0 = new Role(authority:"precos").save (flush: true) }
        if(!Role.findByAuthority('entrada_produto_d')){ def role0 = new Role(authority:"entrada_produto_d").save (flush: true) }
        if(!Role.findByAuthority('entrada_produto_d')){ def role0 = new Role(authority:"entrada_produto_d").save (flush: true) }
        if(!Role.findByAuthority('cotacao_d')){ def role0 = new Role(authority:"cotacao_d").save (flush: true) }
        if(!Role.findByAuthority('pos')){ def role0 = new Role(authority:"pos").save (flush: true) }
        if(!Role.findByAuthority('categoria')){ def role0 = new Role(authority:"categoria").save (flush: true) }
        if(!Role.findByAuthority('vd')){ def role0 = new Role(authority:"vd").save (flush: true) }
        if(!Role.findByAuthority('armazem')){ def role0 = new Role(authority:"armazem").save (flush: true) }
        if(!Role.findByAuthority('diario')){ def role0 = new Role(authority:"diario").save (flush: true) }
        if(!Role.findByAuthority('dia')){ def role0 = new Role(authority:"dia").save (flush: true) }
        if(!Role.findByAuthority('classe')){ def role0 = new Role(authority:"classe").save (flush: true) }
        if(!Role.findByAuthority('mesa')){ def role0 = new Role(authority:"mesa").save (flush: true) }

        /*Classe de cliente publico*/
        def classePublic =new Classe(nomeDaClasse: "indiferenciado",percentualDeDesconto: 0 ).save(flush: true)
        /*Cliente que representa o publico*/
        def classePulicDb = Classe.findByNomeDaClasse('indiferenciado')

        /*unica instancia de settings*/
        if(!Settings.findByDesigracao('settings')){new Settings(desigracao: "settings",aceitarSaidasSemEstoque: false,refletirSaidasEmEstoqueDeVds: false).save(flush: true)}

        /*if(!FormaDePagamento.findByNome('Boleto Bancario')){
            def boletoBancario = new FormaDePagamento(nome: 'Boleto Bancario')
            boletoBancario.save (flush: true)
        }*/
        if(!FormaDePagamento.findByNome('Cartao De Credito')){
            def cartaoDeCredito = new FormaDePagamento(nome: 'Cartao De Credito')
            cartaoDeCredito.save (flush: true)
        }

        if(!FormaDePagamento.findByNome('Cartao De Debito')){
            def cartaoDeDebito = new FormaDePagamento(nome: 'Cartao De Debito')
            cartaoDeDebito.save (flush: true)
        }
        if(!FormaDePagamento.findByNome('Cheque')){
            def cheque = new FormaDePagamento(nome: 'Cheque')
            cheque.save (flush: true)
        }

        if(!FormaDePagamento.findByNome('Dinheiro')){
            def dinheiro = new FormaDePagamento(nome: 'Dinheiro')
            dinheiro.save (flush: true)
        }
        if(!FormaDePagamento.findByNome('Emola')){
            def dinheiro = new FormaDePagamento(nome: 'Emola')
            dinheiro.save (flush: true)
        }
        if(!FormaDePagamento.findByNome('M-Pesa')){
            def dinheiro = new FormaDePagamento(nome: 'M-Pesa')
            dinheiro.save (flush: true)
        }
        if(!FormaDePagamento.findByNome('Carteira Movel')){
            def dinheiro = new FormaDePagamento(nome: 'Carteira Movel')
            dinheiro.save (flush: true)
        }
        if(!FormaDePagamento.findByNome('Mkesh')){
            def dinheiro = new FormaDePagamento(nome: 'Mkesh')
            dinheiro.save (flush: true)
        }

        def adminRole = new Role (authority: 'ROLE_ADMIN').save(flush: true)
        def utilizadorRole = new Role(authority: 'ROLE_USER').save(flush: true)

        if(!RoleGroup.findByName("rg_full")){
            RoleGroup roleGroup = new RoleGroup(name: "rg_full")
            roleGroup.save(flush: true)
            def rolesDB = Role.all
            def grDB = RoleGroup.findByName("rg_full")

            for(Role role in rolesDB){
                if(!grDB.equals(null)){

                    RoleGroupRole.create grDB, role, true
                }



            }
        }

        def finalPwd = encryptionService.encrypt("me")
        def testUser = new Utilizador(username: 'me', password: finalPwd,email: 'fanisso@gmail.com')
        testUser.save(flush: true)

        def finalPwd1 = encryptionService.encrypt("user1")
        def testUser1 = new Utilizador(username: 'user1', password: finalPwd,email: 'fanisso@gmail.com')
        testUser1.save(flush: true)

        def finalPwd2 = encryptionService.encrypt("user2")
        def testUser2 = new Utilizador(username: 'user2', password: finalPwd,email: 'fanisso@gmail.com')
        testUser2.save(flush: true)

        def finalPwd3 = encryptionService.encrypt("user3")
        def testUser3 = new Utilizador(username: 'user3', password: finalPwd,email: 'fanisso@gmail.com')
        testUser3.save(flush: true)

        def finalPwd4 = encryptionService.encrypt("user4")
        def testUser4 = new Utilizador(username: 'user4', password: finalPwd,email: 'fanisso@gmail.com')
        testUser4.save(flush: true)

        if(!Utilizador.findByUsername("admin")){
            def admin = new  Utilizador(username: 'admin',password: 'Admin2021***',email: 'fanisso@gmail.com')

            admin.save(flash:true)
            UtilizadorRole.create admin, adminRole, true
            def rgFull = RoleGroup.findByName("rg_full")
            UtilizadorRoleGroup.create admin, rgFull, true
        }






        if(!Destino.findByCodigo("D1")){
            Destino destino = new Destino(codigo: "D1",nome: "vendas",descricao: "Vendas",estado: "ativo")
            destino.save(flash:true)
        }
        if(Armazem.findByCodigo("A1")){
            def armazem = new Armazem(nome: 'A1', codigo: "A1")
            armazem.save(flush: true)
        }
        def entidadeNome = "PIKAS BAR"
        byte[] cm = encryptionService.encrypt(entidadeNome)
        def entidade = new Entidade(cm: cm, codigo: 'pikas', nome: entidadeNome,nuit: '00000000',mail: 'pikas@bar.co.mz',numTelefone: '8493900906',endereco: 'Bairro Intaka, Q-14, Maputo',nomeDoBanco: '',nib: '',numeroDaConta: '').save(flush: true)
        testUser.save(flush: true)
        def entidadeDB = Entidade.findByNome(entidadeNome)
        def nomeDB = encryptionService.decrypt(entidadeDB.cm)

        def clientePublic=  new Cliente(nome: "indiferenciado",codigo:"pbc",classe: classePulicDb).save(flush: true)
        if(nomeDB!=entidadeNome||nomeDB==null){
            brack
        }
    }
    def destroy = {
      /*  def entidadeDB = Entidade.findByNome("PIKAS BAR")
        entidadeDB.delete(flush: true)*/
    }
}
