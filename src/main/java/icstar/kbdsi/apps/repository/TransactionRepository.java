package icstar.kbdsi.apps.repository;

import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByIsDeleted(boolean isDeleted);
    Page<Transaction> findByNameContainingAndIsDeleted(String name, boolean isDeleted, PageRequest pageable, Sort sort);

    Page<Transaction> findByCategoryContainingAndIsDeletedAndCreatedAtBetween(String category, boolean isDeleted, Date createdAtStart, Date createdAtEnd, PageRequest pageable, Sort sort);

    Transaction findTopByTypeAndIsDeletedOrderByAmountDesc(String type,boolean isDeleted);
    Transaction findTopByTypeAndIsDeletedOrderByAmountAsc(String type, boolean isDeleted);
    List<Transaction> findAllByIsDeletedAndCreatedAtBetween(boolean isDeleted, Date createdAtStart, Date createdAtEnd);

}
