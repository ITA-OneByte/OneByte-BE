package classfit.example.classfit.attendance.dto.response;

public record StatisticsMemberResponse(
    Long studentId,
    String name,
    int present,
    int absent,
    int late,
    int extra) {

    public static StatisticsMemberResponse of(final Long studentId, final String name,
        final int present, final int absent, final int late, final int extra) {
        return new StatisticsMemberResponse(studentId, name, present, absent, late, 0);
    }
}