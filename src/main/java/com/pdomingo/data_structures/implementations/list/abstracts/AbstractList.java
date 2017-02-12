package com.pdomingo.data_structures.implementations.list.abstracts;

import com.pdomingo.data_structures.interfaces.Position;
import com.pdomingo.exceptions.IndexOutOfBoundsException;
import com.pdomingo.data_structures.interfaces.List;

import java.util.Iterator;

/**
 * Created by Pablo on 21/12/16.
 */
public abstract class AbstractList<T> implements List<T> {

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsAll(Iterable<T> items) {
		for(T item : items)
			if( ! contains(item))
				return false;

		return true;
	}

	@Override
	public List<T> addAll(Iterable<T> items) {
		for(T item : items)
			add(item);

		return this;
	}

	@Override
	public Iterable<T> removeAll(Iterable<T> items) {
		for(T item : items)
			remove(item);

		return this;
	}

	/**
	 *
	 * @return
	 */
	public Iterator<T> iterator() {
		return new AbstractListIterator();
	}

	/**
	 *
	 */
	protected class AbstractListIterator implements Iterator<T> {

		private Iterator<Position<T>> iterator = positions().iterator();

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public T next() {
			return iterator.next().getElement();
		}

		public void remove() {
			iterator.remove();
		}
	}

	protected void checkRange(int index) throws IndexOutOfBoundsException {
		if(index < 0 || index >= size())
			throw new IndexOutOfBoundsException("Invalid index: " + index);
	}
}
