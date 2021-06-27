

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

class GeneBankCreateBTreeTest {

	@Test
	void simpleTest() {
		String[] genes = {"aaa","aaa","tat"};
		
		compareGenes(genes, "1 AAA\r\n 4 AAA//\r\n ORIGIN \r\n 7 TAT \r\n Tn", 3);
		
	}
	
	@Test
	void fullSequenceThenN() {
		String[] genes = {"aaaaaaaaaa","tattaaaaaa"};
		
		compareGenes(genes, "1 AAA\r\n 4 AAA\r\n 7 AAAA// ORIGIN \r\n 7 TAT \r\n TAAAAAAn", 10);
	}
	
	@Test
	void fullSwitchExerciseThenN() {
		String[] genes = {"acgaaatatt","tcgtcgatgg"};
		
		compareGenes(genes, "1 ACG\r\n 4 AAA\r\n \r\n 7 TAT \r\n Tn ORIGIN\r\n TCGTCGATGG/", 10);
	}	
	
	
	private void compareGenes(String[] genes, String contents, int sequenceLength) {
		
		List<String> expectedGenes = Arrays.asList(genes); 
		List<String> actual = GeneBankCreateBTree.parseGenes(contents, sequenceLength);
		
		assertEquals(expectedGenes, actual);
	}
	
	
	
	

}
