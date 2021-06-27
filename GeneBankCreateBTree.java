import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//import org.graalvm.compiler.core.common.FieldsScanner.CalcOffset;
/**
 * 12/13/2019
 * @author Nicolas Dupuis
 * GeneBankSearch class
 * This class will look for the DNA sequences of the desired lengh in the designated binary data file created previously with the GeneBankCreateBTree, 
 * it will return the frequency for each DNA sequences of the desired lengh entered. If the DNA sequences of the desired lengh is not found it will return a 0.
 */
public class GeneBankCreateBTree {

	private static int cache;
	private static int degree;
	private static String filename;
	private static int sequenceLength;
	private static int cacheSize;
	private static int debugLevel;
	private static Btree BTree;

	public static <V> void main(String args[]) throws IOException {



		parseInput(args);
		//File file = new File(filename);
		boolean useCache =false;
		if(cache==1) {
			useCache = true;
		}

		BTree = new Btree(filename, sequenceLength, degree, useCache);
		//scan the file for data and store it
		//scan(file);
		String fileContents = GeneConverter.scanFileNametoString(filename);

		List<String> aminoAcids = GeneConverter.parseGenes(fileContents, sequenceLength);
		int i =1;
		for(String s : aminoAcids) {
			long key = GeneConverter.convertGene(s);
			bTreeObject<Long> newObject = new bTreeObject<Long> (key);
			//System.out.println(i++);
			BTree.bTreeInsert(newObject);

			//BTree.print(BTree.getRoot(), debugLevel);

		}
		System.out.println("Frequency: DNA Strand");
		BTree.print(BTree.getRoot(), debugLevel);

		if(debugLevel != 0) {
			File dump = new File("dump");
			PrintWriter author;
			try {
				author = new PrintWriter(dump);
				//Btree.toPrint(BTree.getRoot());//future method
				author.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*since a "sequence" could potentially span more than one line (i.e. last char of 
	the first line and the first two chars of the second line) we add all nucleotides
	to a dna string and partition out the sequences from there*/
	private static void scan(File file) throws IOException {
		//String of dna
		String dna = "";
		//determine if a string contains data we need		
		boolean analyzeString = false;
		try {
			try (Scanner fileScan = new Scanner(file)) {
				while (fileScan.hasNextLine()) {
					// partition the file by lines
					String line = fileScan.nextLine();

					try (Scanner lineScan = new Scanner(line)) {
						//partition each line by word
						while(lineScan.hasNext()) {
							//pull out data from each line
							String string = lineScan.next();
							string = string.toLowerCase();
							//stop signal
							if(string.startsWith("//")) {
								analyzeString = false;
							}

							while(analyzeString && lineScan.hasNext()) {
								int charPosition = 0;

								while(charPosition < string.length() ) {
									char nucleotide = string.charAt(charPosition++);

									switch(nucleotide) {
									case 'a':
									case 'c':
									case 'g':
									case 't':
										dna = dna + nucleotide;
										break;
										//stop signal
									case 'n':
										analyzeString = false;
										break;
										//what should be done to a sequence if we hit a
										//stop signal, but the dna strand is not yet at the sequence length?
									default:
										continue;
									}									

									if(dna.length() == sequenceLength) {
										long dnaBits = GeneConverter.convertGene(dna);

										bTreeObject<Long> o = new bTreeObject<Long>(dnaBits);
										BTree.bTreeInsert(o);
										dna = null;	
									}									
								}
							}
							//start signal
							if(string.startsWith("origin")) {
								analyzeString = true;
							}
						}						
					}					
				}
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Unable to find file " + filename);
		}
	}

	/**
	 * method to take the input from the command line
	 * @param args
	 */
	private static void parseInput(String[] args) {
		try {
			//determine if incorrect number of parameters are provided
			if (args.length > 6 || args.length < 4) {
				throw new IllegalArgumentException("Incorrect number of command line arguements");
			}
			//determine if we are going to cache
			if(Integer.parseInt(args[0]) != 0) {
				throw new UnsupportedOperationException();
			}
			cache = Integer.parseInt(args[0]);
			//get the degree
			degree = Integer.parseInt(args[1]);
			//get the file name
			filename = args[2];
			//determine the data in the file
			sequenceLength = Integer.parseInt(args[3]);

			if(cache == 1) {
				if(args[4] == null) {
					throw new IllegalArgumentException("Cache size needed");
				}
				cacheSize = Integer.parseInt(args[4]);
			}

			if(args.length == 5) {
				// check if debug data is needed
				debugLevel = Integer.parseInt(args[5]);
			}			

			if(degree == 0) {
				calculateDegree();
			}

		}
		catch (IndexOutOfBoundsException e){
			System.out.println("Incorrect parameters");
		}
	}
	/**
	 * calculate the degree of the tree if the value 0 is entered
	 */
	public static void calculateDegree() {

		double disk = 4096;
		//just 8 since each pointer will be a long
		double pointerSize = 8;
		//each node contains an array (12) and a child node array (8 *)
		double metaDataSize = 40;
		//the key size will always be 8
		double keySize = 8; 

		disk -= (pointerSize + metaDataSize);
		disk += keySize;
		disk /= (keySize + pointerSize);
		degree = (int) Math.floor(disk);
	}

}
