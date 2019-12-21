package com.llipter.sigdict.storage;

public interface StorageService {

    void init();

    void deleteAll();

    void store(byte[] data, String storedFilename);

    void remove(String storedFilename);
}
