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
public class OS_lab2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
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
            buffer=sc.next();
            buffer=buffer.replace("\n", "").replace("\r", "");
            int process_num=Integer.parseInt(buffer);
            ArrayList<process> all_process_list=new ArrayList<process>();
            process p;
            int A,B,C,IO;
            StringBuilder str=new StringBuilder();
            str.append("The original input was:    ");
            str.append(process_num);
            str.append("    ");
            for(int i=0;i<process_num;i++){
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                A=Integer.parseInt(buffer);
                str.append(A);
                str.append(" ");
                
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                B=Integer.parseInt(buffer);
                str.append(B);
                str.append(" ");
                
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                C=Integer.parseInt(buffer);
                str.append(C);
                str.append(" ");
                
                buffer=sc.next();
                buffer=buffer.replace("\n", "").replace("\r", "");
                IO=Integer.parseInt(buffer);
                str.append(IO);
                str.append("   ");
                
                p=new process(A,B,C,IO,i);
                all_process_list.add(p);
            }
            System.out.println(str.toString());
            
            //sort all_process_list according to the arrival time
            Collections.sort(all_process_list);
            //update the index after sorting and show the sorted result
            str=new StringBuilder();
            str.append("The (sorted) input was:    ");
            str.append(process_num);
            str.append("    ");
            for(int i=0;i<all_process_list.size();i++){
                p=all_process_list.get(i);
                p.index=i;
                str.append(p.arrive);
                str.append(" ");
                str.append(p.cpu_range);
                str.append(" ");
                str.append(p.cpu_cycle);
                str.append(" ");
                str.append(p.io_range);
                str.append("   ");
            }
            System.out.println(str.toString());
            System.out.println();
            
            //simulation for FCFS
            System.out.println("Simulation I: FCFS");
            System.out.println();
            RNG r=new RNG();
            ArrayList<process> apl=(ArrayList<process>) all_process_list.clone();
            FCFS scheduler_fcfs=new FCFS(apl, r, verbose);
            scheduler_fcfs.run();
            scheduler_fcfs.report();
            System.out.println();
            
            
            //simulation for RR(q=2)
            System.out.println("Simulation II: RR(q=2)");
            System.out.println();
            //reset RNG
            r.clear_history();
            //reset all processes
            for(int i=0;i<all_process_list.size();i++){
                p=all_process_list.get(i);
                p.reset();
            }
            apl=(ArrayList<process>) all_process_list.clone();
            RR scheduler_rr=new RR(apl,r,verbose,2);
            scheduler_rr.run();
            scheduler_rr.report();
            System.out.println();
            
            
            //simulation for Uniprogrammed
            System.out.println("Simulation III: Uniprogrammed");
            System.out.println();
            //reset RNG
            r.clear_history();
            //reset all processes
            for(int i=0;i<all_process_list.size();i++){
                p=all_process_list.get(i);
                p.reset();
            }
            apl=(ArrayList<process>) all_process_list.clone();
            UP scheduler_up=new UP(apl,r,verbose);
            scheduler_up.run();
            scheduler_up.report();
            System.out.println();
            
            //todo: simulation for SRTN
            System.out.println("Simulation IV: SRTN");
            System.out.println();
            //reset RNG
            r.clear_history();
            //reset all processes
            for(int i=0;i<all_process_list.size();i++){
                p=all_process_list.get(i);
                p.reset();
            }
            apl=(ArrayList<process>) all_process_list.clone();
            //todo: implement SRTN 
            SRTN scheduler_srtn=new SRTN(apl,r,verbose);
            scheduler_srtn.run();
            scheduler_srtn.report();
            System.out.println();
            
            sc.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    
}
class SRTN{
    int c_time;
    int verbose;
    //for report purpose
    int cpu_time;
    int io_time;
    int total_t_time;
    int total_w_time;
    //book keeping
    ArrayList<process> all_process_list;
    ArrayList<process> unstarted_list;
    ArrayList<process> running_list;
    ArrayList<process> ready_list;
    ArrayList<process> blocked_list;
    RNG rng;
    
    public SRTN(ArrayList<process> apl, RNG r, int v) {
        this.c_time=0;
        this.verbose=v;
        this.cpu_time=0;
        this.io_time=0;
        this.total_t_time=0;
        this.total_w_time=0;
        
        this.all_process_list=apl;
        this.unstarted_list=(ArrayList<process> )apl.clone();
        this.running_list=new ArrayList<process>();
        this.ready_list=new ArrayList<process>();
        this.blocked_list=new ArrayList<process>();
        this.rng=r;
    }
    
