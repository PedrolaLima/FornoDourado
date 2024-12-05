/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.data;

import br.com.fatec.model.Pedido;
import java.util.ArrayList;

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
}
