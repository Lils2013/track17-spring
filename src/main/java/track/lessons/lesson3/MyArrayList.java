package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * <p>
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {

    int[] array;
    int length;
    static final int DEFAULT_CAPACITY = 10;

    public MyArrayList() {
        array = new int[DEFAULT_CAPACITY];
    }

    public MyArrayList(int capacity) {
        array = new int[capacity];
    }

    @Override
    void add(int item) {
        if (length > 0.5 * array.length || array.length == 0) {
            int[] temp = new int[2 * length + 1];
            System.arraycopy(array, 0, temp, 0, length);
            temp[length] = item;
            array = temp;
        }
        array[length] = item;
        length++;
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx >= length || idx < 0) {
            throw new NoSuchElementException();
        }
        if (length < 0.25 * array.length) {
            int[] temp = new int[length * 2];
            System.arraycopy(temp, 0, array, 0, length);
        }
        int removed;
        removed = array[idx];
        System.arraycopy(array, idx + 1, array, idx, length - idx - 1);
        length--;
        return removed;
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx >= length || idx < 0) {
            throw new NoSuchElementException();
        } else {
            return array[idx];
        }
    }

    @Override
    int size() {
        return length;
    }
}
