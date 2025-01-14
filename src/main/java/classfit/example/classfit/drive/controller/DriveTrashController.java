package classfit.example.classfit.drive.controller;

import classfit.example.classfit.auth.annotation.AuthMember;
import classfit.example.classfit.common.ApiResponse;
import classfit.example.classfit.drive.domain.DriveType;
import classfit.example.classfit.drive.dto.response.FileResponse;
import classfit.example.classfit.drive.service.DriveDeleteService;
import classfit.example.classfit.drive.service.DriveRestoreService;
import classfit.example.classfit.drive.service.DriveTrashService;
import classfit.example.classfit.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drive")
@Tag(name = "드라이브 휴지통 컨트롤러", description = "드라이브 휴지통 관련 API입니다.")
public class DriveTrashController {

    private final DriveTrashService driveTrashService;
    private final DriveRestoreService driveRestoreService;
    private final DriveDeleteService driveDeleteService;

    @GetMapping("/trash")
    @Operation(summary = "휴지통 조회", description = "휴지통 조회 API 입니다.")
    public ApiResponse<List<FileResponse>> trashList(
        @AuthMember Member member,
        @Parameter(description = "내 드라이브는 PERSONAL, 공용 드라이브는 SHARED 입니다.")
        @RequestParam DriveType driveType
    ) {
        List<FileResponse> filesFromTrash = driveTrashService.getFilesFromTrash(member, driveType);
        return ApiResponse.success(filesFromTrash, 200, "조회 성공");
    }

    @PostMapping("/trash")
    @Operation(summary = "휴지통 이동", description = "휴지통 이동 API 입니다.")
    public ApiResponse<List<String>> moveToTrash(
        @AuthMember Member member,
        @Parameter(description = "내 드라이브는 PERSONAL, 공용 드라이브는 SHARED 입니다.")
        @RequestParam DriveType driveType,
        @Parameter(description = "폴더 경로입니다. 비어 있으면 루트 폴더로 지정됩니다.")
        @RequestParam(required = false, defaultValue = "") String folderPath,
        @Parameter(description = "파일 이름")
        @RequestParam List<String> fileNames
    ) {
        List<String> trashPathList = driveTrashService.moveToTrash(member, driveType, folderPath, fileNames);
        return ApiResponse.success(trashPathList, 200, "휴지통 이동 완료");
    }

    @PostMapping("/trash/restore")
    @Operation(summary = "휴지통 복원", description = "휴지통 복원 API 입니다.")
    public ApiResponse<List<String>> restoreFromTrash(
        @AuthMember Member member,
        @Parameter(description = "내 드라이브는 PERSONAL, 공용 드라이브는 SHARED 입니다.")
        @RequestParam DriveType driveType,
        @Parameter(description = "파일 이름")
        @RequestParam List<String> fileNames
    ) {
        List<String> restorePathList = driveRestoreService.restoreFromTrash(member, driveType, fileNames);
        return ApiResponse.success(restorePathList, 200, "복원 성공");
    }

    @DeleteMapping("/trash")
    @Operation(summary = "휴지통 영구삭제", description = "휴지통 영구삭제 API 입니다.")
    public ApiResponse<Nullable> deleteFromTrash(
        @AuthMember Member member,
        @Parameter(description = "내 드라이브는 PERSONAL, 공용 드라이브는 SHARED 입니다.")
        @RequestParam DriveType driveType,
        @Parameter(description = "파일 이름")
        @RequestParam List<String> fileName
    ) {
        driveDeleteService.deleteFromTrash(member, driveType, fileName);
        return ApiResponse.success(null, 200, "삭제 성공");
    }
}
