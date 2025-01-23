package MyCollections;

import java.util.Arrays;
import java.util.Comparator;

public class AppArray<T> implements AppBag<T>{
	
	 	private final T[] bag;
	    private int numberOfElements;
	    private static final int DEFAULT_CAPACITY = 25;
	    
	    private boolean initialized = false;
	    private static final int MAX_CAPACITY = 10000;

	    /** Creates an empty bag whose initial capacity is 25. */
	    public AppArray() {
	        this(DEFAULT_CAPACITY);
	    } // end default constructor

	    /**
	     * Creates an empty bag having a given initial capacity.
	     * @param desiredCapacity The integer capacity desired.
	     */
	    public AppArray(int desiredCapacity) {
	        if (desiredCapacity <= MAX_CAPACITY) {

	            // The cast is safe because the new array contains null entries.
	            @SuppressWarnings("unchecked")
	            T[] tempBag = (T[]) new Object[desiredCapacity]; // Unchecked cast
	            bag = tempBag;
	            numberOfElements = 0;
	            initialized = true;
	        }
	        else
	            throw new IllegalStateException("Attempt to create a bag " +
	                                            "whose capacity exceeds " +
	                                            "allowed maximum.");
	    } // end constructor

	    /** Adds a new entry to this bag.
	     * @param element The object to be added as a new entry.
	     * @return True if the addition is successful, or false if not. 
	     */
	    public boolean add(T element) {
	        checkInitialization();
	        boolean result = true;
	        if (isArrayFull()) {
	            result = false;
	        } else { // Assertion: result is true here
	            bag[numberOfElements] = element;
	            numberOfElements++;
	        } // end if
	        return result;
	    } // end add

	    /** Throws an exception if this object is not initialized.
	     */
	    private void checkInitialization()
	    {
	        if (!initialized)
	             throw new SecurityException("ArrayBag object is not initialized properly.");
	    }
	    /**
	     * Retrieves the element at the specified index in this bag.
	     * 
	     * @param index The index of the element to retrieve.
	     * @return The element at the specified index, or null if the index is out of bounds.
	     */
	    public T get(int index) {
	        if (index >= 0 && index < numberOfElements) {
	            return bag[index];
	        } else {
	            return null;
	        }
	    }

	    
	    /** Retrieves all entries that are in this bag.
	     * @return A newly allocated array of all the entries in the bag. 
	     */
	    public T[] toArray() {
	        // the cast is safe because the new array contains null entries
	        @SuppressWarnings("unchecked")
	        T[] result = (T[]) new Object[numberOfElements]; // unchecked cast
	        for (int index = 0; index < numberOfElements; index++) {
	            result[index] = bag[index];
	        } // end for
	        return result;
	    } // end toArray

	    /** Sees whether this bag is full.
	     * @return True if the bag is full, or false if not. 
	     */
	    private boolean isArrayFull() {
	        return numberOfElements >= bag.length;
	    } // end isArrayFull

	    /** Sees whether this bag is empty.
	     * @return True if the bag is empty, or false if not. 
	     */
	    public boolean isEmpty() {
	        return numberOfElements == 0;
	    } // end isEmpty

	    /** Gets the current number of entries in this bag.
	     * @return The integer number of entries currently in the bag. 
	     */
	    public int getCurrentSize() {
	        return numberOfElements;
	    } // end getCurrentSize

	    /** Counts the number of times a given entry appears in this bag.
	     * @param anEntry The entry to be counted.
	     * @return The number of times anEntry appears in the bag. 
	     */
	    public int getFrequencyOf(T anEntry) {
	        checkInitialization();
	        int counter = 0;
	        for (int index = 0; index < numberOfElements; index++) {
	            if (anEntry.equals(bag[index])) {
	                counter++;
	            } // end if
	        } // end for
	        return counter;
	    } // end getFrequencyOf

	    /** Tests whether this bag contains a given entry.
	     * @param anEntry The entry to locate.
	     * @return True if the bag contains anEntry, or false if not. 
	     */
	    public boolean contains(T anEntry) {
	        checkInitialization();
	        return getIndexOf(anEntry) > -1;
	    } // end contains

	    /** Removes all entries from this bag. */
	    public void clear() {
	        while (!isEmpty()) {
	            remove();
	        }
	    } // end clear

	    /** Removes one unspecified entry from this bag, if possible.
	     * @return Either the removed entry, if the removal was successful, or null if otherwise. 
	     */
	    public T remove() {
	        checkInitialization();
	        T result = removeEntry(numberOfElements - 1);

	        return result;
	    } // end remove

	    /** Removes one occurrence of a given entry from this bag.
	     * @param anEntry The entry to be removed.
	     * @return True if the removal was successful, or false if not. 
	     */
	    public boolean remove(T anEntry) {
	        checkInitialization();
	        int index = getIndexOf(anEntry);
	        T result = removeEntry(index);
	        return anEntry.equals(result);
	    } // end remove

	    /** Removes and returns the entry at a given array index within the array bag.
	     * If no such entry exists, returns null.
	     * Preconditions: 0 <= givenIndex < numberOfEntries;
	     *                  checkInitialization has been called.
	     */
	    private T removeEntry(int givenIndex) {
	        T result = null;
	        if (!isEmpty() && (givenIndex >= 0)) {
	            result = bag[givenIndex];                   
	            bag[givenIndex] = bag[numberOfElements - 1]; 
	            bag[numberOfElements - 1] = null;            
	            numberOfElements--;
	         } // end if
	        return result;
	    } // end removeEntry

	    /** Locates a given entry within the array bag.
	     * Returns the index of the entry, if located, or -1 otherwise.
	     * Precondition: checkInitialization has been called.
	     */
	    private int getIndexOf(T anEntry) {
	        int where = -1;
	        boolean stillLooking = true;
	        int index = 0;
	        while ( stillLooking && (index < numberOfElements)) {
	            if (anEntry.equals(bag[index])) {
	                stillLooking = false;
	                where = index;
	            } // end if
	            index++;
	        } // end for
	        // Assertion: If where > -1, anEntry is in the array bag, and it
	        // equals bag[where]; otherwise, anEntry is not in the array
	        return where;
	    } // end getIndexOf
	    
	 // Other methods and variables...

	    /** Sorts the elements in the bag using the specified comparator. */
	    public void sort(Comparator<? super T> comparator) {
	        // Ensure the bag is initialized
	        checkInitialization();
	        
	        // Sort the elements using the specified comparator
	        Arrays.sort(bag, 0, numberOfElements, comparator);
	    }
	    
	    /** Override the toString() method so that we get a more useful display of
	     *  the contents in the bag.
	     * @return a string representation of the contents of the bag 
	     */
	    public String toString() {
	        String result = "Bag[\n";
	        for (int index = 0; index < numberOfElements; index++) {
	            result += bag[index] + "\n  ";
	        } // end for
	        result += "]";
	        return result;
	    } // end toString}

}
