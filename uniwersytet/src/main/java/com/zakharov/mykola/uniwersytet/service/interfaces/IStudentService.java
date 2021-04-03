package com.zakharov.mykola.uniwersytet.service.interfaces;

import com.zakharov.mykola.uniwersytet.model.ResponseModel;
import com.zakharov.mykola.uniwersytet.model.StudentFilterModel;
import com.zakharov.mykola.uniwersytet.model.StudentModel;

public interface IStudentService {
    ResponseModel create(StudentModel studentModel);
    ResponseModel getAll();
    ResponseModel delete(Long id);
    //ResponseModel getFiltered(StudentFilterModel filter);
}
