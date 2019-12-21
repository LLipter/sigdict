package com.llipter.sigdict.entity;


import com.llipter.sigdict.security.DigitalSignature;
import com.llipter.sigdict.security.SignatureType;
import com.llipter.sigdict.utility.Utility;

import javax.persistence.*;
import java.io.File;
import java.security.PrivateKey;
import java.sql.Timestamp;

@Entity
public class UploadedFile {
    private static int STORED_FILENAME_LENGTH = 16;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer fid;

    private String filename;

    private String identifier;

    // DSA = 62
    // RSA = 256
    @Column(length = 256)
    private byte[] signature;

    @Basic
    private Timestamp uploadTime;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private SignatureType signatureType;

    private boolean encrypted;

    public UploadedFile() {
    }

    public UploadedFile(String filename,
                        byte[] data,
                        SignatureType signatureType,
                        PrivateKey privateKey,
                        boolean encrypted) {
        this.setFilename(filename);
        this.setSignature(DigitalSignature.sign(signatureType, privateKey, data));
        this.setUploadTime(new Timestamp(System.currentTimeMillis()));
        this.setSignatureType(signatureType);
        this.setIdentifier(generateRandomIdentifier());
        this.setEncrypted((encrypted));
    }

    public static String generateRandomIdentifier() {
        String filename = Utility.binary2base64(Utility.getRandomBytes(STORED_FILENAME_LENGTH));
        filename = filename.replaceAll(File.separator, "-");
        filename = filename.replace(File.pathSeparator, "-");
        return filename;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public SignatureType getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(SignatureType signatureType) {
        this.signatureType = signatureType;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
}
