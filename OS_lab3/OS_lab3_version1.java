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
public class OS_lab3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename=null;
        int verbose=-1;
        if(args.length==1){
            filename=args[0];
            System.out.print("extracted filename: ");
            System.out.print(filename);
            System.out.print("\n");
            verbose=0;
        }else if(args.length==2){
            filename=args[1];
            System.out.print("extracted filename: ");
            System.out.print(filename);
            System.out.print("\n");
            if(args[0].equals("--verbose")){
                verbose=1;
            }else{
                verbose=0;
            }
        }
        File file=new File(filename);
        try{
            Scanner sc=new Scanner(file);
            String buffer;
            
            //read in the number of task
            buffer=sc.next();
            buffer=buffer.replace("\n", "").replace("\r", "");
            int task_num=Integer.parseInt(buffer);
            
            //read in the number of resource types and 
            //the number of units present of each resource type
            buffer=sc.next();
            buffer=buffer.replace("\n", "").replace("\r", "");
            int resources_type_num=Integer.parseInt(buffer);
            ArrayList<Integer> resources_total_num=new ArrayList<Integer>();
            for(int i=0;i<resources_type_num;i++){
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                resources_total_num.add(Integer.parseInt(buffer));
            }
            
            //read in the activities setup
            //The variable "activities_type_all" stores at index i an arraylist of string, 
            //which contains the history of activities for task i+1
            ArrayList<ArrayList<String>> activities_type_all=new ArrayList<>();
            ArrayList<String> history_of_activities;
            //The variable "delay_all" stores at index i an arraylist of integer,
            //which contains the history of delays involved for the maching activity of taks i+1
            ArrayList<ArrayList<Integer>> delay_all=new ArrayList<>();
            ArrayList<Integer> history_of_delay;
            //The variable "resources_type_all" stores at index i an arraylist of integer,
            //which contains the history of resources_type involved for the maching activity 
            //of taks i+1
            ArrayList<ArrayList<Integer>> resources_type_all=new ArrayList<>();
            ArrayList<Integer> history_of_resources_type;
            //The variable "resources_type_all" stores at index i an arraylist of integer,
            //which contains the history of resources_num involved for the maching activity
            //of taks i+1
            ArrayList<ArrayList<Integer>> resources_num_all=new ArrayList<>();
            ArrayList<Integer> history_of_resources_num;
            for(int i=0;i<task_num;i++){
                //initilize the variable first
                history_of_activities=new ArrayList<String>();
                history_of_delay=new ArrayList<Integer>();
                history_of_resources_type=new ArrayList<Integer>();
                history_of_resources_num=new ArrayList<Integer>();
                activities_type_all.add(history_of_activities);
                delay_all.add(history_of_delay);
                resources_type_all.add(history_of_resources_type);
                resources_num_all.add(history_of_resources_num);
            }
            
            //read in all the activities
            String a;
            int b,c,d,e;
            while(sc.hasNext()){
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                a=buffer;
                
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                b=Integer.parseInt(buffer)-1;
                
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                c=Integer.parseInt(buffer);
                
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                d=Integer.parseInt(buffer)-1;
                
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                e=Integer.parseInt(buffer);
                
                activities_type_all.get(b).add(a);
                delay_all.get(b).add(c);
                resources_type_all.get(b).add(d);
                resources_num_all.get(b).add(e);
            }
            
            //each simulation use one copy of the following array of all tasks
            ArrayList<task> all_tasks_copy1=new ArrayList<task>();
            ArrayList<task> all_tasks_copy2=new ArrayList<task>();
            task p1, p2;
            for(int i=0;i<task_num;i++){
                p1=new task(i, resources_type_num, activities_type_all.get(i), delay_all.get(i), 
                        resources_type_all.get(i), resources_num_all.get(i));
                p2=new task(i, resources_type_num, activities_type_all.get(i), delay_all.get(i), 
                        resources_type_all.get(i), resources_num_all.get(i));
                all_tasks_copy1.add(p1);
                all_tasks_copy2.add(p2);
            }
            
            //begin simulation for the optimistic manager
            OM om=new OM(verbose, task_num, resources_type_num, resources_total_num, all_tasks_copy1);
            om.run();
            om.report();
            
            //begin simulation for the bank manager
            sc.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    
}
class OM{
    int time;
    int verbose;
    int tasks_num;
    int resources_type_num;
    ArrayList<Integer> resources_total_num;
    ArrayList<Integer> resources_available_num;
    ArrayList<task> om_all_tasks;//for report use only
    ArrayList<task> ready_tasks;
    ArrayList<task> block_tasks;
    ArrayList<task> terminated_tasks;
    ArrayList<ArrayList<task>> pending_request;
    ArrayList<ArrayList<Integer>> pending_request_num;

