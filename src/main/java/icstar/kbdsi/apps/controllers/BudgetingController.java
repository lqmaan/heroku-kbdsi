package icstar.kbdsi.apps.controllers;

import icstar.kbdsi.apps.dto.DeleteDto;
import icstar.kbdsi.apps.dto.PaginationBudgetDto;
import icstar.kbdsi.apps.dto.PaginationTransactionDto;
import icstar.kbdsi.apps.models.Budgeting;
import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.repository.BudgetingRepository;
import icstar.kbdsi.apps.services.BudgetingService;
import icstar.kbdsi.apps.util.BudgetingExcelGenerator;
    import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//https://kbdsi-icstar-g0cg82eie-lh007lucky-gmailcom.vercel.app/

// https://kbdsi-icstar-fe.vercel.app/login

// http://localhost:4200
@CrossOrigin(origins = {"http://localhost:4200", "http://https://kbdsi-icstar-fe.vercel.app/", "https://kbdsi-icstar-g0cg82eie-lh007lucky-gmailcom.vercel.app/" })

@RestController
@RequestMapping("/api")
public class BudgetingController {

    @Autowired
    BudgetingRepository budgetingRepository;

    BudgetingService budgetingService;

    public BudgetingController(BudgetingService budgetingService) {
        super();
        this.budgetingService = budgetingService;
    }

    @GetMapping("/budget")
    public ResponseEntity<List<Budgeting>> getAllBudgetting (@RequestParam(required = false) String name){
        try{
            List<Budgeting> budgetings = new ArrayList<Budgeting>();

            if(name == null)
                budgetings.addAll(budgetingRepository.findByIsDeleted(false));
            if(budgetings.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(budgetings, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/budget")
    public ResponseEntity<Budgeting> createBudgeting(@RequestBody Budgeting budgeting){
        try{
            Budgeting newBudgeting =  new Budgeting(budgeting.getName(), budgeting.getType(), budgeting.getCategory(), budgeting.getAmount(), budgeting.getDescription(), budgeting.getYear(), budgeting.getCreatedBy());
            budgetingRepository.save(newBudgeting);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/budget/{id}")
    public ResponseEntity<Budgeting> updateBudgetById(@PathVariable(required = true) Long id, @RequestBody Budgeting newBudgeting) {
        Optional<Budgeting> optBudgeting = budgetingRepository.findById(id);
        if(optBudgeting.isPresent()) {
            Budgeting oldBudgeting = optBudgeting.get();
            if(Integer.parseInt(oldBudgeting.getYear()) == new Date().getYear()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            else{
                oldBudgeting.setName(newBudgeting.getName());
                oldBudgeting.setAmount(newBudgeting.getAmount());
                oldBudgeting.setCategory(newBudgeting.getCategory());
                oldBudgeting.setYear(newBudgeting.getYear());
                oldBudgeting.setType(newBudgeting.getType());
                oldBudgeting.setDescription(newBudgeting.getDescription());
                oldBudgeting.setUpdatedBy(newBudgeting.getUpdatedBy());
                budgetingRepository.save(oldBudgeting);
                return new ResponseEntity<>(oldBudgeting, HttpStatus.OK);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/budget/delete/{id}")
    public ResponseEntity<String> deleteBudgeting(@PathVariable(required = true) Long id, @RequestBody DeleteDto deleteDto) {
        try {
            Optional<Budgeting> optBudgeting = budgetingRepository.findById(id);
            if(optBudgeting.isPresent()) {
                Budgeting oldBudgeting = optBudgeting.get();
                oldBudgeting.setDeleted(true);
                oldBudgeting.setUpdatedBy(deleteDto.getUpdatedBy());
                budgetingRepository.save(oldBudgeting);
                return new ResponseEntity<>("Budget has been deleted",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Budget doesn't exist",HttpStatus.NOT_FOUND);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/budget/total/{year}")
    private List<Integer> getTotalBudget(@PathVariable(required = true) String year ) {
        List<Integer> total = budgetingService.getTotal(year);
        return total;
    }

    @GetMapping("/budget/year")
    private Page<Budgeting> getBudgetingsWithFilterDesc(PaginationBudgetDto paginationBudgetDto) {
        Page<Budgeting> budgetings = budgetingService.PageBudgetByYear(paginationBudgetDto.getYear(), false,  paginationBudgetDto.getPageNum(), paginationBudgetDto.getPageSize());
        return budgetings;
    }

    @GetMapping("/budgeting/export-to-excel/{year}")
    public void exportIntoExcelFile(HttpServletResponse response, @PathVariable(required = true) String year) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue= "attachment; filename=budgeting_"+ year + "_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey,headerValue);

        List<Budgeting> listOfBudgeting = budgetingService.getAllBudget(year);
        BudgetingExcelGenerator generator = new BudgetingExcelGenerator(listOfBudgeting);
        generator.generateExcelFile(response);
    }
}
