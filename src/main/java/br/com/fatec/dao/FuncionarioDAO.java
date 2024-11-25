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
import java.util.Iterator;

/**
 *
 * @author alberto
 */
public class FuncionarioDAO implements DAO <Funcionario> {
    
    private PreparedStatement ps;
    private ResultSet rs;

    @Override
    public boolean insert(Funcionario model) throws SQLException {
        int  res = 0;
        Database.connect();
        String sql ="INSERT NOME,SENHA,SALT funcionario INTO VALUES(?,?,?)";
        ps = Database.getConnection().prepareStatement(sql);
        
        ps.setString(0, model.getName());
        ps.setString(1, model.getPassword());
        ps.setString(2, model.getSalt());
       
        try {
            ps.executeUpdate();  
        } catch (SQLException e) {
            Database.close();
            throw e;
        }
        
        Database.close();
         if(res==0){
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Funcionario model,int pk) throws SQLException {
        int  res = 0;
        Database.connect();
        String sql ="INSERT NOME,SENHA,SALT funcionario INTO VALUES(?,?,?) WHERE CODFUN = ?";
        ps = Database.getConnection().prepareStatement(sql);
        
        ps.setString(0, model.getName());
        ps.setString(1, model.getPassword());
        ps.setString(2, model.getSalt());
        ps.setInt(3, pk);
        
        try {
            res = ps.executeUpdate();  
        } catch (SQLException e) {
            Database.close();
            throw e;
        }
        
        Database.close();
        if(res==0){
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(int pk) throws SQLException {
        int  res = 0;
        Database.connect();
        String sql ="DELETE FROM funcionario WHERE CODFUN = ?";
        ps = Database.getConnection().prepareStatement(sql);
        
        ps.setInt(0, pk);
        
        try {
            res = ps.executeUpdate();  
        } catch (SQLException e) {
            Database.close();
            throw e;
        }
        
        Database.close();
        if(res==0){
            return false;
        }
        return true;
    }

    @Override
    public Collection search(Collection<String> field,Collection<String> value) throws SQLException {
        
        Collection<Funcionario> r = new ArrayList<>();
        
        Database.connect();
        String sql="SELECT NOME,SALT FROM funcionario WHERE ? = ?;";
        ps = Database.getConnection().prepareStatement(sql);
        
        String s = "";
        
        for (Iterator<String> iterator = field.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            if(iterator.hasNext()){
                s = s + next + ",";
            }else{
                s = s + next;
            }
        }
        
        ps.setString(0,s);
        
        s="";
        
        for (Iterator<String> iterator = value.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            if(iterator.hasNext()){
                s = s + next + ",";
            }else{
                s = s + next;
            }
        }
        
        ps.setString(1,s);
        
        try {
            rs=ps.executeQuery();  
        } catch (SQLException e) {
            Database.close();
            throw e;
        }
        Database.close();
        
        if (rs.next()) {
            Funcionario f = new Funcionario(rs.getString(1), rs.getString(2));
            r.add(f);      
        }
        return r;
    }

    @Override
    public int getCod(Funcionario model) throws SQLException {
        
        String sql ="SELECT CODFUN FROM funcionario WHERE NAME=?;";
        Database.connect();
        ps = Database.getConnection().prepareStatement(sql);
        
        ps.setString(0, model.getName());
        
        try {
            rs=ps.executeQuery();  
        } catch (SQLException e) {
            Database.close();
            throw e;
        }
        
        if (rs.next()) {
            return rs.getInt(1);
        }
        
        return 0;
    }
    
}
