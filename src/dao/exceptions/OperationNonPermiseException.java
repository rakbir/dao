/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.exceptions;

/**
 *
 * @author RAKOTOARISOA
 */
public class OperationNonPermiseException extends Exception{
    public OperationNonPermiseException(){
        super("Cette opération n'est pas permise");
    }
}
