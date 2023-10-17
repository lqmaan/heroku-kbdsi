package icstar.kbdsi.apps.repository;

import icstar.kbdsi.apps.models.Budgeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.logging.SocketHandler;

public interface BudgetingRepository  extends JpaRepository<Budgeting, Long> {
    List<Budgeting> findByYearAndIsDeleted(String year, boolean isDeleted);

    Page<Budgeting> findByYearContainingAndIsDeleted(String year, boolean isDeleted, PageRequest pageable, Sort sort);


}
