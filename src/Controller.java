/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jay
 */
public class Controller {
    
        
    public static void main(String[]arg){
        String sr = "Enter the Filename";
        System.out.println(sr);
        Terminal terminal = new Terminal();
        Generator trial = new Generator(terminal.username());
        trial.wsdlGen(trial.filename);
    }
    
}
