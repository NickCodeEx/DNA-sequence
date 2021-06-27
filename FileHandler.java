import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 12/13/2019
 * @author  Nicolas Dupuis
 * FileHandler class
 * This class will allow to read and write the information from the Btree class 
 * into a binary data file
 */
public class FileHandler {

	private RandomAccessFile fileAccessWorker;

	/**
	 * Constructor
	 * @param metadata
	 * @throws IOException
	 */
	public FileHandler(File metadata) throws IOException {
		fileAccessWorker = new RandomAccessFile(metadata, "rw");
		fileAccessWorker.close(); 
	}

	/**
	 * Constructor for the metada file that will contain metadata on the Btree
	 * @param metadata
	 * @param degree
	 * @param sequenceLength
	 * @throws IOException
	 */
	public FileHandler(File metadata, int degree, int sequenceLength) throws IOException {
		fileAccessWorker = new RandomAccessFile(metadata, "rw");
		fileAccessWorker.write(degree);
		fileAccessWorker.write(sequenceLength);

		fileAccessWorker.close(); 
	}

	/**
	 * method to write the dump file
	 * @param string
	 */
	public void writeDump(String string) {


	}

	/**
	 * method to return the degree from the file
	 * @param fileName
	 * @param position
	 * @return
	 * @throws IOException
	 */
	public int  getDegree(File fileName, long position) throws IOException {
		fileAccessWorker = new RandomAccessFile(fileName, "rw");
		int degree = fileAccessWorker.readInt();
		fileAccessWorker.close(); 

		return degree;
	}

	/**
	 * method to return the sequence lengh from the file
	 * @param fileName
	 * @param position
	 * @return
	 * @throws IOException
	 */
	public int getSequenceLength(File fileName, long position) throws IOException {
		fileAccessWorker = new RandomAccessFile(fileName, "rw");
		int degree = fileAccessWorker.readInt();
		int sequenceLength = fileAccessWorker.readInt();
		fileAccessWorker.close(); 

		return sequenceLength;
	}

	/**
	 * method to search a value in the file and return its frequency
	 * @param fileName
	 * @param valueTofind
	 * @return
	 * @throws IOException
	 */
	public int search(File fileName, long valueTofind) throws IOException {
		//FileHandler.fileAccessWorker = new 
		int degree = fileAccessWorker.readInt();
		int sequenceLength = fileAccessWorker.readInt();

		int frequency = 0;
		long size = 16 + 12 * (2 * degree - 1) + 8 * 2 * degree;// node size in bytes
		String str = "";
		long stopPosition = getEndOfFilePosition(fileName);// get the size of the file which is the same as the size
		// file
		fileAccessWorker = new RandomAccessFile(fileName, "rw"); // gets the file
		for (long pos = 0; pos < stopPosition; pos = pos + size)// increment until the end of the file
		{
			bTreeNode node = diskRead(fileName, pos, degree, sequenceLength);
			for (int i = 0; i < 2 * node.degree() - 1; i++) {
				long key = node.getbTreeNodeArray()[i].getKey();
				frequency = node.getbTreeNodeArray()[i].getFrequency();
				if (key == valueTofind) {
					return frequency;
				}
			}
		}
		fileAccessWorker.close();
		return frequency;
	}

	/**
	 * method to write data in the file
	 * @param fileName
	 * @param node
	 * @return the position where the write stop in the file
	 * @throws IOException
	 */
	public long write(File fileName, bTreeNode node) throws IOException{
		fileAccessWorker = new RandomAccessFile(fileName, "rw");
		fileAccessWorker.seek(node.getPosition());

		fileAccessWorker.writeLong(node.getPosition());

		// writing int isLeaf and convert it as an int (easier to manage space as int = 4bytes) to RandomAccessFile 
		if(node.isLeaf()==true) //4 Bytes
		{
			fileAccessWorker.writeInt(1);
		}
		else
		{
			fileAccessWorker.writeInt(0);
		}
		// writing mumKey into RandomAccessFile 
		fileAccessWorker.writeInt(node.getNumKey());

		bTreeObject[] tempArray = node.getbTreeNodeArray();

		for(int i =0;i<2*node.degree()-1;i++) // total 9 bytes * (2*degreeOfTree-1)
		{

			fileAccessWorker.writeLong(tempArray[i].getKey());//8 bytes each
			fileAccessWorker.writeInt(tempArray[i].getFrequency());
		}

		// writing Btree children to RandomAccessFile 
		long[] bTreeChildren =  node.getChildrenArray(); // long because position in the file
		for(int i =0;i<2*node.degree();i++) // total 8 bytes * (2*degreeOfTree)
		{
			fileAccessWorker.writeLong(bTreeChildren[i]);//8 bytes each
		}	

		long pointer = fileAccessWorker.getFilePointer();
		fileAccessWorker.close(); 
		return pointer; 
	}

	/**
	 * method to read data in the file
	 * @param fileName
	 * @param position
	 * @param degree
	 * @param sequenceLength
	 * @return a btreeNode from the information read
	 * @throws IOException
	 */
	public bTreeNode diskRead(File fileName, long position, int degree, int sequenceLength) throws IOException {
		//opening the file for read and write
		fileAccessWorker = new RandomAccessFile(fileName, "rw");
		// moves file pointer to position specified
		fileAccessWorker.seek(position);

		// reading long node position from RandomAccessFile 
		long nodePosition =	fileAccessWorker.readLong();
		// reading boolean from RandomAccessFile with an int , make it easier to calculate
		boolean isLeaf=true;

		if(fileAccessWorker.readInt()==1) //4 Bytes
		{
			isLeaf=true;
		}
		else
		{
			isLeaf=false;
		}
		// reading int to RandomAccessFile
		int nodeNumKey = fileAccessWorker.readInt();// 4 Bytes

		bTreeNode<Long> newNode = new bTreeNode<Long>(position,degree);
		for (int i = 0; i < (2 * degree) - 1; i++) // total 9 bytes * (2*degreeOfTree-1)
		{

			long key = fileAccessWorker.readLong();// 8 bytes each
			newNode.getbTreeNodeArray()[i].setKey(key);

			int frequency = fileAccessWorker.readInt();
			newNode.getbTreeNodeArray()[i].setFrequency(frequency);

		}

		for (int i = 0; i < (2 * degree); i++) // total 8 bytes * (2*degreeOfTree)
		{
			newNode.getChildrenArray()[i]= fileAccessWorker.readLong();	
		}

		newNode.setLeaf(isLeaf);
		newNode.setNumKey(nodeNumKey);
		newNode.setPosition(nodePosition);

		fileAccessWorker.close();

		return newNode;
	}

	/**
	 * method to get the end of the file
	 * @param fileName
	 * @return the end of the file position
	 * @throws IOException
	 */
	public long getEndOfFilePosition(File fileName) throws IOException {

		fileAccessWorker = new RandomAccessFile(fileName, "rw"); // gets the file
		long temp = fileAccessWorker.length();
		fileAccessWorker.close();
		return temp;

	}

}
