import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 12/13/2019
 * @author Nicolas Dupuis
 * Btree class
 * This class will create a Btree, search, insert and delete BtreeNode. 
 * After each insertion or deletion ,it will update the binary data file from 
 * where we will be able to look for data
 */
public class Btree<T> {

	private int sequenceLength;
	private int degree;
	private boolean cache;
	private int maxSize;
	private FileHandler fileWorker;
	private File workingFile;

	private bTreeNode root;

	public Btree(File fileName, int newSequenceLength, int newDegree) throws IOException {
		sequenceLength = newSequenceLength;
		degree = newDegree;
		maxSize = (2*degree)-1;

		workingFile = fileName;
		fileWorker = new FileHandler(workingFile, degree, sequenceLength);
		root = fileWorker.diskRead(workingFile, 0, degree, sequenceLength);
	}

	public Btree(String gbk, int newSequenceLength, int newDegree, boolean newCache) throws IOException {
		sequenceLength = newSequenceLength;
		degree = newDegree;
		maxSize = (2*degree)-1;

		workingFile = new File(gbk + ".btree.data." + sequenceLength + "." + degree);

		fileWorker = new FileHandler(workingFile, degree, sequenceLength);

		//initialize an empty root
		root = new bTreeNode<T>(0, degree);
		root.setLeaf(true);
		fileWorker.write(workingFile,root);		
	}




	public bTreeNode getRoot() {
		return root;
	}

	/**
	 * method to look for a B tree object in a b tree
	 * @param startNode root node of a subtree
	 * @param bitValue target key to look for
	 * @return the key value if found or null if not found
	 * @throws IOException 
	 */
	public bTreeNode bTreeSearch(bTreeNode startNode, long bitValue) throws IOException 
	{

		//use i to iterate though node array
		int i = 0;
		bTreeNode childNode = null;

		long[] childNodesArray = startNode.getChildrenArray();
		bTreeObject[] startNodeArray = startNode.getbTreeNodeArray();


		while (i < startNode.getNumKey() && bitValue >= startNodeArray[i].getKey()) 
		{
			//check if value is found at index i in the node
			if (bitValue == startNodeArray[i].getKey()) 
			{
				return startNode;

			}
			i++;
		}

		if (startNode.isLeaf() || childNodesArray[i] == -1 ) 
		{
			return null;
		}
		//if not found and has children nodes we check the child node at the index i (as i is define according to bit value > key)
		else 
		{	

			childNode = fileWorker.diskRead(workingFile, childNodesArray[i], degree, sequenceLength);	

			return bTreeSearch(childNode, bitValue);

		}
	}

	/**
	 * Method to insert BtreeObject in the Btree
	 * @param newObject
	 * @throws IOException
	 */
	public void bTreeInsert(bTreeObject<Long> newObject) throws IOException {

		bTreeNode tempNode = bTreeSearch(root,newObject.getKey());

		if(tempNode !=null)//check if the object is not already in the Btree
		{
			bTreeObject[] tempNodeArray = tempNode.getbTreeNodeArray();

			for(int i=0; i<tempNodeArray.length; i++) {

				if(tempNodeArray[i].getKey() == newObject.getKey()) {
					tempNodeArray[i].setFrequency(tempNodeArray[i].getFrequency()+1);
					fileWorker.write(workingFile, tempNode);
					return;
				}
			}	
		}

		if(root.getNumKey() == maxSize) {

			long endPosition = fileWorker.getEndOfFilePosition(workingFile);

			bTreeNode newNode = new bTreeNode(endPosition, degree);
			fileWorker.write(workingFile, newNode);

			endPosition = fileWorker.getEndOfFilePosition(workingFile);
			root.setPosition(endPosition);

			bTreeNode tempOldRoot = root; 

			fileWorker.write(workingFile, root);
			root = newNode; 
			newNode.setLeaf(false);
			newNode.setNumKey(0);//restart the numkey of new root (equivalent to make an empty node)
			newNode.getChildrenArray()[0] = tempOldRoot.getPosition();

			bTreeSplitChild(newNode,0, tempOldRoot);//split the child node
			insertNonFull(newNode,newObject);

		}
		else {
			insertNonFull(root,newObject);
		}


	}

