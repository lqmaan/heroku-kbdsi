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
    public Page<Transaction> findTransactionContainingDescription(String description, Integer pageNum, Integer pageSize) {
        Page<Transaction> transactions = transactionRepository.findByDescriptionContainingAndDeleted(description, false,  PageRequest.of(pageNum, pageSize), Sort.by("description").descending().by("createdAt"));
        return null;
    }

    @Override
    public Transaction getHighestCategory(String type) {
        return transactionRepository.findTopByTypeOrderByAmountDesc(type);
    }

    @Override
    public Transaction getLowestCategory(String type) {
        return transactionRepository.findTopByTypeOrderByAmountAsc(type);
    }

    @Override
    public List<Transaction> getAllTransactionBetween(Date startDate, Date endDate) {

        List<Transaction> transactions = transactionRepository.findAllByIsDeletedAndCreatedAtBetween(false, convert.getFirstDateOfCurrentMonth() , convert.getLastDateOfCurrentMonth());
        return transactions;
    }


}
