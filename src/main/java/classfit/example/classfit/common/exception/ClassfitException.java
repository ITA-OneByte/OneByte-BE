package classfit.example.classfit.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClassfitException extends RuntimeException {
    public static final String INVALID_ENTITY_TYPE = "유효하지 않은 엔티티 타입입니다.";
    public static final String INVALID_STATUS_TYPE = "유효하지 않은 출결 타입입니다.";
    public static final String INVALID_WEEK_VALUE = "4주 전부터 2주 후까지 조회가 가능합니다.";
    public static final String INVALID_MEMBER_ACADEMY = "로그인한 멤버는 학원에 소속되어 있지 않습니다.";
    public static final String STUDENT_NOT_FOUND = "학생을 찾을 수 없습니다.";
    public static final String ATTENDANCE_NOT_FOUND = "출결 정보를 찾을 수 없습니다.";
    public static final String MAIN_CLASS_NOT_FOUND = "메인 클래스를 찾을 수 없습니다.";
    public static final String SUB_CLASS_NOT_FOUND = "서브 클래스를 찾을 수 없습니다.";
    public static final String CATEGORY_NOT_FOUND = "해당 카테고리를 찾을 수 없습니다.";
    public static final String EVENT_NOT_FOUND = "해당 일정을 찾을 수 없습니다.";
    public static final String ACADEMY_MEMBERS_NOT_FOUND = "학원의 멤버들을 찾을 수 없습니다.";
    public static final String MEMBER_NOT_FOUND = "멤버를 찾을 수 없습니다.";

    private final String message;
    private final HttpStatus httpStatus;

    public ClassfitException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getHttpStatusCode() {
        return this.httpStatus.value();
    }
}
