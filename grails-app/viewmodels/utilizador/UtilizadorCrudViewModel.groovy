package utilizador

import base.EncryptionService
import lua.security.UtilizadorService
import lua.security.RoleGroup
import lua.security.Utilizador
import lua.security.UtilizadorRoleGroup
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList


class UtilizadorCrudViewModel {
    EncryptionService encryptionService
    private ListModelList<String> utilizadores
    private ListModelList<RoleGroup> roleGroups
    private String info
    def  roles = new ArrayList()
    private ListModelList<String> roles = new ArrayList<>()
    private ListModelList<String> menus = new ArrayList<>()
    Utilizador utilizador
    private String selectedUtilizador
    private RoleGroup selectedRoleGroup
    private String palavra
    private String email
    private String selectedMenu

    private String filter
    private boolean ativo =true
    private boolean editView =false
    private Boolean btsalvar =false

    String getInfo() {
        return info
    }

    String getSelectedMenu() {
        return selectedMenu
    }



    boolean getEditView() {
        return editView
    }

    RoleGroup getSelectedRoleGroup() {
        return selectedRoleGroup
    }

    void setSelectedRoleGroup(RoleGroup selectedRoleGroup) {
        this.selectedRoleGroup = selectedRoleGroup
    }

    ListModelList<RoleGroup> getRoleGroups() {
        if(roleGroups.equals(null)){
            roleGroups = new ListModelList<RoleGroup>(RoleGroup.findAll())
        }
        return roleGroups
    }

    void setRoleGroups(ListModelList<RoleGroup> roleGroups) {
        this.roleGroups = roleGroups
    }

    ListModelList getRoles() {
        return roles
    }

    String getPalavra() {
        return palavra
    }

    void setPalavra(String palavra) {
        this.palavra = palavra
    }

    String getEmail() {
        return email
    }

    void setEmail(String email) {
        this.email = email
    }

    public  Boolean getBtsalvar() {
        return btsalvar
    }


    @NotifyChange(["btsalvar","roles","editView"])
    @Command
    void showItem(){
        utilizador=Utilizador.findByUsername(selectedUtilizador)
      // utilizadorService.selectedUser=selectedUtilizador

        def ugr = UtilizadorRoleGroup.findAllByUtilizador(utilizador)
        for (UtilizadorRoleGroup urg in ugr){
            roles.add(urg.roleGroup.name)
        }


        btsalvar = false
        editView = true
    }

    @NotifyChange(["selectedUtilizador","btsalvar"])
    @Command
    void addUtilizador(){
        Utilizador c = new Utilizador()
        selectedUtilizador = " "

        btsalvar= true


    }




    @NotifyChange(["selectedUtilizador","utilizadores","info"])
    @Command
    void salvarUtilizador(){
        while (selectedUtilizador.equals(null)||palavra.equals(null)||email.equals(null)){
           //MultiLineMessageBox.show("Preecha os campos!", "Lua", 1, MultiLineMessageBox.ERROR)
            info="Preecha os campos!"
            return
        }
        if (Utilizador.findAllByUsername(selectedUtilizador)) {
          //  MultiLineMessageBox.show("Este nome ja existe!", "Lua", 1, MultiLineMessageBox.ERROR)
            info="Este Utilizador ja existe!"
            return
        }
        Utilizador user = new Utilizador()
        user.username=selectedUtilizador
        user.password=palavra
        user.email=email
        user.save(flush: true)
        utilizadores.add(user.username)

        // MultiLineMessageBox.show("Utilizador "+user.username+" foi criado com sucesso!", "Lua", 1,  MultiLineMessageBox.INFORMATION)
        info= "Utilizador "+user.username+" foi criado com sucesso!"
        selectedUtilizador=null
    }



