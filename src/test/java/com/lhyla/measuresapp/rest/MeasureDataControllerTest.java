package com.lhyla.measuresapp.rest;

import com.lhyla.measuresapp.H2TestProfileJPAConfig;
import com.lhyla.measuresapp.MeasuresAppApplication;
import com.lhyla.measuresapp.data.entity.MeasureData;
import com.lhyla.measuresapp.data.repository.measure.MeasureDataRepository;
import com.lhyla.measuresapp.dto.MeasureDataDto;
import com.lhyla.measuresapp.dto.avg.AvgMeasureDataDto;
import com.lhyla.measuresapp.dto.avg.AvgMeasureDataStatsDto;
import com.lhyla.measuresapp.util.MeasuresAppUtils;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        MeasuresAppApplication.class,
        H2TestProfileJPAConfig.class})
@ActiveProfiles("test")
class MeasureDataControllerTest {

    @Autowired
    private MeasureDataController measureDataController;

    @Autowired
    private MeasureDataRepository measureDataRepository;

    @AfterEach
    void afterEach() {
        measureDataRepository.deleteAll();
    }

    @Test
    void getLatest_twoRecords_dtoValue30() {
        //given
        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new DateTime().minusHours(1).toDate())
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(20))
                .build()
        );

        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new Date())
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(30))
                .build()
        );

        //when
        Optional<MeasureDataDto> latest = measureDataController.getLatest();

        //then
        assertThat(latest)
                .isPresent()
                .map(MeasureDataDto::getValue)
                .map(BigDecimal::toString)
                .get()
                .isEqualTo("30.00");
        assertThat(latest).map(MeasureDataDto::getType).get().isEqualTo(MeasureDataDto.Type.MEASURED);
    }

    @Test
    void getAverageBetween() {
        //given
        Date measurementDate1 = new DateTime().withTimeAtStartOfDay().minusHours(1).toDate();
        measureDataRepository.save(MeasureData.builder()
                .measurementDate(measurementDate1)
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(1))
                .build()
        );

        Date measurementDate2 = new DateTime().withTimeAtStartOfDay().toDate();
        measureDataRepository.save(MeasureData.builder()
                .measurementDate(measurementDate2)
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(3))
                .build()
        );

        Date measurementDate3 = new DateTime().withTimeAtStartOfDay().plusHours(1).toDate();
        measureDataRepository.save(MeasureData.builder()
                .measurementDate(measurementDate3)
                .quality(MeasureData.Quality.BAD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(2))
                .build()
        );

        measureDataRepository.save(MeasureData.builder()
                .measurementDate(measurementDate3)
                .quality(MeasureData.Quality.BAD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(4))
                .build()
        );

        Optional<AvgMeasureDataDto> avgData = measureDataController.getAverageBetween(
                MeasuresAppUtils.parseToDate(measurementDate1),
                MeasuresAppUtils.parseToDate(measurementDate3)
        );

        //then
        assertThat(avgData).isPresent().map(AvgMeasureDataDto::getAvg).map(BigDecimal::toString).get().isEqualTo("2.5");

        Set<AvgMeasureDataStatsDto> avgDataStats = avgData.get().getAvgDataStats();
        assertThat(avgDataStats.size()).isEqualTo(2);

        AvgMeasureDataStatsDto goodDataStats = avgDataStats.stream()
                .filter(e -> AvgMeasureDataStatsDto.Quality.GOOD.equals(e.getQuality()))
                .findFirst()
                .orElse(null);

        assertThat(goodDataStats.getAvg().setScale(1, RoundingMode.DOWN).toString()).isEqualTo("2.0");
        assertThat(goodDataStats.getMin().setScale(1, RoundingMode.DOWN).toString()).isEqualTo("1.0");
        assertThat(goodDataStats.getMax().setScale(1, RoundingMode.DOWN).toString()).isEqualTo("3.0");
        assertThat(goodDataStats.getCount().toString()).isEqualTo("2");

        AvgMeasureDataStatsDto badDataStats = avgDataStats.stream()
                .filter(e -> AvgMeasureDataStatsDto.Quality.BAD.equals(e.getQuality()))
                .findFirst()
                .orElse(null);

        assertThat(badDataStats.getAvg().setScale(1, RoundingMode.DOWN).toString()).isEqualTo("3.0");
        assertThat(badDataStats.getMin().setScale(1, RoundingMode.DOWN).toString()).isEqualTo("2.0");
        assertThat(badDataStats.getMax().setScale(1, RoundingMode.DOWN).toString()).isEqualTo("4.0");
        assertThat(badDataStats.getCount().toString()).isEqualTo("2");
    }

    @Test
    void getGoodDataBetween() {
        //given
        Date measurementDate1 = new DateTime().withTimeAtStartOfDay().minusHours(1).toDate();
        measureDataRepository.save(MeasureData.builder()
                .measurementDate(measurementDate1)
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(10))
                .build()
        );

        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().toDate())
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(110))
                .build()
        );

        Date measurementDate2 = new DateTime().withTimeAtStartOfDay().plusHours(1).toDate();
        measureDataRepository.save(MeasureData.builder()
                .measurementDate(measurementDate2)
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(200))
                .build()
        );

        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().minusHours(2).toDate())
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(20))
                .build()
        );


        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().plusHours(3).toDate())
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(30))
                .build()
        );

        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().toDate())
                .quality(MeasureData.Quality.BAD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(40))
                .build()
        );

        //when
        List<MeasureDataDto> result = measureDataController.getGoodDataBetween(
                MeasuresAppUtils.parseToDate(measurementDate1),
                MeasuresAppUtils.parseToDate(measurementDate2),
                2
        );

        //then
        assertThat(result).size().isEqualTo(2);
        assertEquals(result.get(0).getValue().toString(), "200.00");
        assertEquals(result.get(1).getValue().toString(), "110.00");
        assertThat(result).allMatch(e -> e.getType() == MeasureDataDto.Type.MEASURED);
    }

    @Test
    void getInterpolatedData() {
        //given
        Date measurementDate1 = new DateTime().withTimeAtStartOfDay().minusHours(3).toDate();
        measureDataRepository.save(MeasureData.builder()
                .measurementDate(measurementDate1)
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(5))
                .build()
        );

        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().minusHours(2).toDate())
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(6))
                .build()
        );

        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().minusHours(1).toDate())
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(7))
                .build()
        );

        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().toDate())
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(8))
                .build()
        );

        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().plusHours(1).toDate())
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(7))
                .build()
        );

        measureDataRepository.save(MeasureData.builder()
                .measurementDate(new DateTime().withTimeAtStartOfDay().plusHours(2).toDate())
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(4))
                .build()
        );

        Date measurementDate2 = new DateTime().withTimeAtStartOfDay().plusHours(3).toDate();
        measureDataRepository.save(MeasureData.builder()
                .measurementDate(measurementDate2)
                .quality(MeasureData.Quality.GOOD)
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .value(BigDecimal.valueOf(3))
                .build()
        );

        //when
        List<MeasureDataDto> result = measureDataController.getInterpolation(
                MeasuresAppUtils.parseToDate(measurementDate1),
                MeasuresAppUtils.parseToDate(measurementDate2),
                1
        );

        //then
        assertThat(result).size().isEqualTo(361);
        assertThat(result).allMatch(e -> e.getType() == MeasureDataDto.Type.INTERPOLATED);
        assertThat(result).allMatch(e -> e.getId() == null);
        assertThat(result).allMatch(e -> e.getQuality() == null);
        assertThat(result).allMatch(e -> e.getMeasurementDate().getTime() >= measurementDate1.getTime());
        assertThat(result).allMatch(e -> e.getMeasurementDate().getTime() <= measurementDate2.getTime());
        assertThat(result).allMatch(e -> e.getValue() != null);
        assertThat(result).allMatch(e -> e.getValue().doubleValue() <= 8d);
        assertThat(result).allMatch(e -> e.getValue().doubleValue() >= 3d);
    }
}