package mr.demonid.pk8000.ru.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Определение типов файлов.
 * Сделана вручную, поскольку MIME легко подделать,
 * а тянуть сторонние либы ради такой ерунды не хочется.
 * Поддерживает:
 *      PNG, JPG, GIF
 *      ZIP, RAR, 7Z, GZIP
 */
public class FileType {


    /**
     * Проверка, что файл является PNG, JPG или GIF.
     */
    public static boolean isImage(Path file) {
        byte[] header = readHeader(file, 8);
        return isPng(header) || isJpg(header) || isGif(header);
    }

    /**
     * Проверка, что файл является архивом (zip, 7z, rar, gzip)
     */
    public static boolean isArchive(Path file) {
        byte[] header = readHeader(file, 8);
        return isZip(header) || isRar(header) || is7z(header) || isGzip(header);
    }


    /*
        Читаем заголовок файла.
     */
    private static byte[] readHeader(Path file, int len) {
        try (InputStream is = Files.newInputStream(file)) {
            byte[] buffer = new byte[len];
            int bytesRead = is.read(buffer);
            if (bytesRead < len && bytesRead > 0) {
                byte[] truncated = new byte[bytesRead];
                System.arraycopy(buffer, 0, truncated, 0, bytesRead);
                return truncated;
            } else {
                buffer = new byte[len];
            }
            return buffer;
        } catch (IOException e) {
            return new byte[len];
        }
    }

    /*
        Проверки по сигнатурам.
     */
    private static boolean isPng(byte[] b) {
        byte[] sig = {(byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
        return startsWith(b, sig);
    }

    private static boolean isJpg(byte[] b) {
        byte[] sig = {(byte)0xFF, (byte)0xD8, (byte)0xFF};
        return startsWith(b, sig);
    }

    private static boolean isGif(byte[] b) {
        byte[] sig = {0x47, 0x49, 0x46, 0x38};
        return startsWith(b, sig);
    }

    private static boolean isZip(byte[] b) {
        byte[] sig = {0x50, 0x4B, 0x03, 0x04};
        return startsWith(b, sig);
    }

    private static boolean isRar(byte[] b) {
        byte[] rar4 = {0x52, 0x61, 0x72, 0x21, 0x1A, 0x07, 0x00};
        byte[] rar5 = {0x52, 0x61, 0x72, 0x21, 0x1A, 0x07, 0x01, 0x00};
        return startsWith(b, rar4) || startsWith(b, rar5);
    }

    private static boolean is7z(byte[] b) {
        byte[] sig = {0x37, 0x7A, (byte)0xBC, (byte)0xAF, 0x27, 0x1C};
        return startsWith(b, sig);
    }

    private static boolean isGzip(byte[] b) {
        byte[] sig = {0x1F, (byte)0x8B, 0x08};
        return startsWith(b, sig);
    }

    /*
        Определение вхождения массива sig[] в массив data[], с первой позиции.
     */
    private static boolean startsWith(byte[] data, byte[] sig) {
        if (data == null || data.length < sig.length) return false;
        for (int i = 0; i < sig.length; i++) {
            if (data[i] != sig[i]) return false;
        }
        return true;
    }

}
