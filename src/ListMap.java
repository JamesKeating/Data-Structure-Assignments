import dsa.core.Iterator;
import dsa.core.List;
import dsa.core.Position;
import dsa.impl.LinkedList;
import dsa.impl.ListIterator;


/**
 * Example implementation of a list based iterator.  Uses the Link-based implementation of
 * a list from the DSAI course.
 * 
 * @author Rem Collier
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class ListMap<K,V> implements Map<K,V> {
	/**
	 * This class is an inner class.
	 * 
	 * It is an internal implementation of the entry concept.  This implementation is not visible outside
	 * of the ListMap class.  Only variables of type Entry<K,V> can reference instances of this object.
	 * This means that only the key() and value() methods may be used externally.
	 * 
	 * NB: actually, the toString() method can also be used externally because toString() is defined
	 * in the Object class which all classes inherit by default.  In this case, the toString() method
	 * has been overriden so that it returns a "nice" representatino of the state of the object instead
	 * of an object reference, which is the default implementation of this method.
	 *  
	 * @author Rem Collier
	 *
	 */
	private class ListEntry implements Entry<K,V> {
		K key;
		V value;
		
		/**
		 * Constructor to create a ListEntry instance.  This constructor can only be
		 * invoked from within the ListMap class.  This means that instances of the
		 * ListEntry class can only be created inside the ListMap class.
		 *  
		 * @param k
		 * @param v
		 */
		public ListEntry(K k, V v) {
			key = k;
			value = v;
		}

		/**
		 * return the key of the entry (part of the implementation of the Entry interface)
		 */
		@Override
		public K key() {
			return key;
		}

		/**
		 * return the value of the entry (part of the implementation of the Entry interface)
		 */
		@Override
		public V value() {
			return value;
		}
		
		/**
		 * This method returns a string representation of the state of the object.
		 */
		@Override
		public String toString() {
			return "{ " + key + ", " + value + " }";
		}
	}

	/**
	 * An instance of a link-based implementation of a list from DSAI.  This list will hold
	 * the entries that are stored within the map.
	 */
	private List<Entry<K,V>> list = new LinkedList<Entry<K,V>>();
	
	/**
	 * Returns the number of entries in the map, which is the same as the number of elements
	 * stored in the list.
	 */
	@Override
	public int size() {
		return list.size();
	}

	/**
	 * Returns true if the map is empty, false otherwise.  Again, returns the same value as the
	 * isEmpty() method of the list class (if the list is empty, then the map is empty). 
	 */
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * This method implements a linear search of the list. A match occurs if the entry being
	 * checked has the same key as the key being searched for.
	 * 
	 * This method is core to the implementation of a ListMap and is used in the get(), put() and
	 * remove() methods.
	 * 
	 * @param k the key being searched for
	 * @return the position of the entry with key k or null (if no such entry exists).
	 */
	private Position<Entry<K, V>> find(K k) {
		if (list.isEmpty()) return null;
		Position<Entry<K, V>> position = list.first();
		while (position != list.last()) {
			if (position.element().key().equals(k)) {
				return position;
			}
			
			position = list.next(position);
		}

		if (position.element().key().equals(k)) {
			return position;
		} else {
			return null;
		}

//		Shorthand implementation of the above if statement
//		return (position.element().key().equals(k)) ? position:null;
	}

	/**
	 * Return the value associated with key k (if it exists) or null (otherwise)
	 */
	@Override
	public V get(K k) {
		// Perform a linear search for an entry with key k.
		Position<Entry<K,V>> position = find(k);

		// If there is no entry with key k (the find method returned null), return null
		if (position == null) return null;
		
		// There is an entry with key k at position "position".
		// Return the value of the entry at position "position" in the list
		return position.element().value();
	}

	/**
	 * This method associates key k with value v. If there is already a value associated
	 * with k, then this method updates the entry and returns the previous value.  If there
	 * is no value associated with k then this method inserts a new entry with key k and 
	 * value v.
	 */
	@Override
	public V put(K k, V v) {
		// Perform a linear search for an entry with key k.
		Position<Entry<K,V>> position = find(k);

		// If there is no entry with key k (the find method returned null), we need
		// to create a new entry...
		if (position == null) {
			list.insertLast(new ListEntry(k,v));
			return null;
		}

		// There is an existing entry with key k at position "position", so replace
		// this entry with the new entry.  The replace method return the old entry...
		Entry<K,V> temp = list.replace(position, new ListEntry(k,v));
		
		// Return the value part of the old entry.
		return temp.value();

//		Concise version of the last two lines of code
//		return list.replace(position, new ListEntry(k,v)).value();
	}

	/**
	 * Remove the entry with key k and return the value that was associated
	 * with it (if one exists) or return null (if there was no entry with 
	 * key k).
	 */
	@Override
	public V remove(K k) {
		// Perform a linear search for an entry with key k.
		Position<Entry<K,V>> position = find(k);

		// If there is no entry with key k (the find method returned null), return null
		if (position == null) return null;
		
		// There is an entry with key k at position "position", so remove that position from
		// the list.
		Entry<K,V> temp = list.remove(position);
		
		// Return the value of the entry stored at position "position"
		return temp.value();

//		Concise version of the last two lines of code.
//		return list.remove(position).value();
	}

	/**
	 * Inner class that implements an iterator that returns the keys stored in the map
	 * 
	 * This implementation uses the ListIterator (which iterates through the elements of
	 * a list).
	 * 
	 * @author Rem Collier
	 *
	 */
	private class KeyIterator implements Iterator<K> {
		/*
		 * The list iterator
		 */
		Iterator<Entry<K, V>> iterator;
		
		/*
		 * Constructor to instantiate the list iterator.
		 */
		public KeyIterator() {
			iterator = entries();
		}
		
		@Override
		public boolean hasNext() {
			// If the list contains more elements then there are more keys in the
			// map
			return iterator.hasNext();
		}
		
		@Override
		public K next() {
			// Get the next entry and return the key part (it is a key iterator so
			// we are not interested in the value...
			return iterator.next().key();
		}
	}
	
	/**
	 * Return an iterator that can be used to loop through the keys stored in the map. 
	 */
	@Override
	public Iterator<K> keys() {
		return new KeyIterator();
	}

	/**
	 * Return an iterator that can be used to loop through the values stored in the map
	 */
	@Override
	public Iterator<V> values() {
		// This implementation is basically the same as the Key iterator with two
		// exceptions:
		// 1. The mext() method returns a value instead of a key
		// 2. The class is declared inline and as such, is anonymous (it has no name)
		return new Iterator<V>() {
			Iterator<Entry<K, V>> iterator = entries();
			
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public V next() {
				return iterator.next().value();
			}
		};
	}

	/**
	 * Return an iterator to the entries in the map (this is basically the standard
	 * List Iterator discussed in week 1 of the course.
	 */
	@Override
	public Iterator<Entry<K, V>> entries() {
		return new ListIterator<Entry<K,V>>(list);
	}
}
