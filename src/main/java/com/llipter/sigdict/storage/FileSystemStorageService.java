package com.llipter.sigdict.storage;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
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
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
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

    }

}
