/**
 * @author Nicolas Dupuis
 * bTreeObject class
 * This class will create an object with the following attributes : 
 * Key which is in this project a conversion of the DNA sequences into a long 
 * and the frequency which start at 1 and increase each time 
 * we want to put the same key in the Btree
 */

public class bTreeObject<T> {

	private long key; 
	private int frequency;

	//constructor
	public bTreeObject(long newKey) {
		// TODO Auto-generated constructor stub
		key = newKey;
		frequency = 1;
	}

	/**
	 * getter to the object key
	 * @return key
	 */
	public long getKey() {
		return key;
	}
	/**
	 * setter to the object key, set the key with the parameter entered
	 * @param key
	 */
	public void setKey(long key) {
		this.key = key;
	}

	/**
	 * getter to the object frequency
	 * @return frequency
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * setter to the object frequency, set the frequency with the parameter entered
	 * @param frequency
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}
