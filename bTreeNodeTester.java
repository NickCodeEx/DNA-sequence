import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class bTreeNodeTester<T> {

	@Test
	void testNewBTreeNode() {
		bTreeNode<T> bNode = new bTreeNode<T>(0, 1);
		assertEquals(0, bNode.getPosition());
		assertEquals(1, bNode.degree());
		assertEquals(1, bNode.getNumKey());
	}
	
	@Test //check
	void testNewBTreeArray() {
		bTreeNode<T> bNode = new bTreeNode<T>(0, 2);
		bTreeObject<T>[] bTreeNodeArray = new bTreeObject[(2*bNode.degree())-1];
		
		assertEquals(3, bTreeNodeArray.length);
	}
	
	@Test
	void testDegree() {
		bTreeNode<T> bNode = new bTreeNode<T>(0, 4);
		assertEquals(4, bNode.degree());
	}
	
	@Test //check
	void testGetBTreeNodeArray() {
		bTreeNode<T> bNode = new bTreeNode<T>(0, 1);
		bTreeObject<T>[] bTreeNodeArray = new bTreeObject[(2*bNode.degree())-1];
		for (int i = 0; i < bTreeNodeArray.length; i++) {
			bTreeNodeArray[i] = new bTreeObject(-1L); 
		}
		assertEquals(-1, bNode.getbTreeNodeArray());
	}
	
	@Test
	void testSetBTreeNodeArray() {
		
	}
	
	@Test
	void testIsLeaf() {
		
	}
	
	@Test
	void testSetLeaf() {
		
	}
	
	@Test
	void testGetChildrenArray() {
		
	}
	
	@Test
	void testSetChildrenArray() {
		
	}
	
	@Test
	void testGetNumKey() {
		
	}
	
	@Test
	void testSetNumKey() {
		
	}
	
	@Test
	void testGetPosition() {
		bTreeNode<T> bNode = new bTreeNode<T>(0, 1);
		assertEquals(0, bNode.getPosition());
	}
	
	@Test
	void testSetPosition() {
		bTreeNode<T> bNode = new bTreeNode<T>(0, 1);
		bNode.setPosition(2);
		assertEquals(2, bNode.getPosition());
	}
}
