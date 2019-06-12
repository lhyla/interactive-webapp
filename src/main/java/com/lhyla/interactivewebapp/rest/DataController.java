package com.lhyla.interactivewebapp.rest;

import com.lhyla.interactivewebapp.rest.dto.DataDto;
import com.lhyla.interactivewebapp.rest.mapper.DataMapper;
import com.lhyla.interactivewebapp.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/data")
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
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<DataDto> getLatest() {
        return dataService
                .getLatestData()
                .map(dataMapper::map);
    }
}