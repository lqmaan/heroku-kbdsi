package icstar.kbdsi.apps.controllers;

import icstar.kbdsi.apps.dto.*;
import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.models.User;
import icstar.kbdsi.apps.repository.TransactionRepository;
import icstar.kbdsi.apps.services.TransactionService;
import icstar.kbdsi.apps.services.UserService;
import icstar.kbdsi.apps.util.Convert;
import icstar.kbdsi.apps.util.TransactionExcelGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    TransactionService transactionService;

    Convert convert;
    public TransactionController(TransactionService transactionService, Convert convert) {
        super();
        this.transactionService = transactionService;
        this.convert = convert;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions (@RequestParam(required = false) String name){
        try{
            List<Transaction> transactions = new ArrayList<Transaction>();

            if(name == null)
                transactions.addAll(transactionRepository.findAll());
//            else
//                categoryRepository.findAllByName(name, PageRequest.of(0, 10, Sort.by("name").ascending()));
            if(transactions.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction){
        try{
            Transaction newTransaction =  new Transaction(transaction.getName(), transaction.getType(), transaction.getCategory(), transaction.getAmount(), transaction.getDescription(), transaction.getCreatedBy());
            transactionRepository.save(newTransaction);
            return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/transactions/{id}")
    public ResponseEntity<Transaction> updateTransactionById(@PathVariable(required = true) Long id, @RequestBody Transaction newTransaction) {
        Optional<Transaction> optTransaction = transactionRepository.findById(id);
        if(optTransaction.isPresent()) {
            Transaction oldTransaction = optTransaction.get();
            oldTransaction.setName(newTransaction.getName());
            oldTransaction.setAmount(newTransaction.getAmount());
            oldTransaction.setCategory(newTransaction.getCategory());
            oldTransaction.setDescription(newTransaction.getDescription());
            oldTransaction.setUpdatedBy(newTransaction.getUpdatedBy());
            transactionRepository.save(oldTransaction);
            return new ResponseEntity<>(oldTransaction, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable(required = true) Long id, @RequestBody DeleteDto deleteDto) {
        try {
            Optional<Transaction> optTransaction = transactionRepository.findById(id);
            if(optTransaction.isPresent()) {
                Transaction oldTransaction = optTransaction.get();
                oldTransaction.setDeleted(true);
                oldTransaction.setUpdatedBy(deleteDto.getUpdatedBy());
                transactionRepository.save(oldTransaction);
            return new ResponseEntity<>("Transaction has been deleted",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactions/description")
    private Page<Transaction> getTransactionsWithFilterDesc(PaginationTransactionDto paginationTransactionDto) {
        Page<Transaction> transactions = transactionService.findTransactionContainingDescription(paginationTransactionDto.getDescription(), false,  paginationTransactionDto.getPageNum(), paginationTransactionDto.getPageSize());
        return transactions;
    }

    @GetMapping("/transactions/bookkeeping")
    private Page<Transaction> getTransactionsWithFilterCategory(PaginationBookkeepingDto paginationBookkeepingDto) {
        Page<Transaction> transactions = transactionService.findTransactionContainingCategoryAndRangeDate(paginationBookkeepingDto.getCategory(), false,  paginationBookkeepingDto.getStartDate(), paginationBookkeepingDto.getEndDate(), paginationBookkeepingDto.getYear(),paginationBookkeepingDto.getPageNum(), paginationBookkeepingDto.getPageSize());
        return transactions;
    }

    @GetMapping("/transactions/export-to-excel")
    public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue= "attachment; filename=transaction_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey,headerValue);

        List<Transaction> listOfTransactions = transactionRepository.findAll();
        TransactionExcelGenerator generator = new TransactionExcelGenerator(listOfTransactions);
        generator.generateExcelFile(response);
    }

    @GetMapping("/transactions/export-to-excel/{from}/{to}")
    public void exportIntoExcelFile(HttpServletResponse response, @PathVariable(required = true) String from, @PathVariable(required = true) String to) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue= "attachment; filename=transaction_range_"+ from +  "_" + to + "_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey,headerValue);

        List<Transaction> listOfTransactions = transactionRepository.findAll();
        TransactionExcelGenerator generator = new TransactionExcelGenerator(listOfTransactions);
        generator.generateExcelFile(response);
    }
    @GetMapping("/transactions/max-income")
    public ResponseEntity<Transaction> getHighestIncome() throws IOException {
        Transaction maxIncome = transactionService.getHighest("income");
        System.out.println(maxIncome);
        return new ResponseEntity<>(maxIncome, HttpStatus.OK);
    }

    @GetMapping("/transactions/max-outcome")
    public ResponseEntity<Transaction> getHighestOutcome() throws IOException {
        Transaction maxOutcome = transactionService.getHighest("outcome");
        System.out.println(maxOutcome);
        return new ResponseEntity<>(maxOutcome, HttpStatus.OK);
    }

    @GetMapping("/transactions/min-income")
    public ResponseEntity<Transaction> getLowestIncome() throws IOException {
        Transaction minIncome = transactionService.getLowest("income");
        return new ResponseEntity<>(minIncome, HttpStatus.OK);
    }

    @GetMapping("/transactions/min-outcome")
    public ResponseEntity<Transaction> getLowestOutcome() throws IOException {
        Transaction minOutcome = transactionService.getLowest("outcome");
        return new ResponseEntity<>(minOutcome, HttpStatus.OK);
    }

    @GetMapping("/transactions/range-date")
    public ResponseEntity<List<Transaction>> getAllTransactionBetween(TransactionBetweenDto transactionBetweenDto) throws IOException, ParseException {
        Date startDate = convert.ConvertStringToDate(transactionBetweenDto.getStartDate());
        Date endDate = convert.ConvertStringToDate(transactionBetweenDto.getEndDate());
        List<Transaction> transactions = transactionService.getAllTransactionBetween(startDate, endDate);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
