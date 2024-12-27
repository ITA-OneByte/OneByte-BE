package classfit.example.classfit.scoreReport.domain;

import classfit.example.classfit.category.domain.MainClass;
import classfit.example.classfit.category.domain.SubClass;
import classfit.example.classfit.common.domain.BaseEntity;
import classfit.example.classfit.student.domain.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "score_report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScoreReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_class_id", nullable = false)
    private SubClass subClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_class_id", nullable = false)
    private MainClass mainClass;

    @Column(name = "overall_opinion")
    private String overallOpinion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student",nullable = false)
    private Student student;

    @Builder
    public ScoreReport(SubClass subClass,MainClass mainClass,String reportName,Student student,LocalDate startDate,LocalDate endDate,String overallOpinion) {
        this.subClass = subClass;
        this.mainClass = mainClass;
        this.reportName = reportName;
        this.student = student;
        this.startDate = startDate;
        this.endDate = endDate;
        this.overallOpinion = overallOpinion;
    }
}
