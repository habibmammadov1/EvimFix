package com.example.evimfix.wpAdmin.Services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.evimfix.wpAdmin.Repositories.GeneralRepositoryDAO;

@Service
public class GeneralService {
    @Autowired
    private GeneralRepositoryDAO generalRepositoryDAO;

    public HashMap<Integer, String> getModullar(){
        return generalRepositoryDAO.getModullar();
    }

    // public HashMap<Integer, String> getAllCourseCategories(){
    //     return generalRepositoryDAO.getAllCourseCategories();
    // }
}
