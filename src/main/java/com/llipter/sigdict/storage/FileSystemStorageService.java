package com.llipter.sigdict.storage;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;

@Service
public class FileSystemStorageService implements StorageService {

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//        StringUtils.getFilename()
    }

}
