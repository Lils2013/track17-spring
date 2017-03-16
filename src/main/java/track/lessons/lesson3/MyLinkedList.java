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
        if (head == null) {
            throw new NoSuchElementException();
        }
        final int returnval = head.val;
        head = head.prev;
        if (head != null) {
            head.next = null;
        } else {
            tail = null;
        }
        counter--;
        return returnval;
    }

    @Override
    public void enqueue(int value) {
        add(value);
    }

    @Override
    public int dequeue() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        final int returnval = tail.val;
        tail = tail.next;
        if (tail != null) {
            tail.prev = null;
        } else {
            head = null;
        }
        counter--;
        return returnval;
    }

    @Override
    void add(int item) {
        if (head == null) {
            head = new Node(null,null,item);
            tail = head;
            counter = 1;
        } else {
            head.next = new Node(head,null,item);
            head = head.next;
            counter++;
        }
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx >= counter || idx < 0) {
            throw new NoSuchElementException();
        } else {
            Node current = tail;
            for (int i = 0; i < idx; i++) {
                current = current.next;
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
