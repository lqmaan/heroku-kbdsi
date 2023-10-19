package icstar.kbdsi.apps.services;

import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.models.User;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();

    Page<Transaction> findTransactionContainingDescription(String description, boolean isDeleted, Integer pageNum, Integer pageSize);

    Page<Transaction> findTransactionContainingCategoryAndRangeDate(String category, boolean isDeleted, String startDate, String endDate, String year,Integer pageNum, Integer pageSize) throws ParseException;


    Transaction getHighest(String type);

    Transaction getLowest(String type);

    List<Transaction> getIncome();
    List<Transaction> getOutcome();
    List<Transaction> getAllTransactionBetween(Date startDate, Date endDate);

    List<Integer> getTotalYear(Integer year);

    List<Integer> getTotalMonth(Integer month);

}
