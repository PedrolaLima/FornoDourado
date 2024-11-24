/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.fatec.dao;

import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author alberto
 */
public interface DAO <T>{
    public boolean insert(T model) throws SQLException;
    public boolean update(T model) throws SQLException;
    public boolean delete(T model) throws SQLException;
    public Collection<T> search(T model) throws SQLException;
}
