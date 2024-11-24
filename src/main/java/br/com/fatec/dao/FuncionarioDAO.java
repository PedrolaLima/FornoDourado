/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.dao;

import br.com.fatec.data.Database;
import br.com.fatec.model.Funcionario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author alberto
 */
public class FuncionarioDAO implements DAO {
    
    private Funcionario f;
    private PreparedStatement ps;
    private ResultSet rs;

    @Override
    public boolean insert(Object model) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean update(Object model) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean delete(Object model) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Collection search(Object model) throws SQLException {
        
        Collection<Funcionario> r = new ArrayList<>();
        
        Database.connect();
        String sql="SELECT CODFUN FROM funcionario WHERE NOME =? AND SENHA = ?;";
        ps = Database.getConnection().prepareStatement(sql);
        
        
        rs=ps.executeQuery();
        
        if (rs.next()) {
            Funcionario f =new Funcionario(sql, rs.getString(1), rs.getString(sql));
            r.add(f);      
        }
        return r;
    }
    
}
