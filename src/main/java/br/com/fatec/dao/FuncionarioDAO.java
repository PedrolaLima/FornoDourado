/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.dao;

import br.com.fatec.Messenger;
import br.com.fatec.Security;
import br.com.fatec.data.Database;
import br.com.fatec.model.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
    public boolean update(Funcionario f, String cpf) throws SQLException {
        int  res = 0;
        Database.connect();
        String sql = "UPDATE funcionarios SET "
                + "nome = ?, nasc = ?, cargo = ?, email = ?, cep = ?, endereco = ?, cidade = ?, uf = ?, status = ? "
                + "WHERE cpf = ?;";
        PreparedStatement ps = Database.getConnection().prepareStatement(sql);
        ps.setString(1, f.getName());
        ps.setObject(2, f.getBirth());
        ps.setString(3, f.getOccupation());
        ps.setString(4, f.getEmail());
        ps.setString(5, f.getCep());
        ps.setString(6, f.getEndereco());
        ps.setString(7, f.getCidade());
        ps.setString(8, f.getUf());
        ps.setBoolean(9, f.isStatus());
        ps.setString(10, f.getCpf());
        
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
            Messenger.error("Erro no banco",e.getMessage());
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
            Messenger.error("Erro no banco",e.getMessage());
        }

        while (rs.next()) {
            Funcionario f = new Funcionario(rs.getString(1),rs.getString(2),
                    rs.getDate(3).toLocalDate(),rs.getString(4),rs.getString(5),rs.getString(6),
                    rs.getString(7),rs.getString(8),rs.getString(9), rs.getBoolean(10), rs.getString(11));
            r.add(f); 
        }

        Database.close();
        return r;
    }

    @Override
    public ArrayList<Funcionario> getAll() {
        ArrayList<Funcionario> r = new ArrayList<>();
        try {
            Database.connect();
            String sql = "SELECT CPF,NOME,NASC,CARGO,EMAIL,CEP,ENDERECO,CIDADE,UF,STATUS,IMG FROM funcionarios";
            ps = Database.getConnection().prepareStatement(sql);
            rs= ps.executeQuery();

            while (rs.next()) {
                Funcionario f = new Funcionario(rs.getString(1),rs.getString(2),
                        rs.getDate(3).toLocalDate(),rs.getString(4),rs.getString(4),rs.getString(5),
                        rs.getString(6),rs.getString(7),rs.getString(8), rs.getBoolean(9), rs.getString(10));
                r.add(f);
            }

        }catch (SQLException e){
            Messenger.error("Erro no banco",e.getMessage());
        }
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

    public List<Funcionario> getTable() {
        List<Funcionario> funcionarios = new ArrayList<>();
        try {
            Database.connect();
            String sql = "SELECT CPF, NOME, CARGO, EMAIL, STATUS FROM funcionarios";
            ps = Database.getConnection().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getString("CPF"),
                        rs.getString("NOME"),
                        null, // Se não houver data de nascimento na consulta
                        rs.getString("CARGO"),
                        rs.getString("EMAIL"),
                        null, // Se não houver endereço ou CEP na consulta
                        null,
                        rs.getBoolean("STATUS")
                );
                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            Messenger.error("Erro no banco", e.getMessage());
        }
        return funcionarios;
    }

}
