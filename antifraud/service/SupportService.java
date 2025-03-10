package antifraud.service;


import antifraud.dto.FeedbackRequest;
import antifraud.dto.TransactionDTO;
import antifraud.enums.TransactionType;
import antifraud.model.CardLimits;
import antifraud.model.Transaction;
import antifraud.repository.CardLimitsRepository;
import antifraud.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.LongConsumer;
import java.util.function.LongSupplier;


@Service
public class SupportService {

    private final TransactionRepository transactionRepository;
    private final CardLimitsRepository limitsRepository;

    @Autowired
    public SupportService(TransactionRepository transactionRepository,
                          CardLimitsRepository limitsRepository) {
        this.transactionRepository = transactionRepository;
        this.limitsRepository = limitsRepository;
    }

    @Transactional
    public ResponseEntity<?> addFeedback(FeedbackRequest feedbackRequest) {
        Transaction transaction = transactionRepository.findById(feedbackRequest.getTransactionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
        if (!transaction.getFeedback().getMessage().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Feedback already set");
        }
        if (transaction.getType() == feedbackRequest.getFeedback()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Feedback cannot be equal");
        }
        updateCardAmountLimit(transaction.getNumber(), transaction.getAmount(), transaction.getType(), feedbackRequest.getFeedback());
        transaction.getFeedback().setMessage(feedbackRequest.getFeedback().name());
        TransactionDTO transactionDTO = TransactionDTO.of(transactionRepository.save(transaction));
        return ResponseEntity.ok(transactionDTO);
    }

    private void updateCardAmountLimit(String number, long amount, TransactionType validation, TransactionType feedback) {
        CardLimits cardLimits = limitsRepository.findByNumber(number);
        if(cardLimits == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (validation == TransactionType.ALLOWED) {
            cardLimits.changeLimit(TransactionType.ALLOWED, "DEC", amount);
            if (feedback == TransactionType.PROHIBITED) {
                cardLimits.changeLimit(TransactionType.MANUAL_PROCESSING, "DEC", amount);
            }
        } else if (validation == TransactionType.MANUAL_PROCESSING) {
            if (feedback == TransactionType.ALLOWED) {
                cardLimits.changeLimit(TransactionType.ALLOWED, "INC", amount);
            } else {
                cardLimits.changeLimit(TransactionType.MANUAL_PROCESSING, "DEC", amount);
            }
        } else {
            cardLimits.changeLimit(TransactionType.MANUAL_PROCESSING, "INC", amount);
            if (feedback == TransactionType.ALLOWED) {
                cardLimits.changeLimit(TransactionType.ALLOWED, "INC", amount);
            }
        }
        limitsRepository.save(cardLimits);
    }

}
