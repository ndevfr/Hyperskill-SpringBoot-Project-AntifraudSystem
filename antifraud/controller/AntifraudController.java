package antifraud.controller;

import antifraud.dto.StolenCardDTO;
import antifraud.dto.SuspiciousIpDTO;
import antifraud.service.AntifraudService;
import antifraud.validation.CardNumber;
import antifraud.validation.IPv4;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/antifraud")
@Validated
public class AntifraudController {

    private final AntifraudService antifraudService;

    @Autowired
    public AntifraudController(AntifraudService service) {
        this.antifraudService = service;
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<?> registerSuspiciousIp(@Valid @RequestBody SuspiciousIpDTO request) {
        return antifraudService.createSuspiciousIp(request);
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<?> deleteSuspiciousIp(@PathVariable("ip") @IPv4 String ip) {
        return antifraudService.deleteSuspiciousIp(ip);
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<?> getAllSuspiciousIps() {
        return ResponseEntity.status(HttpStatus.OK).body(antifraudService.getAllSuspiciousIps());
    }

    @PostMapping("/stolencard")
    public ResponseEntity<?> registerStolenCard(@Valid @RequestBody StolenCardDTO request) {
        return antifraudService.createStolenCard(request);
    }

    @DeleteMapping("/stolencard/{card}")
    public ResponseEntity<?> deleteStolenCard(@PathVariable("card") @CardNumber String number) {
        return antifraudService.deleteStolenCard(number);
    }

    @GetMapping("/stolencard")
    public ResponseEntity<?> getAllStolenCards() {
        return ResponseEntity.status(HttpStatus.OK).body(antifraudService.getAllStolenCards());
    }
    
}