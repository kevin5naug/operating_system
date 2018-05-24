
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
public class OS_lab4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int M=-1;
        int P=-1;
        int S=-1;
        int J=-1;
        int N=-1;
        int verbose=-1;
        String R=null;
        if(args.length==6){
            M=Integer.parseInt(args[0]);
            P=Integer.parseInt(args[1]);
            S=Integer.parseInt(args[2]);
            J=Integer.parseInt(args[3]);
            N=Integer.parseInt(args[4]);
            R=args[5];
            verbose=0;
        }else if(args.length==7){
            M=Integer.parseInt(args[0]);
            P=Integer.parseInt(args[1]);
            S=Integer.parseInt(args[2]);
            J=Integer.parseInt(args[3]);
            N=Integer.parseInt(args[4]);
            R=args[5];
            if(args[6].equals("--verbose")){
                verbose=1;
            }else{
                verbose=0;
            }
        }else{
            System.out.println("ERROR: No command Line argument pattern matched. See readme file for usage example");
            System.exit(0);
        }
        RNG r=new RNG();
        System.out.printf("The machine size is %d\n", M);
        System.out.printf("The page size is %d\n", P);
        System.out.printf("The process size is %d\n", S);
        System.out.printf("The job mix number is %d\n", J);
        System.out.printf("The number of references per process is %d\n", N);
        System.out.printf("The replacement algorithm is %s\n", R);
        System.out.println();
        //setup the task list according to the value of J
        ArrayList<Process> all_tasks=tasks_setup(P,J,S,N);
        if(R.equals("fifo")){
            FIFO f=new FIFO(verbose,M,P,S,J,N,all_tasks,r);
            f.Run();
            f.Report();
        }else if(R.equals("random")){
            RANDOM ran=new RANDOM(verbose,M,P,S,J,N,all_tasks,r);
            ran.Run();
            ran.Report();
        }else if(R.equals("lru")){
            LRU l=new LRU(verbose,M,P,S,J,N,all_tasks,r);
            l.Run();
            l.Report();
        }else{
            //ERROR: should never reach here
            System.out.println("ERROR: Invalid pager type!!!!!!!!!!!!!!");
        }
        
    }
    
    public static ArrayList<Process> tasks_setup(int P, int J, int S, int N){
        ArrayList<Process> all_tasks=new ArrayList<Process>();
        Process temp;
        if(J==1){
            //one process with A=1 and B=C=0
            temp=new Process(1,1,0,0,P,S,N);
            all_tasks.add(temp);
        }else if(J==2){
            //four processes, each with A=1 and B=C=0
            temp=new Process(1,1,0,0,P,S,N);
            all_tasks.add(temp);
            temp=new Process(2,1,0,0,P,S,N);
            all_tasks.add(temp);
            temp=new Process(3,1,0,0,P,S,N);
            all_tasks.add(temp);
            temp=new Process(4,1,0,0,P,S,N);
            all_tasks.add(temp);
        }else if(J==3){
            //four processes, each with A=B=C=0(fully random)
            temp=new Process(1,0,0,0,P,S,N);
            all_tasks.add(temp);
            temp=new Process(2,0,0,0,P,S,N);
            all_tasks.add(temp);
            temp=new Process(3,0,0,0,P,S,N);
            all_tasks.add(temp);
            temp=new Process(4,0,0,0,P,S,N);
            all_tasks.add(temp);
        }else if(J==4){
            //special setup
            temp=new Process(1,0.75,0.25,0,P,S,N);
            all_tasks.add(temp);
            temp=new Process(2,0.75,0,0.25,P,S,N);
            all_tasks.add(temp);
            temp=new Process(3,0.75,0.125,0.125,P,S,N);
            all_tasks.add(temp);
            temp=new Process(4,0.5,0.125,0.125,P,S,N);
            all_tasks.add(temp);
        }else{
            System.out.println("ERROR: the arguement value J is not valid!!!!!!!!!!!!!!!!!!!!");
        }
        
        return all_tasks;
    }
}

class FIFO{
    int verbose;
    int machine_size;
    int page_size;
    int vritual_memory_size_for_each_process;
    int J;
    int N;
    int ctime;
    ArrayList<Process> all_tasks;
    ArrayList<Frame> all_frames;
    PriorityQueue<Integer> free_frame_indexes;
    LinkedList<Integer> used_frame_indexes;
    RNG rng;

