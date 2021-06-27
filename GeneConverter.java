import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 12/13/2019
 * @author Nicolas Dupuis
 * GeneConverter class
 * This class will convert DNA sequences of the desired lengh from a String into a numerical value (long) , 
 * making the insertion in the Btree easier and saving memory space.
 */

public class GeneConverter {


	/**
	 * method to convert the DNA string to a long
	 * @param dnaString
	 * @return
	 */
	public static long convertGene(String dnaString) {

		long dnaBits = 0L;

		for (int i = 0; i < dnaString.length(); i++) {
			char c = dnaString.charAt(i);

			switch (c) {
			case 'a':
				dnaBits = (dnaBits << 2);
				break;
			case 'c':
				dnaBits = ((dnaBits << 2) | 0b01L);
				break;
			case 'g':
				dnaBits = ((dnaBits << 2) | 0b10L);
				break;
			case 't':
				dnaBits = ((dnaBits << 2) | 0b11L);
				break;
			}

		}
		return dnaBits;
	}

	/**
	 * method to convert Char to Binary value
	 * @param snip
	 * @return
	 */
	public static String convertToNucleotide(String snip) {

		String nucleotide = "";
		switch(snip) {
		case "00":
			nucleotide = "A";
			break;
		case "01":
			nucleotide = "C";
			break;
		case "10":
			nucleotide = "G";
			break;
		case "11":
			nucleotide = "T";
			break;

		}
		return nucleotide;
	}

	/**
	 * method to create DNA sequence of the sequenceLength entered
	 * @param key
	 * @param sequenceLength
	 * @return
	 */
	public static String convertLong(long key, int sequenceLength) {

		String dna="";

		StringBuilder keyString = new StringBuilder();

		String intermediaryString = Long.toString(key,2);

		int numberMissing =  (sequenceLength *2)- intermediaryString.length();
		if(numberMissing == -1) {
			return dna;
		}

		for(int i = 0; i < numberMissing; i++) {
			keyString.append("0");
		}

		keyString.append(intermediaryString);

		for(int i = 0; i < keyString.length(); i= i + 2) {
			String nucleotide = keyString.substring(i, i+2);

			dna += convertToNucleotide(nucleotide);
		}
		return dna;
	}


	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String scanFileNametoString(String file) throws IOException {
		return new String(Files.readAllBytes(Paths.get(file)));
	}

	/**
	 * method to read through the file and generate the DNA
	 * @param genes
	 * @param sequenceLength
	 * @return
	 */
	public static List<String> parseGenes(String genes, int sequenceLength) {
		List<String> retval = new ArrayList<String>();

		genes = genes.toLowerCase();

		int size = genes.length();
		String dna = "";
		boolean skipToNextSequence = false;

		for (int i = 0; i < size; i++) {

			if (i < (size - 6) && genes.substring(i, i + 6).equals("origin")) {
				dna = "";
				i += 6;
			}

			if (i < (size - 2) && genes.substring(i, i + 2).equals("//")) {
				dna = "";
				skipToNextSequence = true;
			}

			char c = genes.charAt(i);

			if (c >= '0' && c <= '9') {
				continue;
			}
			if (c == ' ' || c == '\r' || c == '\n') {

				continue;
			}

			switch (c) {
			case 'a':
			case 'c':
			case 'g':
			case 't':
				dna = dna + c;
				break;
				// stop signal
			case 'n':
				if (dna.length() == sequenceLength) {
					retval.add(dna);
				}
				dna = "";
				skipToNextSequence = true;
				continue;
			}
			if (dna.length() == sequenceLength) {
				retval.add(dna);
				dna = "";
			}
			if (skipToNextSequence) {
				skipToNextSequence = false;
				while (i < (size - 6) && !genes.substring(i, i + 6).equals("origin")) {
					i++;
				}
				i = i + 5;
			}
		}
		return retval;
	}
}
