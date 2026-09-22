package com.ibm.tcs.RTP.Method.controller;

import com.ibm.tcs.RTP.Method.service.MetaService;
import com.ibm.tcs.RTP.Method.service.ResourceService;
import com.ibm.tcs.RTP.Method.service.RtpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SongController {

    private final ResourceService resourceService;
    private final MetaService metaService;
    private final RtpService rtpService;

    public SongController(ResourceService resourceService, MetaService metaService, RtpService rtpService) {
        this.resourceService = resourceService;
        this.metaService = metaService;
        this.rtpService = rtpService;
    }

    @GetMapping("/api/song/play")
    public String playSong(@RequestParam String countryCode) {
        resourceService.playResourceSong(countryCode);
        return "Call connected - playing song for country code: " + countryCode;
    }

    // NAYA ENDPOINT: call cut hone pe ye hit karo
    @GetMapping("/api/song/stop")
    public String stopSong() {
        resourceService.stopSong();
        return "Call disconnected - song stopped.";
    }

    @GetMapping("/api/song/meta")
    public Map<String, Object> getSongMeta(@RequestParam String countryCode) {
        return metaService.metaMethod(countryCode);
    }

    @GetMapping("/api/song/rtp")
    public String sendSongOverRtp(@RequestParam String countryCode,
                                   @RequestParam String ip,
                                   @RequestParam int port) {
        rtpService.rtpMethod(countryCode, ip, port);
        return "Song sent over RTP for country code: " + countryCode + " to " + ip + ":" + port;
    }
}