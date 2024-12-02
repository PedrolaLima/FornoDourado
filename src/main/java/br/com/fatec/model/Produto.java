/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model;

/**
 *
 * @author alberto
 */
public class Produto {
    private String nome;
    private int cod;
    private float preco;
    private boolean disp;

    public Produto(String nome, float preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public Produto(String nome, int cod, float preco, boolean disp) {
        this.nome = nome;
        this.cod = cod;
        this.preco = preco;
        this.disp=disp;
    }
    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public boolean isDisp() {
        return disp;
    }

    public void setDisp(boolean disp) {
        this.disp = disp;
    }
}
