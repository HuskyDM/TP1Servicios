/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.io.BufferedWriter;
import java.util.Scanner;

/**
 *
 * @author Jay
 */
public class Generator {

    FileInputStream file;
    FileOutputStream file2;
    String name;

    public Generator(String name) {
       this.name = name;
    }

    public void escritor() {
        try {
            file = new FileInputStream("input.txt");
            String str;
            BufferedReader br = new BufferedReader(new FileReader("input.java"));
            BufferedWriter wr = new BufferedWriter(new FileWriter(this.name));
            String line = null;
            wr.write("Teostra");
            wr.newLine();
            wr.write(br.readLine());
            wr.newLine();
            wr.write("Lunastra");
            wr.close();
        } catch (Exception e) {
            System.out.println("File Not Found");
        }
    }
    
    public void example(){
        try{
        BufferedReader br = new BufferedReader(new FileReader("HolaMundo.wsdl"));
        BufferedWriter wr = new BufferedWriter(new FileWriter(this.name));
               
        String line;
        while ((line = br.readLine()) != null) {
        wr.write(line);
        wr.newLine();
        }
        wr.close();
       }
       catch(Exception e){
       System.out.println("Error: Unable to create file");
       }
    }
    
    //Makes the header of the wsdl
    public void genHeader(){} 
    
    //Makes the types of the wsdl
    public void genTypes(BufferedWriter wr, String name, String type){
    
        try{
        wr.write("<xsd:element name=\""+name+"\">");
        wr.newLine();
        wr.write("<xsd:complexType>");
        wr.newLine();
        wr.write("<xsd:sequence>");
        wr.newLine();
        wr.write("<xsd:element name=\""+name+"\" type=\"xsd:"+type+"\"/>");
        wr.newLine();
        wr.write("</xsd:sequence>");
        wr.newLine();
        wr.write("</xsd:complexType>");
        wr.newLine();
        wr.write(" </xsd:element>");
        
        }
        catch(Exception e){
        
        }
        
    }
    
    //Makes the messages of the wsdl
    public void genMessage(){}
    
    //Makes the Port of the wsdl
    public void genPort(){}
    
    //Makes the Binding of the wsdl
    public void genBinding(){}
   
    
    public void wsdlGen(String namespace){
        try{
        BufferedWriter wr = new BufferedWriter(new FileWriter(this.name));
        //types
        wr.write(" <types xmlns=\"http://schemas.xmlsoap.org/wsdl/\">");
        wr.newLine();
        wr.write(" <xsd:schema targetNamespace=\"urn:"+namespace+"\">");
        wr.newLine();
        genTypes(wr, "Ejemplo","string");
        wr.newLine();
        wr.write("</xsd:schema>");
        wr.newLine();
        wr.write("</types>");
        wr.close();
        }catch(Exception e){
        
        }


       
      
      
    }

}
