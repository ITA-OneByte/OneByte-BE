package classfit.example.classfit.scoreReport.dto.response;

import java.time.LocalDate;

public record FindReportResponse(Long studentReportId, Long studentId, String studentName,
                                 String reportName, String memberName,
                                 LocalDate createAt) {

}