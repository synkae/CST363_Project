package disk_store;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * An ordered index.  Duplicate search key values are allowed,
 * but not duplicate index table entries.  In DB terminology, a
 * search key is not a superkey.
 * 
 * A limitation of this class is that only single integer search
 * keys are supported.
 *
 */


public class OrdIndex implements DBIndex {
	
	private class Entry {
		int key;
		ArrayList<BlockCount> blocks;

		public Entry(int key, ArrayList<BlockCount> blocks){
			this.key = key;
			this.blocks = blocks;
		}
	}
	
	private class BlockCount {
		int blockNo;
		int count;

		public BlockCount(int blockNo, int count){
			this.blockNo = blockNo;
			this.count = count;
		}
	}
	
	ArrayList<Entry> entries;
	int size = 0;
	
	/**
	 * Create an new ordered index.
	 */
	public OrdIndex() {
		entries = new ArrayList<>();
	}
	
	@Override
	public List<Integer> lookup(int key) {
		/**
		 * Return a list of all the blockNum values associated with the
		 * given search key in the index (return an empty list if the
		 * key does not appear in the index).
		 * @param key value of a search key
		 * @return
		 */
		// binary search of entries arraylist
		// return list of block numbers (no duplicates). 
		// if key not found, return empty list

		List<Integer> result = new ArrayList<>();
		//int start = findStartPoint(entries, key);
		//int end = findEndPoint(entries, key);
		int index = binarySearch(entries, 0, entries.size()-1, key);
		if (index == -1)
		{
			return result;
		}
		HashSet<Integer> temp = new HashSet<>();
		for (BlockCount b : entries.get(index).blocks){
			temp.add(b.blockNo);
		}
		result = new ArrayList<>(temp);
		return result;
	}
	/**
	 * Insert the key/blockNum pair into the index.  If the pair is
	 * already present, it is not inserted.
	 * @param key value of a search key
	 * @param blockNum a DB block number
	 */
	@Override
	public void insert(int key, int blockNum) {
		//check if entries is empty
		//if not empty, do some complicated stuff
		if (entries.size() > 0){
			//get the index to insert using some kind of binary search
			int index = insertSearch(entries, 0, entries.size()-1, key);
			// if key goes to the end of the list
			if (index > entries.size()-1){
				ArrayList<BlockCount> blocks = new ArrayList<>();
				blocks.add(new BlockCount(blockNum, 1));
				Entry e = new Entry(key, blocks);
				entries.add(index, e);
			}
			else{
				//if key exists
				if (entries.get(index).key == key){
					for (BlockCount b : entries.get(index).blocks){
						if (b.blockNo == blockNum){ // if blocknumber already exists, increase count by 1
							b.count++;
							return;
						}
					}
					entries.get(index).blocks.add(new BlockCount(blockNum, 1));
				}
				else{ // if key does not exist, add new one
					ArrayList<BlockCount> blocks = new ArrayList<>();
					blocks.add(new BlockCount(blockNum, 1));
					Entry e = new Entry(key, blocks);
					entries.add(index, e);
				}
			}
		}
		//else, add a new entry to the entries
		else{
			ArrayList<BlockCount> blocks = new ArrayList<>();
			blocks.add(new BlockCount(blockNum, 1));
			Entry e = new Entry(key, blocks);
			entries.add(e);
		}
		size++;
	}
	// lookup key
	//  if key not found, should not occur.  Ignore it.
	//  decrement count for blockNum.
	//  if count is now 0, remove the blockNum.
	//  if there are no block number for this key, remove the key entry.
	@Override
	public void delete(int key, int blockNum) {
		int index = binarySearch(entries, 0, entries.size()-1, key);
		if (index == -1) return;
		for (int i=0; i<entries.get(index).blocks.size(); i++){
			BlockCount b = entries.get(index).blocks.get(i);
			if (b.blockNo == blockNum){
				b.count--;
				size--;
				if (b.count == 0){
					entries.get(index).blocks.remove(i);
					i--;
				}
			}
		}
		if (entries.get(index).blocks.size()==0){
			entries.remove(index);
		}
	}
	
	/**
	 * Return the number of entries in the index
	 * @return
	 */
	public int size() {
		return size;
		// you may find it useful to implement this
		
	}
	
	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}

	/*
		Modified binary search for insert function
		If found, return the index. If not found, it will return you the position it is supposed to be inserted on.
	 */
	public int insertSearch(ArrayList<Entry> entries, int left, int right, int key){
		int mid = (left + right)/2;
		if (right >= left){
			if (entries.get(mid).key == key){
				return mid;
			}
			if (entries.get(mid).key > key){
				return insertSearch(entries, left, mid-1, key);
			}
			else{
				return insertSearch(entries, mid + 1, right, key);
			}
		}
		return mid+1;
	}
	/*
		Binary search function
	 */
	public int binarySearch(ArrayList<Entry> entries, int left, int right, int key){
		int mid = (left + right)/2;
		if (right >= left){
			if (entries.get(mid).key == key){
				return mid;
			}
			if (entries.get(mid).key > key){
				return binarySearch(entries, left, mid-1, key);
			}
			else{
				return binarySearch(entries, mid + 1, right, key);
			}
		}
		return -1;
	}

	/*
	public int findStartPoint(ArrayList<Entry> entries, int key){
		int index = -1;
		int start = 0;
		int end = entries.size()-1;
		while (start <= end){
			int mid = (start + end)/2;
			if (entries.get(mid).key >= key){
				end = mid -1;
			}
			else{
				start = mid + 1;
			}
			if (entries.get(mid).key == key){
				index = mid;
			}
		}
		return index;
	}

	public int findEndPoint(ArrayList<Entry> entries, int key){
		int index = -1;
		int start = 0;
		int end = entries.size()-1;
		while (start <= end){
			int mid = (start + end)/2;
			if (entries.get(mid).key <= key){
				start = mid + 1;
			}
			else{
				end = mid - 1;
			}
			if (entries.get(mid).key == key){
				index = mid;
			}
		}
		return index;
	}
	*/

}