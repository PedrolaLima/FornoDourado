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
    
    public String name;
    private String password;
    private String salt;

    public Funcionario(String name,String psswd,String salt) {
        this.name = name;
        this.password = psswd;
        this.salt = salt;
    }
    
}
