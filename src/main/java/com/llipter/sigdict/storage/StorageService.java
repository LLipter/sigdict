package com.llipter.sigdict.storage;

import org.springframework.core.io.Resource;

import javax.crypto.SecretKey;

public interface StorageService {

    void init();

    void deleteAll();

    void store(byte[] data, String storedFilename);

    void remove(String storedFilename);

    Resource loadUnencryptedAsResource(String identifier);

    Resource loadEncryptedAsResource(String identifier, SecretKey key);
}
