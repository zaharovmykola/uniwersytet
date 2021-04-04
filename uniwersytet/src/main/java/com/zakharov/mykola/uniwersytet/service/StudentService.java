package com.zakharov.mykola.uniwersytet.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.zakharov.mykola.uniwersytet.dao.StudentHibernateDAO;
import com.zakharov.mykola.uniwersytet.dao.predicate.StudentPredicatesBuilder;
import com.zakharov.mykola.uniwersytet.entity.Nauczyciel;
import com.zakharov.mykola.uniwersytet.entity.Student;
import com.zakharov.mykola.uniwersytet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentHibernateDAO studentDao;

    public ResponseModel create(StudentModel studentModel) {
        @Valid Student student =
                Student.builder()
                        .imie(studentModel.getImie().trim())
                        .nazwisko(studentModel.getNazwisko().trim())
                        .wiek(studentModel.getWiek())
                        .email(studentModel.getEmail().trim())
                        .kierunek(studentModel.getKierunek().trim())
                        .build();
        studentDao.save(student);
        // Demo Logging
        System.out.println(String.format("Student %s Created", student.getImie()));
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Student %s Created", student.getImie()))
                .build();
    }

    public ResponseModel update(StudentModel studentModel) {
        Student student =
                Student.builder()
                        .id(studentModel.getId())
                        .imie(studentModel.getImie().trim())
                        .nazwisko(studentModel.getNazwisko().trim())
                        .wiek(studentModel.getWiek())
                        .email(studentModel.getEmail().trim())
                        .kierunek(studentModel.getKierunek().trim())
                        .build();
        studentDao.save(student);
        // Demo Logging
        System.out.println(String.format("Student %s Updated", student.getImie()));
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Student %s Updated", student.getImie()))
                .build();
    }

    public ResponseModel delete(Long id) {
        Optional<Student> studentOptional = studentDao.findById(id);
        if (studentOptional.isPresent()){
            Student student = studentOptional.get();
            studentDao.delete(student);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Student #%s Deleted", student.getImie()))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Student #%d Not Found", id))
                    .build();
        }
    }

    public ResponseModel getAll() {
//        вызываем метод findAll с Sort по колонке id в обратном порядке descending
        List<Student> students = studentDao.findAll(Sort.by("id").descending());
        List<StudentModel> studentModels =
                students.stream()
                        .map(s ->
                                StudentModel.builder()
                                        .id(s.getId())
                                        .imie(s.getImie())
                                        .nazwisko(s.getNazwisko())
                                        .wiek(s.getWiek())
                                        .email(s.getEmail())
                                        .kierunek(s.getKierunek())
                                        .build()
                        )
                        .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(studentModels)
                .build();
    }

    // поиск отфильтрованного и отсортированного списка товаров
    // на основе запросов query dsl
    public ResponseModel search(StudentSearchModel searchModel) {
        List<Student> students = null;
        if (searchModel.searchString != null && !searchModel.searchString.isEmpty()) {
            StudentPredicatesBuilder builder = new StudentPredicatesBuilder();
            // разбиение значения http-параметра search
            // на отдельные выражения условий фильтрации
            Pattern pattern = Pattern.compile("([\\w]+?)(:|<|>|<:|>:)([\\w\\]\\[\\,]+?);");
            Matcher matcher = pattern.matcher(searchModel.searchString + ";");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            BooleanExpression expression = builder.build();
            // выполнение sql-запроса к БД
            // с набором условий фильтрации
            // и с указанием имени поля и направления сортировки
            students =
                    (List<Student>) studentDao.findAll(
                            expression,
                            Sort.by(
                                    searchModel.sortingDirection,
                                    searchModel.orderBy
                            )
                    );
        } else {
            students =
                    studentDao.findAll(
                            Sort.by(
                                    searchModel.sortingDirection,
                                    searchModel.orderBy
                            )
                    );
        }
        return getResponseModelFromEntities(students);
    }

    private ResponseModel getResponseModelFromEntities(List<Student> students) {
        List<StudentModel> studentModels =
                students.stream()
                        .map((s)->
                                StudentModel.builder()
                                        .id(s.getId())
                                        .imie(s.getImie())
                                        .nazwisko(s.getNazwisko())
                                        .wiek(s.getWiek())
                                        .email(s.getEmail())
                                        .kierunek(s.getKierunek())
                                        .build()
                        )
                        .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(studentModels)
                .build();
    }

    public ResponseModel getAllNauczyciele(Long id) {
        Optional<Student> studentOptional = studentDao.findById(id);
        if (studentOptional.isPresent()){
            Student student = studentOptional.get();
            StudentModel studentModel =
                    StudentModel.builder()
                            .nauczyciele(List.copyOf(student.getSetofNauczyciele()))
                            .build();
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .data(studentModel)
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Student #%d Not Found", id))
                    .build();
        }
    }

    public ResponseModel getStudentByImie(String imie) {
        Student student = studentDao.findStudentByImie(imie);
        if (student != null){
            StudentModel studentModel =
                    StudentModel.builder()
                            .id(student.getId())
                            .imie(student.getImie())
                            .nazwisko(student.getNazwisko())
                            .wiek(student.getWiek())
                            .email(student.getEmail())
                            .kierunek(student.getKierunek())
                            .build();
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .data(studentModel)
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Student #%s Not Found", imie))
                    .build();
        }
    }

    public ResponseModel getStudentByNazwisko(String nazwisko) {
        Student student = studentDao.findStudentByNazwisko(nazwisko);
        if (student != null){
            StudentModel studentModel =
                    StudentModel.builder()
                            .id(student.getId())
                            .imie(student.getImie())
                            .nazwisko(student.getNazwisko())
                            .wiek(student.getWiek())
                            .email(student.getEmail())
                            .kierunek(student.getKierunek())
                            .build();
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .data(studentModel)
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Student #%s Not Found", nazwisko))
                    .build();
        }
    }

}
