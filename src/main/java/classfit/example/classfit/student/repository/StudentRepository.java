package classfit.example.classfit.student.repository;

import classfit.example.classfit.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
  Page<Student> findAll(Pageable pageable);
  
  @Query("SELECT cs.subClass.subClassName FROM ClassStudent cs " +
        "JOIN cs.student s " +
        "WHERE s.id = :studentId")
  List<String> findSubClassesByStudentId(@Param("studentId") Long studentId);
  
  Optional<Student> findByName(String studentName);
}
