/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

/**
 *
 * @author Zachary
 */
public interface TableItem {
    
    void SetItem(int col, Object value);
    Object GetItem(int col);
    Object GetWholeItem();
    
}
