/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.data;

import br.com.fatec.model.Pedido;

/**
 *
 * @author Alberto
 */
public class PedidoHolder {
    private static Pedido p;

    public static Pedido getPedido() {
        return p;
    }

    public static void setPedido(Pedido p) {
        PedidoHolder.p = p;
    }
    
}
