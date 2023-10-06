package icstar.kbdsi.apps.services;

import icstar.kbdsi.apps.models.Budgeting;
import icstar.kbdsi.apps.models.Category;
import icstar.kbdsi.apps.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BudgetingService {
    List<Budgeting> getAllBudget(String year);

    Page<Budgeting> PageBudgetByYear(String year, boolean isDeleted, Integer pageNum, Integer pageSize);


}
