package umk.mat.pajda.ProjektZespolowy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.DTO.TipMonthDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.entity.User;

@Repository
public interface TipRepository extends JpaRepository<Tip, Integer> {
  Tip findFirstByUserOrderByAmountDesc(User user);

  Tip findFirstByUserOrderByAmountAsc(User user);

  @Query("select AVG(t.amount) from Tip t where t.user.id = :userid")
  Double getAvgAmountForAllTips(@Param("userid") Integer userid);

  @Query("select count(*) from Tip t where t.user.id = :userid")
  Integer getNumberOfTips(@Param("userid") Integer userid);

  @Query(
      "select new umk.mat.pajda.ProjektZespolowy.DTO.TipMonthDTO(SUM(t.amount),"
          + "EXTRACT(month from t.createdAt),EXTRACT(year from t.createdAt),t.currency) "
          + "from Tip t where t.user.id = :userid "
          + "group by EXTRACT(month from t.createdAt), EXTRACT(year from t.createdAt),t.currency "
          + "order by EXTRACT(year from t.createdAt) desc,EXTRACT(month from t.createdAt) desc")
  List<TipMonthDTO> getSumAmountForEachMonth(@Param("userid") Integer userid);
}
