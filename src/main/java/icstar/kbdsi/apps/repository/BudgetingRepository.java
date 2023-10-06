package icstar.kbdsi.apps.repository;

import icstar.kbdsi.apps.models.Budgeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetingRepository  extends JpaRepository<Budgeting, Long> {
    List<Budgeting> findByYearAndIsDeleted(String year, boolean isDeleted);
}
