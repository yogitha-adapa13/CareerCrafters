package MyCollections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class AppPriorityQueue<T> implements MyQueue<T> {
    private static final int DEFAULT_CAPACITY = 15;
    private T[] array;
    private int size;
    private Comparator<? super T> comparator;

    @SuppressWarnings("unchecked")

	/**	
	 * A priority queue implementation backed by an array.
	 *
	 * @param <T> the type of elements stored in the priority queue
	 */
    public AppPriorityQueue(Comparator<? super T> comparator) {
        this.comparator = comparator;
        array = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    /**
     * Adds the specified element to the priority queue.
     *
     * @param item the element to add
     */
    public void enqueue(T item) {
        if (size == array.length) {
            resize();
        }
        array[size++] = item;
        bubbleUp(size - 1);
    }

    /**
     * Removes and returns the highest priority element from the priority queue.
     *
     * @return the highest priority element
     * @throws NoSuchElementException if the priority queue is empty
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T item = peek();
        swap(0, size - 1);
        array[--size] = null;
        bubbleDown(0);
        return item;
    }

    /**
     * Returns the highest priority element in the priority queue without removing it.
     *
     * @return the highest priority element
     * @throws NoSuchElementException if the priority queue is empty
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return array[0];
    }

    /**
     * Returns the number of elements in the priority queue.
     *
     * @return the number of elements
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the priority queue is empty.
     *
     * @return true if the priority queue is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Resizes the internal array when it becomes full.
     */
    private void resize() {
        int newSize = array.length * 2;
        T[] newArray = Arrays.copyOf(array, newSize);
        array = newArray;
    }

    /**
     * Moves the element at the specified index up to maintain the heap property.
     *
     * @param index the index of the element to move up
     */
    private void bubbleUp(int index) {
        while (index > 0 && comparator.compare(array[parent(index)], array[index]) < 0) {
            swap(parent(index), index);
            index = parent(index);
        }
    }

    /**
     * Moves the element at the specified index down to maintain the heap property.
     *
     * @param index the index of the element to move down
     */
    private void bubbleDown(int index) {
        while (leftChild(index) < size) {
            int maxIndex = leftChild(index);
            int rightChild = rightChild(index);
            if (rightChild < size && comparator.compare(array[rightChild], array[maxIndex]) > 0) {
                maxIndex = rightChild;
            }
            if (comparator.compare(array[index], array[maxIndex]) >= 0) {
                break;
            }
            swap(index, maxIndex);
            index = maxIndex;
        }
    }

    /**
     * Swaps the elements at the specified indices in the internal array.
     *
     * @param i the index of the first element
     * @param j the index of the second element
     */
    private void swap(int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Calculates the parent index of the specified index.
     *
     * @param index the index of the child element
     * @return the index of the parent element
     */
    private int parent(int index) {
        return (index - 1) / 2;
    }

    /**
     * Calculates the left child index of the specified index.
     *
     * @param index the index of the parent element
     * @return the index of the left child element
     */
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    /**
     * Calculates the right child index of the specified index.
     *
     * @param index the index of the parent element
     * @return the index of the right child element
     */
    private int rightChild(int index) {
        return 2 * index + 2;
    }
    
    /**
     * Traverses through the elements in the priority queue and prints them out.
     *
     * @param priorityQueue the priority queue to traverse
     * @param <T> the type of elements stored in the priority queue
     */
    public void traverse() {
        System.out.println("Traversing the priority queue:");
        for (T element : this.toArray()) {
            System.out.println(element);
        }
    }

    /**
     * Returns an array containing all of the elements in this priority queue.
     * The elements are returned in ascending order according to the comparator.
     *
     * @return an array containing all of the elements in this priority queue
     */
    public T[] toArray() {
        T[] result = Arrays.copyOf(array, size);
        Arrays.sort(result, comparator);
        return result;
    }
}
