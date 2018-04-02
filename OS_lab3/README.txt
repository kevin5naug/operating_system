Lab 3 banker Yixing Guan

Source file for Lab3:
1. OS_lab3.java

This source file gets the path to the test file through command line argument.

Guide:
1. Upload OS_lab3.java and the sample inputs to the test machine.

2. Get to the directory where OS_lab3.java is in (cd).

3. Type ¡°javac OS_lab3.java¡± in command line in order to compile the program.

4. Type ¡°java OS_lab3 /path/to/test_case.txt¡± to activate the program and generate the required output. 

(5. Optional: Type ¡°java OS_lab3 ¡ª-verbose /path/to/test_case.txt¡± to activate the program and generated a more detailed output. This is used for debugging purpose.)






Example(the commands I used to test my program on NYU machine):


1. On the terminal of my computer, I typed and entered:

scp -r /Users/joker/Documents/Coding/operating_system/OS_lab3 yg1227@access.cims.nyu.edu:/home/yg1227/OS_lab3

This allows me to upload OS_lab3.java, and the sample inputs, which are all contained in one directory /Users/joker/Documents/Coding/operating_system/OS_lab3, to the test machine in one command.



2. After using ssh to get connected to the NYU machine yg1227@courses2, I typed and entered the following commands:

1) cd ~
2) cd OS_lab3/



3. type and enter: ls

Output form the terminal:
input-01.txt  input-04.txt  input-07.txt  input-10.txt  input-13.txt
input-02.txt  input-05.txt  input-08.txt  input-11.txt  OS_lab3.java
input-03.txt  input-06.txt  input-09.txt  input-12.txt


4. Since I stored all of my test cases in the current directory /home/yg1227/OS_lab3, the absolute path to one of the test cases can be: /home/yg1227/OS_lab3/input-05.txt




5. type and enter: javac OS_lab3.java  
This will compile the program.



6. type and enter: java OS_lab3 /home/yg1227/OS_lab3/input-05.txt

The command line argument ¡°/home/yg1227/OS_lab3/input-05.txt¡± will then be passed to the main function.


Input(input-05.txt):
3 1 4
initiate  1 0 1 3
request   1 0 1 1
release   1 0 1 1
request   1 0 1 3
release   1 0 1 3
terminate 1 0 0 0
initiate  2 0 1 3
request   2 0 1 1
request   2 0 1 1
release   2 0 1 2
terminate 2 0 0 0
initiate  3 0 1 3
request   3 0 1 2
request   3 0 1 1
release   3 0 1 3
terminate 3 0 0 0

Output:
extracted filename: /home/yg1227/OS_lab3/input-05.txt



Report for FIFO
Task 1      9  4  44.444444%
Task 2      5  1  20.000000%
Task 3      7  3  42.857143%
total       21  8  38.095238%




Report for Banker
Task 1      9  4  44.444444%
Task 2      4  0  0.000000%
Task 3      7  3  42.857143%
total       20  7  35.000000%



(7. Optional: type and enter: java OS_lab3 --verbose /home/yg1227/OS_lab3/input-05.txt

The command line arguments ¡°¡ª-verbose¡± and ¡°/home/yg1227/OS_lab3/input-05.txt¡± will then be passed to the main function.)

Input:
Same as above.

Output:

extracted filename: /home/yg1227/OS_lab3/input-05.txt



During cycle 0 - 1 (already terminate 0 tasks)
 Task 1 completes one initiate activity
 Task 2 completes one initiate activity
 Task 3 completes one initiate activity


During cycle 1 - 2 (already terminate 0 tasks)
 Task 1 completes its request of 1, remaining 3 units
 Task 2 completes its request of 1, remaining 2 units
 Task 3 completes its request of 2, remaining 0 units


During cycle 2 - 3 (already terminate 0 tasks)
 Task 1 releases 1 units of type 1 
 Task 2's request cannot be granted
 Task 3's request cannot be granted


During cycle 3 - 4 (already terminate 0 tasks)
 First check blocked tasks:
         Task 2 completes its request of 1 units
         Task 3's request of 1 units cannot be granted
 Task 1's request cannot be granted


During cycle 4 - 5 (already terminate 0 tasks)
 First check blocked tasks:
         Task 3's request of 1 units cannot be granted
         Task 1's request of 3 units cannot be granted
 Task 2 releases 2 units of type 1 


During cycle 5 - 6 (already terminate 0 tasks)
 First check blocked tasks:
         Task 3 completes its request of 1 units
         Task 1's request of 3 units cannot be granted
 Task 2 terminated at the beginning of this cycle(this cycle is not included in the total time)


During cycle 6 - 7 (already terminate 1 tasks)
 First check blocked tasks:
         Task 1's request of 3 units cannot be granted
 Task 3 releases 3 units of type 1 


During cycle 7 - 8 (already terminate 1 tasks)
 First check blocked tasks:
         Task 1 completes its request of 3 units
 Task 3 terminated at the beginning of this cycle(this cycle is not included in the total time)


During cycle 8 - 9 (already terminate 2 tasks)
 Task 1 releases 3 units of type 1 


During cycle 9 - 10 (already terminate 2 tasks)
 Task 1 terminated at the beginning of this cycle(this cycle is not included in the total time)


Report for FIFO
Task 1      9  4  44.444444%
Task 2      5  1  20.000000%
Task 3      7  3  42.857143%
total       21  8  38.095238%




During cycle 0 - 1 (already terminate 0 tasks)
 Task 1 completes one initiate activity
 Task 2 completes one initiate activity
 Task 3 completes one initiate activity


During cycle 1 - 2 (already terminate 0 tasks)
 Task 1 completes its request of 1, remaining 3 units
 Task 2 completes its request of 1, remaining 2 units
 Task 3's request cannot be granted


During cycle 2 - 3 (already terminate 0 tasks)
 First check blocked tasks:
         Task 3's request cannot be granted
 Task 1 releases 1 units of type 1 
 Task 2 completes its request of 1, remaining 1 units


During cycle 3 - 4 (already terminate 0 tasks)
 First check blocked tasks:
         Task 3's request cannot be granted
 Task 1's request cannot be granted
 Task 2 releases 2 units of type 1 
 Task 2 terminated


During cycle 4 - 5 (already terminate 1 tasks)
 First check blocked tasks:
         Task 3 completes its request of 2 units
         Task 1's request cannot be granted


During cycle 5 - 6 (already terminate 1 tasks)
 First check blocked tasks:
         Task 1's request cannot be granted
 Task 3 completes its request of 1, remaining 1 units


During cycle 6 - 7 (already terminate 1 tasks)
 First check blocked tasks:
         Task 1's request cannot be granted
 Task 3 releases 3 units of type 1 
 Task 3 terminated


During cycle 7 - 8 (already terminate 2 tasks)
 First check blocked tasks:
         Task 1 completes its request of 3 units


During cycle 8 - 9 (already terminate 2 tasks)
 Task 1 releases 3 units of type 1 
 Task 1 terminated


Report for Banker
Task 1      9  4  44.444444%
Task 2      4  0  0.000000%
Task 3      7  3  42.857143%
total       20  7  35.000000%
