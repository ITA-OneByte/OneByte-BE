package classfit.example.classfit.calendarCategory.service;

import classfit.example.classfit.common.annotation.AuthMember;
import classfit.example.classfit.calendarCategory.domain.CalendarCategory;
import classfit.example.classfit.calendarCategory.domain.CategoryColor;
import classfit.example.classfit.calendarCategory.dto.request.CalendarCategoryCreateRequest;
import classfit.example.classfit.calendarCategory.dto.request.CalendarCategoryUpdateRequest;
import classfit.example.classfit.calendarCategory.dto.response.CalendarCategoryCreateResponse;
import classfit.example.classfit.calendarCategory.dto.response.CalendarCategoryListResponse;
import classfit.example.classfit.calendarCategory.dto.response.CalendarCategoryResponse;
import classfit.example.classfit.calendarCategory.repository.CalendarCategoryRepository;
import classfit.example.classfit.common.exception.ClassfitException;
import classfit.example.classfit.common.response.ErrorCode;
import classfit.example.classfit.member.domain.Member;
import classfit.example.classfit.memberCalendar.domain.CalendarType;
import classfit.example.classfit.memberCalendar.domain.MemberCalendar;
import classfit.example.classfit.memberCalendar.service.MemberCalendarService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarCategoryService {
    private final CalendarCategoryRepository calendarCategoryRepository;
    private final MemberCalendarService memberCalendarService;

    @Transactional
    public CalendarCategoryCreateResponse addCategory(@AuthMember Member member, CalendarCategoryCreateRequest request) {
        CalendarCategory category = buildCategory(member, request);
        CalendarCategory savedCategory = calendarCategoryRepository.save(category);
        return CalendarCategoryCreateResponse.of(savedCategory.getId(), savedCategory.getName(),
                String.valueOf(savedCategory.getColor()), savedCategory.getMemberCalendar().getType());
    }

    private CalendarCategory buildCategory(Member member, CalendarCategoryCreateRequest request) {
        MemberCalendar memberCalendar = getMemberCalendarByMemberAndType(member, request.type());
        String uniqueName = createUniqueCategoryName(request.name(), -1L);

        return CalendarCategory.builder()
                .name(uniqueName)
                .color(CategoryColor.valueOf(request.color()))
                .memberCalendar(memberCalendar)
                .build();
    }

    private MemberCalendar getMemberCalendarByMemberAndType(Member member, CalendarType type) {
        return memberCalendarService.getMemberCalendar(member, type);
    }

    private String createUniqueCategoryName(String baseName, Long categoryId) {
        String uniqueName = baseName;
        int count = 1;

        while (isDuplicateName(uniqueName, categoryId)) {
            uniqueName = baseName + " (" + count + ")";
            count++;
        }
        return uniqueName;
    }

    private boolean isDuplicateName(String name, Long categoryId) {
        return calendarCategoryRepository.existsByNameAndIdNot(name, categoryId);
    }

    @Transactional(readOnly = true)
    public CalendarCategoryListResponse getCategories(@AuthMember Member member) {
        MemberCalendar personalCalendar = getMemberCalendarByMemberAndType(member, CalendarType.PERSONAL);
        List<CalendarCategoryResponse> personalCategories = classifyAndSortCategories(personalCalendar);

        List<CalendarCategoryResponse> sharedCategories = classifyAndSortSharedCategories(member);

        return CalendarCategoryListResponse.of(personalCategories, sharedCategories);
    }

    private List<CalendarCategoryResponse> classifyAndSortCategories(MemberCalendar memberCalendar) {
        return calendarCategoryRepository.findByMemberCalendar(memberCalendar)
                .stream()
                .map(category -> CalendarCategoryResponse.of(
                        category.getId(),
                        category.getName(),
                        category.getColor()
                ))
                .sorted(Comparator.comparing(CalendarCategoryResponse::name))
                .collect(Collectors.toList());
    }

    private List<CalendarCategoryResponse> classifyAndSortSharedCategories(Member member) {
        Long academyId = member.getAcademy().getId();

        return calendarCategoryRepository.findByMemberCalendarTypeAndAcademyId(CalendarType.SHARED, academyId)
                .stream()
                .map(category -> CalendarCategoryResponse.of(
                        category.getId(),
                        category.getName(),
                        category.getColor()
                ))
                .sorted(Comparator.comparing(CalendarCategoryResponse::name))
                .collect(Collectors.toList());
    }

    @Transactional
    public CalendarCategoryResponse updateCategory(Long categoryId, CalendarCategoryUpdateRequest request) {
        CalendarCategory category = getCategoryById(categoryId);

        String uniqueName = createUniqueCategoryName(request.newName(), categoryId);
        category.updateNameAndColor(uniqueName, request.newColor());

        return CalendarCategoryResponse.of(category.getId(), category.getName(), category.getColor());
    }

    public CalendarCategory getCategoryById(Long categoryId) {
        return calendarCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ClassfitException(ErrorCode.EVENT_CATEGORY_NOT_FOUND));
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        CalendarCategory category = getCategoryById(categoryId);
        calendarCategoryRepository.delete(category);
    }
}
