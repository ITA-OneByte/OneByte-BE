package classfit.example.classfit.student.controller;

import classfit.example.classfit.common.ApiResponse;
import classfit.example.classfit.student.dto.request.StudentRequest;
import classfit.example.classfit.student.dto.request.UpdateStudentRequest;
import classfit.example.classfit.student.dto.response.StudentResponse;
import classfit.example.classfit.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
@Tag(name = "Student API", description = "학생 관련 API")
public class StudentControllerImpl {

    private final StudentService studentService;

    @PostMapping("/")
    @Operation(summary = "학생 정보 등록", description = "학생 정보 등록하는 API 입니다. "
        + "[성별 : MALE/FEMALE], [생년월일 : YYYY-MM-DD 형식], [전화번호 - 010-0000-0000 형식] ")
    public ApiResponse<StudentResponse> registerStudent(@RequestBody @Valid StudentRequest req) {

        StudentResponse studentResponse = studentService.registerStudent(req);
        return ApiResponse.success(studentResponse, 201, "CREATED STUDENT");
    }

    @GetMapping("/")
    @Operation(summary = "학생 정보 조회", description = "전체 학생 정보 조회하는 API 입니다. ")
    public ApiResponse<List<StudentResponse>> getStudentList() {

        List<StudentResponse> studentList = studentService.getStudentList();
        return ApiResponse.success(studentList, 200, "FIND STUDENTS");
    }

    @DeleteMapping("/{studentId}")
    @Operation(summary = "학생 정보 삭제", description = "학생 정보 삭제하는 API 입니다. ")
    public ApiResponse<StudentResponse> deleteStudent(@PathVariable Long studentId) {

        studentService.deleteStudent(studentId);
        return ApiResponse.success(null, 200, "DELETED STUDENT");
    }

    @PatchMapping("/{studentId}")
    @Operation(summary = "학생 정보 수정", description = "학생 정보를 수정하는 API 입니다. "
        + "변경하고자 하고자 하는 데이터만 입력")
    public ApiResponse<StudentResponse> updateStudent(
        @PathVariable Long studentId, @RequestBody @Valid UpdateStudentRequest req) {

        studentService.updateStudent(studentId, req);
        return ApiResponse.success(null, 200, "UPDATED STUDENT");
    }

    @GetMapping("/{studentId}")
    @Operation(summary = "개인 학생 정보 조회", description = "특정 학생 정보를 조회하는 API 입니다. ")
    public ApiResponse<StudentResponse> studentInfo(@PathVariable Long studentId) {

        StudentResponse studentInfo = studentService.getStudentInfo(studentId);
        return ApiResponse.success(studentInfo, 200, studentInfo.name() + "의 정보");
    }
}