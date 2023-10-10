package icstar.kbdsi.apps.services.impl;

import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.models.User;
import icstar.kbdsi.apps.repository.TransactionRepository;
import icstar.kbdsi.apps.repository.UserRepository;
import icstar.kbdsi.apps.services.TransactionService;
import icstar.kbdsi.apps.util.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    private Convert convert;

    public TransactionServiceImpl(TransactionRepository transactionRepository, Convert convert){
        super();
        this.transactionRepository = transactionRepository;
        this.convert = convert;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findByIsDeleted(false);
    }

    @Override
    public Page<Transaction> findTransactionContainingDescription(String description, boolean isDeleted, Integer pageNum, Integer pageSize) {
        Page<Transaction> transactions = transactionRepository.findByDescriptionContainingAndIsDeleted(description, isDeleted,  PageRequest.of(pageNum, pageSize), Sort.by("description").descending().by("createdAt"));
        return transactions;
    }

    @Override
    public Page<Transaction> findTransactionContainingCategoryAndRangeDate(String category, boolean isDeleted, String startDate, String endDate, String year, Integer pageNum, Integer pageSize) {
        if(year != ""){;
            int parseYear = Integer.parseInt(year);
            Calendar calendarStart=Calendar.getInstance();
            calendarStart.set(Calendar.YEAR,parseYear);
            calendarStart.set(Calendar.MONTH,0);
            calendarStart.set(Calendar.DAY_OF_MONTH,1);
            // returning the first date
            Date startYear = calendarStart.getTime();

            Calendar calendarEnd=Calendar.getInstance();
            calendarEnd.set(Calendar.YEAR,parseYear);
            calendarEnd.set(Calendar.MONTH,11);
            calendarEnd.set(Calendar.DAY_OF_MONTH,31);

            // returning the last date
            Date endYear = calendarEnd.getTime();
            Page<Transaction> transactions = transactionRepository.findByCategoryContainingAndIsDeletedAndCreatedAtBetween(category, isDeleted, startYear, endYear,  PageRequest.of(pageNum, pageSize), Sort.by("description").descending().by("createdAt"));
            return transactions;
        }
        else if(startDate != "" && endDate != ""){
            Date start = convert.ConvertStringToDate(startDate);
            Date end = convert.ConvertStringToDate(endDate);
            Page<Transaction> transactions = transactionRepository.findByCategoryContainingAndIsDeletedAndCreatedAtBetween(category, isDeleted, start, end,  PageRequest.of(pageNum, pageSize), Sort.by("description").descending().by("createdAt"));
            return transactions;
        }
        else{
            Date firstDay = convert.getFirstDateOfCurrentMonth();
            Date lastDay = convert.getLastDateOfCurrentMonth();
            Page<Transaction> transactions = transactionRepository.findByCategoryContainingAndIsDeletedAndCreatedAtBetween(category, isDeleted, firstDay, lastDay,  PageRequest.of(pageNum, pageSize), Sort.by("description").descending().by("createdAt"));
            return transactions;
        }
    }

    @Override
    public Transaction getHighest(String type) {
        return transactionRepository.findTopByTypeAndIsDeletedOrderByAmountDesc(type, false);
    }

    @Override
    public Transaction getLowest(String type) {
        return transactionRepository.findTopByTypeAndIsDeletedOrderByAmountAsc(type, false);
    }

    @Override
    public List<Transaction> getAllTransactionBetween(Date startDate, Date endDate) {

        List<Transaction> transactions = transactionRepository.findAllByIsDeletedAndCreatedAtBetween(false, convert.getFirstDateOfCurrentMonth() , convert.getLastDateOfCurrentMonth());
        return transactions;
    }


}
