package com.llipter.sigdict.storage;

import org.springframework.core.io.Resource;

import java.nio.file.Path;

public interface StorageService {

    void init();

    void deleteAll();

    void store(byte[] data, String storedFilename);

    void remove(String storedFilename);

    Resource loadAsResource(String identifier);
}
