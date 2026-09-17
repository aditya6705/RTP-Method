package com.ibm.tcs.RTP.Method.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CountryRepository {

    private static final Map<String, String> COUNTRY_SONG_MAP = new HashMap<>();
    static {
        COUNTRY_SONG_MAP.put("+91", "audio/IN.wav");
        COUNTRY_SONG_MAP.put("+1", "audio/US.wav");
        COUNTRY_SONG_MAP.put("+44", "audio/UK.wav");
        COUNTRY_SONG_MAP.put("+33", "audio/FR.wav");
        COUNTRY_SONG_MAP.put("+49", "audio/DE.wav");
        COUNTRY_SONG_MAP.put("+34", "audio/ES.wav");
        COUNTRY_SONG_MAP.put("+39", "audio/IT.wav");
        COUNTRY_SONG_MAP.put("+55", "audio/BR.wav");
        COUNTRY_SONG_MAP.put("+7", "audio/RU.wav");
        COUNTRY_SONG_MAP.put("+86", "audio/CN.wav");
        COUNTRY_SONG_MAP.put("+81", "audio/JP.wav");
        COUNTRY_SONG_MAP.put("+61", "audio/AU.wav");
        COUNTRY_SONG_MAP.put("+62", "audio/AU.wav");
    }

    private static final String DEFAULT_COUNTRY_CODE = "+91";

    public String getSongPath(String dialCode) {
        String resolved = resolveDialCode(dialCode);
        return COUNTRY_SONG_MAP.get(resolved);
    }

    public String getDefaultCountryCode() {
        return DEFAULT_COUNTRY_CODE;
    }

    public String resolveDialCode(String dialCode) {
        if (dialCode == null || dialCode.trim().isEmpty()) {
            return DEFAULT_COUNTRY_CODE;
        }
        String cleaned = dialCode.trim().replace(" ", "");
        if (!cleaned.startsWith("+")) {
            cleaned = "+" + cleaned;
        }
        if (COUNTRY_SONG_MAP.containsKey(cleaned)) {
            return cleaned;
        }
        System.out.println("[CountryRepository] Unknown dial code '" + dialCode + "' -> falling back to default (" + DEFAULT_COUNTRY_CODE + ").");
        return DEFAULT_COUNTRY_CODE;
    }
}