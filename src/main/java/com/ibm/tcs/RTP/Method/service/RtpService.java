package com.ibm.tcs.RTP.Method.service;

import org.springframework.stereotype.Service;

import com.ibm.tcs.RTP.Method.repository.CountryRepository;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Service
public class RtpService {

    private static final int RTP_HEADER_SIZE = 12;
    private static final int PAYLOAD_SIZE = 160;
    private static final int RTP_PAYLOAD_TYPE_PCM = 0;

    private final ResourceService resourceService;
    private final CountryRepository countryRepository;

    public RtpService(ResourceService resourceService, CountryRepository countryRepository) {
        this.resourceService = resourceService;
        this.countryRepository = countryRepository;
    }

    public void rtpMethod(String dialCode, String destIp, int destPort) {
        String resolved = countryRepository.resolveDialCode(dialCode);
        System.out.println("[RtpService] Sending song for dial code '" + resolved + "' over RTP...");

        try (InputStream songStream = resourceService.resourceMethod(dialCode);
             DatagramSocket socket = new DatagramSocket()) {

            if (songStream == null) {
                System.out.println("[RtpService] No file found, RTP send cancelled.");
                return;
            }

            BufferedInputStream bufferedIn = new BufferedInputStream(songStream);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);

            InetAddress destAddress = InetAddress.getByName(destIp);
            byte[] buffer = new byte[PAYLOAD_SIZE];
            int sequenceNumber = 0;
            long timestamp = 0;
            int bytesRead;

            while ((bytesRead = audioIn.read(buffer)) != -1) {
                byte[] rtpPacket = buildRtpPacket(buffer, bytesRead, sequenceNumber, timestamp);
                DatagramPacket packet = new DatagramPacket(rtpPacket, rtpPacket.length, destAddress, destPort);
                socket.send(packet);

                sequenceNumber++;
                timestamp += bytesRead;

                Thread.sleep(20);
            }
            audioIn.close();
            System.out.println("[RtpService] Successfully sent song for '" + resolved + "' (" + sequenceNumber + " packets).");

        } catch (Exception e) {
            System.out.println("[RtpService] Error: " + e.getMessage());
        }
    }

    private byte[] buildRtpPacket(byte[] payload, int payloadLength, int seqNum, long timestamp) {
        byte[] packet = new byte[RTP_HEADER_SIZE + payloadLength];

        packet[0] = (byte) 0x80;
        packet[1] = (byte) RTP_PAYLOAD_TYPE_PCM;

        packet[2] = (byte) (seqNum >> 8);
        packet[3] = (byte) seqNum;

        packet[4] = (byte) (timestamp >> 24);
        packet[5] = (byte) (timestamp >> 16);
        packet[6] = (byte) (timestamp >> 8);
        packet[7] = (byte) timestamp; 

        int ssrc = 123456;
        packet[8] = (byte) (ssrc >> 24);
        packet[9] = (byte) (ssrc >> 16);
        packet[10] = (byte) (ssrc >> 8);
        packet[11] = (byte) ssrc;

        System.arraycopy(payload, 0, packet, RTP_HEADER_SIZE, payloadLength);
        return packet;
    }
}  