    public FIFO(int verbose, int M, int P, int S, int J, int N, ArrayList<Process> tasks, RNG r) {
        this.verbose=verbose;
        this.machine_size = M;
        this.page_size = P;
        this.vritual_memory_size_for_each_process = S;
        this.J = J;
        this.N = N;
        this.ctime=0;
        this.all_tasks=tasks;
        this.rng=r;
        
        //setup for the frame
        int total_frame_num=M/P;
        this.free_frame_indexes=new PriorityQueue<>(total_frame_num, Collections.reverseOrder());
        this.used_frame_indexes=new LinkedList<>();
        //test for remainder
        if(M%P!=0){
            System.out.println("ERROR: P cannot be divided by N. We are going to waste some words");
        }
        this.all_frames=new ArrayList<Frame>();
        Frame temp;
        for(int i=0;i<total_frame_num;i++){
            temp=new Frame(i);
            this.all_frames.add(temp);
            this.free_frame_indexes.add(i);
        }
    }
    
    public void Report(){
        Process target;
        int page_fault_sum=0;
        int page_resident_time_sum=0;
        int page_evicted_num_sum=0;
        for(int i=0;i<this.all_tasks.size();i++){
            target=this.all_tasks.get(i);
            page_fault_sum+=target.page_fault_count;
            page_resident_time_sum+=target.page_total_resident_time;
            page_evicted_num_sum+=target.page_total_evict_num;
            System.out.printf("Process %d had %d faults ", i+1, target.page_fault_count);
            if(target.page_total_evict_num==0){
                System.out.printf("and had no evictions. The average residency is undefined.\n");
            }else{
                System.out.printf("and %f average residency\n", target.page_total_resident_time*1.00/target.page_total_evict_num);
            }
        }
        System.out.println();
        System.out.printf("The total number of faults is %d ", page_fault_sum);
        if(page_evicted_num_sum==0){
            System.out.printf("and with no evictions, the overall average residency is undefined.\n");
        }else{
            System.out.printf("and the overall average residency is %f\n", page_resident_time_sum*1.0/page_evicted_num_sum);
        }
    }
    
    public void Run(){
        while(this.ctime<this.N*this.all_tasks.size()){
            this.ctime++;
            int target_index=solve_for_target(this.ctime);
            Process target=this.all_tasks.get(target_index);
            make_a_reference(target);
            //todo: compute next reference address
            setup_next_reference(target);
        }
    }
    
    public void setup_next_reference(Process target){
        int r=this.rng.get_random_number();
        double y=r/(Integer.MAX_VALUE+1d);
        if(y<target.A){
            target.next_address=(target.cur_address+1)%target.virtual_memory_size;
        }else if(y<target.A+target.B){
            target.next_address=(target.cur_address-5+target.virtual_memory_size)%target.virtual_memory_size;
        }else if(y<target.A+target.B+target.C){
            target.next_address=(target.cur_address+4)%target.virtual_memory_size;
        }else{
            r=this.rng.get_random_number();
            target.next_address=r%target.virtual_memory_size;
        }
    }
    
    public void make_a_reference(Process target){
        //make a reference according to the current address
        target.reference_count++;
        target.cur_address=target.next_address;
        int page_num=target.cur_address/this.page_size;
        if(this.verbose==1){
            System.out.printf("%d references word %d (page %d) at time %d: ", 
                target.id, target.cur_address, page_num, this.ctime);
        }
        int isResident=check_resident(target,page_num);
        if(isResident>0){
            //a hit, do nothing
        }else{
            page_fault_handler(target,page_num);
        }
    }
    
    public void page_fault_handler(Process target, int page_num){
        //check if there is any free frame first
        int index;
        Frame f;
        target.page_fault_count++;
        if(!this.free_frame_indexes.isEmpty()){
            index=this.free_frame_indexes.poll();
            this.used_frame_indexes.add(index);
            f=this.all_frames.get(index);
            use_free_frame(f,target,page_num);
            return;
        }else{
            //we don't have a free frame, we dequeue one frame by FIFO standard
            index=this.used_frame_indexes.poll();
            this.used_frame_indexes.add(index);
            f=this.all_frames.get(index);
            use_evicted_frame(f,target,page_num);
            return;
        }
    }
    
    public void use_evicted_frame(Frame f, Process target, int page_num){
        Process pre_owner=f.owner;
        int pre_page=f.owner_page_num;
        pre_owner.page_total_resident_time=pre_owner.page_total_resident_time+this.ctime-f.previous_load_time;
        pre_owner.page_total_evict_num++;
        if(this.verbose==1){
            System.out.printf("Fault, evicting page %d of Process %d from frame %d\n", pre_page, pre_owner.id, f.id);
        }
        f.owner=target;
        f.owner_page_num=page_num;
        f.previous_load_time=this.ctime;
    }
    
