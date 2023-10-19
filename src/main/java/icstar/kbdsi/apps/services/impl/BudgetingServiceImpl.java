package icstar.kbdsi.apps.services.impl;

import icstar.kbdsi.apps.models.Budgeting;
import icstar.kbdsi.apps.repository.BudgetingRepository;
import icstar.kbdsi.apps.repository.ReminderRepository;
import icstar.kbdsi.apps.services.BudgetingService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetingServiceImpl implements BudgetingService {

    @Autowired
    BudgetingRepository budgetingRepository;

    public BudgetingServiceImpl(BudgetingRepository budgetingRepository){
        super();
        this.budgetingRepository = budgetingRepository;
    }
    @Override
    public List<Budgeting> getAllBudget(String year) {
        return budgetingRepository.findByYearAndIsDeleted(year, false);
    }

    @Override
    public List<Integer> getTotal(String year) {
        List<Budgeting> budgetings = budgetingRepository.findByYearAndIsDeleted(year, false);
        int totalIncome = 0;
        int totalOutcome = 0;
        for (Budgeting budget:budgetings) {
            System.out.println(budget.getType());
            if(budget.getType().equals("income")){
                totalIncome += budget.getAmount();
                System.out.println(totalIncome + "income " + budget.getAmount());
            }
            else if(budget.getType().equals("outcome")) {
                totalOutcome += budget.getAmount();
                System.out.println(totalOutcome + " outcome " + budget.getAmount());

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
    public Page<Budgeting> PageBudgetByYear(String year, boolean isDeleted, Integer pageNum, Integer pageSize) {
        return budgetingRepository.findByYearContainingAndIsDeleted(year, isDeleted, PageRequest.of(pageNum, pageSize), Sort.by("createdAt").descending().and(Sort.by("name")));
    }
}
