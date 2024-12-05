/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.data;

import br.com.fatec.Messenger;
import br.com.fatec.dao.ProdutoDAO;
import br.com.fatec.model.Pedido;
import br.com.fatec.model.Produto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alberto
 */
public class PedidoDO {
    private static ArrayList<Pedido> pedidos = new ArrayList();
    
    public void retrievePedido() throws SQLException{
        ArrayList<Pedido> tmp = new ArrayList();
        Database.connect();
        PreparedStatement ps;
        ResultSet rs;
        boolean ok = true;
        try {
            ps = Database.getConnection().prepareStatement("SELECT CODPED,DATAPED,PGTO FROM pedidos;");
             rs = ps.executeQuery();
            while(rs.next()){
                Pedido t = new Pedido();
                t.setNumPed(rs.getInt(1));
                t.setData(rs.getDate(2).toLocalDate());
                t.setPgto(rs.getString(3));
                tmp.add(t);
            }
        }catch(SQLException e){
            Messenger.error("Erro no banco", "Não foi possível recuperar pedidos anteriores");
            ok =false;
        }finally{
            Database.close();
        }
        Database.connect();
        try {
            for(Pedido p : tmp){
                
                ps = Database.getConnection().prepareStatement("SELECT A.CODPROD,A.QTD,B.CAT,B.DISP,B.NOME,B.VALUNI FROM pedidositem AS A INNER JOIN "
                        + "produtos AS B ON A.CODPROD = B.COPROD WHERE A.CODPED + ?;");
                ps.setInt(1,p.getNumPed());
                rs = ps.executeQuery();
                while(rs.next()){
                    Produto pr = new Produto(rs.getString(5),rs.getInt(1),rs.getFloat(6),rs.getString(3),rs.getBoolean(4));
                    p.setItem(pr, rs.getInt(2));
                }
                
            }
        }catch(SQLException e){
            Messenger.error("Erro no banco", "Não foi possível recuperar pedidos anteriores");
            ok =false;
        }finally{
            Database.close();
        }
        
        if(ok){
            pedidos.addAll(tmp);
        }
        
    }
    
    public void addPedido(Pedido p){
        pedidos.add(p);
    }
    
    public void updatePedido(Pedido p,int numPed){
        pedidos.set(numPed, p);
    }
    
    public boolean deletePedido(Pedido p){
        for (Pedido o : pedidos) {
            if(o.equals(p)){
                pedidos.remove(p);
                return true;
            }
        }
        return false;
    }
    
    public int getAmountProdToday(){
        int t =0;
        for(Pedido p : pedidos){
            if(p.getData().equals(LocalDate.now())){
                 HashMap i = p.getItems();
                 for(var v : i.values()){
                    t += (int)v;
                }
            }
        }
        return t;
    }
    
    public int getAmountProd(){
        int t =0;
        for(Pedido p : pedidos){
            if(p.getData().equals(LocalDate.now())){
                 HashMap i = p.getItems();
                 for(var v : i.values()){
                    t += (int)v;
                }
            }
        }
        return t;
    }
    
    public float getAmountSold(){
        float t = 0;
        ProdutoDAO pdo = new ProdutoDAO();
        loop:
        for(Pedido p : pedidos){
            HashMap i = p.getItems();
            for(Object e : i.entrySet()){
                Map.Entry y = (Map.Entry)e;
                float tmp;
                    try {
                        tmp = pdo.getValue(((Produto)y.getKey()).getCod());
                    } catch (SQLException ex) {
                        Messenger.error("Erro no banco", "Não foi possivel obter o valor total de vendas");                        
                        break loop;                         
                    }
                t += tmp*(int)y.getValue();
            }
            
        }
        return t;
    } 
    
    public float getAmountSoldToday(){
        float t = 0;
        ProdutoDAO pdo = new ProdutoDAO();
        loop:
        for(Pedido p : pedidos){
            if(p.getData().equals(LocalDate.now())){
                 HashMap i = p.getItems();
                 for(Object e : i.entrySet()){
                   Map.Entry y = (Map.Entry)e;
                   float tmp;
                     try {
                         tmp = pdo.getValue(((Produto)y.getKey()).getCod());
                     } catch (SQLException ex) {
                         Messenger.error("Erro no banco", "Não foi possivel obter o valor total de vendas");                        
                         break loop;                         
                     }
                   t += tmp*(int)y.getValue();
                }
            }
        }
        return t;
    }
    
    public float getValue(Pedido p){
        float res =0;
        HashMap i = p.getItems();
        ProdutoDAO pdo = new ProdutoDAO();
        boolean problem = false;
        
        for(Object e : i.entrySet()){
            Map.Entry y = (Map.Entry)e;
            float tmp;
            try {
                tmp = pdo.getValue(((Produto)y.getKey()).getCod());
            } catch (SQLException ex) {
                    problem = true;
                    Messenger.error("Erro no banco", "Não foi possivel obter o valor dos produtos listados");                        
                    break;                         
            }
            res += tmp*(int)y.getValue();    
        }
        if(!problem){
            return res;
        }
        return 0;
    }
}
