package com.kevinchwong.example.api.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kevinchwong.example.domain.Statistic;
import com.kevinchwong.example.service.StatInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.kevinchwong.example.Application.LEVEL_LIMIT;

@RestController
@RequestMapping(value = "/statistics")
@Api(tags = {"statistics"})
public class StatisticController extends AbstractRestHandler {

    @Autowired
    private StatInfoService statInfoService;

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get statistic of transactions in last 60 seconds.", notes = "No parameters needed")
    public
    @ResponseBody
    Statistic getTransaction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long timestamp = System.currentTimeMillis();
        Optional<Statistic> statisticOpt = this.statInfoService.loadStatisticByTimestamp(timestamp, LEVEL_LIMIT);

        if (statisticOpt.isPresent()) {
            return statisticOpt.get();
        } else {
            Statistic res = new Statistic();
            res.setSum(0.0);
            res.setCount(0L);
            res.setMax(0.0);
            res.setMin(0.0);
            res.setAvg(0.0);
            return res;
        }
    }
}