    public void use_free_frame(Frame f, Process target, int page_num){
        if(f.occupied==0){
            //this is a free frame
            if(this.verbose==1){
                System.out.printf("Fault, using free frame %d\n", f.id);
            }
            f.occupied=1;
            f.owner=target;
            f.owner_page_num=page_num;
            f.previous_load_time=this.ctime;
        }else{
            System.out.println("ERROR: This frame is not free but the pager thinks it is free");
        }
    }
    
    public int check_resident(Process target, int p){
        int index;
        Frame cur;
        for(int i=0;i<this.used_frame_indexes.size();i++){
            index=this.used_frame_indexes.get(i);
            cur=this.all_frames.get(index);
            if((cur.owner==target)&&(cur.owner_page_num==p)){
                if(this.verbose==1){
                    System.out.printf("Hit in frame %d\n", cur.id);
                }
                return 1;
            }
        }
        return 0;
    }
    
    public int solve_for_target(int ctime){
        int cycle=this.N/3;
        int remainder=this.N-3*cycle;
        int target, gap;
        if(remainder==0){
            if(this.J==1){
                //we only have one task
                return 0;
            }else{
                //we have many tasks
                target=((ctime-1)/3)%this.all_tasks.size();
                return target;
            }
        }else{
            //case 1: we are not at the last "additional" cycle
            if(ctime<=cycle*3*this.all_tasks.size()){
                if(this.J==1){
                    //we only have one task
                    return 0;
                }else{
                    //we have many tasks
                    target=((ctime-1)/3)%this.all_tasks.size();
                    return target;
                }
            }else{
                if(this.J==1){
                    //we only have one task
                    return 0;
                }else{
                    //we have many tasks
                    gap=ctime-cycle*3*this.all_tasks.size();
                    target=((gap-1)/remainder)%this.all_tasks.size();
                    return target;
                }
            }
        }
    }
}

class RANDOM{
    int verbose;
    int machine_size;
    int page_size;
    int vritual_memory_size_for_each_process;
    int J;
    int N;
    int ctime;
    ArrayList<Process> all_tasks;
    ArrayList<Frame> all_frames;
    PriorityQueue<Integer> free_frame_indexes;
    ArrayList<Integer> used_frame_indexes;
    RNG rng;
    
    public RANDOM(int verbose, int M, int P, int S, int J, int N, ArrayList<Process> tasks, RNG r) {
        this.verbose=verbose;
        this.machine_size = M;
        this.page_size = P;
        this.vritual_memory_size_for_each_process = S;
        this.J = J;
        this.N = N;
        this.ctime=0;
        this.all_tasks=tasks;
        this.rng=r;
        
        //setup for the frame
        int total_frame_num=M/P;
        this.free_frame_indexes=new PriorityQueue<>(total_frame_num, Collections.reverseOrder());
        this.used_frame_indexes=new ArrayList<>();
        //test for remainder
        if(M%P!=0){
            System.out.println("ERROR: P cannot be divided by N. We are going to waste some words");
        }
        this.all_frames=new ArrayList<Frame>();
        Frame temp;
        for(int i=0;i<total_frame_num;i++){
            temp=new Frame(i);
            this.all_frames.add(temp);
            this.free_frame_indexes.add(i);
        }
    }
    
    public void Report(){
        Process target;
        int page_fault_sum=0;
        int page_resident_time_sum=0;
        int page_evicted_num_sum=0;
        for(int i=0;i<this.all_tasks.size();i++){
            target=this.all_tasks.get(i);
            page_fault_sum+=target.page_fault_count;
            page_resident_time_sum+=target.page_total_resident_time;
            page_evicted_num_sum+=target.page_total_evict_num;
            System.out.printf("Process %d had %d faults ", i+1, target.page_fault_count);
            if(target.page_total_evict_num==0){
                System.out.printf("and had no evictions. The average residency is undefined.\n");
            }else{
                System.out.printf("and %f average residency\n", target.page_total_resident_time*1.00/target.page_total_evict_num);
            }
        }
        System.out.println();
        System.out.printf("The total number of faults is %d ", page_fault_sum);
        if(page_evicted_num_sum==0){
            System.out.printf("and with no evictions, the overall average residency is undefined.\n");
        }else{
            System.out.printf("and the overall average residency is %f\n", page_resident_time_sum*1.0/page_evicted_num_sum);
        }
    }
    
