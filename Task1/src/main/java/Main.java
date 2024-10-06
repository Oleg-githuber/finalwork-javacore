import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String path = "src/main/resources";
        try {
            backUpFolder(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создание резерва в подкаталоге backup
     * @param directory адрес папки
     * @throws IOException исключение ввода / вывода
     */
    public static void backUpFolder(String directory) throws IOException {
        String newFolderName = "/backup";
        File folder = new File(new File(directory).getPath());
        Path newFolder = Path.of(directory + newFolderName);
        File[] files = folder.listFiles();
        if ((!Files.exists(newFolder)) && (files != null) && (files.length != 0)) {
            new File(directory + newFolderName).mkdir();
        }
        for (File file : files) {
            if (file.isFile()) {
                FileInputStream inputStream = new FileInputStream(file);
                FileOutputStream outputStream = new FileOutputStream(newFolder.toString() + "/" + file.toString().substring(directory.length()));
                int character;
                while ((character = inputStream.read()) != -1) {
                    outputStream.write(character);
                }
                inputStream.close();
                outputStream.close();
            }
        }
    }
}
