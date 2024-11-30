package classfit.example.classfit.attendance.service;

import classfit.example.classfit.attendance.domain.Attendance;
import classfit.example.classfit.attendance.domain.AttendanceStatus;
import classfit.example.classfit.attendance.dto.response.StatisticsDateResponse;
import classfit.example.classfit.attendance.repository.AttendanceRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceStatisticsService {

    private final AttendanceRepository attendanceRepository;

    public List<StatisticsDateResponse> getAttendanceStatisticsByDate(LocalDate startDate, LocalDate endDate, Long subClassId) {
        List<Attendance> attendances = attendanceRepository.findByDateBetween(startDate, endDate);

        // 날짜별로 출석 통계를 계산
        return attendances.stream()
            .collect(Collectors.groupingBy(Attendance::getDate)) // 날짜별로 그룹화
            .entrySet().stream()
            .map(entry -> {
                LocalDate date = entry.getKey();
                List<Attendance> dayAttendances = entry.getValue();

                // 출석 상태별 카운팅
                int presentCount = (int) dayAttendances.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
                int absentCount = (int) dayAttendances.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
                int lateCount = (int) dayAttendances.stream().filter(a -> a.getStatus() == AttendanceStatus.LATE).count();
                int extraCount = 0;

                // 날짜별 출석 통계 응답 생성 (subClassId 포함)
                return new StatisticsDateResponse(
                    date,
                    dayAttendances.get(0).getWeek(),
                    presentCount,
                    absentCount,
                    lateCount,
                    extraCount
                );
            })
            .sorted(Comparator.comparing(StatisticsDateResponse::date)
                .thenComparingInt(StatisticsDateResponse::week))
            .collect(Collectors.toList());
    }
}