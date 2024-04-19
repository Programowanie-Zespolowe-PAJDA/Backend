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

  Tip findFirstByUserAndCurrencyOrderByAmountDesc(User user, String targetCurrency);

  Tip findFirstByUserAndCurrencyOrderByAmountAsc(User user, String targetCurrency);

  @Query("SELECT AVG(t.amount) FROM Tip t WHERE t.user.id = :userid ")
  Double getAvgAmountForAllTips(@Param("userid") Integer userid);

  @Query(
      "SELECT AVG(t.realAmount) FROM Tip t WHERE t.user.id = :userid AND t.currency = :targetCurrency ")
  Double getAvgAmountForAllTips(
      @Param("userid") Integer userid, @Param("targetCurrency") String targetCurrency);

  @Query("SELECT count(*) FROM Tip t WHERE t.user.id = :userid ")
  Integer getNumberOfTips(@Param("userid") Integer userid);

  @Query("SELECT count(*) FROM Tip t WHERE t.user.id = :userid AND t.currency = :targetCurrency ")
  Integer getNumberOfTips(
      @Param("userid") Integer userid, @Param("targetCurrency") String targetCurrency);

  @Query(
      "SELECT new umk.mat.pajda.ProjektZespolowy.DTO.TipMonthDTO(SUM(t.amount),"
          + "EXTRACT(month from t.createdAt),EXTRACT(year from t.createdAt)) "
          + "FROM Tip t WHERE t.user.id = :userid "
          + "GROUP BY EXTRACT(month from t.createdAt), EXTRACT(year from t.createdAt) "
          + "ORDER BY EXTRACT(year from t.createdAt) DESC,EXTRACT(month from t.createdAt) DESC ")
  List<TipMonthDTO> getSumAmountForEachMonth(@Param("userid") Integer userid);

  @Query(
      "SELECT new umk.mat.pajda.ProjektZespolowy.DTO.TipMonthDTO(SUM(t.realAmount),"
          + "EXTRACT(month from t.createdAt),EXTRACT(year from t.createdAt)) "
          + "FROM Tip t WHERE t.user.id = :userid AND t.currency = :targetCurrency "
          + "GROUP BY EXTRACT(month from t.createdAt), EXTRACT(year from t.createdAt) "
          + "ORDER BY EXTRACT(year from t.createdAt) DESC,EXTRACT(month from t.createdAt) DESC ")
  List<TipMonthDTO> getSumAmountForEachMonth(
      @Param("userid") Integer userid, @Param("targetCurrency") String targetCurrency);
}
