package br.com.fatec.data;

import br.com.fatec.model.Funcionario;

public class FuncionarioHolder {
    public static Funcionario f;
    private static Funcionario u;

    public static Funcionario getF() {
        return f;
    }

    public static void setF(Funcionario f) {
        FuncionarioHolder.f = f;
    }

    public static void clear(){
        f =null;
    }

    public static boolean isEmpty(){
        if(f == null){
            return true;
        }
        return false;
    } 
    
    public static void setUser(Funcionario user){
        FuncionarioHolder.u = user;
    }
    
    public static Funcionario getUser(){
        return u;
    }
}
