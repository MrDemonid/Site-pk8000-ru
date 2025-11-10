package mr.demonid.pk8000.ru.services;


import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.util.PathTool;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@Log4j2
public class ZipService {


    private PathTool pathTool;


    /**
     * Создает архив и сохраняет его в файл.
     *
     * @param zipName  Имя создаваемого архива.
     * @param rootBase Корневой каталог, откуда берутся файлы для архива
     * @param files    Список файлов (если List.of(Path.of("")), то архивируется весь каталог).
     */
    public void toFile(String zipName, Path rootBase, List<Path> files) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            makeZipToStream(rootBase, files, baos);
            Path outputFile = Path.of(zipName).normalize();
            Files.write(outputFile, baos.toByteArray());
        }
    }


    /**
     * Создает архив и возвращает его в виде массива байт.
     * Архивирует всё содержимое переданной папки, включая вложенные подкаталоги.
     *
     * @param rootBase Корневой каталог, откуда берутся файлы для архива
     * @return null - в случае ошибки.
     */
    public byte[] makeZipToBytes(Path rootBase) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            makeZipToStream(rootBase, null, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Создает архив и возвращает его в виде массива байт.
     *
     * @param rootBase Корневой каталог, откуда берутся файлы для архива
     * @param files    Список файлов (если List.of(Path.of("")), то архивируется весь каталог).
     * @return null - в случае ошибки.
     */
    public byte[] makeZipToBytes(Path rootBase, List<Path> files) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            makeZipToStream(rootBase, files, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Создает архив и отправляет его в стрим.
     *
     * @param rootBase     Корневой каталог, откуда берутся файлы для архива
     * @param files        Список файлов (если List.of(Path.of("")), то архивируется весь каталог).
     * @param outputStream Куда отдаем архив.
     */
    public void makeZipToStream(Path rootBase, List<Path> files, OutputStream outputStream) {
        if (rootBase == null || outputStream == null) {
            return;
        }
        Path root = (rootBase.isAbsolute() ? rootBase : pathTool.getRootPath().resolve(rootBase)).normalize();

        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            if (files == null || files.isEmpty()) {
                addDirectory(root, root, zos);
            } else {
                files.forEach(e -> {
                    // переводим путь к файлу в абсолютный
                    Path path = PathUtil.toSystemPath(e.isAbsolute() ? e : root.resolve(e).normalize());

                    // добавляем в архив
                    if (Files.exists(path)) {
                        try {
                            if (Files.isDirectory(path)) {
                                addDirectory(root, path, zos);
                            } else {
                                addToZip(path, toEntryName(root, path), zos);
                            }
                        } catch (Exception ex) {
                            log.error("Failed to add entry to zip: {}", ex.getMessage());
                        }
                    }
                });
            }
        } catch (Exception e) {
            log.error("Failed to make zip: {}", e.getMessage());
        }
    }


    /**
     * Добавление всех файлов из каталога, с сохранением относительных путей по отношению к rootDir.
     *
     * @param rootDir   Корневой каталог (абсолютный путь).
     * @param sourceDir Каталог, из которого добавляются файлы (абсолютный или относительный путь).
     * @param zos       Стрим, куда отправляем все найденные файлы.
     */
    public void addDirectory(Path rootDir, Path sourceDir, ZipOutputStream zos) throws IOException {
        Path root = (rootDir.isAbsolute() ? rootDir : pathTool.getRootPath().resolve(rootDir)).normalize();
        Path source = PathUtil.toSystemPath(sourceDir.isAbsolute() ? sourceDir : root.resolve(sourceDir).normalize());

        if (Files.exists(source)) {
            try (var paths = Files.walk(source)) {
                for (Path file : (Iterable<Path>) paths.filter(Files::isRegularFile)::iterator) {
                    addToZip(file, toEntryName(root, file), zos);
                }
            }
        }
    }


    /**
     * Добавляет один файл в ZIP-архив.
     *
     * @param filePath     Путь до файла (включая сам файл).
     * @param zipEntryName Имя в zip-архиве (с относительным путем до файла).
     * @param zos          Стрим для вывода файла.
     */
    private void addToZip(Path filePath, String zipEntryName, ZipOutputStream zos) throws IOException {
        try (InputStream in = Files.newInputStream(filePath)) {
            ZipEntry entry = new ZipEntry(zipEntryName);
            zos.putNextEntry(entry);
            in.transferTo(zos);
            zos.closeEntry();
        }
    }


    /**
     * Получает файл с путем относительно root. Переводит путь в UNIX, как принято для zip.
     *
     * @param root Корень собираемого архива.
     * @param file Файл, с путем или без.
     */
    private String toEntryName(Path root, Path file) {
        try {
            return root.relativize(file.normalize()).toString().replace("\\", "/");
        } catch (IllegalArgumentException ex) {
            // файл вне root, просто возвращаем его имя.
            log.error("File outside of root: {}", file);
            return file.getFileName().toString();
        }
    }

}
