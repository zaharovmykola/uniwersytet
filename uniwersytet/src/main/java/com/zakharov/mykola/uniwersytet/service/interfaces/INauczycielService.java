package com.zakharov.mykola.uniwersytet.service.interfaces;

import com.zakharov.mykola.uniwersytet.model.NauczycielFilterModel;
import com.zakharov.mykola.uniwersytet.model.NauczycielModel;
import com.zakharov.mykola.uniwersytet.model.ResponseModel;

public interface INauczycielService {
    ResponseModel create(NauczycielModel nauczycielModel);
    ResponseModel getAll();
    ResponseModel delete(Long id);
    //ResponseModel getFiltered(NauczycielFilterModel filter);
}
