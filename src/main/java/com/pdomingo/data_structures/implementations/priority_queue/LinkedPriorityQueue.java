package com.pdomingo.data_structures.implementations.priority_queue;

import com.pdomingo.data_structures.implementations.list.LinkedList;
import com.pdomingo.data_structures.implementations.priority_queue.abstracts.AbstractPriorityQueue;
import com.pdomingo.data_structures.interfaces.Entry;
import com.pdomingo.data_structures.interfaces.List;
import com.pdomingo.data_structures.interfaces.Position;

import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @param <K>
 * @param <V>
 */
public class LinkedPriorityQueue<K,V> extends AbstractPriorityQueue<K,V> {

	private List<Entry<K,V>> queue;

	/*************************************************************/

	public LinkedPriorityQueue() {
		super();
		this.queue = new LinkedList<>();
	}

	public LinkedPriorityQueue(Comparator<K> comparator) {
		super(comparator);
		this.queue = new LinkedList<>();
	}

	/*************************************************************/

	@Override
	public int size() {
		return queue.size();
	}

	/**
	 * Empty the priority queue
	 */
	@Override
	public void clear() {

		for (Entry<K,V> entry : queue) {
			entry.setValue(null);
			entry.setValue(null);
		}

		queue.clear();
	}

	@Override
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {

		Entry<K,V> newEntry = PQEntry.of(key,value);
		boolean added = false;

		// Search insertion position traversing queue
		for (Position<Entry<K,V>> entry : queue.positions()) {

			int cmp = compare(entry.getElement(), newEntry);

			// If priority is higher or equal
			if(cmp > 0 || cmp == 0) {

				if(cmp > 0) // higher P
					queue.addBefore(entry, newEntry);
				else // equal P
					queue.addAfter(entry, newEntry);

				added = true;
				break;
			}
		}

		// If priority was lower than every else entry
		if( ! added)
			queue.addLast(newEntry);

		return newEntry;
	}

	@Override
	public Entry<K, V> first() {
		return queue.first().getElement();
	}

	@Override
	public Entry<K, V> removeFirst() {
		return queue.removeFirst().getElement();
	}

	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator<Entry<K, V>> iterator() {
		return queue.iterator();
	}
}
