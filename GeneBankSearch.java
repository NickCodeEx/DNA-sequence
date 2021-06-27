import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;


/**
 * 12/13/2019
 * @author Nicolas Dupuis
 * GeneBankSearch class
 * This class will look for the DNA sequences of the desired lengh in the designated binary data file created previously with the GeneBankCreateBTree, 
 * it will return the frequency for each DNA sequences of the desired lengh entered. If the DNA sequences of the desired lengh is not found it will return a 0.
 */
public class GeneBankSearch {

	private static int cache;
	private static String bFileName;
	private static String queryFileName;
	private static int cacheSize;
	private static int debugLevel;
	private static RandomAccessFile fileAccessWorker;
	
	//0  src/test2.txt.btree.data.5.8 src/query1.txt 0 0
	public static void main(String[] args) throws IOException {

		parseInput(args);
		
		File bFile = new File(bFileName); 
		
		String lastThree = bFileName.substring(bFileName.length()-3);
		
		// Split bFile into segments
		//String segments[] = bFileName.split(".");
		
		//grab degree
		String degreeString = lastThree.substring(2, 3);
		// Grab the sequence length
		String sequenceLengthString = lastThree.substring(0, 1);
		
		int degree = Integer.parseInt(degreeString);
		int sequenceLength = Integer.parseInt(sequenceLengthString);
		
		String fileContents = GeneConverter.scanFileNametoString(queryFileName);
		
		fileAccessWorker = new RandomAccessFile(bFile, "rw");

		
		
		Btree bTree = new Btree(bFile, sequenceLength, degree);
		
		Scanner scanner = new Scanner(new File(queryFileName));
		int lineLength = scanner.nextLine().length();
		
		List<String> aminoAcids = GeneConverter.parseGenes(fileContents, lineLength);
		
		
		for(String s : aminoAcids) {
			long key = GeneConverter.convertGene(s);
		
			bTreeNode foundNode = bTree.bTreeSearch(bTree.getRoot(), key);
		
			for(int i = 0; i < foundNode.getbTreeNodeArray().length; i++) {
				if(foundNode.getbTreeNodeArray()[i].getKey() == key) {
					
					System.out.println(foundNode.getbTreeNodeArray()[i].getFrequency() + ": " + GeneConverter.convertLong(key, lineLength));

				}
			}
		}
		
	}
	
	public static void parseInput(String[] args) {
		try {
			//determine if incorrect number of parameters are provided
			if (args.length > 5 || args.length < 4) {
				throw new IllegalArgumentException("Incorrect number of command line arguements");
			}
			//determine if we are going to cache
			if(Integer.parseInt(args[0]) != 0) {
				throw new UnsupportedOperationException();
			}
			cache = Integer.parseInt(args[0]);
			//get the degree
			bFileName = args[1];
			//get the file name
			queryFileName = args[2];

			//determine the data in the file
			//sequenceLength = Integer.parseInt(args[3]);
			
			if(cache == 1) {
				if(args[3] == null) {
					throw new IllegalArgumentException("Cache size needed");
				}
				cacheSize = Integer.parseInt(args[3]);
			}
			
			if(args.length == 5) {
				// check if debug data is needed
				debugLevel = Integer.parseInt(args[4]);
			}
			else {
				debugLevel = 0;
			}
			
			
		}
		catch (IndexOutOfBoundsException e){
			System.out.println("Incorrect parameters");
		}
	}
	
}
