/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.controller;

import br.com.fatec.dao.FuncionarioDAO;
import java.sql.SQLException;

/**
 *
 * @author alberto
 */
public class LoginController {
    public void login(){
        FuncionarioDAO fd = new FuncionarioDAO();
        try {
            if(fd.login("nome", "senha")){
                //TODO
            }
        } catch (SQLException e) {
            //TODO
        }
    }
}
