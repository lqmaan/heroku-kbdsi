package icstar.kbdsi.apps.services;

import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.models.User;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();

    Page<Transaction> findTransactionContainingDescription(String description, boolean isDeleted, Integer pageNum, Integer pageSize);

    Page<Transaction> findTransactionContainingCategoryAndRangeDate(String category, boolean isDeleted, String startDate, String endDate, String year,Integer pageNum, Integer pageSize);


    Transaction getHighest(String type);

    Transaction getLowest(String type);

    List<Transaction> getAllTransactionBetween(Date startDate, Date endDate);

}
