****************
* Project DNA sequences
* 12/13/2019
* Nicolas Dupuis
**************** 

OVERVIEW:

 This program will consume a file containing strings of DNA, partition the DNA into sequences of
 a desired length, and report the sequences along with the frequency with which it appears in the
 file. There is another class that allows a user to submit a query file to see how often the query
 DNA appears in the original file. 

INCLUDED FILES:

 List the files required for the project with a brief
 explanation of why each is included.


 * bTreeObject.java - source file :
 This class will create an object with the following attributes : Key which is in this project a conversion of the DNA sequences into a long and the frequency which start at 0 and increase each time we want to put the same key in the Btree

 * bTreeNode.java - source file :
This class will create a Node containing :  the position of the node, if the node is a leaf or not, the size of the node, BtreeObjects, pointer to the children of the node.  

 * bTree.java - source file :
This class will create a Btree, search, insert and delete BtreeNode. After each insertion or deletion , it will update the binary data file from where we will be able to look for data 

 * FileHandler.java - source file
This class will allow to read and write the information from the Btree class into a binary data file, avoiding to store the Btree in the memory making the operation faster and saving memory space

 * GeneBankCreateBTree.java - source file
This class is an executable according to the command line entered , it will read the designated gene bank file from where it will capture DNA strings, then it will partition it in DNA sequences of the desired lengh and create a BtreeObject for each sequence. The BtreeObject will then be inserted in the Btree, the Btree order is entered in the command line or if no value is entered it is calculating the optimal Btree Order. You can also require  to use a Cache and a different level of debug .

 * GeneConverter.java - source file
This class will convert DNA sequences of the desired lengh from a String into a numerical value (long) , making the insertion in the Btree easier and saving memory space.

* GeneBankSearch.java - source file
This class is an executable according to the command line entered , it will look for the DNA sequences of the desired lengh in the designated binary data file created previously with the GeneBankCreateBTree, it will return the frequency for each DNA sequences of the desired lengh entered. If the DNA sequences of the desired lengh is not found it will return a 0.

 * README - this file


COMPILING AND RUNNING:
 Run the following commands in the main project directory to compile:

$ javac *.java 

 Console output will give the results after the program finishes.

Run the compiled classes with the following command:

$ java GeneBankCreateBTree [0 | 1(no/with Cache)] [degree] [gbk file] [sequence length] [ | cache size] [ | debug level]

For example: $ java GeneBankCreateBTree 0 8 test1.txt 5


[0 | 1(no/with Cache)]
To improve performance, the program can use an  cache .

[degree]
The degree is the minimum degree, t, to be used for the B-Tree. If the user specifies 0, then the program  choose the optimum degree based on a disk block size of 4096 bytes and the size of the B-Tree node.

[gbk file] 
Designated gene bank file from the National Center for Biotechnology Information

[sequence length]
The sequence length is an integer that must be between 1 and 31, inclusive

 
[ | cache size] [ | debug level]
Size of the cache and level of debug


The debug level is an optional argument with a default value of zero. 
0
Any diagnostic messages, help and status messages printed on standard error stream.

1
The program writes a text file named dump, that has the following line format: [frequency] [DNA string]. The dump file contains frequency and DNA string in an in-order traversal.


$ java GeneBankSearch [0 | 1(no/with Cache)] [btree file] [query file] [ | cache size] [ | debug level]
For example: 0  src/test1.txt.btree.data.5.8 src/query1.txt 0 0

[0 | 1(no/with Cache)]
To improve performance, the program can use an  cache .

[btree file]
binary data file containing the Btree to look into.

[query file]
File containing the query (DNA sequence) to look for in the btree file

[ | cache size] [ | debug level]
Size of the cache and level of debug


The debug level is an optional argument with a default value of zero. 
0
Any diagnostic messages, help and status messages printed on standard error stream.

1
The program writes a text file named dump, that has the following line format: [frequency] [DNA string]. The dump file contains frequency and DNA string in an in-order traversal.



PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 This program will scan a submitted file and extract DNA data from it. This data is then converted to
 a long and then stored in a node along with an int that serves to track the frequency with which
 the DNA sequence appears in the file. The sequence length is configurable and must be set by the user
 as a command line argument. The program will store all the DNA sequence data in a file and then provide
 a report detailing which sequences appeared in the file along with their frequency. The program can also
 search for a DNA strand of interest by consuming a query file, and searching against the storage file.
 The functionality will report how many times the stand of interest appeared in the original file.
 
 I made a bTreeObject class to manage the relationship between DNA sequences and their frequencies.
 The bTreeNode class was made to store an array of bTreeObjects, while the BTree class managed the
 nodes relationship with each other and would call the FileHandler class to write and read from the
 file. The GeneBankCreateBTree class is the driver class and will parse input from the user, send data 
 to the GeneConverter class to generate longs and then send this data to the Btree.  
 