/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jay
 */

import java.util.Scanner;

public class Terminal {
    
    Scanner input;     
    
    public String username(){   
    this.input= new Scanner(System.in);
    String entered= input.nextLine();    
    return entered;
    }
    
}
