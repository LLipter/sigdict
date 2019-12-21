package com.llipter.sigdict.storage;

import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.security.SignatureType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    void init();

    void deleteAll();

    void store(byte[] data, String storedFilename) throws IOException;
}
