package classfit.example.classfit.memberCalendar.repository;

import classfit.example.classfit.member.domain.Member;
import classfit.example.classfit.memberCalendar.domain.CalendarType;
import classfit.example.classfit.memberCalendar.domain.MemberCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCalendarRepository extends JpaRepository<MemberCalendar, Long> {
    MemberCalendar findByMemberAndType(Member member, CalendarType type);
}