    public OM(int verbose, int tasks_num, int resources_type_num, ArrayList<Integer> resources_total_num, ArrayList<task> all_tasks) {
        this.time=0;
        this.verbose=verbose;
        this.tasks_num = tasks_num;
        this.resources_type_num = resources_type_num;
        this.resources_total_num = new ArrayList<>();
        this.resources_available_num=new ArrayList<>();
        for(int i=0;i<resources_type_num;i++){
            this.resources_total_num.add(resources_total_num.get(i));
            this.resources_available_num.add(resources_total_num.get(i));
        }
        this.om_all_tasks=new ArrayList<>();
        this.ready_tasks = new ArrayList<>();
        for(int i=0;i<tasks_num;i++){
            this.om_all_tasks.add(all_tasks.get(i));
            this.ready_tasks.add(all_tasks.get(i));
        }
        this.block_tasks=new ArrayList<>();
        this.pending_request=new ArrayList<>();
        this.pending_request_num=new ArrayList<>();
        this.terminated_tasks=new ArrayList<>();
        ArrayList<task> temp1;
        ArrayList<Integer> temp2;
        for(int i=0;i<resources_type_num;i++){
            temp1=new ArrayList<>();
            temp2=new ArrayList<>();
            pending_request.add(temp1);
            pending_request_num.add(temp2);
        }
    }
    
    public void report(){
        System.out.println("FIFO");
        task target;
        double p, p_total;
        int t_time=0;
        int w_time=0;
        for(int i=0;i<this.tasks_num;i++){
            target=this.om_all_tasks.get(i);
            if(target.aborted==1){
                System.out.printf("Task %d      aborted\n", i+1);
            }else{
                t_time+=target.total_time;
                w_time+=target.wait_time;
                p=100.0*target.wait_time/target.total_time;
                System.out.printf("Task %d      %d  %d  %f\n", 
                        i+1, target.total_time, target.wait_time, p);
            }
        }
        p_total=100.0*w_time/t_time;
        System.out.printf("total       %d  %d  %f\n", t_time, w_time, p_total);
    }
    
