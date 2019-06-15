package com.lhyla.interactivewebapp.rest;

import com.lhyla.interactivewebapp.H2TestProfileJPAConfig;
import com.lhyla.interactivewebapp.InteractiveWebappApplication;
import com.lhyla.interactivewebapp.data.entity.Data;
import com.lhyla.interactivewebapp.data.repository.DataRepository;
import com.lhyla.interactivewebapp.rest.dto.DataDto;
import com.lhyla.interactivewebapp.util.DataUtils;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        InteractiveWebappApplication.class,
        H2TestProfileJPAConfig.class})
@ActiveProfiles("test")
class DataControllerTest {

    @Autowired
    private DataController dataController;

    @Autowired
    private DataRepository dataRepository;

    @AfterEach
    void afterEach() {
        dataRepository.deleteAll();
    }

    @Test
    void getLatest_twoRecords_dtoValue30() {
        //given
        dataRepository.save(Data.builder()
                .measurementDate(new DateTime().minusHours(1).toDate())
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(20))
                .build()
        );

        dataRepository.save(Data.builder()
                .measurementDate(new Date())
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(30))
                .build()
        );

        //when
        Optional<DataDto> latest = dataController.getLatest();

        //then
        assertThat(latest)
                .isPresent()
                .map(DataDto::getValue)
                .map(BigDecimal::toString)
                .get()
                .isEqualTo("30.00");
    }

    @Test
    void getAverageBetween_onlyGoodValues_avg50() {
        //given
        Date measurementDate1 = new DateTime().withTimeAtStartOfDay().minusHours(1).toDate();
        dataRepository.save(Data.builder()
                .measurementDate(measurementDate1)
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(0))
                .build()
        );

        Date measurementDate2 = new DateTime().withTimeAtStartOfDay().toDate();
        dataRepository.save(Data.builder()
                .measurementDate(measurementDate2)
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(100))
                .build()
        );

        Date measurementDate3 = new DateTime().withTimeAtStartOfDay().plusHours(1).toDate();
        dataRepository.save(Data.builder()
                .measurementDate(measurementDate3)
                .quality(Data.Quality.BAD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(200))
                .build()
        );

        //when
        Optional<BigDecimal> average = dataController.getAverageBetween(
                DataUtils.parseToDate(measurementDate1),
                DataUtils.parseToDate(measurementDate3),
                false
        );

        //then
        assertThat(average).isPresent().map(BigDecimal::toString).get().isEqualTo("50.0");
    }

    @Test
    void getAverageBetween_goodAndBadValues_avg100() {
        //given
        Date measurementDate1 = new DateTime().withTimeAtStartOfDay().minusHours(1).toDate();
        dataRepository.save(Data.builder()
                .measurementDate(measurementDate1)
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(0))
                .build()
        );

        Date measurementDate2 = new DateTime().withTimeAtStartOfDay().toDate();
        dataRepository.save(Data.builder()
                .measurementDate(measurementDate2)
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(100))
                .build()
        );

        Date measurementDate3 = new DateTime().withTimeAtStartOfDay().plusHours(1).toDate();
        dataRepository.save(Data.builder()
                .measurementDate(measurementDate3)
                .quality(Data.Quality.BAD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(200))
                .build()
        );

        //when
        Optional<BigDecimal> average = dataController.getAverageBetween(
                DataUtils.parseToDate(measurementDate1),
                DataUtils.parseToDate(measurementDate3),
                true
        );

        //then
        assertThat(average).isPresent().map(BigDecimal::toString).get().isEqualTo("100.0");
    }

    @Test
    void getGoodDataBetween() {
        //given
        Date measurementDate1 = new DateTime().withTimeAtStartOfDay().minusHours(1).toDate();
        dataRepository.save(Data.builder()
                .measurementDate(measurementDate1)
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(10))
                .build()
        );

        dataRepository.save(Data.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().toDate())
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(110))
                .build()
        );

        Date measurementDate2 = new DateTime().withTimeAtStartOfDay().plusHours(1).toDate();
        dataRepository.save(Data.builder()
                .measurementDate(measurementDate2)
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(200))
                .build()
        );

        dataRepository.save(Data.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().minusHours(2).toDate())
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(20))
                .build()
        );


        dataRepository.save(Data.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().plusHours(3).toDate())
                .quality(Data.Quality.GOOD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(30))
                .build()
        );

        dataRepository.save(Data.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().toDate())
                .quality(Data.Quality.BAD)
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(40))
                .build()
        );

        //when
        List<DataDto> result = dataController.getGoodDataBetween(
                DataUtils.parseToDate(measurementDate1),
                DataUtils.parseToDate(measurementDate2),
                2
        );

        //then
        assertThat(result).size().isEqualTo(2);
        assertEquals(result.get(0).getValue().toString(), "200.00");
        assertEquals(result.get(1).getValue().toString(), "110.00");
    }
}