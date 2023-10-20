package icstar.kbdsi.apps.repository;

import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByIsDeleted(boolean isDeleted);
    Page<Transaction> findByDescriptionContainingAndIsDeleted(String description, boolean isDeleted, PageRequest pageable, Sort sort);

    Page<Transaction> findAllByCategoryContainingAndIsDeletedAndCreatedAtBetweenOrderByCreatedAtDesc(String category, boolean isDeleted, Date createdAtStart, Date createdAtEnd, PageRequest pageable, Sort sort);

    Transaction findTopByTypeAndIsDeletedOrderByAmountDesc(String type, Boolean isDeleted);
    Transaction findTopByTypeAndIsDeletedOrderByAmountAsc(String type, Boolean isDeleted);

    Transaction findTopByTypeAndIsDeletedAndCreatedAtBetweenOrderByAmountDesc(String type,boolean isDeleted , Date createdAtStart, Date createdAtEnd);
    Transaction findTopByTypeAndIsDeletedAndCreatedAtBetweenOrderByAmountAsc(String type, boolean isDeleted, Date createdAtStart, Date createdAtEnd);
    List<Transaction> findAllByIsDeletedAndCreatedAtBetween(boolean isDeleted, Date createdAtStart, Date createdAtEnd);

}
