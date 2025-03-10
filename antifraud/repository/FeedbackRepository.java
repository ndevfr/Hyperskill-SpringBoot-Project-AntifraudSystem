package antifraud.repository;

import antifraud.model.Feedback;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface FeedbackRepository extends ListCrudRepository<Feedback, Long> {

}