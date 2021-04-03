package com.zakharov.mykola.uniwersytet.dao;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.zakharov.mykola.uniwersytet.entity.Nauczyciel;
import com.zakharov.mykola.uniwersytet.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

@Repository
public interface NauczycielHibernateDAO extends JpaRepository<Nauczyciel, Long>,
        QuerydslPredicateExecutor<Nauczyciel>, QuerydslBinderCustomizer<QNauczyciel> {
    Student findNauczycielByImie(String imie);
    Student findNauczycielByNazwisko(String nazwisko);

    // добавление поддержки запросов query dsl
    // (предварительно нужно сгенерировать тип QProduct командой
    // mvn apt:process)
    @Override
    default public void customize(
            QuerydslBindings bindings, QNauczyciel root) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.excluding(root.image);
    }

}
