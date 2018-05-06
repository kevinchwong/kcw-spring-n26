package com.kevinchwong.example.api.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.kevinchwong.example.domain.Transaction;
import com.kevinchwong.example.exception.DataFormatException;
import com.kevinchwong.example.service.TransactionService;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.util.resources.cldr.CalendarData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.kevinchwong.example.Application.WINDOW_DURATION_MS;

@RestController
@RequestMapping(value = "/transactions")
@Api(tags = {"transactions"})
public class TransactionController extends AbstractRestHandler {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a transaction resource.", notes = "")
    public ResponseEntity createTransaction(@RequestBody Transaction transaction,
                                 HttpServletRequest request, HttpServletResponse response) {

        long timestamp = transaction.getTimestamp();
        if((System.currentTimeMillis()-timestamp)>WINDOW_DURATION_MS) {
            // If the transaction is older than 60 seconds
            this.transactionService.createTransaction(transaction);
            return ResponseEntity.status(204).build();
        } else {
            this.transactionService.createTransaction(transaction);
            return ResponseEntity.status(201).build();
        }
    }

    // Optional API
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all transactions.", notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)")
    public
    @ResponseBody
    Page<Transaction> getAllTransaction(@ApiParam(value = "The page number (zero-based)", required = true)
                                      @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @ApiParam(value = "Tha page size", required = true)
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.transactionService.getAllTransactions(page, size);
    }

    // Optional API
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single transaction.", notes = "You have to provide a valid transaction ID.")
    public
    @ResponseBody
    Transaction getTransaction(@ApiParam(value = "The ID of the transaction.", required = true)
                             @PathVariable("id") Long id,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        Transaction transaction = this.transactionService.getTransaction(id);
        checkResourceFound(transaction);
        return transaction;
    }

    // Optional API
    @RequestMapping(value = "",
            method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete all transactions and related statInfos resource.", notes = "")
    public void deleteTransaction(HttpServletRequest request,
                                  HttpServletResponse response) {
        this.transactionService.deleteAllTransaction();
    }
}
