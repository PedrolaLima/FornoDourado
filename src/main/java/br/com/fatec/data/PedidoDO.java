/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.data;

import br.com.fatec.Messenger;
import br.com.fatec.dao.ProdutoDAO;
import br.com.fatec.model.Pedido;
import br.com.fatec.model.Produto;
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
    
    public void addPedido(Pedido p){
        pedidos.add(p);
    }
    
    public void updatePedido(Pedido p){
        throw new UnsupportedOperationException("updatePedido não foi programado");
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
}
