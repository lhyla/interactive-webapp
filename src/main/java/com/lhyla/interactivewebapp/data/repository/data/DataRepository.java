package com.lhyla.interactivewebapp.data.repository.data;

import com.lhyla.interactivewebapp.data.entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

interface DataRepository extends JpaRepository<Data, BigInteger> {

    Optional<Data> findFirstByOrderByMeasurementDateDescIdDesc();
}