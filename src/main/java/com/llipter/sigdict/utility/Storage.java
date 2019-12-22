package com.llipter.sigdict.utility;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.exception.InternalServerException;
import com.llipter.sigdict.security.SymmetricEncryption;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Storage {

    /**
     * Folder location for storing files
     */
    private static final Path rootLocation = Paths.get("upload-dir");

    public static void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new InternalServerException("Could not initialize storage", e);
        }
    }

    public static void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException(ErrorMessage.CANNOT_DELETE_FILE, e);
        }
    }

    public static void store(byte[] data, String storedFilename) {
        try {
            Files.write(rootLocation.resolve(storedFilename), data, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException(ErrorMessage.CANNOT_SAVE_FILE, e);
        }
    }

    public static void remove(String storedFilename) {
        try {
            FileSystemUtils.deleteRecursively(rootLocation.resolve(storedFilename));
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException(ErrorMessage.CANNOT_DELETE_FILE, e);
        }
    }

    public static Resource loadUnencryptedAsResource(String identifier) {
        try {
            Path file = rootLocation.resolve(identifier);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new InternalServerException(ErrorMessage.FILE_NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new InternalServerException("Could not read file: " + identifier, e);
        }
    }

    public static Resource loadEncryptedAsResource(String identifier, SecretKey key) {
        byte[] encryptedData = loadFileAsBytes(identifier);
        byte[] unencryptedData = SymmetricEncryption.decrypt(key, encryptedData);
        return new ByteArrayResource(unencryptedData);
    }

    public static byte[] loadFileAsBytes(String identifier) {
        try {
            Resource resource = loadUnencryptedAsResource(identifier);
            InputStream inputStream = resource.getInputStream();
            byte[] data = IOUtils.toByteArray(inputStream);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("Could not read file: " + identifier, e);
        }
    }
}