    public void Run(){
        while(this.ctime<this.N*this.all_tasks.size()){
            this.ctime++;
            int target_index=solve_for_target(this.ctime);
            Process target=this.all_tasks.get(target_index);
            make_a_reference(target);
            //todo: compute next reference address
            setup_next_reference(target);
        }
    }
     
    public void setup_next_reference(Process target){
        int r=this.rng.get_random_number();
        double y=r/(Integer.MAX_VALUE+1d);
        if(y<target.A){
            target.next_address=(target.cur_address+1)%target.virtual_memory_size;
        }else if(y<target.A+target.B){
            target.next_address=(target.cur_address-5+target.virtual_memory_size)%target.virtual_memory_size;
        }else if(y<target.A+target.B+target.C){
            target.next_address=(target.cur_address+4)%target.virtual_memory_size;
        }else{
            r=this.rng.get_random_number();
            target.next_address=r%target.virtual_memory_size;
        }
    }
    
    public void make_a_reference(Process target){
        //make a reference according to the current address
        target.reference_count++;
        target.cur_address=target.next_address;
        int page_num=target.cur_address/this.page_size;
        if(this.verbose==1){
            System.out.printf("%d references word %d (page %d) at time %d: ", 
                target.id, target.cur_address, page_num, this.ctime);
        }
        int isResident=check_resident(target,page_num);
        if(isResident>0){
            //a hit, do nothing
        }else{
            page_fault_handler(target,page_num);
        }
    }
    
    public void page_fault_handler(Process target, int page_num){
        //check if there is any free frame first
        int index;
        Frame f;
        target.page_fault_count++;
        if(!this.free_frame_indexes.isEmpty()){
            index=this.free_frame_indexes.poll();
            this.used_frame_indexes.add(index);
            f=this.all_frames.get(index);
            use_free_frame(f,target,page_num);
            return;
        }else{
            //we don't have a free frame, we randomly choose one used frame and evict it
            //Note that if there is not free frame, all frames must be "used frames"
            index=this.rng.get_random_number()%this.all_frames.size();
            f=this.all_frames.get(index);
            use_evicted_frame(f,target,page_num);
            return;
        }
    }
    
    public void use_evicted_frame(Frame f, Process target, int page_num){
        Process pre_owner=f.owner;
        int pre_page=f.owner_page_num;
        pre_owner.page_total_resident_time=pre_owner.page_total_resident_time+this.ctime-f.previous_load_time;
        pre_owner.page_total_evict_num++;
        if(this.verbose==1){
            System.out.printf("Fault, evicting page %d of Process %d from frame %d\n", pre_page, pre_owner.id, f.id);
        }
        f.owner=target;
        f.owner_page_num=page_num;
        f.previous_load_time=this.ctime;
    }
    
    public void use_free_frame(Frame f, Process target, int page_num){
        if(f.occupied==0){
            //this is a free frame
            if(this.verbose==1){
                System.out.printf("Fault, using free frame %d\n", f.id);
            }
            f.occupied=1;
            f.owner=target;
            f.owner_page_num=page_num;
            f.previous_load_time=this.ctime;
        }else{
            System.out.println("ERROR: This frame is not free but the pager thinks it is free");
        }
    }
    
    public int check_resident(Process target, int p){
        int index;
        Frame cur;
        for(int i=0;i<this.used_frame_indexes.size();i++){
            index=this.used_frame_indexes.get(i);
            cur=this.all_frames.get(index);
            if((cur.owner==target)&&(cur.owner_page_num==p)){
                if(this.verbose==1){
                    System.out.printf("Hit in frame %d\n", cur.id);
                }
                return 1;
            }
        }
        return 0;
    }
    
    public int solve_for_target(int ctime){
        int cycle=this.N/3;
        int remainder=this.N-3*cycle;
        int target, gap;
        if(remainder==0){
            if(this.J==1){
                //we only have one task
                return 0;
            }else{
                //we have many tasks
                target=((ctime-1)/3)%this.all_tasks.size();
                return target;
            }
        }else{
            //case 1: we are not at the last "additional" cycle
            if(ctime<=cycle*3*this.all_tasks.size()){
                if(this.J==1){
                    //we only have one task
                    return 0;
                }else{
                    //we have many tasks
                    target=((ctime-1)/3)%this.all_tasks.size();
                    return target;
                }
            }else{
                if(this.J==1){
                    //we only have one task
                    return 0;
                }else{
                    //we have many tasks
                    gap=ctime-cycle*3*this.all_tasks.size();
                    target=((gap-1)/remainder)%this.all_tasks.size();
                    return target;
                }
            }
        }
    }
     
}

