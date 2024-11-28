/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.dao;

import br.com.fatec.Security;
import br.com.fatec.data.Database;
import br.com.fatec.model.Funcionario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
        String sql ="INSERT * funcionario INTO VALUES(?,?,?,?,?)";
        ps = Database.getConnection().prepareStatement(sql);
        
        ps.setInt(0,model.getCpf());
        ps.setString(1,model.getName());
        ps.setString(2, model.getOccupation());
        ps.setString(3, model.getUsername());
        ps.setBoolean(4, model.isStatus());

       
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
    public boolean update(Funcionario model,int cpf) throws SQLException {
        int  res = 0;
        Database.connect();
        String sql ="INSERT NOME,CARGO,USUARIO,STATUS INTO funcionario VALUES(?,?,?,?) WHERE CPF = ?";
        ps = Database.getConnection().prepareStatement(sql);
        
        ps.setString(0,model.getName());
        ps.setString(1, model.getOccupation());
        ps.setString(2, model.getUsername());
        ps.setBoolean(3, model.isStatus());

        ps.setInt(4, cpf);
        
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
    public boolean delete(int cpf) throws SQLException {
        int  res = 0;
        Database.connect();
        String sql ="DELETE FROM funcionario WHERE CPF = ?";
        ps = Database.getConnection().prepareStatement(sql);
        
        ps.setInt(0, cpf);
        
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
        String sql="SELECT * FROM funcionario WHERE ? = ?;";
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
        
        while (rs.next()) {
            Funcionario f = new Funcionario(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getBoolean(5));
            r.add(f); 
        }
                 
        
        return r;
    }
    
    public boolean login(String name,String psswd) throws SQLException{
        Collection<Funcionario> f = new ArrayList<>();

        f = search(new ArrayList<>(Arrays.asList("NAME")),new ArrayList<>(Arrays.asList(name)));

        
        if(!f.isEmpty()){
            try {
                Database.connect();
                String sql = "SELECT SENHA SALT FROM shadow WHERE CPF = ?";
                ps = Database.getConnection().prepareStatement(sql);
                Iterator<Funcionario> r= f.iterator();
                ps.setInt(0, r.next().getCpf());
                    
                rs = ps.executeQuery();;
                
                if(Security.hashPassword(psswd,rs.getString(2)).equals(rs.getString(1))){
                    return true;
                }
                
            } catch (SQLException ex) {
                   Database.close();
                   throw ex;
            }         
        }
        return false;
    }
}
