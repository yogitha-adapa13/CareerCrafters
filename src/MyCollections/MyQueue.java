package MyCollections;

public interface MyQueue<T> {

    /**
     * Adds an item to the queue.
     * @param item the item to be added to the queue
     */
    public void enqueue(T item);

    /**
     * Removes and returns the item at the front of the queue.
     * @return the item at the front of the queue
     * @throws NoSuchElementException if the queue is empty
     */
    public T dequeue();

    /**
     * Returns the item at the front of the queue without removing it.
     * @return the item at the front of the queue
     * @throws NoSuchElementException if the queue is empty
     */
    public T peek();

    /**
     * Returns the number of items in the queue.
     * @return the number of items in the queue
     */
    public int size();

    /**
     * Checks if the queue is empty.
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty();
    
    /**
     * Traverses through the elements in the priority queue and prints them out.
     *
     * @param priorityQueue the priority queue to traverse
     * @param <T> the type of elements stored in the priority queue
     */
    public void traverse();
    
    /** Retrieves all entries that are in this bag.
     * @return A newly allocated array of all the entries in the bag.
     * Note: If the bag is empty, the returned array is empty. 
     */
    public T[] toArray();
}
