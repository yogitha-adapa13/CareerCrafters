package MyCollections;

import java.util.Comparator;

public interface AppBag<T> {
	 /** Gets the current number of entries in this bag.
     * @return The integer number of entries currently in the bag. 
     */
    public int getCurrentSize();

    /** Sees whether this bag is empty.
     * @return True if the bag is empty, or false if not. 
     */
    public boolean isEmpty();

    /** Adds a new entry to this bag.
     * @param newEntry The object to be added as a new entry.
     * @return True if the addition is successful, or false if not. 
     */
    public boolean add(T newEntry);
    
    /**
     * Retrieves the element at the specified index in this bag.
     * 
     * @param index The index of the element to retrieve.
     * @return The element at the specified index, or null if the index is out of bounds.
     */
    public T get(int index);
    /** Removes one unspecified entry from this bag, if possible.
     * @return Either the removed entry, if the removal was successful, or null. 
     */
    public T remove();

    /** Removes one occurrence of a given entry from this bag, if possible.
     * @param anEntry The entry to be removed.
     * @return True if the removal was successful, or false if not 
     */
    public boolean remove(T anEntry);

    /** Removes all entries from this bag. */
    public void clear();

    /** Counts the number of times a given entry appears in this bag.
     * @param anEntry The entry to be counted.
     * @return The number of times anEntry appears in the bag. 
     */
    public int getFrequencyOf(T anEntry);
    
    /** Tests whether this bag contains a given entry.
     * @param anEntry The entry to locate.
     * @return True if the bag contains anEntry, or false if not. 
     */
    public boolean contains(T anEntry);

    /** Retrieves all entries that are in this bag.
     * @return A newly allocated array of all the entries in the bag.
     * Note: If the bag is empty, the returned array is empty. 
     */
    public T[] toArray();
}
