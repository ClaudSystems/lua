package lua

import org.springframework.transaction.annotation.Transactional

/**
 * HibernateService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class HibernateService {

    def sessionFactory

    def cleanSession(){
        sessionFactory.currentSession.clear()
    }

    def closeSession(){
        sessionFactory.currentSession.close()
    }

}
