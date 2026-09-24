package com.ibm.tcs.RTP.Method.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries")
public class Country {

    @Id
    @Column(name = "dial_code")
    private String dialCode;

    @Lob
    @Column(name = "audio_data", columnDefinition = "LONGBLOB")
    private byte[] audioData;

    public Country() {
    }

    public Country(String dialCode, byte[] audioData) {
        this.dialCode = dialCode;
        this.audioData = audioData;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public void setAudioData(byte[] audioData) {
        this.audioData = audioData;
    }
}