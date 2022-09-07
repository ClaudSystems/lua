package lua.security

import base.EncryptionService
import grails.transaction.Transactional

/**
 * UtilizadorService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class UtilizadorService {
    Utilizador utilizador
    EncryptionService encryptionService

}
