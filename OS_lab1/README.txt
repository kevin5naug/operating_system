Lab 1¡ªLinker Yixing Guan

Source file for Lab1:
1. OS_lab1.java

This source file gets the path to the test file through command line argument.

Guide:
1. Upload OS_lab1.java to the test machine.

2. Get to the directory where OS_lab1.java is in (cd).

3. Type ¡°javac OS_lab1.java¡± in command line in order to compile the program. It might display the following, though it doesn¡¯t matter:

Note: OS_lab1.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.

4. Type ¡±java OS_lab1 /path/to/test_case.txt¡± to activate the program.





Example(the commands I used to test my program on NYU machine):
1. On the terminal of my computer, I typed and entered:
scp /Users/joker/Desktop/OS_lab1.java yg1227@access.cims.nyu.edu:~/OS_lab1/

2. After using ssh to get connected to the NYU machine yg1227@courses2, I typed and entered the following commands:
1) cd ~
2) cd OS_lab1/

3. type and enter: javac OS_lab1.java  
This will compile the program.

4. I stored all of my test cases in the directory /home/yg1227/OS_lab1/test_case/. Therefore, the absolute path to one of the test cases can be: /home/yg1227/OS_lab1/test_case/test7.txt

5. type and enter: java OS_lab1 /home/yg1227/OS_lab1/test_case/test7.txt

The command line argument ¡±/home/yg1227/OS_lab1/test_case/test7.txt¡± will then be passed to the main function.


Input(test7.txt):
3

0
1  X21
3  E 1000  E 1004  E 1222

1  X21 0
0
1  A 2956

1  X31 0
0
1  R 3456


Output:

yg1227@courses2[OS_lab1]$ java OS_lab1 /home/yg1227/OS_lab1/test_case/test7.txt
extracted filename: /home/yg1227/OS_lab1/test_case/test7.txt
Symbol Table
X21 = 3  
X31 = 4  
Memory Map
0:    1003  
1:    1004  Error: External address too large to reference. Treated as immediate address. 
2:    1222  Error: External address too large to reference. Treated as immediate address. 
3:    2000  Error: Absolute address exceeds machine size; zero used. 
4:    3000  Error: Relative address exceeds module size; zero used. 
Warning: X31 was defined in module 2 but never used. 
