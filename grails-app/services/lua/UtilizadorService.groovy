package lua

import base.EncryptionService
import lua.security.Role
import lua.security.RoleGroup
import lua.security.RoleGroupRole
import lua.security.Utilizador
import lua.security.UtilizadorRoleGroup
import org.zkoss.zul.ListModel
import org.zkoss.zul.ListModelList

import java.sql.Array


/**
 * UtilizadorService
 * A service class encapsulates the core business logic of a Grails application
 */

class UtilizadorService {
    EncryptionService encryptionService
    String selectedUser
    String aacess
    List utilizaodres = new ArrayList<String>()
    def  roles = new ArrayList()

    void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser
    }

    List getUtilizaodres() {
       utilizaodres.clear()
        def usersDB = Utilizador.findAll()
        for(Utilizador u in usersDB){
            utilizaodres.add(u.username)
        }
        return utilizaodres
    }

    ArrayList getRoles (){
        roles.clear()
        System.println("getRoles selectedUser="+selectedUser)
        Utilizador user = Utilizador.findByUsername(selectedUser)
        System.println("get Users user="+user)
        def ugr = UtilizadorRoleGroup.findAllByUtilizador(user)
        for (UtilizadorRoleGroup urg in ugr){
           roles.add(urg.roleGroup.name)
        }

        return roles
    }

boolean getAccess(String rol,String utl){
        List rolegs = new ArrayList<RoleGroup>()
        Utilizador user = Utilizador.findByUsername(utl)
        def ugr = UtilizadorRoleGroup.findAllByUtilizador(user)
        System.println(ugr)
        for (UtilizadorRoleGroup urg in ugr){
            rolegs.add(urg.roleGroup)

        }
        for(RoleGroup rg in rolegs){
            def roles =RoleGroupRole.findAllByRoleGroup(rg).role
            for (Role r in roles){
                if(r.authority.equals(rol)){return true}
            }
        }

        return aacess

    }
    boolean getAccess(String rol){
        List rolegs = new ArrayList<RoleGroup>()
        System.println("utilizaodr service get Acess encryptionService.localUser.username = "+encryptionService?.localUser?.username)

        def ugr = UtilizadorRoleGroup?.findAllByUtilizador(encryptionService?.localUser)
        System.println(ugr)
        for (UtilizadorRoleGroup urg in ugr){
            rolegs.add(urg.roleGroup)

        }
        for(RoleGroup rg in rolegs){
            def roles =RoleGroupRole.findAllByRoleGroup(rg).role
            for (Role r in roles){
                if(r.authority.equals(rol)){return true}
            }
        }

        return aacess

    }

}
