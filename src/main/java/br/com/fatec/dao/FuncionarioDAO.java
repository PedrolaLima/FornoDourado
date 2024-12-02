/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.dao;

import br.com.fatec.Security;
import br.com.fatec.data.Database;
import br.com.fatec.model.Funcionario;

import java.sql.*;
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
        //String sql ="INSERT CPF,NOME,NASC,CARGO,EMAIL,CEP,ENDERECO,CIDADE,UF,STATUS,IMG INTO funcionarios VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        String sql ="INSERT INTO funcionarios(CPF,NOME,NASC,CARGO,EMAIL,CEP,ENDERECO,CIDADE,UF,STATUS)  VALUES(?,?,?,?,?,?,?,?,?,?)";
        ps = Database.getConnection().prepareStatement(sql);

        ps.setString(1,model.getCpf());
        ps.setString(2,model.getName());
        ps.setDate(3, Date.valueOf(model.getBirth()));
        ps.setString(4, model.getOccupation());
        ps.setString(5, model.getEmail());
        ps.setString(6,model.getCep());
        ps.setString(7, model.getEndereco());
        ps.setString(8, model.getCidade());
        ps.setString(9, model.getUf());
        ps.setBoolean(10, model.isStatus());
        //ps.setString(11,  model.getImg());

       
        try {
            res = ps.executeUpdate();  
        } catch (SQLException e) {
            Database.close();
            throw e;
        }
        
        Database.close();
        return res != 0;
    }

    @Override
    public boolean update(Funcionario model,String cpf) throws SQLException {
        int  res = 0;
        Database.connect();
        String sql ="INSERT CPF,NOME,NASC,CARGO,EMAIL,CEP,ENDERECO,CIDADE,UF,STATUS,IMG INTO funcionarios VALUES(?,?,?,?,?,?,?,?,?,?,?) WHERE CPF = ?";
        ps = Database.getConnection().prepareStatement(sql);

        ps.setString(1,model.getCpf());
        ps.setString(2,model.getName());
        ps.setDate(3, Date.valueOf(model.getBirth()));
        ps.setString(4, model.getOccupation());
        ps.setString(5, model.getEmail());
        ps.setString(6,model.getCep());
        ps.setString(7, model.getEndereco());
        ps.setString(8, model.getCidade());
        ps.setString(9, model.getUf());
        ps.setBoolean(10, model.isStatus());
        ps.setString(11,  model.getImg());
        ps.setString(12,cpf);
        
        try {
            res = ps.executeUpdate();  
        } catch (SQLException e) {
            Database.close();
            throw e;
        }
        
        Database.close();
        return res != 0;
    }

    @Override
    public boolean delete(String cpf) throws SQLException {
        int  res = 0;
        Database.connect();
        String sql ="DELETE FROM funcionarios WHERE CPF = ?";
        ps = Database.getConnection().prepareStatement(sql);
        
        ps.setString(1, cpf);
        
        try {
            res = ps.executeUpdate();  
        } catch (SQLException e) {
            Database.close();
            throw e;
        }
        
        Database.close();
        return res != 0;
    }

    @Override
    public Collection<Funcionario> search(String field,String value) throws SQLException {
        Collection<Funcionario> r = new ArrayList<>();
        
        Database.connect();
        String sql="SELECT CPF,NOME,NASC,CARGO,EMAIL,CEP,ENDERECO,CIDADE,UF,STATUS,IMG FROM funcionarios WHERE "+field+" = ?;";
        ps = Database.getConnection().prepareStatement(sql);

        ps.setString(1,value);
        
        try {
            rs=ps.executeQuery();  
        } catch (SQLException e) {
            Database.close();
            throw e;
        }

        while (rs.next()) {
            Funcionario f = new Funcionario(rs.getString(1),rs.getString(2),
                    rs.getDate(3).toLocalDate(),rs.getString(4),rs.getString(4),rs.getString(5),
                    rs.getString(6),rs.getString(7),rs.getString(8), rs.getBoolean(9), rs.getString(10));
            r.add(f); 
        }

        Database.close();
        return r;
    }
    
    public boolean login(String user,String psswd) throws SQLException{

        Collection<Funcionario> f =  search("NOME",user);
        String cpf = "";
        if(!f.isEmpty()){
            Iterator<Funcionario> r= f.iterator();
            Funcionario fu = r.next();
            cpf=fu.getCpf();
        }else{
            Collection<Funcionario> u =search("EMAIL",user);
            Iterator<Funcionario> r= u.iterator();
            if(!u.isEmpty()) {
                Funcionario fu = r.next();
                cpf = fu.getCpf();
            }
        }
        if(!cpf.isEmpty()) {
            try {
                Database.connect();
                String sql = "SELECT HASH,SALT FROM shadow WHERE CPF = ?";
                ps = Database.getConnection().prepareStatement(sql);

                ps.setString(1, cpf);

                rs = ps.executeQuery();
                if (rs.next()) {
                    if (Security.hashPassword(psswd, rs.getString(2)).equals(rs.getString(1))) {

                        return true;
                    }
                }

            } catch (SQLException ex) {
                Database.close();
                throw new RuntimeException(ex);
            }
        }
        return false;
    }
}
