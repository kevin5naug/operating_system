Lab 2 Scheduling Yixing Guan

Source file for Lab2:
1. OS_lab2.java
2. random_numbers.txt

This source file gets the path to the test file through command line argument.

Guide:
1. Upload OS_lab2.java and the sample inputs to the test machine.

2. Get to the directory where OS_lab2.java is in (cd).

3. Type ¡°javac OS_lab2.java¡± in command line in order to compile the program. It might display the following, though it doesn¡¯t matter:

Note: OS_lab2.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.

4. IMPORTANT: check that in current directory, there is a file named ¡°random-numbers.txt¡± that provides the same list of random-numbers as https://cs.nyu.edu/courses/spring18/CSCI-UA.0202-001/labs/random-numbers

If not, upload it to the current directory. (See my submission for one copy of random_numbers.txt)

5. Type ¡°java OS_lab2 /path/to/test_case.txt¡± or ¡°java OS_lab2 ¡ª-verbose /path/to/test_case.txt¡±to activate the program. If the latter is used, a more detailed output will be provided.





Example(the commands I used to test my program on NYU machine):


1. On the terminal of my computer, I typed and entered:

scp -r /Users/joker/Documents/2018\ Spring/OS/OS_lab2  yg1227@access.cims.nyu.edu:/home/yg1227/OS_lab2

This allows me to upload OS_lab2.java, the random-numbers.txt and the sample inputs, which are all contained in one directory /Users/joker/Documents/2018\ Spring/OS/OS_lab2, to the test machine in one command.



2. After using ssh to get connected to the NYU machine yg1227@courses2, I typed and entered the following commands:

1) cd ~
2) cd OS_lab2/



3. type and enter: ls

Output form the terminal:
input-1.txt  input-3.txt  input-5.txt  input-7.txt   random-numbers.txt
input-2.txt  input-4.txt  input-6.txt  OS_lab2.java

There is indeed a ¡°random-numbers.txt¡± file that provides the same list of random-numbers as https://cs.nyu.edu/courses/spring18/CSCI-UA.0202-001/labs/random-numbers



4. Since I stored all of my test cases in the current directory /home/yg1227/OS_lab2, the absolute path to one of the test cases can be: /home/yg1227/OS_lab2/input-1.txt




5. type and enter: javac OS_lab2.java  
This will compile the program.



6. type and enter: java OS_lab2 /home/yg1227/OS_lab2/input-1.txt

The command line argument ¡°/home/yg1227/OS_lab2/input-1.txt¡± will then be passed to the main function.


Input(input-1.txt):
1  0 1 5 1  about as easy as possible

Output:

extracted filename: /home/yg1227/OS_lab2/input-1.txt
The original input was:    1    0 1 5 1   
The (sorted) input was:    1    0 1 5 1   

Simulation I: FCFS

The scheduling algorithm used was First Come First Served

Process 0: 
(A,B,C,IO) = (0,1,5,1) 
Finishing time: 9 
Turnaround time: 9 
I/O time: 4 
Waiting time 0 

Summary Data: 
Finishing time: 9 
CPU Utilization: 0.555556 
I/O Utilization: 0.444444 
Throughtput: 11.111111 
Average turnaround time: 9.000000 
Averaged waiting time: 0.000000 


Simulation II: RR(q=2)

The scheduling algorithm used was Round Robin with q=2 

Process 0: 
(A,B,C,IO) = (0,1,5,1) 
Finishing time: 9 
Turnaround time: 9 
I/O time: 4 
Waiting time 0 

Summary Data: 
Finishing time: 9 
CPU Utilization: 0.555556 
I/O Utilization: 0.444444 
Throughtput: 11.111111 
Average turnaround time: 9.000000 
Averaged waiting time: 0.000000 


Simulation III: Uniprogrammed

The scheduling algorithm used was Uniprocessor

Process 0: 
(A,B,C,IO) = (0,1,5,1) 
Finishing time: 9 
Turnaround time: 9 
I/O time: 4 
Waiting time 0 

Summary Data: 
Finishing time: 9 
CPU Utilization: 0.555556 
I/O Utilization: 0.444444 
Throughtput: 11.111111 
Average turnaround time: 9.000000 
Averaged waiting time: 0.000000 


Simulation IV: SRTN

The scheduling algorithm used was Preemptive Shortest Job First