class LRU{
    int verbose;
    int machine_size;
    int page_size;
    int vritual_memory_size_for_each_process;
    int J;
    int N;
    int ctime;
    ArrayList<Process> all_tasks;
    ArrayList<Frame> all_frames;
    PriorityQueue<Integer> free_frame_indexes;
    LinkedList<Integer> used_frame_indexes;
    RNG rng;
    
    public LRU(int verbose, int M, int P, int S, int J, int N, ArrayList<Process> tasks, RNG r) {
        this.verbose=verbose;
        this.machine_size = M;
        this.page_size = P;
        this.vritual_memory_size_for_each_process = S;
        this.J = J;
        this.N = N;
        this.ctime=0;
        this.all_tasks=tasks;
        this.rng=r;
        
        //setup for the frame
        int total_frame_num=M/P;
        this.free_frame_indexes=new PriorityQueue<>(total_frame_num, Collections.reverseOrder());
        this.used_frame_indexes=new LinkedList<>();
        //test for remainder
        if(M%P!=0){
            System.out.println("ERROR: P cannot be divided by N. We are going to waste some words");
        }
        this.all_frames=new ArrayList<Frame>();
        Frame temp;
        for(int i=0;i<total_frame_num;i++){
            temp=new Frame(i);
            this.all_frames.add(temp);
            this.free_frame_indexes.add(i);
        }
    }
    
    public void Report(){
        Process target;
        int page_fault_sum=0;
        int page_resident_time_sum=0;
        int page_evicted_num_sum=0;
        for(int i=0;i<this.all_tasks.size();i++){
            target=this.all_tasks.get(i);
            page_fault_sum+=target.page_fault_count;
            page_resident_time_sum+=target.page_total_resident_time;
            page_evicted_num_sum+=target.page_total_evict_num;
            System.out.printf("Process %d had %d faults ", i+1, target.page_fault_count);
            if(target.page_total_evict_num==0){
                System.out.printf("and had no evictions. The average residency is undefined.\n");
            }else{
                System.out.printf("and %f average residency\n", target.page_total_resident_time*1.00/target.page_total_evict_num);
            }
        }
        System.out.println();
        System.out.printf("The total number of faults is %d ", page_fault_sum);
        if(page_evicted_num_sum==0){
            System.out.printf("and with no evictions, the overall average residency is undefined.\n");
        }else{
            System.out.printf("and the overall average residency is %f\n", page_resident_time_sum*1.0/page_evicted_num_sum);
        }
    }
    
    public void Run(){
        while(this.ctime<this.N*this.all_tasks.size()){
            this.ctime++;
            int target_index=solve_for_target(this.ctime);
            Process target=this.all_tasks.get(target_index);
            make_a_reference(target);
            //todo: compute next reference address
            setup_next_reference(target);
        }
    }
    
    public void setup_next_reference(Process target){
        int r=this.rng.get_random_number();
        double y=r/(Integer.MAX_VALUE+1d);
        if(y<target.A){
            target.next_address=(target.cur_address+1)%target.virtual_memory_size;
        }else if(y<target.A+target.B){
            target.next_address=(target.cur_address-5+target.virtual_memory_size)%target.virtual_memory_size;
        }else if(y<target.A+target.B+target.C){
            target.next_address=(target.cur_address+4)%target.virtual_memory_size;
        }else{
            r=this.rng.get_random_number();
            target.next_address=r%target.virtual_memory_size;
        }
    }
    
    public void make_a_reference(Process target){
        //make a reference according to the current address
        target.reference_count++;
        target.cur_address=target.next_address;
        int page_num=target.cur_address/this.page_size;
        if(this.verbose==1){
            System.out.printf("%d references word %d (page %d) at time %d: ", 
                target.id, target.cur_address, page_num, this.ctime);
        }
        int[] result=check_resident(target,page_num);
        int isResident=result[0];
        int index=result[1];
        if(isResident>0){
            //a hit, update the used frame list
            this.used_frame_indexes.remove((Integer) index);
            this.used_frame_indexes.add((Integer) index);
        }else{
            page_fault_handler(target,page_num);
        }
    }
    