	/**
	 * Method to split a node if the node is at full capacity to store key
	 * @param parentNode
	 * @param index
	 * @param splitNode
	 * @throws IOException
	 */
	public void bTreeSplitChild(bTreeNode parentNode, int index, bTreeNode splitNode) throws IOException {
		bTreeNode newChildNode = new bTreeNode(fileWorker.getEndOfFilePosition(workingFile), degree);
		int partition = degree-1;

		newChildNode.setLeaf(splitNode.isLeaf());
		newChildNode.setNumKey(partition); 
		fileWorker.write(workingFile, newChildNode);

		for(int j=0; j<partition; j++) {
			long key = splitNode.getbTreeNodeArray()[j+degree].getKey();
			int frequency = splitNode.getbTreeNodeArray()[j+degree].getFrequency();

			newChildNode.getbTreeNodeArray()[j] = new bTreeObject(key);
			newChildNode.getbTreeNodeArray()[j].setFrequency(frequency);

			splitNode.getbTreeNodeArray()[j+degree] = new bTreeObject(-1L);
		}
		//check if the split node has children
		if(splitNode.isLeaf() == false) {
			for(int j=0; j<degree; j++) {

				newChildNode.getChildrenArray()[j] = splitNode.getChildrenArray()[j+degree];
				splitNode.getChildrenArray()[j+degree] = -1L;
			}
		}

		splitNode.setNumKey(partition);

		// shift the children of the parent node by one slot to insert the newNode
		// start from the top = numkey +1 as number of child =
		// numkey +1
		for(int j = parentNode.getNumKey(); j>index; j--) {

			parentNode.getChildrenArray()[j+1] = parentNode.getChildrenArray()[j];

			//empty out the array
			parentNode.getChildrenArray()[j] = -1L;
		}

		//insert the new node as the child
		parentNode.getChildrenArray()[index+1] = newChildNode.getPosition();

		// shift the keys of the parent node by one slot to make room for insert
		for(int j = parentNode.getNumKey()-1; j>index-1; j--) {
			long key = parentNode.getbTreeNodeArray()[j].getKey();
			int frequency =  parentNode.getbTreeNodeArray()[j].getFrequency();

			parentNode.getbTreeNodeArray()[j+1] = new bTreeObject(key);
			parentNode.getbTreeNodeArray()[j+1].setFrequency(frequency);
		}

		//gather promoted data
		long key = splitNode.getbTreeNodeArray()[partition].getKey();
		int frequency = splitNode.getbTreeNodeArray()[partition].getFrequency();

		//insert the promoted object into the parent
		parentNode.getbTreeNodeArray()[index] = new bTreeObject(key);
		parentNode.getbTreeNodeArray()[index].setFrequency(frequency);

		//space is now empty
		splitNode.getbTreeNodeArray()[degree-1] = new bTreeObject(-1L);

		//increase numkey of the parent node
		parentNode.setNumKey(parentNode.getNumKey() + 1);

		//write to the disk
		fileWorker.write(workingFile,newChildNode);		
		fileWorker.write(workingFile,splitNode);		
		fileWorker.write(workingFile,parentNode);
	}

	/**
	 * Method to insert the BtreeObject in a none full internal node
	 * @param x, a none full internal node
	 * @param o, pointer to object inserting into node
	 * @throws IOException 
	 */
	private void insertNonFull(bTreeNode internalNode, bTreeObject<Long> newObject) throws IOException {
		int i = internalNode.getNumKey()-1;

		if(internalNode.isLeaf()==true) // no child
		{		
			if(internalNode.getNumKey()!=0)//check if not is not empty, no need to look for the index
			{
				while(i >= 0 && newObject.getKey() < internalNode.getbTreeNodeArray()[i].getKey())
				{

					internalNode.getbTreeNodeArray()[i+1] = new bTreeObject(internalNode.getbTreeNodeArray()[i].getKey());//shift by one spot on the left the data
					internalNode.getbTreeNodeArray()[i+1].setFrequency(internalNode.getbTreeNodeArray()[i].getFrequency());
					i--;
				}
			}
			internalNode.getbTreeNodeArray()[i+1] = newObject;//insert object o in the node
			internalNode.setNumKey(internalNode.getNumKey()+1);//increase node numkey

			fileWorker.write(workingFile, internalNode);
		}
		else //node with children
		{
			//look for the right index
			while( i >= 0 && newObject.getKey() < internalNode.getbTreeNodeArray()[i].getKey()) {
				i--;
			}
			i++;//increase again i to reach the good index in the child node array
			bTreeNode childNode;
			if(internalNode.getChildrenArray()[i] != -1) {

				childNode = fileWorker.diskRead(workingFile,internalNode.getChildrenArray()[i], degree, sequenceLength);

				if( childNode.getNumKey() == maxSize ) {

					bTreeSplitChild(internalNode, i, childNode);

					if( newObject.getKey() > internalNode.getbTreeNodeArray()[i].getKey()){
						i++;
					}
				}
				//Recursive method , insert the object in the child node
				insertNonFull(childNode, newObject);

			}

		}

	}

	public void insertToChildOfNode(bTreeNode parentNode, bTreeObject<Long> newObject) {

		for(int i = 0; i < parentNode.getChildrenArray().length; i++) {

			if(parentNode.getChildrenArray()[i]!=1) {

			}
		}
	}
	/**
	 * method to insert object to the child node of the node entered in parameter
	 * @param parentNode
	 * @param newObject
	 */
	public void print(bTreeNode node, int debugLevel) throws IOException {

		StringBuilder string = new StringBuilder();

		for(int i = 0; i < maxSize; i++) {
			if(node.isLeaf() == false) {
				if(node.getChildrenArray()[i] != -1) {
					bTreeNode childNode = fileWorker.diskRead(workingFile, node.getChildrenArray()[i], degree, sequenceLength);
					print(childNode, debugLevel);
				}
			}

			bTreeObject printObject = node.getbTreeNodeArray()[i];

			if(printObject.getKey() !=-1) {

				System.out.println(printObject.getFrequency() + ": " + GeneConverter.convertLong(printObject.getKey(), sequenceLength));

				if(debugLevel == 1) {
					string.append(printObject.getFrequency() + ": " + GeneConverter.convertLong(printObject.getKey(), sequenceLength) + "\n");
				}
			}
		}

		FileWriter writer =new FileWriter("dump");
		writer.write(string.toString());
		writer.close();
	}

}
