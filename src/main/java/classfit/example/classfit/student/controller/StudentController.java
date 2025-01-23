package classfit.example.classfit.student.controller;

import classfit.example.classfit.auth.annotation.AuthMember;
import classfit.example.classfit.common.CustomApiResponse;
import classfit.example.classfit.member.domain.Member;
import classfit.example.classfit.student.controller.docs.StudentControllerDocs;
import classfit.example.classfit.student.dto.request.StudentRequest;
import classfit.example.classfit.student.dto.request.StudentUpdateRequest;
import classfit.example.classfit.student.dto.response.StudentInfoResponse;
import classfit.example.classfit.student.dto.response.StudentResponse;
import classfit.example.classfit.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController implements StudentControllerDocs {

    private final StudentService studentService;

    @PostMapping("/")
    public CustomApiResponse<StudentResponse> registerStudent(@RequestBody @Valid StudentRequest req) {
        StudentResponse studentResponse = studentService.registerStudent(req);
        return CustomApiResponse.success(studentResponse, 201, "CREATED STUDENT");
    }

    @GetMapping("/")
    public CustomApiResponse<List<StudentResponse>> studentInfoAll(@AuthMember Member member) {
        List<StudentResponse> studentList = studentService.studentInfoAll(member);
        return CustomApiResponse.success(studentList, 200, "FIND STUDENTS");
    }

    @DeleteMapping("/")
    public CustomApiResponse<List<Long>> deleteStudent(
        @AuthMember Member member,
        @RequestParam List<Long> studentIds
    ) {
        studentService.deleteStudent(member, studentIds);
        return CustomApiResponse.success(studentIds.stream().toList(), 200, "DELETED STUDENT");
    }

    @PatchMapping("/{studentId}")
    public CustomApiResponse<Long> updateStudent(
        @PathVariable Long studentId,
        @RequestBody @Valid StudentUpdateRequest req
    ) {
        studentService.updateStudent(studentId, req);
        return CustomApiResponse.success(studentId, 200, "UPDATED STUDENT");
    }

    @GetMapping("/{studentId}")
    public CustomApiResponse<StudentInfoResponse> studentInfo(@PathVariable Long studentId) {
        StudentInfoResponse studentInfo = studentService.getStudentInfo(studentId);
        return CustomApiResponse.success(studentInfo, 200, studentInfo.name() + "의 정보");
    }

    @GetMapping("/search")
    public CustomApiResponse<List<StudentResponse>> findStudentByName(@RequestParam(value = "name") String studentName) {
        List<StudentResponse> findStudents = studentService.findStudentsByName(studentName);
        return CustomApiResponse.success(findStudents, 200, "FIND STUDENTS");
    }
}
