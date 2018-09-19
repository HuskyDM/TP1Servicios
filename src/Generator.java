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

    public void example() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("HolaMundo.wsdl"));
            BufferedWriter wr = new BufferedWriter(new FileWriter(this.name));

            String line;
            while ((line = br.readLine()) != null) {
                wr.write(line);
                wr.newLine();
            }
            wr.close();
        } catch (Exception e) {
            System.out.println("Error: Unable to create file");
        }
    }

    //Makes the header of the wsdl
    public void genHeader(BufferedWriter wr, String className) {
        try {
            wr.write("<?xml version=\"1.0\"?>");
            wr.newLine();
            wr.write("<definitions name=\"" + className + "\"");
            wr.newLine();
            wr.write("targetNamespace=\"urn:" + className + "\"");
            wr.newLine();
            wr.write("xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\"");
            wr.newLine();
            wr.write(" xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\"");
            wr.newLine();
            wr.write("xmlns:tns=\"urn:" + className + "\"");
            wr.newLine();
            wr.write("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
            wr.newLine();
            wr.write("xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"");
            wr.newLine();
            wr.write(" xmlns=\"http://schemas.xmlsoap.org/wsdl/\">");
            wr.newLine();
        } catch (Exception e) {
            System.out.println("Error: Cannot Write File");
            e.printStackTrace();
            System.out.println(e);
        }
    }

    //Makes the types of the wsdl
    public void genTypes(BufferedWriter wr, String namespace, String name, String type) {

        try {

            wr.write(" <types xmlns=\"http://schemas.xmlsoap.org/wsdl/\">");
            wr.newLine();
            wr.write(" <xsd:schema targetNamespace=\"urn:" + namespace + "\">");
            wr.newLine();

            for (int i = 0; i < 1; ++i) {
                wr.write("<xsd:element name=\"" + name + "\">");
                wr.newLine();
                wr.write("<xsd:complexType>");
                wr.newLine();
                wr.write("<xsd:sequence>");
                wr.newLine();
                wr.write("<xsd:element name=\"" + name + "\" type=\"xsd:" + type + "\"/>");
                wr.newLine();
                wr.write("</xsd:sequence>");
                wr.newLine();
                wr.write("</xsd:complexType>");
                wr.newLine();
                wr.write(" </xsd:element>");
                wr.newLine();
            }
            
            wr.write("</xsd:schema>");
            wr.newLine();
            wr.write("</types>");
        } catch (Exception e) {
            System.out.println("Error: Cannot Write File");
            e.printStackTrace();
            System.out.println(e);
        }

    }

    //Makes the messages of the wsdl
    public void genMessage(BufferedWriter wr, String methodName, String elementName) {
        try{
        wr.write("<message name=\""+methodName+"\">");
        wr.write("<part name=\"parameters\" element=\"tns:"+elementName+"\" />");
        wr.write(" </message>");
        }
        catch(Exception e){        
        System.out.println("Error: Cannot Write File");
        e.printStackTrace();
        System.out.println(e);
        } 
    }

    //Makes the Port of the wsdl
    public void genPort(BufferedWriter wr, String namespace, String operation, String request, String response) {
    try{
        wr.write("<portType name=\""+namespace+"Port\">");
        wr.write("<operation name=\""+operation+"\">");
        wr.write("<input message=\"tns:"+request+"\" />");
      wr.write("<output message=\"tns:"+response+"\" />");
     wr.write("</operation>");
      wr.write("</portType>");
    }
    catch(Exception e){
     System.out.println("Error: Cannot Write File");
        e.printStackTrace();
        System.out.println(e);
    }
    }

    //Makes the Binding of the wsdl
    public void genBinding() {
    }

    public void wsdlGen() {
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter(this.name));
            //types
            genHeader(wr, "Ejemplo");
            genTypes(wr, "Ejemplo", "Ejemplo", "string");

            wr.close();
        } catch (Exception e) {
            System.out.println("Error: Cannot Write File");
            e.printStackTrace();
            System.out.println(e);
        }

    }

}

/*
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

*/