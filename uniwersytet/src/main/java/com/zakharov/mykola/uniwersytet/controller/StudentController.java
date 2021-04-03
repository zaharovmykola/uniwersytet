package com.zakharov.mykola.uniwersytet.controller;

import com.zakharov.mykola.uniwersytet.model.ResponseModel;
import com.zakharov.mykola.uniwersytet.model.StudentModel;
import com.zakharov.mykola.uniwersytet.model.StudentSearchModel;
import com.zakharov.mykola.uniwersytet.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService service;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ResponseModel> create(@RequestBody StudentModel student) {
        return new ResponseEntity<>(service.create(student), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ResponseModel> update(@PathVariable Long id, @RequestBody StudentModel student) {
        student.setId(id);
        return new ResponseEntity<>(service.update(student), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseModel> deleteStudent(@PathVariable Long id) {
        ResponseModel responseModel = service.delete(id);
        System.out.println(responseModel);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    // поиск списка товаров согласно query dsl-запроса из http-параметра search
    // и сортировка по значению поля orderBy в направлении sortingDirection,
    // заданным как часть начальной строки с произвольно выбранными разделителями:
    // "::" - между парами ключ-значение,
    // ":" - между каждым ключом и его значением
    @GetMapping("/students/filtered::orderBy:{orderBy}::sortingDirection:{sortingDirection}")
    public ResponseEntity<ResponseModel> search(
            @RequestParam(value = "search") String searchString,
            @PathVariable String orderBy,
            @PathVariable Sort.Direction sortingDirection
    ) {
        System.err.println("searchString = " + searchString);
        return new ResponseEntity<>(
                service.search(
                        new StudentSearchModel(searchString, orderBy, sortingDirection)
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/nauczyciele/{id}")
    public ResponseEntity<ResponseModel> getAllNauczyciele(@PathVariable Long id) {
        return new ResponseEntity<>(service.getAllNauczyciele(id), HttpStatus.OK);
    }

}
