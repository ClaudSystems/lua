package base

import lua.AdvancedEncryptionStandard

import lua.security.Role

import lua.security.Utilizador
import lua.security.RoleGroup

import java.nio.charset.StandardCharsets


/**
 * EncryptionService
 * A service class encapsulates the core business logic of a Grails application
 */

class EncryptionService {
    Utilizador localUser
    byte[] encryptionKey = "MZygpewJsCpRrfOr".getBytes(StandardCharsets.UTF_8)
    AdvancedEncryptionStandard aes = new AdvancedEncryptionStandard(
            encryptionKey)

byte[] encrypt (String key){
    byte[] plainText = key.getBytes(StandardCharsets.UTF_8)

    byte[] cipherText = aes.encrypt(plainText)
    return cipherText
}
    String decrypt(byte[] chiperTxt ){
        byte[] decryptedCipherText = aes.decrypt(chiperTxt)
        return (new String(decryptedCipherText))
    }

    boolean checkPassword(String pass, Utilizador user){
       String passDB = decrypt(user.password)
        if(passDB.equals(pass)){
            localUser=user
            return true
        }
        return false
    }

    boolean getPermission( String role){

    }


}
