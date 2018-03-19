/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.util.*;
/**
 *
 * @author joker
 */
public class OS_lab1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String filename=args[0];
        System.out.print("extracted filename: ");
        System.out.print(filename);
        System.out.print("\n");
        
        File file=new File(filename);
        try{
            Scanner sc=new Scanner(file);
            String buffer;
            buffer=sc.next();
            buffer=buffer.replace("\n", "").replace("\r", "");
            int num_module=Integer.parseInt(buffer);
            int current_module=0;
            int def, use, text, co, count, temp, address, op, last3digits;
            String s, t;
            //def
            ArrayList<String> sym=new ArrayList<String>();
            ArrayList<Integer> sym_loc=new ArrayList<Integer>();
            ArrayList<Integer> sym_module_num=new ArrayList<Integer>();
            ArrayList<Integer> sym_repeated_flag=new ArrayList<Integer>();
            ArrayList<Integer> sym_address_overflow_flag=new ArrayList<Integer>();
            //use
            ArrayList<String> sym_use=new ArrayList<String>();
            ArrayList<String> sym_use_checkbook=new ArrayList<String>();
            //text
            ArrayList<Integer> module_loc=new ArrayList<Integer>();
            ArrayList<Integer> memory_map=new ArrayList<Integer>();
            ArrayList<Integer> defined_flag=new ArrayList<Integer>();
            ArrayList<String> defined_v=new ArrayList<String>();
            ArrayList<Integer> bound_flag=new ArrayList<Integer>();
            ArrayList<String> text_use_checkbook=new ArrayList<String>();
            ArrayList<String> sym_err6=new ArrayList<String>();
            ArrayList<Integer> sym_err6_module_num=new ArrayList<Integer>();
            ArrayList<Integer> memory_err7_flag=new ArrayList<Integer>();
            ArrayList<Integer> memory_err8_flag=new ArrayList<Integer>();
            //First Pass
            module_loc.add(0);
            while(current_module<num_module){
                //def
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                def=Integer.parseInt(buffer);
                while(def>0){
                    buffer=sc.next();
                    buffer=buffer.replace("\n", "").replace("\r", "");
                    sym.add(buffer);
                    buffer=sc.next();
                    buffer=buffer.replace("\n", "").replace("\r", "");
                    sym_loc.add(Integer.parseInt(buffer)+module_loc.get(current_module));
                    sym_module_num.add(current_module);
                    def--;
                }
                
                //use
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                use=Integer.parseInt(buffer);
                while(use>0){
                    sc.next();
                    use--;
                }
                
                //text
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                text=Integer.parseInt(buffer);
                module_loc.add(text);
                while(text>0){
                    sc.next();
                    sc.next();
                    text--;
                }
                
                current_module++;
                module_loc.set(current_module, module_loc.get(current_module)+module_loc.get(current_module-1));
            }
            sc.close();
            
            ArrayList<String> cp=(ArrayList<String>) sym.clone();
            //second pass
            sc=new Scanner(file);
            buffer=sc.next();
            buffer=buffer.replace("\n", "").replace("\r", "");
            num_module=Integer.parseInt(buffer);
            current_module=0;
            while(current_module<num_module){
                //def
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                def=Integer.parseInt(buffer);
                while(def>0){
                    buffer=sc.next();
                    buffer=buffer.replace("\n", "").replace("\r", "");
                    s=buffer;
                    buffer=sc.next();
                    buffer=buffer.replace("\n", "").replace("\r", "");
                    co=Integer.parseInt(buffer)+module_loc.get(current_module);
                    //Error 2: symbol multiply defined
                    cp.remove(s);
                    count=1;
                    if(cp.contains(s)){
                        sym_repeated_flag.add(1);
                    }else{
                        sym_repeated_flag.add(0);
                    }
                    while(cp.contains(s)){
                        temp=cp.indexOf(s)+count;
                        sym.remove(temp);
                        sym_loc.remove(temp);
                        sym_module_num.remove(temp);
                        count++;
                        cp.remove(s);
                    }
                    
                    //Error 4: Address in a definition exceeds the size of the module
                    if(co>=module_loc.get(current_module+1)){
                        co=module_loc.get(current_module);
                        sym_address_overflow_flag.add(1);
                        sym_loc.set(sym.indexOf(s), co);
                    }else{
                        sym_address_overflow_flag.add(0);
                    }
                    
                    def--;
                }
                
                //use
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                use=Integer.parseInt(buffer);
                while(use>0){
                    buffer=sc.next();
                    buffer=buffer.replace("\n", "").replace("\r", "");
                    s=buffer;
                    sym_use.add(s);
                    sym_use_checkbook.add(s);
                    use--;
                }
                
                //text
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                text=Integer.parseInt(buffer);
                while(text>0){
                    buffer=sc.next();
                    buffer=buffer.replace("\n", "").replace("\r", "");
                    t=buffer;
                    buffer=sc.next();
                    buffer=buffer.replace("\n", "").replace("\r", "");
                    address=Integer.parseInt(buffer);
                    if(t.equals("I")){
                        memory_map.add(address);
                        memory_err7_flag.add(0);
                        memory_err8_flag.add(0);
                        bound_flag.add(0);
                        defined_flag.add(0);
                    }else if(t.equals("A")){
                        op=address/1000;
                        last3digits=address-op*1000;
                        if(last3digits<200){
                            memory_map.add(address);
                            memory_err7_flag.add(0);
                        }else{
                            //Error 7: absolute address exceeds the size of the machine
                            address=op*1000;
                            memory_map.add(address);
                            memory_err7_flag.add(1);
                        }
                        memory_err8_flag.add(0);
                        bound_flag.add(0);
                        defined_flag.add(0);
                    }else if(t.equals("R")){
                        op=address/1000;
                        last3digits=address-op*1000+module_loc.get(current_module);
                        //Error 8: Relative address exceeds the size of the module
                        if(last3digits>=module_loc.get(current_module+1)){
                            address=op*1000;
                            memory_map.add(address);
                            memory_err8_flag.add(1);
                        }else{
                            address=op*1000+last3digits;
                            memory_map.add(address);
                            memory_err8_flag.add(0);
                        }
                        memory_err7_flag.add(0);
                        bound_flag.add(0);
                        defined_flag.add(0);
                    }else if(t.equals("E")){
                        op=address/1000;
                        last3digits=address-op*1000;
                        
                        if(last3digits<sym_use.size()){
                            bound_flag.add(0);
                            //Error 3: Symbol used but not defined
                            if(sym.contains(sym_use.get(last3digits))){
                                defined_flag.add(0);
                                //Bookkeeping for Error 6: symbol appears in use list but not actually used
                                //by text at all
                                text_use_checkbook.add(sym_use.get(last3digits));
                                last3digits=sym_loc.get(sym.indexOf(sym_use.get(last3digits)));
                            }else{
                                text_use_checkbook.add(sym_use.get(last3digits));
                                last3digits=0;
                                defined_flag.add(1);
                                defined_v.add(sym_use.get(last3digits));
                            }
                        }else{
                            //Error 5: external address exceeds use list upperbound
                            bound_flag.add(1);
                            defined_flag.add(0);
                        }
                        
                        address=op*1000+last3digits;
                        memory_map.add(address);
                        memory_err7_flag.add(0);
                        memory_err8_flag.add(0);
                    }
                    text--;
                }
                
                //Error 6: symbol appears in use list but not actually used by text at all
                while(!sym_use.isEmpty()){
                    s=sym_use.get(0);
                    sym_use.remove(0);
                    if(!text_use_checkbook.contains(s)){
                        sym_err6.add(s);
                        sym_err6_module_num.add(current_module);
                    }
                }
                
                text_use_checkbook.clear();
                sym_use.clear();
                current_module++;
                
            }
            sc.close();
            
            //Symbol Table
            System.out.println("Symbol Table");
            for(int i=0;i<sym.size();i++){
                s=sym.get(i);
                System.out.print(s+" = "+(sym_loc.get(i).toString())+"  ");
                
                //Check for Error 2
                if(sym_repeated_flag.get(i)==1){
                    System.out.print("Error: This variable is multiply defined; first value used.");
                }
                
                //Check for Error 4
                if(sym_address_overflow_flag.get(i)==1){
                    System.out.print("Error: The address provided by the definition of this variable "
                            + "exceeds the size of the module; (relative) zero used. ");
                }
                System.out.print("\n");
            }
            
            //Memory Map
            System.out.println("Memory Map");
            for(int i=0;i<memory_map.size();i++){
                System.out.print(Integer.toString(i)+":    "+memory_map.get(i)+"  ");
                
                //Check for Error 3
                if(defined_flag.get(i)==1){
                    System.out.print("Error: "+defined_v.get(0)+"  is not defined; zero used. ");
                    defined_v.remove(0);
                }
                
                //Check for Error 5
                if(bound_flag.get(i)==1){
                    System.out.print("Error: External address too large to reference. Treated as immediate address. ");
                }
                
                //Check for Error 7
                if(memory_err7_flag.get(i)==1){
                    System.out.print("Error: Absolute address exceeds machine size; zero used. ");
                }
                
                //Check for Error 8
                if(memory_err8_flag.get(i)==1){
                    System.out.print("Error: Relative address exceeds module size; zero used. ");
                }
                System.out.print("\n");
            }
            
            
            //Error 1: symbol defined but not used
            for(int i=0;i<sym.size();i++){
                s=sym.get(i);
                if(!sym_use_checkbook.contains(s)){
                    System.out.println("Warning: "+ s+" was defined in module "
                            +Integer.toString(sym_module_num.get(i))
                            +" but never used. ");
                }
            }
            
            //Check for Error 6
            for(int i=0;i<sym_err6.size();i++){
                System.out.println("Warning: In module "+Integer.toString(sym_err6_module_num.get(i))+", variable: "
                        +sym_err6.get(i)+" appeared in the use list but was not actually used.");
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        
        
    }
    
}
