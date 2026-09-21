package com.ibm.tcs.RTP.Method.repository;

import com.ibm.tcs.RTP.Method.entity.Country;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CountryRepository
 * ------------------
 * Country Entity objects ki List rakhta hai (database ki jagah in-memory).
 * Naya country add karna ho to bas neeche ek naya "new Country(...)" line daal do.
 */
@Repository
public class CountryRepository {

    private final List<Country> countryList = new ArrayList<>();

    public CountryRepository() {
        countryList.add(new Country("+91", "audio/IN.wav")); // India
        countryList.add(new Country("+1", "audio/US.wav"));  // USA
        countryList.add(new Country("+44", "audio/UK.wav")); // United Kingdom
        countryList.add(new Country("+33", "audio/FR.wav")); // France
        countryList.add(new Country("+49", "audio/DE.wav")); // Germany
        countryList.add(new Country("+34", "audio/ES.wav")); // Spain
        countryList.add(new Country("+39", "audio/IT.wav")); // Italy
        countryList.add(new Country("+55", "audio/BR.wav")); // Brazil
        countryList.add(new Country("+7", "audio/RU.wav"));  // Russia
        countryList.add(new Country("+86", "audio/CN.wav")); // China
        countryList.add(new Country("+81", "audio/JP.wav")); // Japan
        countryList.add(new Country("+61", "audio/AU.wav")); // Australia
        countryList.add(new Country("+971", "audio/AE.wav"));

        // ---- Naya country add karna ho, bas yahan neeche ek line daalo: ----
        // countryList.add(new Country("+971", "audio/AE.wav")); // UAE
    }

    private static final String DEFAULT_COUNTRY_CODE = "+91";

    public String getSongPath(String dialCode) {
        String resolved = resolveDialCode(dialCode);
        return findByDialCode(resolved)
                .map(Country::getSongPath)
                .orElse(null);
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
        if (findByDialCode(cleaned).isPresent()) {
            return cleaned;
        }
        System.out.println("[CountryRepository] Unknown dial code '" + dialCode + "' -> falling back to default (" + DEFAULT_COUNTRY_CODE + ").");
        return DEFAULT_COUNTRY_CODE;
    }

    private Optional<Country> findByDialCode(String dialCode) {
        return countryList.stream()
                .filter(c -> c.getDialCode().equals(dialCode))
                .findFirst();
    }
}