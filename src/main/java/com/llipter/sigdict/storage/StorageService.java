package com.llipter.sigdict.storage;

import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.security.SignatureType;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void store(MultipartFile file, User user, SignatureType signatureType, boolean encrypted);
}
