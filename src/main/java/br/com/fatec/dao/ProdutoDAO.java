/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.dao;

import br.com.fatec.Messenger;
import br.com.fatec.data.Database;
import br.com.fatec.model.Funcionario;
import br.com.fatec.model.Produto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alberto
 */
public class ProdutoDAO implements DAO<Produto> {

    private PreparedStatement ps;
    private ResultSet rs;

    @Override
    public boolean insert(Produto model) throws SQLException {
        int res = 0;
        Database.connect();
        String sql = "INSERT INTO produtos(CODPROD,NOME,VALUNI,CAT,DISP) VALUES(?,?,?,?,?)";
        ps = Database.getConnection().prepareStatement(sql);

        ps.setInt(1, model.getCod());
        ps.setString(2, model.getNome());
        ps.setFloat(3, model.getPreco());
        ps.setString(4, model.getCat());
        ps.setBoolean(5, model.isDisp());
        //ps.setString(5,  model.getImg());

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
    public boolean update(Produto model, String pk) throws SQLException {
        int res = 0;
        Database.connect();
        String sql = "UPDATE produtos SET "
                + "NOME = ?, VALUNI = ?, CAT = ?, DISP = ? "
                + "WHERE CODPROD = ?;";
        ps = Database.getConnection().prepareStatement(sql);

        ps.setString(1, model.getNome());
        ps.setFloat(2, model.getPreco());
        ps.setString(3, model.getCat());
        ps.setBoolean(4, model.isDisp());
        ps.setInt(5, Integer.parseInt(pk));

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
    public boolean delete(String pk) throws SQLException {
        int res = 0;
        Database.connect();
        String sql = "DELETE FROM produtos WHERE CODPROD = ?";
        ps = Database.getConnection().prepareStatement(sql);

        ps.setInt(1, Integer.parseInt(pk));

        try {
            res = ps.executeUpdate();
        } catch (SQLException e) {
            Database.close();
            Messenger.error("Erro no banco", e.getMessage());
            throw e;
        }

        Database.close();
        return res != 0;
    }

    @Override
    public Collection<Produto> search(String Field, String Value) throws SQLException {
        ArrayList<Produto> r = new ArrayList<>();
        try {
            Database.connect();
            String sql = "SELECT CODPROD,NOME,VALUNI,CAT,DISP FROM produtos WHERE " + Field + " = ?;";
            ps = Database.getConnection().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Produto f = new Produto(rs.getString(2), rs.getInt(1), rs.getFloat(3), rs.getString(4), rs.getBoolean(5));
                r.add(f);
            }

        } catch (SQLException e) {
            Messenger.error("Erro no banco", e.getMessage());
        }
        return r;
    }

    @Override
    public ArrayList<Produto> getAll() {
        ArrayList<Produto> r = new ArrayList<>();
        try {
            Database.connect();
            String sql = "SELECT CODPROD,NOME,VALUNI,CAT,DISP FROM produtos";
            ps = Database.getConnection().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Produto f = new Produto(rs.getString(2), rs.getInt(1), rs.getFloat(3), rs.getString(4), rs.getBoolean(5));
                r.add(f);
            }

        } catch (SQLException e) {
            Messenger.error("Erro no banco", e.getMessage());
        }
        return r;
    }
    
    public int getAmount() throws SQLException{
        try {
            Database.connect();
            String sql = "SELECT COUNT(CODPROD) FROM produtos;";
            ps = Database.getConnection().prepareStatement(sql);
            rs = ps.executeQuery();
            return rs.getInt(1);
        } finally{
            Database.close();
        }
    }
    
    public float getValue(int cod) throws SQLException{
        try {
            Database.connect();
            String sql = "SELECT VALUNI FROM produtos WHERE CODPROD = ?;";
            ps = Database.getConnection().prepareStatement(sql);
            ps.setInt(1, cod);
            rs = ps.executeQuery();
            return rs.getFloat(1);
        } finally{
            Database.close();
        }
    }
    
    //Arrumar função getTable
    /* public List<Produto> getTable() {
        List<Produto> produtos = new ArrayList<>();
        try {
            Database.connect();
            String sql = "SELECT NOME, NOME, CARGO, EMAIL, STATUS FROM funcionarios";
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
     */
}
