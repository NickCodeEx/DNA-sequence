import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 
 */

/**
 * @author Nic
 *
 */
class GeneConverterTest {

	@Test
	void noNucleotides() {
		testDnaString("", 0L);
		testLong("", 0L,0);
	}
	
	@Test
	void minAllNucleotides() {
		testDnaString("a", 0L);
		testDnaString("c", 0B01L);
		testDnaString("g", 0B10L);
		testDnaString("t", 0B11L);
		
		testLong("A", 0B00L, 1);
		testLong("C", 0B01L, 1);
		testLong("G", 0B10L, 1);
		testLong("T", 0B11L, 1);
	}
	@Test
	void doubles(){
		testDnaString("aa", 0L);
		testDnaString("cc", 0B0101L);
		testDnaString("gg", 0B1010L);
		testDnaString("tt", 0B1111L);
	
		testLong("AAAAAAA",0L, 7);
		testLong("CCCCCC", 0B010101010101L, 6);
		testLong("CCCCCCCCCCCCCCCCCC", 0B010101010101010101010101010101010101L, 18);
		testLong("GGGGGGGGGGGGGGGGGG", 0B101010101010101010101010101010101010L, 18);
		testLong("TTTTTTTTTTTTTTTTTT", 0B111111111111111111111111111111111111L, 18);

	}
	
	@Test
	void maxSingleNucleotides() {
		testDnaString("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 0L);
		testDnaString("cccccccccccccccccccccccccccccccc", 0B0101010101010101010101010101010101010101010101010101010101010101L);
		testDnaString("gggggggggggggggggggggggggggggggg", 0B1010101010101010101010101010101010101010101010101010101010101010L);
		testDnaString("tttttttttttttttttttttttttttttttt", 0B1111111111111111111111111111111111111111111111111111111111111111L);
	
		testLong("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",0L, 32);
		testLong("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", 0B0101010101010101010101010101010101010101010101010101010101010101L, 32);
//		testLong("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG", 0B1010101010101010101010101010101010101010101010101010101010101010L, 32);
//		testLong("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", 0B1111111111111111111111111111111111111111111111111111111111111111L, 32);

	}
	
	@Test
	void tooManySingleNucleotides() {
		testDnaString("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 0L);
		testDnaString("ccccccccccccccccccccccccccccccccc", 0B0101010101010101010101010101010101010101010101010101010101010101L);
		testDnaString("ggggggggggggggggggggggggggggggggg", 0B1010101010101010101010101010101010101010101010101010101010101010L);
		testDnaString("ttttttttttttttttttttttttttttttttt", 0B1111111111111111111111111111111111111111111111111111111111111111L);
	}
	
	@Test
	void allNucleotides() {
		testDnaString("acgt", 0B00011011L);
		testLong("ACGT",  0B00011011L,4);
	}
	
	@Test
	void maxAllNucleotides() {
		testDnaString("acgtacgtacgtacgtacgtacgtacgtacgt", 0B0001101100011011000110110001101100011011000110110001101100011011L);
		testLong("ACGTACGTACGTACGTACGTACGTACGTACGT",0B0001101100011011000110110001101100011011000110110001101100011011L, 32);
		testLong("ACTTTTTTTTTTTTTTTTTTTTTTTTTTTTTG",0B0001111111111111111111111111111111111111111111111111111111111110L,32);
	
	}
	
	
	private void testDnaString(String dnaString, long expected)
	{		
		long actual = GeneConverter.convertGene(dnaString);
		assertEquals(expected, actual);
			
	}
	
	private void testLong(String dnaString, long key, int sequenceLength) {
		String actual = GeneConverter.convertLong(key, sequenceLength);
		assertEquals(dnaString, actual);
	}
	
	
	

}
