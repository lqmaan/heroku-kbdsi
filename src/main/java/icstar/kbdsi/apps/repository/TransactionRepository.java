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

//    @Query("SELECT\n" +
//            "  MAX(`transaction`.`amount`) AS `transaction__amount__max`\n" +
//            "FROM\n" +
//            "  `kbdsi`.`transaction`\n" +
//            "WHERE\n" +
//            "  (\n" +
//            "    `transaction`.`created_at`>'2023-09-30 00:00'\n" +
//            "    AND `transaction`.`created_at`<'2023-11-01 00:00'\n" +
//            "    AND `transaction`.`is_deleted`=0\n" +
//            "    AND `transaction`.`type`='income'\n" +
//            "  )")
//    Transaction getHighest();


    Transaction findTopByTypeAndIsDeletedOrderByAmountDesc(String type, Boolean isDeleted);
    Transaction findTopByTypeAndIsDeletedOrderByAmountAsc(String type, Boolean isDeleted);

    Transaction findTopByTypeAndIsDeletedAndCreatedAtBetweenOrderByAmountDesc(String type,boolean isDeleted , Date createdAtStart, Date createdAtEnd);
    Transaction findTopByTypeAndIsDeletedAndCreatedAtBetweenOrderByAmountAsc(String type, boolean isDeleted, Date createdAtStart, Date createdAtEnd);
    List<Transaction> findAllByIsDeletedAndCreatedAtBetween(boolean isDeleted, Date createdAtStart, Date createdAtEnd);

}
