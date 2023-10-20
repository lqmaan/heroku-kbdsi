package icstar.kbdsi.apps.services.impl;

import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.repository.TransactionRepository;
import icstar.kbdsi.apps.services.TransactionService;
import icstar.kbdsi.apps.util.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    private final Convert convert;

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
        Page<Transaction> transactions = transactionRepository.findByDescriptionContainingAndIsDeleted(description, isDeleted,  PageRequest.of(pageNum, pageSize), Sort.by("createdAt").descending().and(Sort.by("description")));
        return transactions;
    }

    @Override
    public Page<Transaction> findTransactionContainingCategoryAndRangeDate(String category, boolean isDeleted, String startDate, String endDate, String year, Integer pageNum, Integer pageSize) throws ParseException {
        if(startDate != "" && endDate != ""){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Date start = convert.ConvertStringToDate(startDate);

            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(endDate));
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date end = convert.ConvertStringToDate(sdf.format(cal.getTime()));

            System.out.println(start + "_" + end);
            Page<Transaction> transactions = transactionRepository.findAllByCategoryContainingAndIsDeletedAndCreatedAtBetweenOrderByCreatedAtDesc(category, isDeleted, start, end,  PageRequest.of(pageNum, pageSize), Sort.by("description"));
            return transactions;
        }
        else if(year != ""){;
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
            calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);

            // returning the last date
            Date endYear = calendarEnd.getTime();
            Page<Transaction> transactions = transactionRepository.findAllByCategoryContainingAndIsDeletedAndCreatedAtBetweenOrderByCreatedAtDesc(category, isDeleted, startYear, endYear,  PageRequest.of(pageNum, pageSize), Sort.by("description"));
            return transactions;
        }
        else{
            Date firstDay = convert.getFirstDateOfCurrentMonth();
            Date lastDay = convert.getLastDateOfCurrentMonth();
            Page<Transaction> transactions = transactionRepository.findAllByCategoryContainingAndIsDeletedAndCreatedAtBetweenOrderByCreatedAtDesc(category, isDeleted, firstDay, lastDay,  PageRequest.of(pageNum, pageSize), Sort.by("description"));
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
    public List<Transaction> getIncome() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        Transaction maxIncome = transactionRepository.findTopByTypeAndIsDeletedAndCreatedAtBetweenOrderByAmountDesc("income", false, convert.getFirstDateOfCurrentMonth(), convert.getLastDateOfCurrentMonth());
        Transaction minIncome = transactionRepository.findTopByTypeAndIsDeletedAndCreatedAtBetweenOrderByAmountAsc("income", false, convert.getFirstDateOfCurrentMonth(), convert.getLastDateOfCurrentMonth());
        transactions.add(maxIncome);
        transactions.add(minIncome);
        return transactions;
    }

    @Override
    public List<Transaction> getOutcome() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        Transaction maxOutcome = transactionRepository.findTopByTypeAndIsDeletedAndCreatedAtBetweenOrderByAmountDesc("outcome", false, convert.getFirstDateOfCurrentMonth(), convert.getLastDateOfCurrentMonth());
        Transaction minOutcome = transactionRepository.findTopByTypeAndIsDeletedAndCreatedAtBetweenOrderByAmountAsc("outcome", false, convert.getFirstDateOfCurrentMonth(), convert.getLastDateOfCurrentMonth());
        transactions.add(maxOutcome);
        transactions.add(minOutcome);
        return transactions;
    }

    @Override
    public List<Transaction> getAllTransactionBetween(Date startDate, Date endDate) {
        List<Transaction> transactions = transactionRepository.findAllByIsDeletedAndCreatedAtBetween(false, convert.getFirstDateOfCurrentMonth() , convert.getLastDateOfCurrentMonth());
        return transactions;
    }

    @Override
    public List<Integer> getTotalYear(Integer year) {
        List<Transaction> transactions = transactionRepository.findAllByIsDeletedAndCreatedAtBetween(false, convert.getFirstDateOfYear(year), convert.getLastDateOfYear(year));
        int totalIncome = 0;
        int totalOutcome = 0;
        for (Transaction transaction:transactions) {
            if(transaction.getType().equals("income")){
                totalIncome += transaction.getAmount();
            }
            else if(transaction.getType().equals("outcome")) {
                totalOutcome += transaction.getAmount();
            }
            else{
                System.out.println("else");;
            }
        }
        List<Integer> total = new ArrayList<>();
        total.add(totalIncome);
        total.add(totalOutcome);
        return total;
    }

    @Override
    public List<Integer> getTotalMonth(Integer month) {
        Date currDate = new Date();
        Date startDate = new Date(currDate.getYear(), month, convert.getFirstDayOfMonthUsingCalendar(month));
        Date endDate = new Date(currDate.getYear(), month, convert.getLastDayOfMonthUsingCalendar(month));
        List<Transaction> transactions = transactionRepository.findAllByIsDeletedAndCreatedAtBetween(false, startDate, endDate);
        int totalIncome = 0;
        int totalOutcome = 0;
        for (Transaction transaction:transactions) {
            System.out.println(transaction.getType());
            if(transaction.getType().equals("income")){
                totalIncome += transaction.getAmount();
            }
            else if(transaction.getType().equals("outcome")) {
                totalOutcome += transaction.getAmount();
            }
            else{
                System.out.println("else");;
            }
        }
        List<Integer> total = new ArrayList<>();
        total.add(totalIncome);
        total.add(totalOutcome);
        return total;
    }


}
