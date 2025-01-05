package classfit.example.classfit.invitation.service;

import classfit.example.classfit.academy.domain.Academy;
import classfit.example.classfit.common.exception.ClassfitException;
import classfit.example.classfit.invitation.domain.Invitation;
import classfit.example.classfit.invitation.dto.request.InvitationRequest;
import classfit.example.classfit.invitation.dto.response.InvitationResponse;
import classfit.example.classfit.invitation.repository.InvitationRepository;
import classfit.example.classfit.mail.dto.request.EmailPurpose;
import classfit.example.classfit.mail.service.EmailService;
import classfit.example.classfit.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final EmailService emailService;

    @Transactional
    public String findAcademyCode(Member member) {
        Academy academy = member.getAcademy();

        if (academy == null) {
            throw new ClassfitException("해당 사용자는 학원에 등록되지 않았습니다.", HttpStatus.NOT_FOUND);
        }

        return academy.getCode();
    }

    @Transactional
    public void inviteStaffByEmail(Member member, InvitationRequest request) {
        emailService.sendEmail(request.email(), EmailPurpose.INVITATION);
        Invitation invitation = request.toEntity(member.getAcademy());

        invitationRepository.save(invitation);
    }

    @Transactional(readOnly = true)
    public List<InvitationResponse> staffInfoAll(Member member) {
        List<Invitation> invitations = invitationRepository.findByAcademy(member.getAcademy());
        return invitations.stream()
            .map(InvitationResponse::from)
            .collect(Collectors.toList());
    }
}