package track.lessons.lesson1;

import java.io.File;
import java.util.Scanner;

/**
 * Задание 1: Реализовать два метода
 *
 * Формат файла: текстовый, на каждой его строке есть (или/или)
 * - целое число (int)
 * - текстовая строка
 * - пустая строка (пробелы)
 *
 *
 * Пример файла - words.txt в корне проекта
 *
 * ******************************************************************************************
 *  Пожалуйста, не меняйте сигнатуры методов! (название, аргументы, возвращаемое значение)
 *
 *  Можно дописывать новый код - вспомогательные методы, конструкторы, поля
 *
 * ******************************************************************************************
 *
 */
public class CountWords {

    /**
     * Метод на вход принимает объект File, изначально сумма = 0
     * Нужно пройти по всем строкам файла, и если в строке стоит целое число,
     * то надо добавить это число к сумме
     * @param file - файл с данными
     * @return - целое число - сумма всех чисел из файла
     */
    public long countNumbers(File file) throws Exception {
        Scanner scan = new Scanner(file);
        int count = 0;
        while (scan.hasNext()) {
            if (scan.hasNextInt()) {
                count += scan.nextInt();
                scan.nextLine();
            } else {
                scan.nextLine();
            }
        }
        return count;
    }


    /**
     * Метод на вход принимает объект File, изначально результат= ""
     * Нужно пройти по всем строкам файла, и если в строка не пустая и не число
     * то надо присоединить ее к результату через пробел
     * @param file - файл с данными
     * @return - результирующая строка
     */
    public String concatWords(File file) throws Exception {
        Scanner scan = new Scanner(file);
        StringBuilder strbld = new StringBuilder();
        while (scan.hasNext()) {
            if (scan.hasNextInt()) {
                scan.nextLine();
            } else {
                String next = scan.nextLine();
                if (!next.equals("")) {
                    strbld.append(next + " ");
                }
            }
        }
        return strbld.toString().trim();
    }

}
