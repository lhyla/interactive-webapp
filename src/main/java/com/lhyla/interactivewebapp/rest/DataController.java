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

import java.math.BigDecimal;
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
        Optional<DataDto> dataDto = dataService
                .getLatestData()
                .map(dataMapper::map);
        return dataDto;
    }

    /**
     * @param from         olderDate
     * @param to           newerDate
     * @param isIncludeBad not required, by default false. If true, records with GOOD and BAD quality
     *                     will be taken into consideration in the avg value calculation
     *                     otherwise only records with GOOD quality will take part in calculation the avg of value
     * @return average value between two dates
     */
    @RequestMapping(
            value = "/avg",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<BigDecimal> getAverageBetween(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to,
            @RequestParam(value = "includeBadValues", required = false, defaultValue = "false") final boolean isIncludeBad) {
        return dataService.getAvgDataBetween(from, to, isIncludeBad);
    }

    /**
     * @param from  olderDate
     * @param to    newerDate
     * @param limit of records which will be returned, not required and by default setup to 1000
     * @return series of good values between two dates sorted by measurementDate descending and id descending
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
        return dataMapper
                .map(dataService.getGoodDataBetween(from, to, limit));
    }
}