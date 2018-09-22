
import java.io.*;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Generator {

    FileInputStream file;
    FileOutputStream file2;
    String filename;
    List<String> typeName;
    List<String> typeType;
    List<String> messageName;
    List<String> messageElement;
     List<String> opName;
     List<String> opMethod;
    List<String> bindOpName;

    public Generator(String name) { //name se envia como parametro desde controller
        this.filename = name;
    }
   

    //Genera la cabeza del documento
    //Recibe un string namespace que es el nombre de la clase
    public void genHeader(BufferedWriter wr, String namespace) {
        try {
            wr.write("<?xml version=\"1.0\"?>");
            wr.newLine();
            wr.write("<definitions name=\"" + namespace + "\"");
            wr.newLine();
            wr.write("targetNamespace=\"urn:" + namespace + "\"");
            wr.newLine();
            wr.write("xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\"");
            wr.newLine();
            wr.write(" xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\"");
            wr.newLine();
            wr.write("xmlns:tns=\"urn:" + namespace + "\"");
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

    //Genera los tipos de wsdl
    //Como pueden haber varios se llama a typeWriter que
    //Genera tantos types como sean necesarios
    public void genTypes(BufferedWriter wr, String namespace, List<String> typeName, List<String> typeType) {

        //There can be multiple types, should have an array loop
        try {

            wr.write(" <types xmlns=\"http://schemas.xmlsoap.org/wsdl/\">");
            wr.newLine();
            wr.write(" <xsd:schema targetNamespace=\"urn:" + namespace + "\">");
            wr.newLine();

           typeWriter(typeName, typeType, wr);

            wr.write("</xsd:schema>");
            wr.newLine();
            wr.write("</types>");
            wr.newLine();
        } catch (Exception e) {
            System.out.println("Error: Cannot Write File");
            e.printStackTrace();
            System.out.println(e);
        }

    }
    
    //Genera tantas etiquetas types a como hayan tipos
    //Todos los metodos tienen nombre pero algunos no tienen tipos por lo que se generan tantos como
    //hayan nombres disponibles en la lista
    public void typeWriter(List<String> typeName, List<String> typeType, BufferedWriter wr){
    try{
         for (int i = 0; i < typeName.size(); ++i) {
                wr.write("<xsd:element name=\"" + typeName.get(i) + "\">");
                wr.newLine();
                wr.write("<xsd:complexType>");
                wr.newLine();
                wr.write("<xsd:sequence>");
                wr.newLine();
                
                /*TODO: Nota: hay metodos sin tipos, hay que preguntar eso
                Asuma que name y type tienen la misma cantidad de elementos en sus
                listas y que no hay ningun nillable
                */
                wr.write("<xsd:element name=\"" + typeName.get(i) + "\" type=\"xsd:" + typeType.get(i) + "\"/>");
                
                wr.newLine();
                wr.write("</xsd:sequence>");
                wr.newLine();
                wr.write("</xsd:complexType>");
                wr.newLine();
                wr.write(" </xsd:element>");
                wr.newLine();
            }
    }
    catch(Exception e){
    e.printStackTrace();
    }
    }
    

    //Hace los mensajes de wsdl
    //Todos los mensajes tienen name y element
    //messageName y messageElement entonces tienen el mismo tamaño
    
    public void genMessage(BufferedWriter wr, List<String> messageName, List<String> messageElement) {
        try {               
            
            for(int i=0;i<messageName.size();++i){
            wr.write("<message name=\"" + messageName.get(i) + "\">");
            wr.newLine();
            
            //TODO parameters es igual en todo?
            wr.write("<part name=\"parameters\" element=\"tns:" + messageElement.get(i) + "\" />");
            
            wr.newLine();
            wr.write(" </message>");
            wr.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error: Cannot Write File");
            e.printStackTrace();
            System.out.println(e);
        }
    }

    //Genera la seccion de port
    //Como en types hay varias etiquetas de operacion
    //Solo hay dos listas, opName y opMethod ya que se le añader Request o Response dependiendo
    //si es Input o Output
    
    public void genPort(BufferedWriter wr, String namespace, List<String> opName, List<String> opMethod) {
        try {
            wr.write("<portType name=\"" + namespace + "Port\">");
            wr.newLine();
           
            opGen(opName, opMethod, wr);
            
            wr.write("</portType>");
            wr.newLine();
        } catch (Exception e) {
            System.out.println("Error: Cannot Write File");
            e.printStackTrace();
            System.out.println(e);
        }
    }
    
    public void opGen(List<String>opName, List<String> opMethod, BufferedWriter wr){
    
        try{ 
            for(int i=0;i<opName.size();++i){
            wr.write("<operation name=\"" + opName.get(i) + "\">");
            wr.newLine();
            wr.write("<input message=\"tns:" + opMethod.get(i) + "Request\" />");
            wr.newLine();
            wr.write("<output message=\"tns:" + opMethod.get(i) + "Response\" />");
            wr.newLine();
            wr.write("</operation>");
            wr.newLine();}
        }
        catch(Exception e){
        e.printStackTrace();
        }
        
    }
    

    //Gebera el binding del wsdl
    //Tiene tantas operaciones como hay metodos pero esa es la unica lista que se necesita
    public void genBinding(BufferedWriter wr, String namespace, List<String> bindOpName, String className) {
        try {
            //multiple operations, should have a loop
            wr.write("<binding name=\"" + namespace + "Binding\" type=\"tns:" + namespace + "Port\">");
            wr.newLine();
            wr.write("<soap:binding style=\"document\" transport=\"http://schemas.xmlsoap.org/soap/http\" />");
            wr.newLine();
           
            bindingWriter(namespace, className, bindOpName, wr);
            
            wr.write("</binding>");
            wr.newLine();
        } catch (Exception e) {
            System.out.println("Error: Cannot Write File");
            e.printStackTrace();
            System.out.println(e);
        }
    }
    
    public void bindingWriter(String namespace, String className, List<String> bindOpName, BufferedWriter wr){
    try{
        for(int i=0; i<bindOpName.size();++i){
            wr.write("<operation name=\"" + bindOpName.get(i) + "\">");
            wr.newLine();
            wr.write("<soap:operation soapAction=\"urn:" + namespace + "#" + className + "#" + bindOpName.get(i) + "\" style=\"document\" />");
            wr.newLine();
            //Es ECCIHolaMundo#HolaMundo#salude un namespace#clase#metodo?
            wr.write("<input>");
            wr.newLine();
            wr.write("<soap:body use=\"literal\" />");
            wr.newLine();
            wr.write("</input>");
            wr.newLine();
            wr.write(" <output>");
            wr.newLine();
            wr.write("<soap:body use=\"literal\" />");
            wr.newLine();
            wr.write("</output>");
            wr.newLine();
            wr.write(" </operation>");
            wr.newLine();
        }
    }
    catch(Exception e)
    {
     e.printStackTrace();
    }
    
    }
    
    //Genera la etiqueta de servicios
    //Es una etiqueta unica y solo requiere el nombre de la clase y el address del wsdl
    public void genService(BufferedWriter wr, String namespace, String address) {
        try {
            wr.write("<service name=\"" + namespace + "\">");
            wr.newLine();
            wr.write("<documentation />");
            wr.newLine();
            wr.write("<port name=\"" + namespace + "Port\" binding=\"tns:" + namespace + "Binding\">");
            wr.newLine();
            wr.write("<soap:address location=\"http://titanic.ecci.ucr.ac.cr:80/~bsolano/HolaMundoServiceDocumentLiteral/\" />");
            wr.newLine();
            //Se requiere del address?
            wr.write("</port>");
            wr.newLine();
            wr.write("</service>");
            wr.newLine();
            wr.write("</definitions>");
            wr.newLine();
        } catch (Exception e) {
            System.out.println("Error: Cannot Write File");
            e.printStackTrace();
            System.out.println(e);
        }

    }

    public void wsdlGen() {
        try {            
            this.typeName = new ArrayList<String>();
            this.typeType = new ArrayList<String>();
            this.messageName = new ArrayList<String>();
            this.messageElement = new ArrayList<String>();
            this.opName = new ArrayList<String>();
            this.opMethod = new ArrayList<String>();
            this.bindOpName = new ArrayList<String>();
            
            this.typeName.add("Kushala");
            this.typeType.add("Elder");
            this.messageName.add("Freeze");
            this.messageElement.add("Element");
            this.opName.add("Wind");
            this.opMethod.add("Attack");
            this.bindOpName.add("Wind");
            
            
            
            BufferedWriter wr = new BufferedWriter(new FileWriter(this.filename));
            genHeader(wr, "Ejemplo");
            genTypes(wr,"Ejemplo",typeName, typeType);
            genMessage(wr,messageName, messageElement);
            genPort(wr,"Ejemplo", opName, opMethod);
            genBinding(wr,"Ejemplo",bindOpName,"Ejemplo");
            genService(wr,"Ejemplo","Ejemplo");
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


*/