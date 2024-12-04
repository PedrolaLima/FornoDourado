package br.com.fatec.data;

import br.com.fatec.model.Produto;

public class ProdutoHolder {
    public static Produto p;

    public static Produto getP() {
        return p;
    }

    public static void setP(Produto p) {
        ProdutoHolder.p = p;
    }

    public static void clear(){
        p =null;
    }

    public static boolean isEmpty(){
        if(p == null){
            return true;
        }
        return false;
    }
}
