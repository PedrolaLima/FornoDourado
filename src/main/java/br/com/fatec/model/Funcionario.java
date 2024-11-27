/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model;

/**
 *
 * @author alberto
 */
public class Funcionario {
    
    private String name;
    private int cod= 0;

    public Funcionario(int cod,String name) {
        this.name = name;
        this.cod = cod;
    }

    public int getCod() {
        return cod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}