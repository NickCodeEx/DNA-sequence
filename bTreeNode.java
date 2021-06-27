/**
 * 12/13/2019
 * @author Nicolas Dupuis
 * bTreeNode class
 * This class will create a Node containing :  the position of the node,
 *  if the node is a leaf or not, the size of the node, BtreeObjects,
 *  pointer to the children of the node
 */
public class bTreeNode<T> {

	private bTreeObject<T>[] bTreeNodeArray; // array containing the keys
	private boolean isLeaf;//if ht node has children or not (leaf)
	private long[] childrenArray;
	private int numKey; //current size of the node
	private long position;// node position
	private int degree;// Btree degree


	/**
	 * constructor
	 * @param newPosition
	 * @param degree
	 */
	public bTreeNode(long newPosition, int degree) {
		position = newPosition;//node position
		this.degree = degree; //Btree degree
		numKey=0;// node size

		bTreeNodeArray = new bTreeObject[(2*degree)-1];// array containing the keys
		for (int i = 0; i < bTreeNodeArray.length; i++) {
			bTreeNodeArray[i] = new bTreeObject(-1L); 

		}

		childrenArray = new long[2*degree];  // array containing the child position
		//set to -1 so the first run won't blow up
		for (int i = 0; i < childrenArray.length; i++) {
			childrenArray[i] = -1L; 
		}

	}

	/**
	 * getter to degree
	 * @return degree
	 */
	public int degree() {
		return degree;
	}

	/**
	 * getter for the bTreeNodeArray
	 * @return bTreeNodeArray
	 */
	public bTreeObject<T>[] getbTreeNodeArray() {
		return bTreeNodeArray;
	}

	/**
	 * setter for the bTreeNodeArray according to the parameter entered
	 * @param bTreeNodeArray
	 */
	public void setbTreeNodeArray(bTreeObject<T>[] bTreeNodeArray) {
		this.bTreeNodeArray = bTreeNodeArray;
	}

	/**
	 * getter to isLeaf of the node
	 * @return isLeaf
	 */
	public boolean isLeaf() {
		return isLeaf;
	}

	/**
	 * setter to isLeaf of the node
	 * @param isLeaf
	 */
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	/**
	 * getter to  childrenArray of the node
	 * @return  childrenArray
	 */
	public long[] getChildrenArray() {
		return childrenArray;
	}

	/**
	 * setter to  childrenArray of the node
	 * @param children
	 */
	public void setChildrenArray(long[] children) {
		this.childrenArray = children;
	}

	/**
	 * getter to  numKey of the node
	 * @return numKey
	 */
	public int getNumKey() {
		return numKey;
	}

	/**
	 * setter to  numKey of the node
	 * @param numKey
	 */
	public void setNumKey(int numKey) {
		this.numKey = numKey;
	}

	/**
	 * getter to position of the node
	 * @return
	 */
	public long getPosition() {
		return position;
	}

	/**
	 * setter to position of the node
	 * @param position
	 */
	public void setPosition(long position) {
		this.position = position;
	}

}