    public void run(){
        task target;
        String a;
        int c,d,e, asked_amount;
        ArrayList<Integer> temp_list1;
        ArrayList<Integer> ready_to_block_index;
        ArrayList<Integer> ready_to_terminated_index;
        ArrayList<ArrayList<Integer>> block_to_ready_index;
        ArrayList<Integer> returned_resources;
        ready_to_block_index=new ArrayList<>();
        block_to_ready_index=new ArrayList<>();
        ready_to_terminated_index=new ArrayList<>();
        returned_resources=new ArrayList<>();
        for(int i=0;i<this.resources_type_num;i++){
            temp_list1=new ArrayList<>();
            block_to_ready_index.add(temp_list1);
            returned_resources.add(0);
        }
        while (this.terminated_tasks.size()!=this.tasks_num){
            if(this.verbose==1){
                System.out.printf("During cycle %d - %d (already terminate %d tasks)\n", this.time, this.time+1, this.terminated_tasks.size());
            }
            
//todo: check whether some requests of the blocked tasks can now be satisfied
            if(this.block_tasks.size()>0){
                if(this.verbose==1){
                    System.out.printf(" First check blocked tasks:\n");
                }
            }
            for(int i=0;i<this.resources_type_num;i++){
                for(int j=0;j<this.pending_request.get(i).size();j++){
                    target=this.pending_request.get(i).get(j);
                    asked_amount=this.pending_request_num.get(i).get(j);
                    if(asked_amount<=this.resources_available_num.get(i)){
                        //resource granted, move from blocked to ready
                        if(this.verbose==1){
                            System.out.printf("         Task %d completes its request of %d units\n", target.number+1, asked_amount);
                        }
                        this.resources_available_num.set(i, 
                                this.resources_available_num.get(i)-asked_amount);
                        target.wait_time++;
                        target.total_time++;
                        target.history_pointer++;
                        block_to_ready_index.get(i).add(j);
                    }else{
                        //resource not granted
                        if(this.verbose==1){
                            System.out.printf("         Task %d's request cannot be granted\n", target.number+1, asked_amount);
                        }
                        target.wait_time++;
                        target.total_time++;
                    }
                }
            }
            
            
            //run task in the ready list
            for(int i=0;i<this.ready_tasks.size();i++){
                target=this.ready_tasks.get(i);
                if(target.delay>0){
                    if(this.verbose==1){
                        System.out.printf(" Task %d computes (remaining %d cycles)\n",target.number+1, target.delay);
                    }
                    target.compute();
                }else{
                    a=target.history_of_activities.get(target.history_pointer);
                    c=target.history_of_delay.get(target.history_pointer);
                    d=target.history_of_resources_type.get(target.history_pointer);
                    e=target.history_of_resources_num.get(target.history_pointer);
                    target.history_pointer++;
                    if(a.equals("initiate")){
                        if(this.verbose==1){
                            System.out.printf(" Task %d completes one initiate activity\n",target.number+1);
                        }
                        target.init(c,d,e);
                    }else if(a.equals("request")){
                        if(c>0){
                            if(this.verbose==1){
                                System.out.printf(" Task %d computes (1 of %d cycles)\n",target.number+1, c);
                            }
                            target.delay_activity(c,d,e);
                        }else{
                            if(this.resources_available_num.get(d)>=e){
                                //requested resource granted
                                if(this.verbose==1){
                                    System.out.printf(" Task %d completes its request of %d, remaining %d units\n", 
                                            target.number+1, e, this.resources_available_num.get(d)-e);
                                }
                                this.resources_available_num.set(d, this.resources_available_num.get(d)-e);
                                target.request(c,d,e);
                            }else{
                                //cannot grant the resources, task is blocked
                                if(this.verbose==1){
                                    System.out.printf(" Task %d's request cannot be granted\n",target.number+1);
                                }
                                target.history_pointer--;
                                target.total_time++;
                                ready_to_block_index.add(0, i);
                            }
                        }
                    }else if(a.equals("release")){
                        if(c>0){
                            if(this.verbose==1){
                                System.out.printf(" Task %d computes (1 of %d cycles)\n",target.number+1, c);
                            }
                            target.delay_activity(c, d, e);
                        }else{
                            if(this.verbose==1){
                                System.out.printf(" Task %d releases %d units of type %d \n",target.number+1, e, d+1);
                            }
                            returned_resources.set(d, returned_resources.get(d)+e);
                            target.release(c,d,e);
                        }
                    }else if(a.equals("terminate")){
                        //******************************************************
                        //MIGHT WANT TO CHECK THAT ALL RESOURCES HELD BY THIS TASK
                        //ARE RETURNED!!!!!!!!!!!!!!!!
                        //******************************************************
                        if(c>0){
                            if(this.verbose==1){
                                System.out.printf(" Task %d computes (1 of %d cycles)\n",target.number+1, c);
                            }
                            target.delay_activity(c, d, e);
                        }else{
                            if(this.verbose==1){
                                System.out.printf(" Task %d terminated\n",target.number+1);
                            }
                            ready_to_terminated_index.add(0, i);
                        }
                    }
                }
            }
            
            
            
            //todo: block_to_ready task
            int index;
            for(int i=0;i<this.resources_type_num;i++){
                temp_list1=block_to_ready_index.get(i);
                for(int j=0;j<temp_list1.size();j++){
                    index=temp_list1.get(temp_list1.size()-1-j);
                    target=this.pending_request.get(i).get(index-j);
                    this.pending_request.get(i).remove(index-j);
                    this.pending_request_num.get(i).remove(index-j);
                    this.block_tasks.remove(target);
                    //**********************************************************
                    //NOTE: THE ORDERING IN THE READY_TASKS MIGHT MATTER?
                    //**********************************************************
                    this.ready_tasks.add(target);
                }
                while(!temp_list1.isEmpty()){
                    index=temp_list1.get(0);
                    temp_list1.remove(0);
                }
            }
            
            
            //todo: ready_to_block task
            for(int i=0;i<ready_to_block_index.size();i++){
                index=ready_to_block_index.get(ready_to_block_index.size()-1-i);
                target=this.ready_tasks.get(index-i);
                this.ready_tasks.remove(index-i);
                this.block_tasks.add(target);
                d=target.history_of_resources_type.get(target.history_pointer);
                e=target.history_of_resources_num.get(target.history_pointer);
                this.pending_request.get(d).add(target);
                this.pending_request_num.get(d).add(e);
            }
            while(!ready_to_block_index.isEmpty()){
                index=ready_to_block_index.get(0);
                ready_to_block_index.remove(0);
            }
            
            
            //todo: ready_to_terminated task
            while(!ready_to_terminated_index.isEmpty()){
                index=ready_to_terminated_index.get(0);
                ready_to_terminated_index.remove(0);
                target=this.ready_tasks.get(index);
                this.ready_tasks.remove(index);
                this.terminated_tasks.add(target);
            }
            
            
            //todo: return resources
            int temp_amount;
            for(int i=0;i<this.resources_type_num;i++){
                temp_amount=returned_resources.get(i)+this.resources_available_num.get(i);
                returned_resources.set(i, 0);
                this.resources_available_num.set(i, temp_amount);
            }
            
            //if all process are blocked now, we have a deadlock. Abort some tasks until one of them
            //can be run
            int min;
            task min_task;
            while(check_deadlock()){
                //Abort the lowest numbered deadlock task
                //First, we find the lowest numbered deadlock task
                min=this.block_tasks.get(0).number;
                min_task=this.block_tasks.get(0);
                for(int i=0;i<this.block_tasks.size();i++){
                    index=this.block_tasks.get(i).number;
                    if(index<min){
                        min=index;
                        min_task=this.block_tasks.get(i);
                    }
                }
                if(this.verbose==1){
                    System.out.printf("According to the spec task %d is aborted now\n", min+1);
                }
                abort(min, min_task);
            }
            
            this.time++;
        }
    }
    public void abort(int min, task min_task){
        //First, we remove it from the block_tasks
        this.block_tasks.remove(min_task);
        //Next, we remove it from the pending_request & pending_request_num
        boolean b=true;
        for(int i=0;i<this.resources_type_num && b;i++){
            for(int j=0;j<this.pending_request.get(i).size();j++){
                if(min_task.equals(this.pending_request.get(i).get(j))){
                    this.pending_request.get(i).remove(j);
                    b=false; //break out of the nested loop
                }
            }
        }
        //Third, we return all the resources it holds
        int new_amount;
        for(int i=0;i<this.resources_type_num;i++){
            new_amount=min_task.resources_hold.get(i)+this.resources_available_num.get(i);
            min_task.resources_hold.set(i, 0);
            this.resources_available_num.set(i, new_amount);
        }
        //Fourth, mark the task as aborted
        min_task.aborted=1;
        //Finally, add the aborted task to the terminated_tasks
        this.terminated_tasks.add(min_task);
    }
    
