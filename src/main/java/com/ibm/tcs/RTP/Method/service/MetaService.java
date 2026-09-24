package com.ibm.tcs.RTP.Method.service;

import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class MetaService {

    private final ResourceService resourceService;

    public MetaService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public Map<String, Object> metaMethod(String dialCode) {
        Map<String, Object> meta = new HashMap<>();
        try (InputStream in = resourceService.resourceMethod(dialCode)) {
            if (in == null) {
                System.out.println("[MetaService] No audio data found.");
                return meta;
            }
            BufferedInputStream bufferedIn = new BufferedInputStream(in);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat format = audioIn.getFormat();
            long frames = audioIn.getFrameLength();
            double durationSeconds = (frames + 0.0) / format.getFrameRate();

            meta.put("dialCode", dialCode);
            meta.put("sampleRate", format.getSampleRate());
            meta.put("channels", format.getChannels());
            meta.put("bitDepth", format.getSampleSizeInBits());
            meta.put("durationSeconds", durationSeconds);
            audioIn.close();
        } catch (UnsupportedAudioFileException | IOException e) {
            System.out.println("[MetaService] Error reading metadata: " + e.getMessage());
        }
        return meta;
    }
}