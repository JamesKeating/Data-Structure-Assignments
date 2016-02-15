package dsa.impl;
import dsa.core.Iterator;
import dsa.core.List;
import dsa.core.Position;


public class LinkedList<T> implements List<T> {
	private class Node implements Position<T> {
		T value;
		Node next, prev;
		
		public Node(T v, Node p, Node n) {
			this.value = v;
			this.next = n;
			this.prev = p;
		}
		
		public T element() {
			return value;
		}
	}
	
	private Node first, last;
	private int size;
	
	public LinkedList() {
		first = last = null;
		size = 0;
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	private Node toNode(Position<T> p) {
		return (Node) p;
	}
	
	@Override
	public Position<T> first() {
		if (first == null) throw new ListEmptyException();
		return first;
	}

	@Override
	public Position<T> last() {
		if (last == null) throw new ListEmptyException();
		return last;
	}

	@Override
	public Position<T> prev(Position<T> p) {
		Node node = toNode(p);
		return node.prev;
	}

	@Override
	public Position<T> next(Position<T> p) {
		Node node = toNode(p);
		return node.next;
	}

	@Override
	public Position<T> insertFirst(T e) {
		Node node = new Node(e, null, first);
		if (first != null) first.prev = node;
		else last = node;
		
		first = node;
		
		size++;
		
		return node;
	}

	@Override
	public Position<T> insertLast(T e) {
		Node node = new Node(e, last, null);
		if (last != null) last.next = node;
		else first = node;
		last = node;
		size++;
		return node;
	}

	@Override
	public Position<T> insertBefore(Position<T> p, T e) {
		if (p == first) return insertFirst(e);

		Node n = toNode(p);
		Node node = new Node(e, n.prev, n);
		n.prev.next = node;
		n.prev = node;
		size++;
		return node;
	}

	@Override
	public Position<T> insertAfter(Position<T> p, T e) {
		if (p == last) return insertLast(e);

		Node n = toNode(p);
		Node node = new Node(e, n, n.next);
		n.next.prev = node;
		n.next = node;
		size++;
		return node;
	}

	@Override
	public T replace(Position<T> p, T e) {
		Node node = toNode(p);
		T temp = node.value;
		node.value = e;
		return temp;
	}

	@Override
	public T remove(Position<T> p) {
		Node node = toNode(p);
		if (node == first) first = first.next;
		else node.prev.next = node.next;

		if (node == last) last = last.prev;
		else node.next.prev = node.prev;

		size--;
		return node.value;
	}

	public String toString() {
		if (size == 0) return "";
		
		String output = "";
		Node c = first;
		while (c != null) {
			output += c.value + " ";
			c = c.next;
		}
		return output;
	}
	@Override
	public Iterator<T> iterator() {
		return new ListIterator<T>(this);
	}
	
	public static void main(String[] args) {
		List<String> list = new LinkedList<String>();
		Position<String> p = list.insertFirst("r");
		list.insertBefore(p, "e");
		list.insertFirst("h");
		System.out.println(list);
	}
}
