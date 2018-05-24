Lab 4 Pager Yixing Guan

Source file for Lab4:
1. OS_lab4.java
2. random_numbers.txt

The program is invoked with 6 command line arguments as specified in lab requirement. Optionally, a 7th command line argument --verbose can be added at the end to produce information for debugging purpose (see example below).

Guide:
1. Upload OS_lab4.java and the random_numbers.txt to the test machine.

2. Get to the directory where OS_lab4.java is in (cd).

3. Type ¡°javac OS_lab4.java¡± in command line in order to compile the program.

4. IMPORTANT: check that in current directory, there is a file named ¡°random-numbers.txt¡± that provides the same list of random-numbers as https://cs.nyu.edu/courses/spring18/CSCI-UA.0202-001/labs/random-numbers

If not, upload it to the current directory. (See my submission for one copy of random_numbers.txt)

5. Type ¡°java OS_lab4 M P S J N R¡± or ¡°java OS_lab4 M P S J N R ¡ª-verbose¡±to activate the program. If the latter is used, a more detailed output will be provided.





Example(the commands I used to test my program on NYU machine):


1. On the terminal of my computer, I typed and entered:

scp -r /Users/joker/Documents/2018\ Spring/OS/OS_lab4  yg1227@access.cims.nyu.edu:/home/yg1227/OS_lab4

This allows me to upload OS_lab4.java and the random-numbers.txt, which are all contained in one directory /Users/joker/Documents/2018\ Spring/OS/OS_lab4, to the test machine in one command.



2. After using ssh to get connected to the NYU machine yg1227@courses2, I typed and entered the following commands:

1) cd ~
2) cd OS_lab4/



3. type and enter: ls

Output form the terminal:
OS_lab4.java  random-numbers.txt

There is indeed a ¡°random-numbers.txt¡± file that provides the same list of random-numbers as https://cs.nyu.edu/courses/spring18/CSCI-UA.0202-001/labs/random-numbers



4. type and enter: javac OS_lab4.java  
This will compile the program.



6. type and enter: java OS_lab4 20 10 10 2 10 fifo

The command line arguments M=20, P=10, S=10, J=2, N=10, R=¡°fifo¡± will then be passed to the main function.


Input(input-1.txt):
See Input Number 6 on https://cs.nyu.edu/courses/spring18/CSCI-UA.0202-001/labs/lab4/

Output:

The machine size is 20
The page size is 10
The process size is 10
The job mix number is 2
The number of references per process is 10
The replacement algorithm is fifo

Process 1 had 4 faults and 5.000000 average residency
Process 2 had 4 faults and 5.000000 average residency
Process 3 had 4 faults and 6.000000 average residency
Process 4 had 4 faults and 5.333333 average residency

The total number of faults is 16 and the overall average residency is 5.285714




7. type and enter: java OS_lab4 20 10 10 2 10 fifo --verbose

The command line arguments M=20, P=10, S=10, J=2, N=10, R=¡°fifo¡± and the optional argument ¡°--verbose¡± will then be passed to the main function.

Input:
See Input Number 6 on https://cs.nyu.edu/courses/spring18/CSCI-UA.0202-001/labs/lab4/

Output:

The machine size is 20
The page size is 10
The process size is 10
The job mix number is 2
The number of references per process is 10
The replacement algorithm is fifo

1 references word 1 (page 0) at time 1: Fault, using free frame 1
1 references word 2 (page 0) at time 2: Hit in frame 1
1 references word 3 (page 0) at time 3: Hit in frame 1
2 references word 2 (page 0) at time 4: Fault, using free frame 0
2 references word 3 (page 0) at time 5: Hit in frame 0
2 references word 4 (page 0) at time 6: Hit in frame 0
3 references word 3 (page 0) at time 7: Fault, evicting page 0 of Process 1 from frame 1
3 references word 4 (page 0) at time 8: Hit in frame 1
3 references word 5 (page 0) at time 9: Hit in frame 1
4 references word 4 (page 0) at time 10: Fault, evicting page 0 of Process 2 from frame 0
4 references word 5 (page 0) at time 11: Hit in frame 0
4 references word 6 (page 0) at time 12: Hit in frame 0
1 references word 4 (page 0) at time 13: Fault, evicting page 0 of Process 3 from frame 1
1 references word 5 (page 0) at time 14: Hit in frame 1
1 references word 6 (page 0) at time 15: Hit in frame 1
2 references word 5 (page 0) at time 16: Fault, evicting page 0 of Process 4 from frame 0
2 references word 6 (page 0) at time 17: Hit in frame 0
2 references word 7 (page 0) at time 18: Hit in frame 0
3 references word 6 (page 0) at time 19: Fault, evicting page 0 of Process 1 from frame 1
3 references word 7 (page 0) at time 20: Hit in frame 1
3 references word 8 (page 0) at time 21: Hit in frame 1
4 references word 7 (page 0) at time 22: Fault, evicting page 0 of Process 2 from frame 0
4 references word 8 (page 0) at time 23: Hit in frame 0
4 references word 9 (page 0) at time 24: Hit in frame 0
1 references word 7 (page 0) at time 25: Fault, evicting page 0 of Process 3 from frame 1
1 references word 8 (page 0) at time 26: Hit in frame 1
1 references word 9 (page 0) at time 27: Hit in frame 1
2 references word 8 (page 0) at time 28: Fault, evicting page 0 of Process 4 from frame 0
2 references word 9 (page 0) at time 29: Hit in frame 0
2 references word 0 (page 0) at time 30: Hit in frame 0
3 references word 9 (page 0) at time 31: Fault, evicting page 0 of Process 1 from frame 1
3 references word 0 (page 0) at time 32: Hit in frame 1
3 references word 1 (page 0) at time 33: Hit in frame 1
4 references word 0 (page 0) at time 34: Fault, evicting page 0 of Process 2 from frame 0
4 references word 1 (page 0) at time 35: Hit in frame 0
4 references word 2 (page 0) at time 36: Hit in frame 0
1 references word 0 (page 0) at time 37: Fault, evicting page 0 of Process 3 from frame 1
2 references word 1 (page 0) at time 38: Fault, evicting page 0 of Process 4 from frame 0
3 references word 2 (page 0) at time 39: Fault, evicting page 0 of Process 1 from frame 1
4 references word 3 (page 0) at time 40: Fault, evicting page 0 of Process 2 from frame 0
Process 1 had 4 faults and 5.000000 average residency
Process 2 had 4 faults and 5.000000 average residency
Process 3 had 4 faults and 6.000000 average residency
Process 4 had 4 faults and 5.333333 average residency

The total number of faults is 16 and the overall average residency is 5.285714

