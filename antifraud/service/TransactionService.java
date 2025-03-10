package antifraud.service;

import antifraud.dto.TransactionDTO;
import antifraud.dto.TransactionResponse;
import antifraud.enums.Region;
import antifraud.enums.TransactionType;
import antifraud.model.CardLimits;
import antifraud.model.Transaction;
import antifraud.repository.CardLimitsRepository;
import antifraud.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final AntifraudService anfifraudService;
    private final TransactionRepository transactionRepo;
    private final CardLimitsRepository cardLimitsRepo;

    @Autowired
    public TransactionService(AntifraudService anfifraudService, TransactionRepository transactionRepo, CardLimitsRepository cardLimitsRepo) {
        this.anfifraudService = anfifraudService;
        this.transactionRepo = transactionRepo;
        this.cardLimitsRepo = cardLimitsRepo;
    }

    @Transactional
    public ResponseEntity<?> processTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TransactionResponse("Invalid amount", "amount"));
        }

        CardLimits cardLimits = cardLimitsRepo.findByNumber(transactionDTO.getNumber());
        if(cardLimits == null) {
            cardLimits = cardLimitsRepo.save(new CardLimits(transactionDTO.getNumber()));
        }
        Map<String, String> result = calculateResult(transactionDTO, cardLimits);
        transactionDTO.setResult(TransactionType.valueOf(result.get("type")));
        transactionRepo.save(transactionDTO.toTransaction());

        return ResponseEntity.status(HttpStatus.OK).body(new TransactionResponse(result.get("type"), result.get("info")));

    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepo.findAllBy().stream()
                .map(TransactionDTO::of)
                .toList();
    }

    public ResponseEntity<?> getAllTransactionsByNumber(String number) {
        List<Transaction> transactions = transactionRepo.findAllByNumber(number);
        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transactions not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(transactions.stream()
                .map(TransactionDTO::of)
                .toList());
    }

    public Map<String, String> calculateResult(TransactionDTO transactionDTO, CardLimits cardLimits) {
        String info;
        TransactionType type;
        List<String> manual = new ArrayList<>();
        List<String> prohibited = new ArrayList<>();

        System.out.println(transactionDTO.toString());

        if (transactionDTO.getAmount() > cardLimits.getManual()) {
            prohibited.add("amount");
        } else if (transactionDTO.getAmount() > cardLimits.getAllowed()) {
            manual.add("amount");
        }

        String number = transactionDTO.getNumber();
        String ip = transactionDTO.getIp();
        Region region = transactionDTO.getRegion();
        LocalDateTime end = transactionDTO.getDate();
        LocalDateTime start = transactionDTO.getDate().minusHours(1);

        if (anfifraudService.isSuspiciousIp(ip)) {
            prohibited.add("ip");
        }

        if (anfifraudService.isStolenCard(number)) {
            prohibited.add("card-number");
        }

        long numberOfOthersRegions = transactionRepo.countBetweenDateTimesByNumberNotRegion(number, region, start, end);
        long numberOfOthersIps = transactionRepo.countBetweenDateTimesByNumberNotIp(number, ip, start, end);

        if(numberOfOthersRegions == 2) {
            manual.add("region-correlation");
        } else if(numberOfOthersRegions > 2) {
            prohibited.add("region-correlation");
        }

        if(numberOfOthersIps == 2) {
            manual.add("ip-correlation");
        } else if(numberOfOthersIps > 2) {
            prohibited.add("ip-correlation");
        }

        if (!prohibited.isEmpty()) {
            type = TransactionType.PROHIBITED;
            info = String.join(", ", prohibited.stream().sorted().toList());
        } else if (!manual.isEmpty()) {
            type = TransactionType.MANUAL_PROCESSING;
            info = String.join(", ", manual.stream().sorted().toList());

        } else {
            type = TransactionType.ALLOWED;
            info = "none";
        }

        return Map.of("type", type.toString(), "info", info);
    }

}
