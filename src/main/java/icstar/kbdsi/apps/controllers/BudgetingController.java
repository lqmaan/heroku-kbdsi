package icstar.kbdsi.apps.controllers;

import icstar.kbdsi.apps.models.Budgeting;
import icstar.kbdsi.apps.repository.BudgetingRepository;
import icstar.kbdsi.apps.services.BudgetingService;
import icstar.kbdsi.apps.util.BudgetingExcelGenerator;
    import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

@CrossOrigin(origins = "http://localhost:4200")
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
                budgetings.addAll(budgetingRepository.findAll());
//            else
//                categoryRepository.findAllByName(name, PageRequest.of(0, 10, Sort.by("name").ascending()));
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
            oldBudgeting.setName(newBudgeting.getName());
            oldBudgeting.setAmount(newBudgeting.getAmount());
            oldBudgeting.setCategory(newBudgeting.getCategory());
            oldBudgeting.setDescription(newBudgeting.getDescription());
            oldBudgeting.setUpdatedBy(newBudgeting.getUpdatedBy());
            budgetingRepository.save(oldBudgeting);
            return new ResponseEntity<>(oldBudgeting, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/budget/{id}")
    public ResponseEntity<String> deleteBudgeting(@PathVariable(required = true) Long id, @RequestBody String updatedBy) {
        try {
            Optional<Budgeting> optBudgeting = budgetingRepository.findById(id);
            if(optBudgeting.isPresent()) {
                Budgeting oldBudgeting = optBudgeting.get();
                oldBudgeting.setDeleted(true);
                oldBudgeting.setUpdatedBy(updatedBy);
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
