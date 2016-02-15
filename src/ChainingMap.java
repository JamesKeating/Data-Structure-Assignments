import dsa.core.Iterator;
import dsa.core.List;
import dsa.core.Position;
import dsa.impl.LinkedList;
import dsa.impl.ListIterator;


public class ChainingMap<K,V> implements Map<K, V> {
	private class ListEntry implements Entry<K,V> {
		K key;
		V value;
		
		/**
		 * Constructor to create a ListEntry instance.
		 * @param k
		 * @param v
		 */
		public ListEntry(K k, V v) {
			key = k;
			value = v;
		}

		@Override
		public K key() {
			return key;
		}

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

	private static final int DEFAULT_SIZE = 13;
	private List<Entry<K,V>>[] array;
	private int size;
	
	@SuppressWarnings("unchecked")
	public ChainingMap(int N) {
		this.size = 0;
		array = (List<Entry<K,V>>[]) new List[N];
	}
	
	public ChainingMap() {
		this(DEFAULT_SIZE);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public V get(K k) 
	{
		int h = hashFunction(k);
		if (array[h] == null)
		{
			return null;
		}
	
		Position<Entry<K,V>> P = find(array[h], k);
		
		if (P == null)
		{
			return null;
		}
		
		return P.element().value();

	}

	@Override
	public V put(K k, V v) 
	{
		int h = hashFunction(k);
		V temp = null;
		Entry<K,V> entry = new ListEntry(k,v);
		if (array[h] == null)
		{
		    array[h] = new LinkedList<Entry<K,V>>();
		    array[h].insertLast(entry);
		}
		else
		{
			Position<Entry<K,V>> P = find(array[h], k);
			
		    if (P == null)
		    {
		    	array[h].insertLast(entry);
		    }
		        
		    
		    else
		    {
		        entry = array[h].replace(P, entry);
		        temp = entry.value();
		    }
		    
		    size = size + 1;
		}
		    
		return temp;
	}

	private int hashFunction(K k) {
		if (k instanceof Integer) {
			return (Integer) k % array.length;
		}
		throw new UnsupportedOperationException("Invalid Key");
	}

	@Override
	public V remove(K k)
	{
		int h = hashFunction(k);
		if (array[h] == null)
		{
			return null;
		}
		Position<Entry<K,V>> P = find(array[h], k);
		if (P == null)
		{
			return null;	
		}
		Entry<K, V> e = array[h].remove(P);
		size = size - 1;
		return e.value();

	}

	@Override
	public Iterator<K> keys() 
	{
		return new KeyIterator();
	}
	
	private class KeyIterator implements Iterator<K> {
		
		// the list iterator
		Iterator<Entry<K, V>> iterator;
		
		
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
		public K next() 
		{
			return iterator.next().key();
		}
	}

	
	@Override
	public Iterator<V> values() 
	{		
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
	

	@Override
	public Iterator<Entry<K, V>> entries() {
		return new Iterator<Entry<K, V>>() {
			// Keep track of the next element to be checked in the array.
			int index = 0;
			
			// The current iterator - a new one will be created each time a new element is processed.
			Iterator<Entry<K,V>> it;
			
			@Override
			public boolean hasNext() {
				if (it == null || !it.hasNext()) {
					
					while (index < array.length && array[index] == null) index++;
					if (index == array.length) return false;
					it = new ListIterator<Entry<K,V>>(array[index++]);
				}
				return it.hasNext();
			}

			@Override
			public Entry<K, V> next() {
				return (Entry<K,V>) it.next();
			}
		};
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i < array.length; i++) {
			if (i < 10) buf.append(" ");
			buf.append(i + ": ");
			if (array[i] != null) { 
				buf.append("[ ");
				Iterator<Entry<K,V>> it = new ListIterator<Entry<K, V>>(array[i]);
				while (it.hasNext()) {
					buf.append(it.next() + " ");
				}
				buf.append("]");
			}
			buf.append("\n");
		}
		return buf.toString();
	}
	
	private Position<Entry<K,V>> find(List<Entry<K, V>> list, K key)
	{
		
		if (list.isEmpty()) return null;
		Position<Entry<K, V>> position = list.first();
		while (position != list.last()) 
		{
			if (position.element().key().equals(key))
			{
				return position;
			}
			
			position = list.next(position);
		}

		if (position.element().key().equals(key))
		{
			return position;	
		}
		
		else 
		{
			return null;
		}

		
	}
	
}