Process 0: 
(A,B,C,IO) = (0,1,5,1) 
Finishing time: 9 
Turnaround time: 9 
I/O time: 4 
Waiting time 0 

Summary Data: 
Finishing time: 9 
CPU Utilization: 0.555556 
I/O Utilization: 0.444444 
Throughtput: 11.111111 processes per hundred cycles 
Average turnaround time: 9.000000 
Averaged waiting time: 0.000000




7. type and enter: java OS_lab2 --verbose /home/yg1227/OS_lab2/input-1.txt

The command line arguments ¡°¡ª-verbose¡± and ¡°/home/yg1227/OS_lab2/input-1.txt¡± will then be passed to the main function.

Input:
Same as above.

Output:

extracted filename: /home/yg1227/OS_lab2/input-1.txt
The original input was:    1    0 1 5 1   
The (sorted) input was:    1    0 1 5 1   

Simulation I: FCFS

Before cycle 0:    unstarted  0  
Before cycle 1:    running  1  
Before cycle 2:    blocked  1  
Before cycle 3:    running  1  
Before cycle 4:    blocked  1  
Before cycle 5:    running  1  
Before cycle 6:    blocked  1  
Before cycle 7:    running  1  
Before cycle 8:    blocked  1  
Before cycle 9:    running  1  
The scheduling algorithm used was First Come First Served

Process 0: 
(A,B,C,IO) = (0,1,5,1) 
Finishing time: 9 
Turnaround time: 9 
I/O time: 4 
Waiting time 0 

Summary Data: 
Finishing time: 9 
CPU Utilization: 0.555556 
I/O Utilization: 0.444444 
Throughtput: 11.111111 
Average turnaround time: 9.000000 
Averaged waiting time: 0.000000 


Simulation II: RR(q=2)

Before cycle 0:    unstarted  0  
Before cycle 1:    running  1  
Before cycle 2:    blocked  1  
Before cycle 3:    running  1  
Before cycle 4:    blocked  1  
Before cycle 5:    running  1  
Before cycle 6:    blocked  1  
Before cycle 7:    running  1  
Before cycle 8:    blocked  1  
Before cycle 9:    running  1  
The scheduling algorithm used was Round Robin with q=2 

Process 0: 
(A,B,C,IO) = (0,1,5,1) 
Finishing time: 9 
Turnaround time: 9 
I/O time: 4 
Waiting time 0 

Summary Data: 
Finishing time: 9 
CPU Utilization: 0.555556 
I/O Utilization: 0.444444 
Throughtput: 11.111111 
Average turnaround time: 9.000000 
Averaged waiting time: 0.000000 


Simulation III: Uniprogrammed

Before cycle 0:    unstarted  0  
Before cycle 1:    running  1  
Before cycle 2:    blocked  1  
Before cycle 3:    running  1  
Before cycle 4:    blocked  1  
Before cycle 5:    running  1  
Before cycle 6:    blocked  1  
Before cycle 7:    running  1  
Before cycle 8:    blocked  1  
Before cycle 9:    running  1  
The scheduling algorithm used was Uniprocessor

Process 0: 
(A,B,C,IO) = (0,1,5,1) 
Finishing time: 9 
Turnaround time: 9 
I/O time: 4 
Waiting time 0 

Summary Data: 
Finishing time: 9 
CPU Utilization: 0.555556 
I/O Utilization: 0.444444 
Throughtput: 11.111111 
Average turnaround time: 9.000000 
Averaged waiting time: 0.000000 


Simulation IV: SRTN

Before cycle 0:    unstarted  0  
Before cycle 1:    running    1  
Before cycle 2:    blocked    1  
Before cycle 3:    running    1  
Before cycle 4:    blocked    1  
Before cycle 5:    running    1  
Before cycle 6:    blocked    1  
Before cycle 7:    running    1  
Before cycle 8:    blocked    1  
Before cycle 9:    running    1  
The scheduling algorithm used was Preemptive Shortest Job First

Process 0: 
(A,B,C,IO) = (0,1,5,1) 
Finishing time: 9 
Turnaround time: 9 
I/O time: 4 
Waiting time 0 

Summary Data: 
Finishing time: 9 
CPU Utilization: 0.555556 
I/O Utilization: 0.444444 
Throughtput: 11.111111 processes per hundred cycles 
Average turnaround time: 9.000000 
Averaged waiting time: 0.000000 

