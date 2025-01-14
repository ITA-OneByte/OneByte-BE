package classfit.example.classfit.scoreReport.domain;

import classfit.example.classfit.academy.domain.Academy;
import classfit.example.classfit.scoreReport.dto.process.ReportExam;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreReportRepository extends JpaRepository<ScoreReport, Long> {

    @Query("SELECT new classfit.example.classfit.scoreReport.dto.process.ReportExam(" +
            "e.id, e.examPeriod, e.mainClass.mainClassName, e.subClass.subClassName, e.examName,e.createdAt) " +
            "FROM Exam e " +
            "WHERE FUNCTION('DATE', e.createdAt) BETWEEN :startDate AND :endDate " +
            "AND e.mainClass.id = :mainClassId " +
            "AND e.subClass.id = :subClassId " +
            "ORDER BY e.createdAt ASC")
    List<ReportExam> findExamsByCreatedAtBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("mainClassId") Long mainClassId,
            @Param("subClassId") Long subClassId);

    @Query("""
            SELECT sr
            FROM ScoreReport sr
            WHERE sr.mainClass.id = :mainClassId
              AND sr.subClass.id = :subClassId
""")
    List<ScoreReport> findAllReportsByMainClassAndSubClass(@Param("mainClassId") Long mainClassId,
            @Param("subClassId") Long subClassId);


    @Query("SELECT sr FROM ScoreReport sr WHERE sr.id = :studentReportId")
    Optional<ScoreReport> findByStudentReportId(@Param("studentReportId") Long studentReportId);

    @Query("SELECT r FROM ScoreReport r " +
            "WHERE r.mainClass.academy = :academy")
    List<ScoreReport> findAllByAcademy(@Param("academy") Academy academy);

}

