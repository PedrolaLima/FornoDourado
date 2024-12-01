/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author alberto
 */
public class Funcionario {
    
    private String cpf;
    private String name;
    private LocalDate birth;
    private String occupation;
    private String email;
    private String cep;
    private String endereco;
    private String cidade ="";
    private String uf="";
    private String img = "";
    private boolean status;
    
    public Funcionario(String cpf, String name,LocalDate birth, String occupation, String email,
                       String cep,String endereco,String cidade,String uf,boolean status,String img) {
        this.name = name;
        this.birth=birth;
        this.cpf = cpf;
        this.occupation=occupation;
        this.email = email;
        this.cep=cep;
        this.endereco=endereco;
        this.cidade=cidade;
        this.uf=uf;
        this.status=status;
        this.img=img;
    }

    public Funcionario(String cpf, String name,LocalDate birth, String occupation, String email,
                       String cep,String endereco,boolean status) {
        this.name = name;
        this.birth=birth;
        this.cpf = cpf;
        this.occupation=occupation;
        this.email = email;
        this.cep=cep;
        this.endereco=endereco;
        this.status=status;
    }

    public Funcionario(String cpf, String name,LocalDate birth, String occupation, String email,
                       String cep,String endereco,String cidade,String uf,boolean status) {
        this.name = name;
        this.birth=birth;
        this.cpf = cpf;
        this.occupation=occupation;
        this.email = email;
        this.cep=cep;
        this.endereco=endereco;
        this.cidade=cidade;
        this.uf=uf;
        this.status=status;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public String getCep() {
        return cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public String getUf() {
        return uf;
    }

    public String getImg() {
        return img;
    }
}