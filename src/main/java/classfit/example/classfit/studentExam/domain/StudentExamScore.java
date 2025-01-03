package classfit.example.classfit.studentExam.domain;

import classfit.example.classfit.scoreReport.domain.ScoreReport;
import classfit.example.classfit.student.domain.Student;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_exam_score")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentExamScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(name = "score", nullable = false)
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private ScoreReport scoreReport;

    @Builder
    public StudentExamScore(Student student, Exam exam, Integer score, ScoreReport scoreReport) {
        this.student = student;
        this.exam = exam;
        this.score = score;
        this.scoreReport = scoreReport;
    }

    public void updateScore(Integer score) {
        if (score < 0) {
            throw new IllegalArgumentException("점수는 음수일 수 없습니다.");
        }
        this.score = score;
    }

    public void updateScoreReport(ScoreReport scoreReport) {
        this.scoreReport = scoreReport;
    }
}
