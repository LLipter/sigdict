package com.llipter.sigdict.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void store(MultipartFile file);
}