    public void report(){
        this.c_time-=1;
        process p;
        System.out.println("The scheduling algorithm used was Preemptive Shortest Job First");
        System.out.println();
        for(int i=0;i<this.all_process_list.size();i++){
            p=all_process_list.get(i);
            this.total_t_time+=p.t_time;
            this.total_w_time+=p.w_time;
            p.report(i);
            System.out.println();
        }
        System.out.println("Summary Data: ");
        System.out.printf("Finishing time: %d \n", this.c_time);
        double ratio=(double)(this.cpu_time)/(double)(this.c_time);
        System.out.printf("CPU Utilization: %f \n", ratio);
        ratio=(double)(this.io_time)/(double)(this.c_time);
        System.out.printf("I/O Utilization: %f \n", ratio);
        ratio=(double)(this.all_process_list.size())*100.0/(double)(this.c_time);
        System.out.printf("Throughtput: %f processes per hundred cycles \n", ratio);
        ratio=(double)(this.total_t_time)/(double)(this.all_process_list.size());
        System.out.printf("Average turnaround time: %f \n", ratio);
        ratio=(double)(this.total_w_time)/(double)(this.all_process_list.size());
        System.out.printf("Averaged waiting time: %f \n", ratio);
        System.out.println();
        
        this.c_time+=1;
    }
    public void verbose_report(){
        StringBuilder sb=new StringBuilder();
        sb.append("Before cycle ");
        sb.append(this.c_time);
        sb.append(":    ");
        for(process p:this.all_process_list){
            if(this.unstarted_list.contains(p)){
                sb.append("unstarted  0  ");
            }else if(this.blocked_list.contains(p)){
                sb.append("blocked    ");
                sb.append(p.io_burst);
                sb.append("  ");
            }else if(this.ready_list.contains(p)){
                sb.append("ready      0  ");
            }else if(this.running_list.contains(p)){
                sb.append("running    ");
                sb.append(p.cpu_burst);
                sb.append("  ");
            }else{
                sb.append("terminated 0  ");
            }
        }
        System.out.println(sb.toString());
    }
    
