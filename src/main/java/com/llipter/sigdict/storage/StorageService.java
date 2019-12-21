package com.llipter.sigdict.storage;

import java.io.IOException;

public interface StorageService {

    void init();

    void deleteAll();

    void store(byte[] data, String storedFilename) throws IOException;
}
