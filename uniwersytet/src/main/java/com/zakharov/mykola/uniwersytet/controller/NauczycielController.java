package com.zakharov.mykola.uniwersytet.controller;

import com.zakharov.mykola.uniwersytet.model.NauczycielModel;
import com.zakharov.mykola.uniwersytet.model.NauczycielSearchModel;
import com.zakharov.mykola.uniwersytet.model.ResponseModel;
import com.zakharov.mykola.uniwersytet.service.NauczycielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nauczyciel")
public class NauczycielController {

    @Autowired
    private NauczycielService service;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ResponseModel> create(@RequestBody NauczycielModel nauczyciel) {
        return new ResponseEntity<>(service.create(nauczyciel), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ResponseModel> update(@PathVariable Long id, @RequestBody NauczycielModel nauczyciel) {
        nauczyciel.setId(id);
        return new ResponseEntity<>(service.update(nauczyciel), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseModel> deleteNauczyciel(@PathVariable Long id) {
        ResponseModel responseModel = service.delete(id);
        System.out.println(responseModel);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    // поиск списка товаров согласно query dsl-запроса из http-параметра search
    // и сортировка по значению поля orderBy в направлении sortingDirection,
    // заданным как часть начальной строки с произвольно выбранными разделителями:
    // "::" - между парами ключ-значение,
    // ":" - между каждым ключом и его значением
    @GetMapping("/nauczyciele/filtered::orderBy:{orderBy}::sortingDirection:{sortingDirection}")
    public ResponseEntity<ResponseModel> search(
            @RequestParam(value = "search") String searchString,
            @PathVariable String orderBy,
            @PathVariable Sort.Direction sortingDirection
    ) {
        System.err.println("searchString = " + searchString);
        return new ResponseEntity<>(
                service.search(
                        new NauczycielSearchModel(searchString, orderBy, sortingDirection)
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ResponseModel> getAllStudents(@PathVariable Long id) {
        return new ResponseEntity<>(service.getAllStudents(id), HttpStatus.OK);
    }

    @GetMapping("/byImie/{imie}")
    public ResponseEntity<ResponseModel> getNauczycielByImie(@PathVariable String imie) {
        return new ResponseEntity<>(service.getNauczycielByImie(imie), HttpStatus.OK);
    }

    @GetMapping("/byNazwisko/{nazwisko}")
    public ResponseEntity<ResponseModel> getNauczycielByNazwisko(@PathVariable String nazwisko) {
        return new ResponseEntity<>(service.getNauczycielByNazwisko(nazwisko), HttpStatus.OK);
    }

}
