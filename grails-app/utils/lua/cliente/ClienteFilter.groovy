package lua.cliente



class ClienteFilter {

    private String codigo="", nome="", nuit=""

    public String getCodigo() {
        return codigo
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo==null?"":codigo.trim()
    }

    public String getNome() {
        return nome
    }

    public void setNome(String nome) {
        this.nome = nome==null?"":nome.trim()
    }

    public String getNuit() {
        return nuit
    }

    public void setNuit(String nuit) {
        this.nuit = nuit==null?"":nuit.trim()
    }
}
