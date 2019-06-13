package com.lhyla.interactivewebapp.rest;

import com.lhyla.interactivewebapp.rest.dto.AvgDataDto;
import com.lhyla.interactivewebapp.rest.dto.DataDto;
import com.lhyla.interactivewebapp.rest.mapper.DataMapper;
import com.lhyla.interactivewebapp.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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
        return dataService
                .getLatestData()
                .map(dataMapper::map);
    }

    /**
     * @param from         olderDate
     * @param to           newestDate
     * @param isIncludeBad if true, records with GOOD and BAD quality
     *                     will be taken into consideration in the avg value calculation
     *                     otherwise only records with GOOD quality will take part in calculation the avg of value
     * @return average value between two dates
     */
    @RequestMapping(
            value = "/avg",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<AvgDataDto> getAverage(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyyMMddhhmmss") Date from,
            @RequestParam(value = "to") @DateTimeFormat(pattern = "yyyyMMddhhmmss") Date to,
            @RequestParam(value = "includeBadValues", required = false, defaultValue = "false") final boolean isIncludeBad) {
        return dataService
                .getAvgDataBetween(from, to, isIncludeBad)
                .map(dataMapper::map);
    }
}