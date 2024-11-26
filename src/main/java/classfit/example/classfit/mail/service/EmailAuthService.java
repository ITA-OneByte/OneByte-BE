package classfit.example.classfit.mail.service;

import classfit.example.classfit.common.exception.ClassfitException;
import classfit.example.classfit.mail.dto.request.EmailAuthRequest;
import classfit.example.classfit.mail.dto.request.EmailAuthVerifyRequest;
import classfit.example.classfit.mail.dto.response.EmailAuthResponse;
import classfit.example.classfit.member.repository.MemberRepository;
import classfit.example.classfit.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    @Transactional
    public EmailAuthResponse sendEmail(EmailAuthRequest request) {

        boolean existsByEmail = memberRepository.existsByEmail(fromEmail);

        if (existsByEmail) {
            throw new ClassfitException("이미 가입된 이메일입니다.", HttpStatus.CONFLICT);
        }

        String email = createEmailForm(request.email());
        return EmailAuthResponse.from(email);
    }

    @Transactional
    public EmailAuthResponse verifyAuthCode(EmailAuthVerifyRequest request) {
        String authCode = redisUtil.getData(request.email()).toString();

        if (!authCode.equals(request.code())) {
            throw new ClassfitException("인증 번호가 일치하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        return EmailAuthResponse.from(request.email());
    }

    private String createEmailForm(String email) {

        String authCode = createdCode();

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[ClassFit] 인증코드 확인");
            message.setText(setContext(authCode), "utf-8", "html");

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new ClassfitException("이메일 전송에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        redisUtil.setDataExpire(email, authCode, 60 * 10L);         // 10분동안 유효
        return email;
    }

    private String createdCode() {
        int leftLimit = 48;             // number '0'
        int rightLimit = 122;           // alphabet 'z'
        int targetStringLength = 8;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    private String setContext(String authCode) {
        Context context = new Context();
        context.setVariable("code", authCode);
        return templateEngine.process("mail", context);     // mail.html
    }
}
