package com.llipter.sigdict.entity;


import com.llipter.sigdict.security.DigitalSignature;
import com.llipter.sigdict.security.SignatureType;

import javax.persistence.*;
import java.security.PrivateKey;
import java.sql.Timestamp;

@Entity
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer fid;

    private String filename;

    @Column(length = 512)
    private String signature;

    @Basic
    private Timestamp uploadTime;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private SignatureType signatureType;

    public UploadedFile() {
    }

    public UploadedFile(String filename, byte[] data, SignatureType signatureType, PrivateKey privateKey) {
        this.setFilename(filename);
        this.setSignature(DigitalSignature.sign(signatureType, privateKey, data));
        this.setUploadTime(new Timestamp(System.currentTimeMillis()));
        this.setSignatureType(signatureType);
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
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
}
