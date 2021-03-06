package com.zakharov.mykola.uniwersytet.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.zakharov.mykola.uniwersytet.dao.NauczycielHibernateDAO;
import com.zakharov.mykola.uniwersytet.dao.predicate.NauczycielPredicatesBuilder;
import com.zakharov.mykola.uniwersytet.entity.Nauczyciel;
import com.zakharov.mykola.uniwersytet.model.NauczycielModel;
import com.zakharov.mykola.uniwersytet.model.NauczycielSearchModel;
import com.zakharov.mykola.uniwersytet.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class NauczycielService {

    @Autowired
    private NauczycielHibernateDAO nauczycielDao;

    public ResponseModel create(NauczycielModel nauczycielModel) {
        @Valid Nauczyciel nauczyciel =
                Nauczyciel.builder()
                        .imie(nauczycielModel.getImie().trim())
                        .nazwisko(nauczycielModel.getNazwisko().trim())
                        .wiek(nauczycielModel.getWiek())
                        .email(nauczycielModel.getEmail().trim())
                        .przedmiot(nauczycielModel.getPrzedmiot().trim())
                        .build();
        nauczycielDao.save(nauczyciel);
        // Demo Logging
        System.out.println(String.format("Nauczyciel %s Created", nauczyciel.getImie()));
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Nauczyciel %s Created", nauczyciel.getImie()))
                .build();
    }

    public ResponseModel update(NauczycielModel nauczycielModel) {
        Nauczyciel nauczyciel =
                Nauczyciel.builder()
                        .id(nauczycielModel.getId())
                        .imie(nauczycielModel.getImie().trim())
                        .nazwisko(nauczycielModel.getNazwisko().trim())
                        .wiek(nauczycielModel.getWiek())
                        .email(nauczycielModel.getEmail().trim())
                        .przedmiot(nauczycielModel.getPrzedmiot().trim())
                        .build();
        nauczycielDao.save(nauczyciel);
        // Demo Logging
        System.out.println(String.format("Nauczyciel %s Updated", nauczyciel.getImie()));
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Nauczyciel %s Updated", nauczyciel.getImie()))
                .build();
    }

    public ResponseModel delete(Long id) {
        Optional<Nauczyciel> nauczycielOptional = nauczycielDao.findById(id);
        if (nauczycielOptional.isPresent()){
            Nauczyciel nauczyciel = nauczycielOptional.get();
            nauczycielDao.delete(nauczyciel);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Nauczyciel #%s Deleted", nauczyciel.getImie()))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Nauczyciel #%d Not Found", id))
                    .build();
        }
    }

    public ResponseModel getAll() {
//        ???????????????? ?????????? findAll ?? Sort ???? ?????????????? id ?? ???????????????? ?????????????? descending
        List<Nauczyciel> nauczyciele = nauczycielDao.findAll(Sort.by("id").descending());
        List<NauczycielModel> nauczycielModels =
                nauczyciele.stream()
                        .map(n ->
                                NauczycielModel.builder()
                                        .id(n.getId())
                                        .imie(n.getImie())
                                        .nazwisko(n.getNazwisko())
                                        .wiek(n.getWiek())
                                        .email(n.getEmail())
                                        .przedmiot(n.getPrzedmiot())
                                        .build()
                        )
                        .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(nauczycielModels)
                .build();
    }

    // ?????????? ???????????????????????????????? ?? ???????????????????????????????? ???????????? ??????????????
    // ???? ???????????? ???????????????? query dsl
    public ResponseModel search(NauczycielSearchModel searchModel) {
        List<Nauczyciel> nauczyciele = null;
        if (searchModel.searchString != null && !searchModel.searchString.isEmpty()) {
            NauczycielPredicatesBuilder builder = new NauczycielPredicatesBuilder();
            // ?????????????????? ???????????????? http-?????????????????? search
            // ???? ?????????????????? ?????????????????? ?????????????? ????????????????????
            Pattern pattern = Pattern.compile("([\\w]+?)(:|<|>|<:|>:)([\\w\\]\\[\\,]+?);");
            Matcher matcher = pattern.matcher(searchModel.searchString + ";");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            BooleanExpression expression = builder.build();
            // ???????????????????? sql-?????????????? ?? ????
            // ?? ?????????????? ?????????????? ????????????????????
            // ?? ?? ?????????????????? ?????????? ???????? ?? ?????????????????????? ????????????????????
            nauczyciele =
                    (List<Nauczyciel>) nauczycielDao.findAll(
                            expression,
                            Sort.by(
                                    searchModel.sortingDirection,
                                    searchModel.orderBy
                            )
                    );
        } else {
            nauczyciele =
                    nauczycielDao.findAll(
                            Sort.by(
                                    searchModel.sortingDirection,
                                    searchModel.orderBy
                            )
                    );
        }
        return getResponseModelFromEntities(nauczyciele);
    }

    private ResponseModel getResponseModelFromEntities(List<Nauczyciel> nauczyciele) {
        List<NauczycielModel> nauczycielModels =
                nauczyciele.stream()
                        .map((n)->
                                NauczycielModel.builder()
                                        .id(n.getId())
                                        .imie(n.getImie())
                                        .nazwisko(n.getNazwisko())
                                        .wiek(n.getWiek())
                                        .email(n.getEmail())
                                        .przedmiot(n.getPrzedmiot())
                                        .build()
                        )
                        .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(nauczycielModels)
                .build();
    }

    public ResponseModel getAllStudents(Long id) {
        Optional<Nauczyciel> nauczycielOptional = nauczycielDao.findById(id);
        if (nauczycielOptional.isPresent()){
            Nauczyciel nauczyciel = nauczycielOptional.get();
            NauczycielModel nauczycielModel =
                    NauczycielModel.builder()
                            .students(List.copyOf(nauczyciel.getSetOfStudents()))
                            .build();
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .data(nauczycielModel)
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Nauczyciel #%d Not Found", id))
                    .build();
        }
    }

    public ResponseModel getNauczycielByImie(String imie) {
        Nauczyciel nauczyciel = nauczycielDao.findNauczycielByImie(imie);
        if (nauczyciel != null){
            NauczycielModel nauczycielModel =
                    NauczycielModel.builder()
                            .id(nauczyciel.getId())
                            .imie(nauczyciel.getImie())
                            .nazwisko(nauczyciel.getNazwisko())
                            .wiek(nauczyciel.getWiek())
                            .email(nauczyciel.getEmail())
                            .przedmiot(nauczyciel.getPrzedmiot())
                            .build();
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .data(nauczycielModel)
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Nauczyciel #%s Not Found", imie))
                    .build();
        }
    }

    public ResponseModel getNauczycielByNazwisko(String nazwisko) {
        Nauczyciel nauczyciel = nauczycielDao.findNauczycielByNazwisko(nazwisko);
        if (nauczyciel != null){
            NauczycielModel nauczycielModel =
                    NauczycielModel.builder()
                            .id(nauczyciel.getId())
                            .imie(nauczyciel.getImie())
                            .nazwisko(nauczyciel.getNazwisko())
                            .wiek(nauczyciel.getWiek())
                            .email(nauczyciel.getEmail())
                            .przedmiot(nauczyciel.getPrzedmiot())
                            .build();
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .data(nauczycielModel)
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Nauczyciel #%s Not Found", nazwisko))
                    .build();
        }
    }

}