    public void run(){
        process temp=null;
        process q=null;
        process t=null;
        ArrayList<process> new_ready_list=new ArrayList<process>();
        ArrayList<process> new_blocked_list=new ArrayList<process>();
        ArrayList<process> new_unstarted_list=new ArrayList<process>();
        ArrayList<process> new_running_list=new ArrayList<process>();
        while((!unstarted_list.isEmpty()) || (!running_list.isEmpty()) || (!ready_list.isEmpty())
                ||(!blocked_list.isEmpty())){
            if(this.verbose==1){
                verbose_report();
            }
            //increase the wait time for all process in ready_list first.
            for(process p:ready_list){
                p.w_time+=1;
            }
            
            //blocked_state transit
            if(!blocked_list.isEmpty()){
                this.io_time+=1;
            }
            while(!this.blocked_list.isEmpty()){
                temp=this.blocked_list.get(0);
                continue_io(temp);
                if(temp.io_burst<=0){
                    new_ready_list.add(temp);
                }else{
                    new_blocked_list.add(temp);
                }
                blocked_list.remove(0);
            }
            while(!new_blocked_list.isEmpty()){
                temp=new_blocked_list.get(0);
                blocked_list.add(temp);
                new_blocked_list.remove(0);
            }
            
            //unstarted_state transit
            while(!this.unstarted_list.isEmpty()){
                temp=this.unstarted_list.get(0);
                if(temp.arrive==this.c_time){
                    new_ready_list.add(temp);
                }else{
                    new_unstarted_list.add(temp);
                }
                unstarted_list.remove(0);
            }
            while(!new_unstarted_list.isEmpty()){
                temp=new_unstarted_list.get(0);
                unstarted_list.add(temp);
                new_unstarted_list.remove(0);
            }
            
            //running_state transit
            if(!running_list.isEmpty()){
                this.cpu_time+=1;
            }
            while(!this.running_list.isEmpty()){
                temp=this.running_list.get(0);
                temp.cpu_work-=1;
                temp.cpu_burst-=1;
                if(temp.cpu_work<=0){
                    //exit the process
                    temp.exit(c_time);
                }else if(temp.cpu_burst<=0){
                    start_io(temp);
                    blocked_list.add(temp);
                }else{
                    //no quantum preempt situation
                    new_running_list.add(temp);
                }
                this.running_list.remove(0);
            }
            while(!new_running_list.isEmpty()){
                temp=new_running_list.get(0);
                running_list.add(temp);
                new_running_list.remove(0);
            }
            
            //ready-state transit
            //if multiple job transit from "blocked" state to "ready" state, break the tier
            Collections.sort(new_ready_list);
            //After we break the tier, put them in the ready_list
            //Also for SRTN, we want to maintain a priority queue
            int target;
            while(!new_ready_list.isEmpty()){
                temp=new_ready_list.get(0);
                new_ready_list.remove(0);
                target=ready_list.size()-1;
                while(target>=0){
                    q=ready_list.get(target);
                    if(q.cpu_work>temp.cpu_work){
                        target--;
                    }else if(q.cpu_work==temp.cpu_work){
                        if(q.index>temp.index){
                            target--;
                        }else{
                            break;
                        }
                    }else{
                        break;
                    }
                }
                ready_list.add(target+1, temp);
            }
            
            if(running_list.isEmpty()){
                //schedule a process to run
                if(!ready_list.isEmpty()){
                    temp=ready_list.get(0);
                    //continue to use cpu-burst
                    start_cpu(temp);
                    ready_list.remove(temp);
                    running_list.add(temp);
                }else{
                    //cpu wastes one cycle
                }
            }else{
                //check whether we have a ready process with less work remaining
                if(!ready_list.isEmpty()){
                    //priority queue ready_list should guarantee that we only need to
                    //check the first process
                    t=ready_list.get(0);
                    temp=running_list.get(0);
                    if(temp.cpu_work>t.cpu_work){
                        //preempt the running process and
                        running_list.remove(0);
                        ready_list.remove(0);
                        //put it into the right position of the ready_list
                        target=ready_list.size()-1;
                        while(target>=0){
                            q=ready_list.get(target);
                            if(q.cpu_work>temp.cpu_work){
                                target--;
                            }else if(q.cpu_work==temp.cpu_work){
                                if(q.index>temp.index){
                                    target--;
                                }else{
                                    break;
                                }
                            }else{
                                break;
                            }
                        }
                        ready_list.add(target+1, temp);
                        //put process t to work
                        start_cpu(t);
                        running_list.add(t);
                    }else{
                        //No need to switch. Do nothing.
                    }
                }else{
                    //no processes are ready. Do nothing
                }
            }
            this.c_time++;
        }
    }
    
    public void start_cpu(process p){
        if(p.cpu_burst>0){
            return;
        }else{
            int c=rng.get_random_number(p.cpu_range);
            if (c>p.cpu_work){
                c=p.cpu_work;
            }
            p.cpu_burst=c;
        }
    }
    
    public void start_io(process p){
        int io=rng.get_random_number(p.io_range);
        p.io_burst=io;
    }
    
    public void continue_io(process p){
        p.IO_time+=1;
        p.io_burst-=1;
    }
}

class UP{
    int c_time;
    int verbose;
    //for report purpose
    int cpu_time;
    int io_time;
    int total_t_time;
    int total_w_time;
    //book keeping
    ArrayList<process> all_process_list;
    ArrayList<process> unstarted_list;
    ArrayList<process> running_list;
    ArrayList<process> ready_list;
    ArrayList<process> blocked_list;
    RNG rng;
    
    public UP(ArrayList<process> apl, RNG r, int v) {
        this.c_time=0;
        this.verbose=v;
        this.cpu_time=0;
        this.io_time=0;
        this.total_t_time=0;
        this.total_w_time=0;
        
        this.all_process_list=apl;
        this.unstarted_list=(ArrayList<process> )apl.clone();
        this.running_list=new ArrayList<process>();
        this.ready_list=new ArrayList<process>();
        this.blocked_list=new ArrayList<process>();
        this.rng=r;
    }
    
