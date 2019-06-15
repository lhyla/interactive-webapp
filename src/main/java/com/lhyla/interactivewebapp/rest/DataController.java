package com.lhyla.interactivewebapp.rest;

import com.lhyla.interactivewebapp.rest.dto.DataDto;
import com.lhyla.interactivewebapp.rest.mapper.DataMapper;
import com.lhyla.interactivewebapp.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/data/v1")
public class DataController {

    private DataService dataService;
    private DataMapper dataMapper;

    public DataController(@Autowired DataService dataService,
                          @Autowired DataMapper dataMapper) {
        this.dataService = dataService;
        this.dataMapper = dataMapper;
    }

    /**
     * @return latest value, along with timestamp and quality
     */
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<DataDto> getLatest() {
        Optional<DataDto> data = dataService
                .getLatestData()
                .map(dataMapper::map);
        data.ifPresent(e -> e.setType(DataDto.Type.MEASURED));

        return data;
    }

    /**
     * @param from         olderDate in format yyyy-MM-dd.HH:mm:ss
     * @param to           newerDate in format yyyy-MM-dd.HH:mm:ss
     * @param isIncludeBad If true, records with GOOD and BAD quality
     *                     will be taken into consideration in the avg value calculation
     *                     otherwise only records with GOOD quality will take part in calculation the avg of value
     *                     not required, default = false
     * @return average value between two dates
     */
    @RequestMapping(
            value = "/avg",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<DataDto> getAverageBetween(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to,
            @RequestParam(value = "includeBadValues", required = false, defaultValue = "false") final boolean isIncludeBad) {
        Optional<DataDto> dataDto = dataService
                .getAvgDataBetween(from, to, isIncludeBad)
                .map(dataMapper::map);
        dataDto.ifPresent(e -> e.setType(DataDto.Type.AVERAGE));

        return dataDto;
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
    public List<DataDto> getGoodDataBetween(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to,
            @RequestParam(value = "limit", required = false, defaultValue = "1000") Integer limit) {
        List<DataDto> data = dataMapper.map(
                dataService.getGoodDataBetween(from, to, limit)
        );
        data.forEach(e -> e.setType(DataDto.Type.MEASURED));

        return data;
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
    public List<DataDto> getInterpolation(@RequestParam(value = "from") String from,
                                          @RequestParam(value = "to") String to,
                                          @RequestParam(value = "intervals", defaultValue = "1", required = false) int intervalsInMinutes) {
        List<DataDto> interpolatedData = dataMapper.map(
                dataService.getInterpolation(from, to, intervalsInMinutes)
        );
        interpolatedData.forEach(e -> e.setType(DataDto.Type.INTERPOLATED));

        return interpolatedData;
    }
}