package lua.produto

/**
 * Created by Claudino on 05/04/2016.
 */
class ProdutoFilter {
    private String codigo
    private String nome
    private String descricao

    public String getCodigo (){
        return  codigo
    }

    public String getNome (){
        return  nome
    }

    public String getDescricao (){
        return  descricao
    }



    public void setCodigo(String codigo) {
        this.codigo = codigo==null?"":codigo.trim()
    }

    public void setNome(String nome) {
        this.nome = nome==null?"":nome.trim()
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao==null?"":descricao.trim();
    }
}
