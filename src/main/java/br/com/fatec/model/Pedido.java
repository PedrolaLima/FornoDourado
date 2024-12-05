/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model;

import br.com.fatec.data.FuncionarioHolder;
import java.time.LocalDate;
import java.util.HashMap;

/**
 *
 * @author Alberto
 */
public class Pedido {
    private static int gNumPed =0;
    private int numPed;
    private String pgto;
    private Funcionario caixa;
    private LocalDate data;
    private float desconto;
    private HashMap items = new HashMap();

    public Pedido() {
        this.data = LocalDate.now();
        gNumPed++;
        this.numPed=getNumPed();
    }

    public HashMap getItems() {
        return items;
    }

    public void setItem(Produto item,int qtd) {
        this.items.put(item,qtd);
    }
    
    public void removeItem(Produto item) {
        this.items.remove(item);
    }
    
    public int getNumPed() {
        return numPed;
    }

    public void setNumPed(int numPed) {
        this.numPed = numPed;
    }
    
    public int newNum(){
        return gNumPed++;
    }
    
    public void setNum(int n){
        gNumPed = n;
    }

    public String getPgto() {
        return pgto;
    }

    public void setPgto(String pgto) {
        this.pgto = pgto;
    }

    public Funcionario getCaixa() {
        return caixa;
    }

    public void setCaixa(Funcionario caixa) {
        this.caixa = caixa;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.numPed;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pedido other = (Pedido) obj;
        return this.numPed == other.numPed;
    }
    
    
}
