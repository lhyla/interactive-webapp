package com.lhyla.interactivewebapp.data.repository.data;

import com.lhyla.interactivewebapp.data.entity.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handles more complicated business logic
 * which are not possible or not efficient to be executed on DB level
 * <p>
 * Also could contain logic, which query method from repository should be executed, based on parameters values
 */
@Component
public class DataDao {

    private DataRepository dataRepository;

    public DataDao(@Autowired DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Optional<Data> getLatestDataByMeasurementDate() {
        return dataRepository.findFirstByOrderByMeasurementDateDescIdDesc();
    }
}