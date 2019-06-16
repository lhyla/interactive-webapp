package com.lhyla.measuresapp.rest;

import com.lhyla.measuresapp.dto.MeasureDataDto;
import com.lhyla.measuresapp.dto.avg.AvgMeasureDataDto;
import com.lhyla.measuresapp.service.AvgMeasureDataService;
import com.lhyla.measuresapp.service.MeasureDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/data/v1")
@AllArgsConstructor
public class MeasureDataController {

    private MeasureDataService measureDataService;
    private AvgMeasureDataService avgMeasureDataService;

    /**
     * @return latest value, along with timestamp and quality
     */
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<MeasureDataDto> getLatest() {
        return measureDataService.getLatestData();
    }

    /**
     * @param from olderDate in format yyyy-MM-dd.HH:mm:ss
     * @param to   newerDate in format yyyy-MM-dd.HH:mm:ss
     * @return average value between two dates
     */
    @RequestMapping(
            value = "/avg",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<AvgMeasureDataDto> getAverageBetween(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        return avgMeasureDataService.getAvgDataBetween(from, to);
    }

    /**
     * @param from  olderDate in format yyyy-MM-dd.HH:mm:ss
     * @param to    newerDate in format yyyy-MM-dd.HH:mm:ss
     * @param limit of records which will be returned, not required, default = 1000
     * @return good values between given dates
     */
    @RequestMapping(
            value = "/series",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MeasureDataDto> getGoodDataBetween(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to,
            @RequestParam(value = "limit", required = false, defaultValue = "1000") Integer limit) {
        return measureDataService.getGoodDataBetween(from, to, limit);
    }

    /**
     * @param from               olderDate in format yyyy-MM-dd.HH:mm:ss
     * @param to                 newerDate in format yyyy-MM-dd.HH:mm:ss
     * @param intervalsInMinutes custom interval in minutes, not required, default = 1
     * @return interpolated values in given intervals from specific period of time
     */
    @RequestMapping(
            value = "/interpolate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MeasureDataDto> getInterpolation(@RequestParam(value = "from") String from,
                                                 @RequestParam(value = "to") String to,
                                                 @RequestParam(value = "intervals",
                                                         defaultValue = "1",
                                                         required = false) int intervalsInMinutes) {
        return measureDataService.getInterpolation(from, to, intervalsInMinutes);
    }
}