package antifraud.controller;

import antifraud.dto.FeedbackRequest;
import antifraud.dto.TransactionDTO;
import antifraud.dto.TransactionRequest;
import antifraud.service.SupportService;
import antifraud.service.TransactionService;
import antifraud.validation.CardNumber;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
@Validated
public class TransactionController {

    private final TransactionService transactionService;
    private final SupportService supportService;

    @Autowired
    public TransactionController(TransactionService transactionService, SupportService supportService) {
        this.transactionService = transactionService;
        this.supportService = supportService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> processTransaction(@Valid @RequestBody TransactionDTO transaction) {
        return transactionService.processTransaction(transaction);
    }

    @GetMapping("/history")
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/history/{number}")
    public ResponseEntity<?> getAllTransactionsByCardNumber(@PathVariable @CardNumber String number) {
        return transactionService.getAllTransactionsByNumber(number);
    }

    @PutMapping("/transaction")
    public ResponseEntity<?> addFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {
        return supportService.addFeedback(feedbackRequest);
    }

}