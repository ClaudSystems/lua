package fatura

import lua.SessionStorageService
import lua.estoque.itemProduto.ItemProduto
import lua.vendas.fatura.Fatura
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zul.ListModelList

class PrinterFaturaViewModel {
    SessionStorageService sessionStorageService
    private Fatura fatura
    private String original = "Original"
    private ListModelList<ItemProduto> itemProdutos

    ListModelList<ItemProduto> getItemProdutos() {
        if(itemProdutos==null){
            itemProdutos = new ListModelList<ItemProduto>(fatura.itemsProduto)
        }
        itemProdutos.sort{it.id}
        return itemProdutos
    }

    String getOriginal() {
        return original
    }

    void setOriginal(String original) {
        this.original = original
    }

    @Command
    @NotifyChange(["original"])
    def change(){
        if(original=="Original"){
            original ="Duplicado"
        }else if(original=="Duplicado"){
            original="Original"
        }
    }

    Fatura getFatura() {
        return fatura
    }

    void setFatura(Fatura  fatura) {
        this.fatura = fatura
    }

    @Init init() {
       def  fa = sessionStorageService.getFatura() as Fatura
        fatura = Fatura.findById(fa.id)

    }

}
