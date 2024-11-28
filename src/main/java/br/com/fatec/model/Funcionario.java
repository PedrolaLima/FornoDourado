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
    
    private int cpf= 0;
    private String name;
    private String occupation;
    private String username;
    private boolean status;
    
    public Funcionario(int cpf,String name,String occupation,String username,boolean status) {
        this.name = name;
        this.cpf = cpf;
        this.occupation=occupation;
        this.username=username;
        this.status=status;
    }

    public void setCpf(int cpf) {
        this.cpf = cpf;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    

    public int getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}