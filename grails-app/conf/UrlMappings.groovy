
class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/" {
            controller = "home"
            action = "index"

        }
        "500"(view:'/error')

        "/destino/$action?"(controller: "destino",action: "destinoCrud")
        "/produto/$action?"(controller: "produto",action: "produtoCrud")
        "/fornecedor/$action?"(controller: "fornecedor",action: "fornecedorCrud")
        "/utilizador/$action?"(controller: "utilizador",action: "utilizadorCrud")
        "/produto/$action?"(controller: "produto",action: "produtoCrud")
        "/unidade/$action?"(controller: "unidade",action: "unidadeCrud")
        "/modelo/$action?"(controller: "modelo",action: "modeloCrud")
        "/modelo/$action?"(controller: "modelo",action: "modeloCrud")
      //  "/cotacao/$action?"(controller: "cotacao",action: "listCotacao")
        "/cotacao/edit"(controller: "cotacao",action: "editCotacao")
        "/cotacao/new"(controller: "cotacao",action: "newCotacao")
        "/cotacao/show"(controller: "cotacao",action: "showCotacao")
        "/cotacao/imprimir_cotacao"(controller: "cotacao",action: "Imprimir")
        "/roleGroup/$action?"(controller: "roleGroup",action: "roleGroup")
        "/recibo/$action?"(controller: "recibo",action: "reciboCrud")
        "/ordemDeCompra/$action?"(controller: "ordemDeCompra",action: "ordemDeCompraCrud")
        "/guiaDeRemessa/$action?"(controller: "guiaDeRemessa",action: "guiaDeRemessaCrud")
        "/armazem/$action?"(controller: "armazem",action: "armazemCrud")
        "/mesa/$action?"(controller: "mesa",action: "mesaCrud")
        "/fatura/list"(controller: "fatura",action: "faturas")
        "/fatura/new"(controller: "fatura",action: "newFatura")
        "/fatura/edit"(controller: "fatura",action: "editFatura")
        "/classe/$action?"(controller: "classe",action: "classe")

	}
}