    @NotifyChange(["selectedUtilizador","utilizadores","info"])
    @Command
    void updateUtilizador(){
        while (selectedUtilizador.equals(null)||palavra.equals(null)||email.equals(null)){

           // MultiLineMessageBox.show("Preecha os campos!", "Lua", 1, MultiLineMessageBox.ERROR)
            info="Preecha os campos!"
            return
        }
        Utilizador c = Utilizador.findByUsername(selectedUtilizador)
        c.password = palavra
        c.username = selectedUtilizador
        c.email = email
        c.enabled = ativo
        c.save(flush: true)
        selectedUtilizador = null
       // MultiLineMessageBox.show("Os dados do Utilizador "+c.username+" foram actualizados com sucesso!", "Lua", 1,  MultiLineMessageBox.INFORMATION)
        info="Os dados do Utilizador "+c.username+" foram actualizados com sucesso!"
    }

    public String getFilter() {
        return filter
    }

    @NotifyChange
    void setFilter(String filter) {
        this.filter = filter
    }

    String getSelectedUtilizador(){
        if(selectedUtilizador==null){
        }
        return  selectedUtilizador
    }
    void setSelectedUtilizador (String utilizador){
        this.selectedUtilizador = utilizador
    }


    ListModelList<String> getUtilizadores() {
        if (utilizadores == null) {
            utilizadores = new ListModelList<String>()
        }
        def utilizadoresDb = Utilizador.all
        for(Utilizador u in utilizadoresDb){
            utilizadores.add(u.username)
        }
        return utilizadores
    }


    @NotifyChange("utilizadores")
    @Command
    public void doSearch() {

        utilizadores.clear()
        def utilizadoresDb = Utilizador.all
        def users = new ArrayList()
        for(Utilizador u in utilizadoresDb){
            users.add(u.username)
        }
        List<String> allItems = users
        if (filter == null || "".equals(filter)) {
            utilizadores.addAll(allItems)
        } else {
            for (String c : allItems) {
                if (

                c.toLowerCase().indexOf(filter.toLowerCase()) >= 0) {
                    utilizadores.add(c)
                }
            }
        }
    }

    @Command
    @NotifyChange('info')
    def confirmar(){
        info="Se tem certeza que pretende eliminar este Utilizador por favor click duas vezes sobre o botão Eliminar!"
    }

    @Command
    @NotifyChange(["selectedUtilizador","utilizadores","info"])
    public void deleteUtilizador (){
        try {

            while (selectedUtilizador.equals(null)){
               // MultiLineMessageBox.show("Selecione O Utilizador que deseja eliminar!", "Lua", 1, MultiLineMessageBox.ERROR)
                info="Selecione O Utilizador que deseja eliminar!"
                return
            }
           /* MultiLineMessageBox.show("Tem certeza que deseja eliminar este Utilizador?", "Execute?", MultiLineMessageBox.YES | MultiLineMessageBox.NO,
                    MultiLineMessageBox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (MultiLineMessageBox.ON_YES.equals(evt.getName())) {
                        if(!UtilizadorRoleGroup.findByUtilizador(utilizador)?.roleGroup?.name.equals("rg_full")){
                            utilizador.delete()
                        }

                        utilizadores.remove(selectedUtilizador)
                        selectedUtilizador = null
                    }
                }
            }
            )*/
            if(!UtilizadorRoleGroup?.findByUtilizador(utilizador)?.roleGroup?.name?.equals("rg_full")){
                utilizador.delete()
            }

            utilizadores.remove(selectedUtilizador)
            selectedUtilizador = null

        }catch (Exception e ){
          //  MultiLineMessageBox.show("Selecione Um Utilizador!")
            info = "Selecione Um Utilizador!"
        }


    }

    @Command
    @NotifyChange(["selectedUtilizador","editView","btsalvar"])
    void fecharEditor(){
        selectedUtilizador = null
        selectedRoleGroup = null
        btsalvar = false
        editView = false

    }

    @Command
    @NotifyChange(["selectedUtilizador"])
    def linkUtilizadorRoleGroup(){

        UtilizadorRoleGroup.create utilizador, selectedRoleGroup, true

       //   MultiLineMessageBox.show("Os papeis (Roles) do Utilizador "+utilizador.username+" foram actualizados com sucesso!", "Lua", 1,  MultiLineMessageBox.INFORMATION)
        info ="Os papeis (Roles) do Utilizador "+utilizador.username+" foram actualizados com sucesso!"
    }

    @Init init() {
        selectedUtilizador = ""
    }


}
