package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Односвязный список
 */
public class MyLinkedList extends List implements Stack, Queue {

    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */
    private static class Node {
        Node prev;
        Node next;
        int val;

        Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    private Node head;
    private Node tail;
    private int counter = 0;

    @Override
    public void push(int value) {
        add(value);
    }

    @Override
    public int pop() {
        return remove(counter - 1);
    }

    @Override
    public void enqueue(int value) {
        add(value);
    }

    @Override
    public int dequeue() {
        return remove(0);
    }

    @Override
    void add(int item) {
        if (head == null) {
            head = new Node(null, null, item);
            tail = head;
            counter = 1;
        } else {
            head.next = new Node(head, null, item);
            head = head.next;
            counter++;
        }
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        Node current = null;
        if (idx >= counter || idx < 0) {
            throw new NoSuchElementException();
        } else if (idx < 0.5 * counter) {
            current = tail;
            for (int i = 0; i < idx; i++) {
                current = current.next;
            }
        } else {
            current = head;
            for (int i = 0; i < counter - idx - 1; i++) {
                current = current.prev;
            }
        }
        final int returnval = current.val;
        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            tail = current.next;
            if (tail != null) {
                tail.prev = null;
            } else {
                head = null;
            }
        }
        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            head = current.prev;
            if (head != null) {
                head.next = null;
            } else {
                tail = null;
            }
        }
        counter--;
        return returnval;

    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx >= counter || idx < 0) {
            throw new NoSuchElementException();
        } else {
            Node current = tail;
            for (int i = 0; i < idx; i++) {
                current = current.next;
            }
            int returnval = current.val;
            return returnval;
        }
    }

    @Override
    int size() {
        return counter;
    }
}