    public void report(){
        this.c_time-=1;
        process p;
        System.out.println("The scheduling algorithm used was Uniprocessor");
        System.out.println();
        for(int i=0;i<this.all_process_list.size();i++){
            p=all_process_list.get(i);
            this.total_t_time+=p.t_time;
            this.total_w_time+=p.w_time;
            p.report(i);
            System.out.println();
        }
        System.out.println("Summary Data: ");
        System.out.printf("Finishing time: %d \n", this.c_time);
        double ratio=(double)(this.cpu_time)/(double)(this.c_time);
        System.out.printf("CPU Utilization: %f \n", ratio);
        ratio=(double)(this.io_time)/(double)(this.c_time);
        System.out.printf("I/O Utilization: %f \n", ratio);
        ratio=(double)(this.all_process_list.size())*100.0/(double)(this.c_time);
        System.out.printf("Throughtput: %f \n", ratio);
        ratio=(double)(this.total_t_time)/(double)(this.all_process_list.size());
        System.out.printf("Average turnaround time: %f \n", ratio);
        ratio=(double)(this.total_w_time)/(double)(this.all_process_list.size());
        System.out.printf("Averaged waiting time: %f \n", ratio);
        System.out.println();
        
        this.c_time+=1;
    }
    
    public void verbose_report(){
        StringBuilder sb=new StringBuilder();
        sb.append("Before cycle ");
        sb.append(this.c_time);
        sb.append(":    ");
        for(process p:this.all_process_list){
            if(this.unstarted_list.contains(p)){
                sb.append("unstarted  0  ");
            }else if(this.ready_list.contains(p)){
                sb.append("ready  0  ");
            }else if(this.running_list.contains(p)){
                if(p.cpu_burst>0){
                    sb.append("running  ");
                    sb.append(p.cpu_burst);
                    sb.append("  ");
                }else if(p.io_burst>0){
                    sb.append("blocked  ");
                    sb.append(p.io_burst);
                    sb.append("  ");
                }
            }else{
                sb.append("terminated  0  ");
            }
        }
        System.out.println(sb.toString());
    }
    
