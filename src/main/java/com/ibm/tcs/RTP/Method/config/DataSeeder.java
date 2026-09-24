package com.ibm.tcs.RTP.Method.config;

import com.ibm.tcs.RTP.Method.entity.Country;
import com.ibm.tcs.RTP.Method.repository.CountryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CountryRepository countryRepository;

    public DataSeeder(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void run(String... args) {
        if (countryRepository.count() > 0) {
            System.out.println("[DataSeeder] Database already has " + countryRepository.count() + " songs. Skipping seed.");
            return;
        }

        // ---- Naya country add karna ho, bas yahan ek line daalo: ----
        Map<String, String> dialCodeToFile = new LinkedHashMap<>();
        dialCodeToFile.put("+91", "audio/IN.wav");
        dialCodeToFile.put("+1", "audio/US.wav");
        dialCodeToFile.put("+44", "audio/UK.wav");
        dialCodeToFile.put("+33", "audio/FR.wav");
        dialCodeToFile.put("+49", "audio/DE.wav");
        dialCodeToFile.put("+34", "audio/ES.wav");
        dialCodeToFile.put("+39", "audio/IT.wav");
        dialCodeToFile.put("+55", "audio/BR.wav");
        dialCodeToFile.put("+7", "audio/RU.wav");
        dialCodeToFile.put("+86", "audio/CN.wav");
        dialCodeToFile.put("+81", "audio/JP.wav");
        dialCodeToFile.put("+61", "audio/AU.wav");
        dialCodeToFile.put("+971", "audio/AE.wav");
        dialCodeToFile.put("+65", "audio/SG.wav");
        dialCodeToFile.put("+82", "audio/KR.wav");
        // dialCodeToFile.put("+XX", "audio/XX.wav"); // ← Bas yehi ek line badhani hai naye country ke liye

        int count = 0;
        for (Map.Entry<String, String> entry : dialCodeToFile.entrySet()) {
            try {
                byte[] bytes = readFileBytes(entry.getValue());
                countryRepository.save(new Country(entry.getKey(), bytes));
                count++;
            } catch (IOException e) {
                System.out.println("[DataSeeder] Could not load " + entry.getValue() + ": " + e.getMessage());
            }
        }

        System.out.println("[DataSeeder] Seeded " + count + " songs into the database.");
    }

    private byte[] readFileBytes(String classpathLocation) throws IOException {
        ClassPathResource resource = new ClassPathResource(classpathLocation);
        try (InputStream in = resource.getInputStream()) {
            return in.readAllBytes();
        }
    }
}