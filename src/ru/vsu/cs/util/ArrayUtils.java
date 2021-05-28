package ru.vsu.cs.util;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ArrayUtils {
    private static final Random RND = new Random();


    public static int[] toPrimitive(Integer[] arr) {
        if (arr == null) {
            return null;
        }
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            // автоматическая распаковка из объекта
            result[i] = arr[i];
        }
        return result;
    }

    public static double[] toPrimitive(Double[] arr) {
        if (arr == null) {
            return null;
        }
        double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            // автоматическая распаковка из объекта
            result[i] = arr[i];
        }
        return result;
    }

    public static char[] toPrimitive(Character[] arr) {
        if (arr == null) {
            return null;
        }
        char[] result = new char[arr.length];
        for (int i = 0; i < arr.length; i++) {
            // автоматическая распаковка из объекта
            result[i] = arr[i];
        }
        return result;
    }

    public static boolean[] toPrimitive(Boolean[] arr) {
        if (arr == null) {
            return null;
        }
        boolean[] result = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            // автоматическая распаковка из объекта
            result[i] = arr[i];
        }
        return result;
    }

    public static Integer[] toObject(int[] arr) {
        if (arr == null) {
            return null;
        }
        Integer[] result = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            // автоматическая упаковка в объект
            result[i] = arr[i];
        }
        return result;
    }

    public static Double[] toObject(double[] arr) {
        if (arr == null) {
            return null;
        }
        Double[] result = new Double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            // автоматическая упаковка в объект
            result[i] = arr[i];
        }
        return result;
    }

    public static Character[] toObject(char[] arr) {
        if (arr == null) {
            return null;
        }
        Character[] result = new Character[arr.length];
        for (int i = 0; i < arr.length; i++) {
            // автоматическая упаковка в объект
            result[i] = arr[i];
        }
        return result;
    }

    public static Boolean[] toObject(boolean[] arr) {
        if (arr == null) {
            return null;
        }
        Boolean[] result = new Boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            // автоматическая упаковка в объект
            result[i] = arr[i];
        }
        return result;
    }

    public static int[] toIntArray(String str) {
        Scanner scanner = new Scanner(str);
        scanner.useLocale(Locale.ROOT);
        scanner.useDelimiter("(\\s|[,;])+");
        List<Integer> list = new ArrayList<>();
        while (scanner.hasNext()) {
            list.add(scanner.nextInt());
        }

        // из List<Integer> можно получить Integer[]
        Integer[] arr = list.toArray(new Integer[0]);
        // Integer[] -> int[]
        return ArrayUtils.toPrimitive(arr);
    }

    public static double[] toDoubleArray(String str) {
        Scanner scanner = new Scanner(str);
        scanner.useLocale(Locale.ROOT);
        scanner.useDelimiter("(\\s|[,;])+");
        List<Double> list = new ArrayList<>();
        while (scanner.hasNext()) {
            list.add(scanner.nextDouble());
        }

        // из List<Double> можно получить Double[]
        Double[] arr = list.toArray(new Double[0]);
        // Double[] -> double[]
        return ArrayUtils.toPrimitive(arr);
    }

    /*
    // "Магия" со Stream

    public static int[] strToIntArray(String str) {
        return Arrays.stream(str.split("(\\s|[,;])+")).mapToInt(Integer::parseInt).toArray();
    }

    public static double[] strToDoubleArray(String str) {
        return Arrays.stream(str.split("(\\s|[,;])+")).mapToDouble(Double::parseDouble).toArray();
    }
    */

    public static String toString(int[] arr, String itemFormat) {
        if (arr == null) {
            return null;
        }
        if (itemFormat == null || itemFormat.length() == 0) {
            itemFormat = "%s";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(String.format(Locale.ROOT, itemFormat, arr[i]));
        }
        return sb.toString();
    }

    public static String toString(int[] arr) {
        return toString(arr, null);
    }

    public static String toString(double[] arr, String itemFormat) {
        if (arr == null) {
            return null;
        }
        if (itemFormat == null || itemFormat.length() == 0) {
            itemFormat = "%s";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(String.format(Locale.ROOT, itemFormat, arr[i]));
        }
        return sb.toString();
    }

    public static String toString(double[] arr) {
        return toString(arr, null);
    }

    /*
    // Вариант с ипользованием класса Arrays

    public static String toString(int[] arr) {
        if (arr == null) {
            return null;
        }

        String str = Arrays.toString(arr);
        return str.substring(1, str.length() - 1);
    }

    public static String toString(double[] arr) {
        if (arr == null) {
            return null;
        }

        String str = Arrays.toString(arr);
        return str.substring(1, str.length() - 1);
    }
    */

    public static int[] readIntArrayFromConsole(String arrName) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                if (arrName == null || arrName.length() == 0) {
                    arrName = "";
                } else {
                    arrName = " " + arrName;
                }
                System.out.printf("Введите массив%s:%n", arrName);
                String line = scanner.nextLine();
                return toIntArray(line);
            }
            catch(Exception e) {
                System.out.print("Вы ошиблись, попробуйте еще раз! ");
            }
        }
    }

    public static int[] readIntArrayFromConsole() {
        return readIntArrayFromConsole(null);
    }

    public static double[] readDoubleArrayFromConsole(String arrName) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                if (arrName == null || arrName.length() == 0) {
                    arrName = "";
                } else {
                    arrName = " " + arrName;
                }
                System.out.printf("Введите массив%s:%n", arrName);
                String line = scanner.nextLine();
                return toDoubleArray(line);
            }
            catch(Exception e) {
                System.out.print("Вы ошиблись, попробуйте еще раз! ");
            }
        }
    }

    public static double[] readDoubleArrayFromConsole() {
        return readDoubleArrayFromConsole(null);
    }

    /*
    // Вариант с ипользованием дженериков и функциональных интерфейсов (Java 8 и старше)

    public static <T> T readFromConsole(String name, java.util.function.Function<String, T> converter) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.printf("Введите %s:%n  ", name);
                String line = scanner.nextLine();
                return converter.apply(line);
            }
            catch(Exception e) {
                System.out.print("Вы ошиблись, попробуйте еще раз! ");
            }
        }
    }

    private static <T> T readArrayFromConsole(String arrName, java.util.function.Function<String, T> strToArrConverter) {
        return readFromConsole(
            "массив" + ((arrName == null || arrName.length() == 0) ? "" : " " + arrName),
            strToArrConverter
        );
    }

    public static int[] readIntArrayFromConsole(String arrName) {
        return readArrayFromConsole(arrName, ArrayUtils::toIntArray);
    }

    public static double[] readDoubleArrayFromConsole(String arrName) {
        return readArrayFromConsole(arrName, ArrayUtils::toDoubleArray);
    }
    */


    /**
     * Конвертация массива строк в двухмерный массив чисел int[][]
     */
    public static int[][] toIntArray2(String[] lines) {
        int[][] arr2 = new int[lines.length][];
        for (int r = 0; r < lines.length; r++) {
            arr2[r] = toIntArray(lines[r]);
        }
        return arr2;
    }

    /**
     * Конвертация массива строк в двухмерный массив чисел double[][]
     */
    public static double[][] toDoubleArray2(String[] lines) {
        double[][] arr2 = new double[lines.length][];
        for (int r = 0; r < lines.length; r++) {
            arr2[r] = toDoubleArray(lines[r]);
        }
        return arr2;
    }

    /**
     * Чтение массива строк из консоли, признак окончания - пустая строка
     */
    public static String[] readLinesFromConsole() {
        Scanner scanner = new Scanner(System.in);
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line == null || line.trim().length() == 0)
                break;
            lines.add(line);
        }
        return lines.toArray(new String[0]);
    }

    /**
     * Чтение двухмерного массива int[][] с консоли;
     * checkMatrix - задает режим, при котором,
     * если строки содержат разное кол-во элементот, то
     * это считается ошибкой и предлагается повторить ввод
     */
    public static int[][] readIntArray2FromConsole(String arrName, boolean checkMatrix) {
        while (true) {
            try {
                if (arrName == null || arrName.length() == 0) {
                    arrName = "";
                } else {
                    arrName = " " + arrName;
                }
                System.out.printf("Введите двумерный массив%s:%n", arrName);
                int[][] arr2 = toIntArray2(readLinesFromConsole());
                if (checkMatrix) {
                    for (int i = 1; i < arr2.length; i++)
                        if (arr2[i].length != arr2[0].length) {
                            throw new Exception("Строки с разным кол-вом элементов");
                        }
                }
                return arr2;
            }
            catch(Exception e) {
                System.out.print("Вы ошиблись, попробуйте еще раз! ");
            }
        }
    }

    /**
     * Чтение двухмерного массива int[][] с консоли;
     * checkMatrix - задает режим, при котором,
     * если строки содержат разное кол-во элементот, то
     * это считается ошибкой и предлагается повторить ввод
     */
    public static int[][] readIntArray2FromConsole(boolean checkMatrix) {
        return readIntArray2FromConsole(null, checkMatrix);
    }

    /**
     * Чтение двухмерного массива int[][] с консоли;
     * checkMatrix - задает режим, при котором,
     * если строки содержат разное кол-во элементот, то
     * это считается ошибкой и предлагается повторить ввод
     */
    public static int[][] readIntArray2FromConsole() {
        return readIntArray2FromConsole(false);
    }

    /* Чтение двухмерного массива double[][] с консоли;
     * checkMatrix - задает режим, при котором,
     * если строки содержат разное кол-во элементот, то
     * это считается ошибкой и предлагается повторить ввод
     */
    public static double[][] readDoubleArray2FromConsole(String arrName, boolean checkMatrix) {
        while (true) {
            try {
                if (arrName == null || arrName.length() == 0) {
                    arrName = "";
                } else {
                    arrName = " " + arrName;
                }
                System.out.printf("Введите двумерный массив%s:%n", arrName);
                double[][] arr2 = toDoubleArray2(readLinesFromConsole());
                if (checkMatrix) {
                    for (int i = 1; i < arr2.length; i++)
                        if (arr2[i].length == arr2[0].length) {
                            throw new Exception("Строки с разным кол-вом элементов");
                        }
                }
                return arr2;
            }
            catch(Exception e) {
                System.out.print("Вы ошиблись, попробуйте еще раз! ");
            }
        }
    }

    /* Чтение двухмерного массива int[][] с консоли;
     * checkMatrix - задает режим, при котором,
     * если строки содержат разное кол-во элементот, то
     * это считается ошибкой и предлагается повторить ввод
     */
    public static double[][] readDoubleArray2FromConsole(boolean checkMatrix) {
        return readDoubleArray2FromConsole(null, checkMatrix);
    }

    /* Чтение двухмерного массива int[][] с консоли;
     * checkMatrix - задает режим, при котором,
     * если строки содержат разное кол-во элементот, то
     * это считается ошибкой и предлагается повторить ввод
     */
    public static double[][] readDoubleArray2FromConsole() {
        return readDoubleArray2FromConsole(false);
    }

    public static String toString(int[][] arr2, String itemFormat) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < arr2.length; r++) {
            if (r > 0) {
                sb.append(System.lineSeparator());
            }
            sb.append(toString(arr2[r], itemFormat));
        }
        return sb.toString();
    }

    public static String toString(int[][] arr2) {
        return toString(arr2, null);
    }

    public static String toString(double[][] arr2, String itemFormat) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < arr2.length; r++) {
            if (r > 0) {
                sb.append(System.lineSeparator());
            }
            sb.append(toString(arr2[r], itemFormat));
        }
        return sb.toString();
    }

    public static String toString(double[][] arr2) {
        return toString(arr2, null);
    }

    /**
     * Чтение всего текстового файла в массив строк
     */
    public static String[] readLinesFromFile(String fileName) throws FileNotFoundException {
        List<String> lines;
        try (Scanner scanner = new Scanner(new File(fileName), "UTF-8")) {
            lines = new ArrayList<>();
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
            // обязательно, чтобы закрыть открытый файл
        }
        return lines.toArray(new String[0]);
    }

    /**
     * Чтение массива int[] из первой строки текстового файла
     */
    public static int[] readIntArrayFromFile(String fileName) {
        try {
            return toIntArray(readLinesFromFile(fileName)[0]);
        }
        catch(FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Чтение массива double[] из первой строки текстового файла
     */
    public static double[] readDoubleArrayFromFile(String fileName) {
        try {
            return toDoubleArray(readLinesFromFile(fileName)[0]);
        }
        catch(FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Чтение двухмерного массива из текстового файла
     */
    public static int[][] readIntArray2FromFile(String fileName) {
        try {
            return toIntArray2(readLinesFromFile(fileName));
        }
        catch(FileNotFoundException e) {
            return null;
        }
    }

    public static double[][] readDoubleArray2FromFile(String fileName) {
        try {
            return toDoubleArray2(readLinesFromFile(fileName));
        }
        catch(FileNotFoundException e) {
            return null;
        }
    }

    public static void writeArrayToFile(String fileName, int[] arr, String itemFormat)
            throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(toString(arr, itemFormat));
        }
    }

    public static void writeArrayToFile(String fileName, int[] arr)
            throws FileNotFoundException {
        writeArrayToFile(fileName, arr, null);
    }

    public static void writeArrayToFile(String fileName, double[] arr, String itemFormat)
            throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(toString(arr, itemFormat));
        }
    }

    public static void writeArrayToFile(String fileName, double[] arr)
            throws FileNotFoundException {
        writeArrayToFile(fileName, arr, null);
    }

    public static void writeArrayToFile(String fileName, int[][] arr2, String itemFormat)
            throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(toString(arr2, itemFormat));
        }
    }

    public static void writeArrayToFile(String fileName, int[][] arr2)
            throws FileNotFoundException {
        writeArrayToFile(fileName, arr2, null);
    }

    public static void writeArrayToFile(String fileName, double[][] arr2, String itemFormat)
            throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(toString(arr2, itemFormat));
        }
    }

    public static void writeArrayToFile(String fileName, double[][] arr2)
            throws FileNotFoundException {
        writeArrayToFile(fileName, arr2, null);
    }

    /**
     * Cоздание одномерного массива целых чисел, заполненного случайными числами
     * @param length Кол-во элементов в массиве
     * @param minValue Минимальное значение для случайных чисел (включая)
     * @param maxValue Максимальное значение (не включая)
     * @return Массив int[]
     */
    public static int[] createRandomIntArray(int length, int minValue, int maxValue) {
        int[] arr = new int[length];
        for (int i = 0; i < length; i++)
            arr[i] = minValue + RND.nextInt(maxValue - minValue);
        return arr;
    }

    /**
     * @see #createRandomIntArray(int, int, int)
     */
    public static int[] createRandomIntArray(int length, int maxValue) {
        return createRandomIntArray(length, 0, maxValue);
    }

    /**
     * Cоздание двухмерного массива целых чисел, заполненного случайными числами
     * @param rowCount Кол-во сток в двумерном массиве
     * @param colCount Кол-во столбцов (элементов в каждой строке)
     * @param minValue Минимальное значение для случайных чисел (включая)
     * @param maxValue Максимальное значение (не включая)
     * @return Массив int[][]
     */
    public static int[][] createRandomIntMatrix(int rowCount, int colCount, int minValue, int maxValue) {
        int[][] matrix = new int[rowCount][];
        for (int r = 0; r < rowCount; r++)
            matrix[r] = createRandomIntArray(colCount, minValue, maxValue);
        return matrix;
    }

    /**
     * @see #createRandomIntMatrix(int, int, int, int)
     */
    public static int[][] createRandomIntMatrix(int rowCount, int colCount, int maxValue) {
        return createRandomIntMatrix(rowCount, colCount, 0, maxValue);
    }
}
