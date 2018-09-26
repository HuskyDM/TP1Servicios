
import java.io.*;
import java.io.BufferedWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Generator {

    FileInputStream file;
    FileOutputStream file2;
    String filename;
    List<String> methodName;
    List<String> methodType;
    List<String> variableName;
    List<String> variableType;
    List<String> primitives;

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
    public void genTypes(BufferedWriter wr, String namespace, List<String> methodName, List<String> methodType) {

        //There can be multiple types, should have an array loop
        try {

            wr.write(" <types xmlns=\"http://schemas.xmlsoap.org/wsdl/\">");
            wr.newLine();
            wr.write(" <xsd:schema targetNamespace=\"urn:" + namespace + "\">");
            wr.newLine();

           typeWriter(methodName, methodType, wr);

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
    public void typeWriter(List<String> methodName, List<String> methodType, BufferedWriter wr){
    try{
         for (int i = 0; i < methodName.size(); ++i) {
                wr.write("<xsd:element name=\"" + methodName.get(i) + "\">");
                wr.newLine();
                wr.write("<xsd:complexType>");
                wr.newLine();
                wr.write("<xsd:sequence>");
                wr.newLine();
                
                /*TODO: Nota: hay metodos sin tipos, hay que preguntar eso
                Asuma que name y type tienen la misma cantidad de elementos en sus
                listas y que no hay ningun nillable
                */
                wr.write("<xsd:element name=\"" + methodName.get(i) + "\" type=\"xsd:" + methodType.get(i) + "\"/>");
                
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
    
    public void genMessage(BufferedWriter wr, List<String> messageName) {
        String extra;
        String extra2;
        try {
            for(int i=0;i<messageName.size()*2;++i){
                if(i%2 == 0){
                    extra = "Request";
                    extra2 = "";
                }
                else{
                    extra = "Response";
                    extra2 = "Return";
                }
                wr.write("<message name=\"" + messageName.get(i/2) + extra + "\">");
                wr.newLine();
            
                //TODO parameters es igual en todo?
                wr.write("<part name=\"parameters\" element=\"tns:" + messageName.get(i/2) + extra2 + "\" />");
            
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
    
    public void genPort(BufferedWriter wr, String namespace, List<String> opName) {
        try {
            wr.write("<portType name=\"" + namespace + "Port\">");
            wr.newLine();
           
            opGen(opName, wr);
            
            wr.write("</portType>");
            wr.newLine();
        } catch (Exception e) {
            System.out.println("Error: Cannot Write File");
            e.printStackTrace();
            System.out.println(e);
        }
    }
    
    public void opGen(List<String>opName, BufferedWriter wr){
    
        try{ 
            for(int i=0;i<opName.size();++i){
            wr.write("<operation name=\"" + opName.get(i) + "\">");
            wr.newLine();
            wr.write("<input message=\"tns:" + opName.get(i) + "Request\" />");
            wr.newLine();
            wr.write("<output message=\"tns:" + opName.get(i) + "Response\" />");
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

    public void wsdlGen(String classname) {
        try {            
            this.methodName = new ArrayList<String>();
            this.methodType = new ArrayList<String>();
            this.variableName = new ArrayList<String>();
            this.variableType = new ArrayList<String>();
            this.primitives = new ArrayList<String>();
            
            primitives.add("Boolean");
            primitives.add("Character");
            primitives.add("Byte");
            primitives.add("Short");
            primitives.add("Integer");
            primitives.add("Long");
            primitives.add("Float");
            primitives.add("Double");
            primitives.add("Void");
            primitives.add("Int");
            
            // Stats
            
            ClassLoader loader = Generator.class.getClassLoader();
            String clazzname = getClass().getName();
            URL pathtothis = loader.getResource(clazzname + ".class");
            ArrayList<String> demodirectory = new ArrayList<String>(Arrays.asList(pathtothis.toExternalForm().split("/")));
            String demoliteraldir = "";
            
            if(3 < demodirectory.size()){
                for(int i = 0; i < 3; ++i){
                    demodirectory.remove(demodirectory.size() - 1);
                }
            }
            
            
            for (String item : demodirectory) {
                demoliteraldir += item + "/";
            }
            
            demoliteraldir = demoliteraldir + "demo/";
            
            Class hostage;
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] {
                new URL(
                    demoliteraldir
                )
            });
            hostage = urlClassLoader.loadClass(classname);
            List<Field> privateFields = new ArrayList<>();
            Field[] fields = hostage.getDeclaredFields();
            Method[] methods = hostage.getDeclaredMethods();
            
            for (Field field : fields) {
                if(!(primitives.contains(field.getType().getCanonicalName()) || variableType.contains(field.getType().getCanonicalName()))){
                    privateFields.add(field);
                    this.variableType.add(field.getType().getCanonicalName());
                    this.variableName.add(field.getName());
                }
            }
            
            for (Method method : methods) {
                this.methodType.add(method.getName());
                this.methodName.add(method.getReturnType().getCanonicalName());
            }
            
            BufferedWriter wr = new BufferedWriter(new FileWriter(this.filename + ".wsdl"));
            genHeader(wr, classname); //
            genTypes(wr,classname,methodName, methodType); //
            genMessage(wr,methodType);
            genPort(wr,classname, methodType);
            genBinding(wr,classname,methodType,classname);
            genService(wr,classname,classname);
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