    public boolean check_deadlock(){
        int remain, asked;
        if(this.terminated_tasks.size()==this.tasks_num){
            return false;
        }
        if(this.ready_tasks.size()>0){
            return false;
        }else{
            for(int i=0;i<this.resources_type_num;i++){
                remain=this.resources_available_num.get(i);
                for(int j=0;j<this.pending_request_num.get(i).size();j++){
                    asked=this.pending_request_num.get(i).get(j);
                    if(asked<=remain){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
}

class task{
    int number;
    int aborted;
    int delay;
    int resources_num;
    int wait_time;
    int total_time;
    ArrayList<Integer> initial_claim;
    ArrayList<Integer> resources_hold;
    int history_pointer;
    ArrayList<String> history_of_activities;
    ArrayList<Integer> history_of_delay;
    ArrayList<Integer> history_of_resources_type;
    ArrayList<Integer> history_of_resources_num;

    public task(int index, int resources_num, ArrayList<String> history_of_activities, ArrayList<Integer> history_of_delay, ArrayList<Integer> history_of_resources_type, ArrayList<Integer> history_of_resources_num) {
        this.number=index;
        this.aborted=0;
        this.delay=0;
        this.resources_num = resources_num;
        this.wait_time=0;
        this.total_time=0;
        this.initial_claim=new ArrayList<Integer>();
        this.resources_hold=new ArrayList<Integer>();
        for(int i=0;i<resources_num;i++){
            this.initial_claim.add(-1);
            this.resources_hold.add(0);
        }
        this.history_pointer=0;
        this.history_of_activities = new ArrayList<String>();
        this.history_of_delay = new ArrayList<Integer>();
        this.history_of_resources_type = new ArrayList<Integer>();
        this.history_of_resources_num = new ArrayList<Integer>();
        for(int i=0;i<history_of_activities.size();i++){
            this.history_of_activities.add(history_of_activities.get(i));
            this.history_of_delay.add(history_of_delay.get(i));
            this.history_of_resources_num.add(history_of_resources_num.get(i));
            this.history_of_resources_type.add(history_of_resources_type.get(i));
        }
    }
    
    //initialize the claim e for resrouce type d. Argument c is not used.
    public void init(int c, int d, int e){
        this.initial_claim.set(d, e);
        this.total_time++;
    }
    
    //the task spends one cycle computing
    public void compute(){
        this.delay--;
        this.total_time++;
    }
    
    //the task are told to compute something before the next activity
    //therefore, we decrement the history pointer, set the delay field to be argument c,
    //and set the delay in history to be 0
    public void delay_activity(int c, int d, int e){
        this.delay=c-1; 
        
        //******************************************************************
        //        THIS MIGHT BE A BUG! c-1 MIGHT BE c INSTEAD!
        //******************************************************************
        
        this.history_pointer--;
        this.history_of_delay.set(this.history_pointer,0);
        this.total_time++;
    }
    
    //after we know that the request is granted, we have to do some book-keeping
    public void request(int c, int d, int e){
        this.resources_hold.set(d, this.resources_hold.get(d)+e);
        this.total_time++;
    }
    
    //book-keeping when the task is told to release some resources
    public void release(int c, int d, int e){
        this.resources_hold.set(d, this.resources_hold.get(d)-e);
        this.total_time++;
    }
    
    
}
