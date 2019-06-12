package com.lhyla.interactivewebapp.service;

import com.lhyla.interactivewebapp.data.entity.Data;
import com.lhyla.interactivewebapp.data.repository.data.DataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DataService {

    private DataDao dataDao;

    public DataService(@Autowired DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public Optional<Data> getLatestData() {
        return dataDao.getLatestDataByMeasurementDate();
    }
}