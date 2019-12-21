package com.llipter.sigdict.storage;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.exception.InternalServerException;
import com.llipter.sigdict.security.SymmetricEncryption;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class FileSystemStorageService implements StorageService {


    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new InternalServerException("Could not initialize storage", e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException(ErrorMessage.CANNOT_DELETE_FILE, e);
        }
    }

    @Override
    public void store(byte[] data, String storedFilename) {
        try {
            Files.write(this.rootLocation.resolve(storedFilename), data, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException(ErrorMessage.CANNOT_SAVE_FILE, e);
        }
    }

    @Override
    public void remove(String storedFilename) {
        try {
            FileSystemUtils.deleteRecursively(this.rootLocation.resolve(storedFilename));
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException(ErrorMessage.CANNOT_DELETE_FILE, e);
        }
    }

    @Override
    public Resource loadUnencryptedAsResource(String identifier) {
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

    @Override
    public Resource loadEncryptedAsResource(String identifier, SecretKey key) {
        try {
            Resource resource = this.loadUnencryptedAsResource(identifier);
            InputStream inputStream = resource.getInputStream();
            byte[] encryptedData = IOUtils.toByteArray(inputStream);
            byte[] unencryptedData = SymmetricEncryption.decrypt(key, encryptedData);
            return new ByteArrayResource(unencryptedData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("Could not read file: " + identifier, e);
        }
    }

}
