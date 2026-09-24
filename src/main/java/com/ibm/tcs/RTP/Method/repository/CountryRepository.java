package com.ibm.tcs.RTP.Method.repository;

import com.ibm.tcs.RTP.Method.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    String DEFAULT_COUNTRY_CODE = "+91";

    default String resolveDialCode(String dialCode) {
        if (dialCode == null || dialCode.trim().isEmpty()) {
            return DEFAULT_COUNTRY_CODE;
        }
        String cleaned = dialCode.trim().replace(" ", "");
        if (!cleaned.startsWith("+")) {
            cleaned = "+" + cleaned;
        }
        if (existsById(cleaned)) {
            return cleaned;
        }
        System.out.println("[CountryRepository] Unknown dial code '" + dialCode + "' -> falling back to default (" + DEFAULT_COUNTRY_CODE + ").");
        return DEFAULT_COUNTRY_CODE;
    }

    default String getDefaultCountryCode() {
        return DEFAULT_COUNTRY_CODE;
    }
}