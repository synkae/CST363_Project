package disk_store;

import java.util.ArrayList;
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
		//else, add a new entry to the entries
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

				}
				else{

				}
			}
		}
		else{
			ArrayList<BlockCount> blocks = new ArrayList<>();
			blocks.add(new BlockCount(blockNum, 1));
			Entry e = new Entry(key, blocks);
			entries.add(e);
		}
	}

	@Override
	public void delete(int key, int blockNum) {
		// lookup key 
		//  if key not found, should not occur.  Ignore it.
		//  decrement count for blockNum.
		//  if count is now 0, remove the blockNum.
		//  if there are no block number for this key, remove the key entry.
		throw new UnsupportedOperationException();
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
		return mid;
	}

	public int binarySearch(ArrayList<Entry> entries, int left, int right, int key){
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
		return -1;
	}
}