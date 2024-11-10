package classfit.example.classfit.category.service;

import classfit.example.classfit.category.dto.request.MainClassRequest;
import classfit.example.classfit.category.dto.response.MainClassResponse;
import classfit.example.classfit.category.repository.MainClassRespository;
import classfit.example.classfit.common.ApiResponse;
import classfit.example.classfit.domain.MainClass;
import classfit.example.classfit.domain.Member;
import classfit.example.classfit.exception.ClassfitException;
import classfit.example.classfit.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainClassService {
    private final MainClassRespository mainClassRespository;
    private final MemberRepository memberRepository;

    // 메인 클래스 추가
    public ApiResponse<MainClassResponse> addMainClass(Long memberId, MainClassRequest req) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new ClassfitException("회원을 찾을 수 없어요", HttpStatus.NOT_FOUND));

        MainClass mainClass = new MainClass();
        mainClass.setMember(findMember);
        mainClass.setMainClassName(req.mainClassName());

         mainClassRespository.save(mainClass);

        return ApiResponse.success(new MainClassResponse(mainClass.getId(),mainClass.getMainClassName()),201,"CREATED");
    }

}
