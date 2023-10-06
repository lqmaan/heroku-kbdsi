package icstar.kbdsi.apps.services.impl;

import icstar.kbdsi.apps.models.Budgeting;
import icstar.kbdsi.apps.repository.BudgetingRepository;
import icstar.kbdsi.apps.repository.ReminderRepository;
import icstar.kbdsi.apps.services.BudgetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
    public Page<Budgeting> PageBudgetByYear(String name, boolean isDeleted, Integer pageNum, Integer pageSize) {
        return null;
    }
}
