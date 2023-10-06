package icstar.kbdsi.apps.services;

import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.models.User;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();

    Page<Transaction> findTransactionContainingDescription(String description, Integer pageNum, Integer pageSize);

    Transaction getHighestCategory(String type);

    Transaction getLowestCategory(String type);

    List<Transaction> getAllTransactionBetween(Date startDate, Date endDate);

}
