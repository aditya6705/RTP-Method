package com.ibm.tcs.RTP.Method.entity;

/**
 * Country
 * -------
 * Ek "Entity" (data blueprint) jo ek country ke dial code aur uske gaane ko represent karta hai.
 * Koi database nahi hai - ye sirf ek plain Java object (POJO) hai jo data ko structured
 * tarike se hold karta hai, HashMap ke raw String pairs ki jagah.
 */
public class Country {

    private String dialCode;   // jaise "+91"
    private String songPath;   // jaise "audio/IN.wav"

    public Country() {
    }

    public Country(String dialCode, String songPath) {
        this.dialCode = dialCode;
        this.songPath = songPath;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    @Override
    public String toString() {
        return "Country{dialCode='" + dialCode + "', songPath='" + songPath + "'}";
    }
}