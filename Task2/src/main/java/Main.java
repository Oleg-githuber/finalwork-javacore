import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args) {
        final int[] arr = {
                2, 2, 1,
                0, 1, 0,
                1, 0, 0
        };
        final int size = 3;
        String filename = "src/main/resources/file.txt";

        byte[] bytes = integersToBytes(arr, size);
        try {
            writeFile(filename, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            int number = reedFile(filename, size);
            printFromInt(number, size, arr.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Преобразование int[9] в byte[3]
     * @param arr массив int
     * @param size ожидаемый размер массива байтов
     * @return массив байтов
     */
    public static byte[] integersToBytes(int[] arr, int size) {
        int[] shortArr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            shortArr[i / (arr.length / size)] = shortArr[i / (arr.length / size)] + (arr[i] << (2 * (i % (arr.length / size))));
        }

        char[] chars = new char[size];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) shortArr[i];
        }

        byte[] bytes = new byte[size];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) chars[i];
        }

        return bytes;
    }

    /**
     * Запись в файл
     * @param filename полное имя файла
     * @param bytes массив байтов
     * @throws IOException исключение ввода / вывода
     */
    public static void writeFile(String filename, byte[] bytes) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filename);
        for (byte b : bytes) {
            outputStream.write(b);
        }
        outputStream.close();
    }

    /**
     * Чтение из файла
     * @param filename имя файла
     * @param size размер массива (количество записаных в файл байт)
     * @return целое число
     * @throws IOException исключение ввода / вывода
     */
    public static int reedFile(String filename, int size) throws IOException {
        FileInputStream inputStream = new FileInputStream(filename);
        int result = 0;
        for (int i = 0; i < size; i++) {
            result = result + (inputStream.read() << (i * 8));
        }
        inputStream.close();
        return result;
    }

    /**
     * Преобразование числа в массив
     * @param number число
     * @param size размер промежуточного массива
     * @param fullSize размер массива
     * @return возвращаемый массив
     */
    public static int[] convertArray(int number, int size, int fullSize) {
        int[] arr = new int[size];
        int[] fullArr = new int[fullSize];
        for (int i = 0; i < size; i++) {
            arr[i] = number % 256;
            number /= 256;
            for (int j = i * size; j < (i * size + fullSize / size); j++) {
                int temp = arr[i] - ((arr[i] >> 2) << 2);
                arr[i] = (arr[i] >> (2));
                fullArr[j] = temp;
            }
        }
        return fullArr;
    }

    /**
     * Преобразование массива чисел в массив символов
     * @param fullArr массив чисел
     * @return массив символов
     */
    public static char[] convertIntToChar(int[] fullArr) {
        int fullSize = fullArr.length;
        char[] chars = new char[fullSize];
        for (int i = 0; i < fullSize; i++) {
            switch (fullArr[i]) {
                case 0:
                    chars[i] = '-';
                    break;
                case 1:
                    chars[i] = 'X';
                    break;
                case 2:
                    chars[i] = 'O';
                    break;
                default:
                    chars[i] = 'r';
            }
        }
        return chars;
    }

    /**
     * Печать игрового поля из целого числа
     * @param number целое число
     * @param size количество используемых байт
     * @param fullSize количество игровых клеток
     */
    public static void printFromInt(int number, int size, int fullSize) {

        int[] fullArr = convertArray(number, size, fullSize);
        char[] chars = convertIntToChar(fullArr);
        for (int i = 0; i < size; i++) {
            for (int j = i * size; j < i * size + fullSize / size; j++) {
                System.out.print(chars[j]);
                System.out.print('\t');
            }
            System.out.println();
        }
    }
}
