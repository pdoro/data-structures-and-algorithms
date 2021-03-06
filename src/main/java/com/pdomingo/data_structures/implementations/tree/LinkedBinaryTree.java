package com.pdomingo.data_structures.implementations.tree;

import com.pdomingo.data_structures.implementations.tree.abstracts.AbstractBinaryTree;
import com.pdomingo.data_structures.interfaces.BinaryTree;
import com.pdomingo.data_structures.interfaces.Position;
import com.pdomingo.data_structures.interfaces.TraversalStrategy;

/**
 *
 * @param <T>
 *
 * <h4>Complexity summary</h4>
 * <table>
 *      <thead>
 *          <td>Method</td><td>Worst Case Complexity</td>
 *      </thead>
 *      <tr><td>{@link LinkedBinaryTree#size()}</td><td>O(1)</td></tr>
 *      <tr><td>{@link LinkedBinaryTree#isEmpty()}</td><td>O(1)</td></tr>
 *      <tr><td>{@link LinkedBinaryTree#clear()}</td><td>O(n)</td></tr>
 *      <tr><td>{@link LinkedBinaryTree#left(Position)}</td><td>O(1)</td></tr>
 *      <tr><td>{@link LinkedBinaryTree#right(Position)}</td><td>O(1)</td></tr>
 *      <tr><td>{@link LinkedBinaryTree#sibling(Position)}</td><td>O(1)</td></tr>
 *
 * </table>
 */
public class LinkedBinaryTree<T> extends AbstractBinaryTree<T> {

	private BinaryNode<T> root;
	private int size;

	/**
	 *
	 * @param <T>
	 */
	private static class BinaryNode<T> implements Position<T> {

		private BinaryNode<T> parent;
		private BinaryNode<T> left;
		private BinaryNode<T> right;

		private T element;

		private BinaryNode(BinaryNode<T> parent, BinaryNode<T> left, BinaryNode<T> right, T element) {
			this.parent = parent;
			this.left = left;
			this.right = right;
			this.element = element;
		}

		@Override
		public T getElement() {
			return element;
		}

		private void removeChild(BinaryNode<T> node) {
			if(node == left)
				left = null;
			else if(node == right)
				right = null;

		}

		private boolean hasLeft() {
			return left != null;
		}

		private boolean hasRight() {
			return right != null;
		}

		private T delete() {

			if(parent != null)
				parent.removeChild(this);

			this.parent = null;
			this.left = null;
			this.right = null;
			T element = this.element;
			this.element = null;
			return element;
		}
	}

	public LinkedBinaryTree() {
		this.root = null;
		this.size = 0;
	}

	private BinaryTree<T> asSubTree(Position<T> root) {
		BinaryNode<T> bnRoot = binaryNode(root);
		return new LinkedBinaryTree<>(bnRoot);
	}

	private LinkedBinaryTree(BinaryNode<T> root) {
		this.root = root;
		this.size = subtreeSize(root);
	}

	private int subtreeSize(BinaryNode<T> root) {

		int size = 0;

		if(root.hasLeft())
			size += subtreeSize(root.left);
		if(root.hasRight())
			size += subtreeSize(root.right);

		size += numChildren(root);

		return size;
	}

	/**********************************************************/

	@Override
	public Position<T> root() {
		return root;
	}

	@Override
	public Position<T> parent(Position<T> position) {
		return binaryNode(position).parent;
	}

	@Override
	public Position<T> left(Position<T> position) {
		return binaryNode(position).left;
	}

	@Override
	public Position<T> right(Position<T> position) {
		return binaryNode(position).right;
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * Empty the deque
	 */
	@Override
	public void clear() {
		for(Position<T> position : positions())
			binaryNode(position).delete();

		size = 0;
	}

	@Override
	public int depth(Position<T> position) {

		BinaryNode<T> node = binaryNode(position);

		if(node == root)
			return 0;
		else
			return 1 + depth(node.parent);
	}

	public Position<T> addRoot(T element) {
		if(!isEmpty())
			throw new IllegalStateException("Tree is not empty");

		root = new BinaryNode<>(null, null, null, element);
		size++;

		return root;
	}

	public Position<T> addLeft(Position<T> position, T element) {

		if(left(position) != null)
			throw new IllegalStateException("Position already has left");

		BinaryNode<T> parent = binaryNode(position);
		parent.left = new BinaryNode<>(parent, null, null, element);
		size++;

		return parent.left;
	}

	public Position<T> addRight(Position<T> position, T element) {

		if(right(position) != null)
			throw new IllegalStateException("Position already has left");

		BinaryNode<T> parent = binaryNode(position);
		parent.right = new BinaryNode<>(parent, null, null, element);
		size++;

		return parent.right;
	}

	public T set(Position<T> position, T element) {

		BinaryNode<T> binaryNode = binaryNode(position);
		T prevElement = binaryNode.element;
		binaryNode.element = element;

		return prevElement;
	}

	public void attach(Position<T> position, BinaryTree<T> leftTree, BinaryTree<T> rightTree) {

		if( ! isLeaf(position))
			throw new IllegalStateException("Position is not a leaf");

		BinaryNode<T> parent = binaryNode(position);

		if(!leftTree.isEmpty()) {
			BinaryNode<T> leftRoot = binaryNode(leftTree.root());

			parent.left = leftRoot;
			leftRoot.parent = parent;
		}

		if(!rightTree.isEmpty()) {
			BinaryNode<T> rightRoot = binaryNode(rightTree.root());

			parent.right = rightRoot;
			rightRoot.parent = parent;
		}

		size += (leftTree.size() + rightTree.size());
	}

	public void prune(Position<T> position) {

		/*
		Notes: After deleting recursively the left subtree, delete the 'root' node of the subree
		 */

		if(position == null)
			return;

		BinaryNode<T> node = binaryNode(position);

		// Prune left sutree
		if(node.hasLeft()) {
			prune(node.left);
			node.left.delete();
			size--;
		}

		// Prune right sutree
		if(node.hasRight()) {
			prune(node.right);
			node.right.delete();
			size--;
		}
	}

	@Override
	public Iterable<Position<T>> traverse(TraversalStrategy<T> strategy) {
		return strategy.traverse(this);
	}

	/**
	 *
	 * @param position
	 * @return node that corresponds to given position
	 */
	private BinaryNode<T> binaryNode(Position<T> position) {

		if( ! (position instanceof BinaryNode))
			throw new IllegalArgumentException("Invalid position");

		// Safe cast
		BinaryNode<T> binaryNode = (BinaryNode<T>) position;

		if(binaryNode.parent == binaryNode)
			throw new IllegalArgumentException("Position is no longer in the tree");

		return binaryNode;
	}
}