    public void run(){
        process temp;
        ArrayList<process> new_ready_list=new ArrayList<process>();
        ArrayList<process> new_unstarted_list=new ArrayList<process>();
        ArrayList<process> new_running_list=new ArrayList<process>();
        while((!unstarted_list.isEmpty()) || (!running_list.isEmpty()) || (!ready_list.isEmpty())
                ||(!blocked_list.isEmpty())){
            if(this.verbose==1){
                verbose_report();
            }
            //increase the wait time for all process in ready_list first.
            for(process p:ready_list){
                p.w_time+=1;
            }
            
            
            
            //no blocked_state transit
            
            
            
            //unstarted_state transit
            while(!this.unstarted_list.isEmpty()){
                temp=this.unstarted_list.get(0);
                if(temp.arrive==this.c_time){
                    new_ready_list.add(temp);
                }else{
                    new_unstarted_list.add(temp);
                }
                unstarted_list.remove(0);
            }
            while(!new_unstarted_list.isEmpty()){
                temp=new_unstarted_list.get(0);
                unstarted_list.add(temp);
                new_unstarted_list.remove(0);
            }
            
            //running_state transit
            
            if(!running_list.isEmpty()){
                //should only has one process running
                temp=this.running_list.get(0);
                this.running_list.remove(0);
                if(temp.cpu_burst>0){
                    //cpu working
                    this.cpu_time+=1;
                    
                    temp.cpu_work-=1;
                    temp.cpu_burst-=1;
                    if(temp.cpu_work<=0){
                        //exit the process
                        temp.exit(c_time);
                    }else if(temp.cpu_burst<=0){
                        start_io(temp);
                        //blocked but still in the running_list
                        new_running_list.add(temp);
                    }else{
                        new_running_list.add(temp);
                    }
                }else if(temp.io_burst>0){
                    //io working
                    this.io_time+=1;
                    
                    continue_io(temp);
                    if(temp.io_burst<=0){
                        //start cpu work
                        start_cpu(temp);
                        new_running_list.add(temp);
                    }else{
                        //continue io
                        new_running_list.add(temp);
                    }
                }else{
                    //should not be here in any circumstances
                    System.out.println();
                    System.out.println("FATAL ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println("FATAL ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println("FATAL ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println();
                }
            }
            
            if(!this.running_list.isEmpty()){
                //has two or more processes in the running list
                //should not be here in any circumstances
                System.out.println();
                System.out.println("FATAL ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("FATAL ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("FATAL ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println();
            }
            
            while(!new_running_list.isEmpty()){
                temp=new_running_list.get(0);
                running_list.add(temp);
                new_running_list.remove(0);
            }
            
            //ready-state transit
            //if multiple job transit from "blocked" state to "ready" state, break the tier
            Collections.sort(new_ready_list);
            //After we break the tier, put them in the ready_list
            while(!new_ready_list.isEmpty()){
                temp=new_ready_list.get(0);
                new_ready_list.remove(0);
                ready_list.add(temp);
            }
            
            if(running_list.isEmpty()){
                //schedule a process to run
                if(!ready_list.isEmpty()){
                    temp=ready_list.get(0);
                    start_cpu(temp);
                    ready_list.remove(temp);
                    running_list.add(temp);
                }else{
                    //cpu wastes one cycle
                }
            }
            
            this.c_time++;
        }
    }
    public void start_cpu(process p){
        if(p.cpu_burst>0){
            return;
        }else{
            int c=rng.get_random_number(p.cpu_range);
            if (c>p.cpu_work){
                c=p.cpu_work;
            }
            p.cpu_burst=c;
        }
    }
    
    public void start_io(process p){
        int io=rng.get_random_number(p.io_range);
        p.io_burst=io;
    }
    
    public void continue_io(process p){
        p.IO_time+=1;
        p.io_burst-=1;
    }
    
    
}
class RR{
    int c_time;
    int verbose;
    int quantum;
    //for report purpose
    int cpu_time;
    int io_time;
    int total_t_time;
    int total_w_time;
    //book keeping
    ArrayList<process> all_process_list;
    ArrayList<process> unstarted_list;
    ArrayList<process> running_list;
    ArrayList<process> ready_list;
    ArrayList<process> blocked_list;
    RNG rng;

    public RR(ArrayList<process> apl, RNG r, int v, int q) {
        this.c_time=0;
        this.verbose=v;
        this.quantum=q;
        this.cpu_time=0;
        this.io_time=0;
        this.total_t_time=0;
        this.total_w_time=0;
        
        this.all_process_list=apl;
        this.unstarted_list=(ArrayList<process> )apl.clone();
        this.running_list=new ArrayList<process>();
        this.ready_list=new ArrayList<process>();
        this.blocked_list=new ArrayList<process>();
        this.rng=r;
    }
    
    public void report(){
        this.c_time-=1;
        process p;
        System.out.printf("The scheduling algorithm used was Round Robin with q=%d \n", this.quantum);
        System.out.println();
        for(int i=0;i<this.all_process_list.size();i++){
            p=all_process_list.get(i);
            this.total_t_time+=p.t_time;
            this.total_w_time+=p.w_time;
            p.report(i);
            System.out.println();
        }
        System.out.println("Summary Data: ");
        System.out.printf("Finishing time: %d \n", this.c_time);
        double ratio=(double)(this.cpu_time)/(double)(this.c_time);
        System.out.printf("CPU Utilization: %f \n", ratio);
        ratio=(double)(this.io_time)/(double)(this.c_time);
        System.out.printf("I/O Utilization: %f \n", ratio);
        ratio=(double)(this.all_process_list.size())*100.0/(double)(this.c_time);
        System.out.printf("Throughtput: %f \n", ratio);
        ratio=(double)(this.total_t_time)/(double)(this.all_process_list.size());
        System.out.printf("Average turnaround time: %f \n", ratio);
        ratio=(double)(this.total_w_time)/(double)(this.all_process_list.size());
        System.out.printf("Averaged waiting time: %f \n", ratio);
        System.out.println();
        
        this.c_time+=1;
    }
    
    public void verbose_report(){
        StringBuilder sb=new StringBuilder();
        sb.append("Before cycle ");
        sb.append(this.c_time);
        sb.append(":    ");
        for(process p:this.all_process_list){
            if(this.unstarted_list.contains(p)){
                sb.append("unstarted  0  ");
            }else if(this.blocked_list.contains(p)){
                sb.append("blocked  ");
                sb.append(p.io_burst);
                sb.append("  ");
            }else if(this.ready_list.contains(p)){
                sb.append("ready  0  ");
            }else if(this.running_list.contains(p)){
                sb.append("running  ");
                sb.append(p.cpu_burst);
                sb.append("  ");
            }else{
                sb.append("terminated  0  ");
            }
        }
        System.out.println(sb.toString());
    }
    
    public void run(){
        process temp;
        ArrayList<process> new_ready_list=new ArrayList<process>();
        ArrayList<process> new_blocked_list=new ArrayList<process>();
        ArrayList<process> new_unstarted_list=new ArrayList<process>();
        ArrayList<process> new_running_list=new ArrayList<process>();
        while((!unstarted_list.isEmpty()) || (!running_list.isEmpty()) || (!ready_list.isEmpty())
                ||(!blocked_list.isEmpty())){
            if(this.verbose==1){
                verbose_report();
            }
            //increase the wait time for all process in ready_list first.
            for(process p:ready_list){
                p.w_time+=1;
            }
            
            //blocked_state transit
            if(!blocked_list.isEmpty()){
                this.io_time+=1;
            }
            while(!this.blocked_list.isEmpty()){
                temp=this.blocked_list.get(0);
                continue_io(temp);
                if(temp.io_burst<=0){
                    new_ready_list.add(temp);
                }else{
                    new_blocked_list.add(temp);
                }
                blocked_list.remove(0);
            }
            while(!new_blocked_list.isEmpty()){
                temp=new_blocked_list.get(0);
                blocked_list.add(temp);
                new_blocked_list.remove(0);
            }
            
            //unstarted_state transit
            while(!this.unstarted_list.isEmpty()){
                temp=this.unstarted_list.get(0);
                if(temp.arrive==this.c_time){
                    new_ready_list.add(temp);
                }else{
                    new_unstarted_list.add(temp);
                }
                unstarted_list.remove(0);
            }
            while(!new_unstarted_list.isEmpty()){
                temp=new_unstarted_list.get(0);
                unstarted_list.add(temp);
                new_unstarted_list.remove(0);
            }
            
            //running_state transit
            
            if(!running_list.isEmpty()){
                this.cpu_time+=1;
            }
            while(!this.running_list.isEmpty()){
                temp=this.running_list.get(0);
                temp.cpu_work-=1;
                temp.cpu_burst-=1;
                temp.quantum-=1;
                if(temp.cpu_work<=0){
                    //exit the process
                    temp.exit(c_time);
                }else if(temp.cpu_burst<=0){
                    start_io(temp);
                    temp.quantum=-1;
                    blocked_list.add(temp);
                }else if(temp.quantum==0){
                    temp.quantum=-1;
                    new_ready_list.add(temp);
                }else{
                    //keep running
                    new_running_list.add(temp);
                }
                this.running_list.remove(0);
            }
            while(!new_running_list.isEmpty()){
                temp=new_running_list.get(0);
                running_list.add(temp);
                new_running_list.remove(0);
            }
            
            //ready-state transit
            //if multiple job transit from "blocked" state to "ready" state, break the tier
            Collections.sort(new_ready_list);
            //After we break the tier, put them in the ready_list
            while(!new_ready_list.isEmpty()){
                temp=new_ready_list.get(0);
                new_ready_list.remove(0);
                ready_list.add(temp);
            }
            
            if(running_list.isEmpty()){
                //schedule a process to run
                if(!ready_list.isEmpty()){
                    temp=ready_list.get(0);
                    start_cpu(temp);
                    temp.quantum=this.quantum;
                    ready_list.remove(temp);
                    running_list.add(temp);
                }else{
                    //cpu wastes one cycle
                }
            }
            
            this.c_time++;
        }
    }
    
    public void start_cpu(process p){
        if(p.cpu_burst>0){
            return;
        }else{
            int c=rng.get_random_number(p.cpu_range);
            if (c>p.cpu_work){
                c=p.cpu_work;
            }
            p.cpu_burst=c;
        }
    }
    
    public void start_io(process p){
        int io=rng.get_random_number(p.io_range);
        p.io_burst=io;
    }
    
    public void continue_io(process p){
        p.IO_time+=1;
        p.io_burst-=1;
    }
    
}
class FCFS{
    int c_time;
    int verbose;
    //for report purpose
    int cpu_time;
    int io_time;
    int total_t_time;
    int total_w_time;
    //book keeping
    ArrayList<process> all_process_list;
    ArrayList<process> unstarted_list;
    ArrayList<process> running_list;
    ArrayList<process> ready_list;
    ArrayList<process> blocked_list;
    RNG rng;
    
    public FCFS(ArrayList<process> apl, RNG r, int v){
        this.c_time=0;
        this.verbose=v;
        this.cpu_time=0;
        this.io_time=0;
        this.total_t_time=0;
        this.total_w_time=0;
        
        this.all_process_list=apl;
        this.unstarted_list=(ArrayList<process> )apl.clone();
        this.running_list=new ArrayList<process>();
        this.ready_list=new ArrayList<process>();
        this.blocked_list=new ArrayList<process>();
        this.rng=r;
    }
    
    public void report(){
        this.c_time-=1;
        process p;
        System.out.println("The scheduling algorithm used was First Come First Served");
        System.out.println();
        for(int i=0;i<this.all_process_list.size();i++){
            p=all_process_list.get(i);
            this.total_t_time+=p.t_time;
            this.total_w_time+=p.w_time;
            p.report(i);
            System.out.println();
        }
        System.out.println("Summary Data: ");
        System.out.printf("Finishing time: %d \n", this.c_time);
        double ratio=(double)(this.cpu_time)/(double)(this.c_time);
        System.out.printf("CPU Utilization: %f \n", ratio);
        ratio=(double)(this.io_time)/(double)(this.c_time);
        System.out.printf("I/O Utilization: %f \n", ratio);
        ratio=(double)(this.all_process_list.size())*100.0/(double)(this.c_time);
        System.out.printf("Throughtput: %f \n", ratio);
        ratio=(double)(this.total_t_time)/(double)(this.all_process_list.size());
        System.out.printf("Average turnaround time: %f \n", ratio);
        ratio=(double)(this.total_w_time)/(double)(this.all_process_list.size());
        System.out.printf("Averaged waiting time: %f \n", ratio);
        System.out.println();
        
        this.c_time+=1;
    }
    public void verbose_report(){
        StringBuilder sb=new StringBuilder();
        sb.append("Before cycle ");
        sb.append(this.c_time);
        sb.append(":    ");
        for(process p:this.all_process_list){
            if(this.unstarted_list.contains(p)){
                sb.append("unstarted  0  ");
            }else if(this.blocked_list.contains(p)){
                sb.append("blocked  ");
                sb.append(p.io_burst);
                sb.append("  ");
            }else if(this.ready_list.contains(p)){
                sb.append("ready  0  ");
            }else if(this.running_list.contains(p)){
                sb.append("running  ");
                sb.append(p.cpu_burst);
                sb.append("  ");
            }else{
                sb.append("terminated  0  ");
            }
        }
        System.out.println(sb.toString());
    }
    
    public void run(){
        process temp;
        ArrayList<process> new_ready_list=new ArrayList<process>();
        ArrayList<process> new_blocked_list=new ArrayList<process>();
        ArrayList<process> new_unstarted_list=new ArrayList<process>();
        ArrayList<process> new_running_list=new ArrayList<process>();
        while((!unstarted_list.isEmpty()) || (!running_list.isEmpty()) || (!ready_list.isEmpty())
                ||(!blocked_list.isEmpty())){
            if(this.verbose==1){
                verbose_report();
            }
            //increase the wait time for all process in ready_list first.
            for(process p:ready_list){
                p.w_time+=1;
            }
            
            //blocked_state transit
            if(!blocked_list.isEmpty()){
                this.io_time+=1;
            }
            while(!this.blocked_list.isEmpty()){
                temp=this.blocked_list.get(0);
                continue_io(temp);
                if(temp.io_burst<=0){
                    new_ready_list.add(temp);
                }else{
                    new_blocked_list.add(temp);
                }
                blocked_list.remove(0);
            }
            while(!new_blocked_list.isEmpty()){
                temp=new_blocked_list.get(0);
                blocked_list.add(temp);
                new_blocked_list.remove(0);
            }
            
            //unstarted_state transit
            while(!this.unstarted_list.isEmpty()){
                temp=this.unstarted_list.get(0);
                if(temp.arrive==this.c_time){
                    new_ready_list.add(temp);
                }else{
                    new_unstarted_list.add(temp);
                }
                unstarted_list.remove(0);
            }
            while(!new_unstarted_list.isEmpty()){
                temp=new_unstarted_list.get(0);
                unstarted_list.add(temp);
                new_unstarted_list.remove(0);
            }
            
            //running_state transit
            if(!running_list.isEmpty()){
                this.cpu_time+=1;
            }
            while(!this.running_list.isEmpty()){
                temp=this.running_list.get(0);
                temp.cpu_work-=1;
                temp.cpu_burst-=1;
                if(temp.cpu_work<=0){
                    //exit the process
                    temp.exit(c_time);
                }else if(temp.cpu_burst<=0){
                    start_io(temp);
                    blocked_list.add(temp);
                }else{
                    //no quantum preempt situation
                    new_running_list.add(temp);
                }
                this.running_list.remove(0);
            }
            while(!new_running_list.isEmpty()){
                temp=new_running_list.get(0);
                running_list.add(temp);
                new_running_list.remove(0);
            }
            
            //ready-state transit
            //if multiple job transit from "blocked" state to "ready" state, break the tier
            Collections.sort(new_ready_list);
            //After we break the tier, put them in the ready_list
            while(!new_ready_list.isEmpty()){
                temp=new_ready_list.get(0);
                new_ready_list.remove(0);
                ready_list.add(temp);
            }
            
            if(running_list.isEmpty()){
                //schedule a process to run
                if(!ready_list.isEmpty()){
                    temp=ready_list.get(0);
                    //continue to use cpu-burst
                    start_cpu(temp);
                    ready_list.remove(temp);
                    running_list.add(temp);
                }else{
                    //cpu wastes one cycle
                }
            }
            
            this.c_time++;
        }
    }
    
    public void start_cpu(process p){
        if(p.cpu_burst>0){
            return;
        }else{
            int c=rng.get_random_number(p.cpu_range);
            if (c>p.cpu_work){
                c=p.cpu_work;
            }
            p.cpu_burst=c;
        }
    }
    
    public void start_io(process p){
        int io=rng.get_random_number(p.io_range);
        p.io_burst=io;
    }
    
    public void continue_io(process p){
        p.IO_time+=1;
        p.io_burst-=1;
    }
}

class RNG{
    ArrayList<Integer> arr;
    int use;

    public RNG() {
        this.arr=new ArrayList<Integer>();
        this.use=0;
        String fn=System.getProperty("user.dir");
        fn+="/random-numbers.txt";
        File f=new File(fn);
        try{
            String str;
            BufferedReader br=new BufferedReader(new FileReader(f));
            str=br.readLine();
            int r;
            while(str!=null){
                str=str.trim();
                r=Integer.parseInt(str);
                arr.add(r);
                str=br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int get_random_number(int bound){
        int r=arr.get(use);
        use++;
        return 1+(r%bound);
    }
    
    public void clear_history(){
        use=0;
    }
}

class process implements Comparable{
    int arrive;
    int cpu_range;
    int cpu_cycle;
    int io_range;
    int index;
    int quantum;
    
    int cpu_work;
    int cpu_burst;
    int io_burst;
    
    int f_time;
    int t_time;
    int IO_time;
    int w_time;

    public process(int arrive, int cpu_range, int cpu_cycle, int io_range, int order) {
        this.arrive = arrive;
        this.cpu_range = cpu_range;
        this.cpu_cycle = cpu_cycle;
        this.io_range = io_range;
        this.index=order;
        this.quantum=-1;
        
        this.cpu_work= cpu_cycle;
        this.cpu_burst=-1;
        this.io_burst=-1;
        
        this.f_time=-1;
        this.t_time=-1;
        this.IO_time=0;
        this.w_time=0;
    }
    
    public void reset(){
        this.quantum=-1;
        this.cpu_work=this.cpu_cycle;
        this.cpu_burst=-1;
        this.io_burst=-1;
        
        this.f_time=-1;
        this.t_time=-1;
        this.IO_time=0;
        this.w_time=0;
    }
    
    public void exit(int c_time){
        this.f_time=c_time;
        this.t_time=c_time-this.arrive;
    }
    
    public void report(int i){
        //do some report
        System.out.printf("Process %d: \n",i);
        System.out.printf("(A,B,C,IO) = (%d,%d,%d,%d) \n", arrive, cpu_range, cpu_cycle, io_range);
        System.out.printf("Finishing time: %d \n", this.f_time);
        System.out.printf("Turnaround time: %d \n", this.t_time);
        System.out.printf("I/O time: %d \n", this.IO_time);
        System.out.printf("Waiting time %d \n", this.w_time);
    }

    @Override
    public int compareTo(Object o) {
        int arrive_p=((process) o).arrive;
        int index_p=((process) o).index;
        int cmp=this.arrive-arrive_p;
        if(cmp==0){
            return this.index-index_p;
        }else{
            return this.arrive-arrive_p;
        }
    }
    
    
}
