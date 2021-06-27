import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class bTreeObjectTester<T> {

	@Test
	public void testgetKey() {
		bTreeObject<T> bObject = new bTreeObject<T>(-1);
		assertEquals(-1, bObject.getKey());
	}
	
	@Test
	public void testsetKey() {
		bTreeObject<T> bObject = new bTreeObject<T>(-1);
		bObject.setKey(-2);
		assertEquals(-2, bObject.getKey());
	}
	
	@Test
	public void testgetFrequency() {
		bTreeObject<T> bObject = new bTreeObject<T>(-1);
		assertEquals(1, bObject.getFrequency());
	}
	
	@Test
	public void testsetFrequency() {
		bTreeObject<T> bObject = new bTreeObject<T>(-1);
		bObject.setFrequency(2);
		assertEquals(2, bObject.getFrequency());
	}

}
