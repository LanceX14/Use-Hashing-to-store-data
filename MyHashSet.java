import java.io.*;
import java.util.*;

public class MyHashSet implements HS_Interface
{	private int numBuckets; // changes over life of the hashset due to resizing the array
	private Node[] bucketArray;
	private int size; // total # keys stored in set right now

	// THIS IS A TYPICAL AVERAGE BUCKET SIZE. IF YOU GET A LOT BIGGER THEN YOU ARE MOVING AWAY FROM (1)
	private final int MAX_ACCEPTABLE_AVE_BUCKET_SIZE = 20;  // MAY CHOSE ANOTHER NUMBER

	public MyHashSet( int numBuckets )
	{	size=0;
		this.numBuckets = numBuckets;
		bucketArray = new Node[numBuckets]; // array of linked lists
		System.out.format("IN CONSTRUCTOR: INITIAL TABLE LENGTH=%d RESIZE WILL OCCUR EVERY TIME AVE BUCKET LENGTH EXCEEDS %d\n", numBuckets, MAX_ACCEPTABLE_AVE_BUCKET_SIZE );
	}

	public int hashCode( String key )
{
	int hash = 997;
	for (int i=0 ; i< key.length() ; ++i )
		hash = Math.abs( hash * 33 + (short)key.charAt(i) );
  return hash % numBuckets;

}

	public boolean add( String key )
	{
		if (contains(key)) return false;
		int index = hashCode(key);
		bucketArray[ index ] = new Node(key, bucketArray[ index ] );

		++size; // you just added a key to one of the lists
		if ( size > MAX_ACCEPTABLE_AVE_BUCKET_SIZE * this.numBuckets)
		{
			//System.out.format("DEBUG: #keys: %d  this.numBuckets: %d\n", size, this.numBuckets );
			upSize_ReHash_AllKeys(); // DOUBLE THE ARRAY .length THEN REHASH ALL KEYS
		}
		return true;
	}


		public boolean contains( String key )
						 {
				int index = hashCode(key);
				if ( bucketArray[index] == null ) return false;
				Node curr=bucketArray[index];
			  while( curr != null )
					if (curr.data.equals(key))
					return true;
					else
					 curr=curr.next;
					return false; // ONLY IF YOU SEARCHED ALL LISTS THEN U RET FALSE
						 }


	private void upSize_ReHash_AllKeys()
	{
		System.out.format("KEYS HASHED=%10d UPSIZING TABLE FROM %8d to %8d REHASHING ALL KEYS\n",
							 size, bucketArray.length, bucketArray.length*2  );
		Node[] biggerArray = new Node[ bucketArray.length * 2 ];
		this.numBuckets = biggerArray.length;

		for(Node head : bucketArray)
		{	if (head==null) continue; // skip this one goto next list
			Node curr=head;
			while (curr!=null)
			{ 	int index = hashCode(curr.data);
				biggerArray[index] = new Node(curr.data, biggerArray[index] );
				curr=curr.next;
			}
		}

		bucketArray = biggerArray;

	}


	public boolean remove( String key )
	{
		int index = hashCode(key);
		if (bucketArray[index]==null) return false;
		if (bucketArray[index].data.equals(key))
		{
			bucketArray[index] = bucketArray[index].next;
			return true;
		}

		Node curr = bucketArray[index];

		while(curr.next!=null)
		{
			if (curr.next.data.equals(key))
			{
				curr.next = curr.next.next;
				return true;
			}
			curr = curr.next;
		}
		return false;
	}

	public void clear(){
		bucketArray = null;
		size = 0;
	}

	public int size(){
		return size;
	}
	public boolean isEmpty(){
		return (size==0);
	}


} //END MyHashSet CLASS

class Node
{	String data;
	Node next;
	public Node ( String data, Node next )
	{ 	this.data = data;
		this.next = next;
	}
}
