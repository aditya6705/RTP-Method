package com.ibm.tcs.RTP.Method.service;

import com.ibm.tcs.RTP.Method.repository.CountryRepository;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.BufferedInputStream;
import java.io.InputStream;

@Service
public class ResourceService {

    private final CountryRepository countryRepository;

    // Abhi jo gaana chal raha hai, uska reference yahan rakha jata hai (taaki stop kiya ja sake)
    private volatile Clip currentClip;

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

    /**
     * Gaana START karta hai (call connect hone jaisa) - ye BLOCK NAHI karta,
     * turant return ho jata hai taaki baad me 'stop' call ki ja sake.
     */
    public void playResourceSong(String dialCode) {
        // Agar pehle se koi gaana chal raha hai, use pehle band karo
        stopSong();

        String resolved = countryRepository.resolveDialCode(dialCode);
        System.out.println("[ResourceService] Call connected - playing song for dial code: " + resolved);

        try {
            InputStream in = resourceMethod(dialCode);
            if (in == null) {
                System.out.println("[ResourceService] No file found, playback cancelled.");
                return;
            }
            BufferedInputStream bufferedIn = new BufferedInputStream(in);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Jab gaana khud khatam ho jaye (naturally), to clip clean up ho jaye
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    if (currentClip == clip) {
                        currentClip = null;
                    }
                }
            });

            currentClip = clip;
            clip.start();

        } catch (Exception e) {
            System.out.println("[ResourceService] Error: " + e.getMessage());
        }
    }

    /**
     * Gaana turant BAND karta hai (call cut hone jaisa).
     */
    public void stopSong() {
        if (currentClip != null) {
            System.out.println("[ResourceService] Call disconnected - stopping song.");
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }
}