    public void page_fault_handler(Process target, int page_num){
        //check if there is any free frame first
        int index;
        Frame f;
        target.page_fault_count++;
        if(!this.free_frame_indexes.isEmpty()){
            index=this.free_frame_indexes.poll();
            this.used_frame_indexes.add(index);
            f=this.all_frames.get(index);
            use_free_frame(f,target,page_num);
            return;
        }else{
            //we don't have a free frame, we dequeue one frame by FIFO standard
            index=this.used_frame_indexes.poll();
            this.used_frame_indexes.add(index);
            f=this.all_frames.get(index);
            use_evicted_frame(f,target,page_num);
            return;
        }
    }
    
    public void use_evicted_frame(Frame f, Process target, int page_num){
        Process pre_owner=f.owner;
        int pre_page=f.owner_page_num;
        pre_owner.page_total_resident_time=pre_owner.page_total_resident_time+this.ctime-f.previous_load_time;
        pre_owner.page_total_evict_num++;
        if(this.verbose==1){
            System.out.printf("Fault, evicting page %d of Process %d from frame %d\n", pre_page, pre_owner.id, f.id);
        }
        f.owner=target;
        f.owner_page_num=page_num;
        f.previous_load_time=this.ctime;
    }
    
    public void use_free_frame(Frame f, Process target, int page_num){
        if(f.occupied==0){
            //this is a free frame
            if(this.verbose==1){
                System.out.printf("Fault, using free frame %d\n", f.id);
            }
            f.occupied=1;
            f.owner=target;
            f.owner_page_num=page_num;
            f.previous_load_time=this.ctime;
        }else{
            System.out.println("ERROR: This frame is not free but the pager thinks it is free");
        }
    }
    
    public int[] check_resident(Process target, int p){
        int[] result={-1,-1};
        int index;
        Frame cur;
        for(int i=0;i<this.used_frame_indexes.size();i++){
            index=this.used_frame_indexes.get(i);
            cur=this.all_frames.get(index);
            if((cur.owner==target)&&(cur.owner_page_num==p)){
                if(this.verbose==1){
                    System.out.printf("Hit in frame %d\n", cur.id);
                }
                result[0]=1;
                result[1]=index;
                return result;
            }
        }
        result[0]=0;
        return result;
    }
    
    public int solve_for_target(int ctime){
        int cycle=this.N/3;
        int remainder=this.N-3*cycle;
        int target, gap;
        if(remainder==0){
            if(this.J==1){
                //we only have one task
                return 0;
            }else{
                //we have many tasks
                target=((ctime-1)/3)%this.all_tasks.size();
                return target;
            }
        }else{
            //case 1: we are not at the last "additional" cycle
            if(ctime<=cycle*3*this.all_tasks.size()){
                if(this.J==1){
                    //we only have one task
                    return 0;
                }else{
                    //we have many tasks
                    target=((ctime-1)/3)%this.all_tasks.size();
                    return target;
                }
            }else{
                if(this.J==1){
                    //we only have one task
                    return 0;
                }else{
                    //we have many tasks
                    gap=ctime-cycle*3*this.all_tasks.size();
                    target=((gap-1)/remainder)%this.all_tasks.size();
                    return target;
                }
            }
        }
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
    
    public int get_random_number(){
        int r=arr.get(use);
        use++;
        return r;
    }
    
    public void clear_history(){
        use=0;
    }
}

class Frame{
    int id;
    int occupied;
    Process owner;
    int owner_page_num;
    int previous_load_time;
    int recent_reference_time;

    public Frame(int id) {
        this.id = id;
        this.occupied=0;
        this.owner=null;
        this.owner_page_num=-1;
        this.previous_load_time=-1;
        this.recent_reference_time=-1;
    }
}

class Process{
    int id;
    double A;
    double B;
    double C;
    int page_size;
    int virtual_memory_size;
    int num_of_reference;
    int reference_count;
    int cur_address;
    int next_address;
    int page_fault_count;
    int page_total_resident_time;
    int page_total_evict_num;

    public Process(int id, double A, double B, double C, int P, int S, int N) {
        this.id=id;
        this.A = A;
        this.B = B;
        this.C = C;
        this.page_size = P;
        this.virtual_memory_size = S;
        this.num_of_reference = N;
        this.reference_count=0;
        this.cur_address=-1;
        this.next_address=(111*id)%S;
        this.page_fault_count=0;
        this.page_total_resident_time=0;
        this.page_total_evict_num=0;
    }
}


