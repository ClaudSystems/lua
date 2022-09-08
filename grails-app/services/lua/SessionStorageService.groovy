package lua

import lua.vendas.cotacao.Cotacao
import org.springframework.web.context.request.RequestContextHolder

import javax.servlet.http.HttpSession

/**
 * SessionStorageService
 * A service class encapsulates the core business logic of a Grails application
 */

class SessionStorageService {
    static transactional = false
    static scope = "singleton"
    private HttpSession getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    def setCotacao(Cotacao cotacao) { getSession().cotacao = cotacao }
    def getCotacao() { getSession().cotacao }
}
