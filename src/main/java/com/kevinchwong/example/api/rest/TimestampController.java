package com.kevinchwong.example.api.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/ts")
@Api(tags = {"timestamp"})
public class TimestampController extends AbstractRestHandler {

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get current system timestamp.", notes = "No parameters needed")
    public
    @ResponseBody
    String getTransaction(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject res = new JsonObject();

        res.addProperty("systemTimestamp", System.currentTimeMillis());

        return res.toString();
    }
}
