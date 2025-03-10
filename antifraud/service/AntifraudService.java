package antifraud.service;

import antifraud.dto.*;
import antifraud.model.StolenCard;
import antifraud.model.SuspiciousIp;
import antifraud.repository.StolenCardRepository;
import antifraud.repository.SuspiciousIpRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AntifraudService {

    private final SuspiciousIpRepository suspiciousRepo;
    private final StolenCardRepository stolenCardRepo;

    @Autowired
    public AntifraudService(SuspiciousIpRepository suspiciousRepo, StolenCardRepository stolenCardRepo) {
        this.suspiciousRepo = suspiciousRepo;
        this.stolenCardRepo = stolenCardRepo;
    }

    @Transactional
    public ResponseEntity<?> createSuspiciousIp(SuspiciousIpDTO suspiciousIpDTO) {
        if (suspiciousRepo.existsByIp(suspiciousIpDTO.getIp())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("IP already saved");
        }
        SuspiciousIp suspiciousIp = suspiciousIpDTO.toSuspiciousIp();
        suspiciousRepo.save(suspiciousIp);
        return ResponseEntity.status(HttpStatus.OK).body(suspiciousIpDTO.of(suspiciousIp));
    }

    @Transactional
    public ResponseEntity<?> deleteSuspiciousIp(String ip) {
        if (!suspiciousRepo.existsByIp(ip)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("IP not found");
        }
        suspiciousRepo.deleteByIp(ip);
        Map<String, String> response = new HashMap<>();
        response.put("status", "IP %s successfully removed!".formatted(ip));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public List<SuspiciousIpDTO> getAllSuspiciousIps() {
        return suspiciousRepo.findAllByOrderById().stream()
                .map(SuspiciousIpDTO::of)
                .toList();
    }

    @Transactional
    public ResponseEntity<?> createStolenCard(StolenCardDTO stolenCardDTO) {
        if (stolenCardRepo.existsByNumber(stolenCardDTO.getNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Stolen card already saved");
        }
        StolenCard stolenCard = stolenCardDTO.toStolenCard();
        stolenCardRepo.save(stolenCard);
        return ResponseEntity.status(HttpStatus.OK).body(stolenCardDTO.of(stolenCard));
    }

    @Transactional
    public ResponseEntity<?> deleteStolenCard(String number) {
        if (!stolenCardRepo.existsByNumber(number)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stolen card not found");
        }
        stolenCardRepo.deleteByNumber(number);
        Map<String, String> response = new HashMap<>();
        response.put("status", "Card %s successfully removed!".formatted(number));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public List<StolenCardDTO> getAllStolenCards() {
        return stolenCardRepo.findAllByOrderById().stream()
                .map(StolenCardDTO::of)
                .toList();
    }

    public boolean isSuspiciousIp(String ip) {
        return suspiciousRepo.existsByIp(ip);
    }

    public boolean isStolenCard(String number) {
        return stolenCardRepo.existsByNumber(number);
    }

}
