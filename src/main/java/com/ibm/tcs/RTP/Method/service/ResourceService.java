package com.ibm.tcs.RTP.Method.service;

import com.ibm.tcs.RTP.Method.entity.Country;
import com.ibm.tcs.RTP.Method.repository.CountryRepository;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class ResourceService {

    private final CountryRepository countryRepository;
    private volatile Clip currentClip;

    public ResourceService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public InputStream resourceMethod(String dialCode) {
        String resolved = countryRepository.resolveDialCode(dialCode);
        Country country = countryRepository.findById(resolved)
                .orElseGet(() -> countryRepository.findById(countryRepository.getDefaultCountryCode()).orElse(null));
        if (country == null || country.getAudioData() == null) {
            return null;
        }
        return new ByteArrayInputStream(country.getAudioData());
    }

    public void playResourceSong(String dialCode) {
        stopSong();
        String resolved = countryRepository.resolveDialCode(dialCode);
        System.out.println("[ResourceService] Playing song for: " + resolved);
        try {
            InputStream in = resourceMethod(dialCode);
            if (in == null) return;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    if (currentClip == clip) currentClip = null;
                }
            });
            currentClip = clip;
            clip.start();
        } catch (Exception e) {
            System.out.println("[ResourceService] Error: " + e.getMessage());
        }
    }

    public void stopSong() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }
}