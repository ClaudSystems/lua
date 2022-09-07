package lua

import grails.transaction.Transactional

/**
 * SettingsService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class SettingsService {
    Settings settings

    Settings getSettings() {
        return Settings.findByDesigracao('settings')
    }
}
