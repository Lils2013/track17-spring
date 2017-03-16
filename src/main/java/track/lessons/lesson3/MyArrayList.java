package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 *
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {

    int[] array;

    public MyArrayList() {
        array = new int[0];
    }

    public MyArrayList(int capacity) {
        array = new int[capacity];
    }

    @Override
    void add(int item) {
        int[] temp = new int[array.length + 1];
        System.arraycopy(array,0,temp,0,array.length);
        temp[temp.length - 1] = item;
        array = temp;
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx >= array.length || idx < 0) {
            throw new NoSuchElementException();
        } else {
            int removed;
            removed = array[idx];
            int[] temp = new int[array.length - 1];
            System.arraycopy(array,0,temp,0,idx);
            System.arraycopy(array, idx + 1, temp, idx, temp.length - idx);
            array = temp;
            return removed;
        }
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx >= array.length || idx < 0) {
            throw new NoSuchElementException();
        } else {
            return array[idx];
        }
    }

    @Override
    int size() {
        return array.length;
    }
}
