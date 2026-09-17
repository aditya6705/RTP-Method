package com.ibm.tcs.RTP.Method.service;

import org.springframework.stereotype.Service;

import com.ibm.tcs.RTP.Method.repository.CountryRepository;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

@Service
public class ResourceService {

    private final CountryRepository countryRepository;

    public ResourceService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public InputStream resourceMethod(String dialCode) {
        String path = countryRepository.getSongPath(dialCode);
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);

        if (in == null) {
            String defaultPath = countryRepository.getSongPath(countryRepository.getDefaultCountryCode());
            in = getClass().getClassLoader().getResourceAsStream(defaultPath);
        }
        return in;
    }

    public void playResourceSong(String dialCode) {
        String resolved = countryRepository.resolveDialCode(dialCode);
        System.out.println("[ResourceService] Playing song for dial code: " + resolved);

        try (InputStream in = resourceMethod(dialCode)) {
            if (in == null) {
                System.out.println("[ResourceService] No file found, playback cancelled.");
                return;
            }
            BufferedInputStream bufferedIn = new BufferedInputStream(in);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

            long durationMillis = (clip.getMicrosecondLength() / 1000) + 200;
            Thread.sleep(durationMillis);

            clip.close();
            audioIn.close();
            System.out.println("[ResourceService] Finished playing song for: " + resolved);

        } catch (Exception e) {
            System.out.println("[ResourceService] Error: " + e.getMessage());
        }
    }
}