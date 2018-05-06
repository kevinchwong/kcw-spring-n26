package com.kevinchwong.example.test;

import com.kevinchwong.example.Application;
import com.kevinchwong.example.api.rest.TransactionController;
import com.kevinchwong.example.dao.jpa.TransactionRepository;
import com.kevinchwong.example.domain.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TransactionControllerTest {

    @InjectMocks
    TransactionController controller;

    @Autowired
    WebApplicationContext context;

    @Autowired
    TransactionRepository transactionRepository;

    private MockMvc mvc;

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldHaveEmptyDB() throws Exception {
        mvc.perform(get("/transactions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    public void ShouldCreateRetrieveDeleteThruResp() throws Exception {
        Transaction r1 = mockTransaction("ShouldCreateRetrieveDeleteThruResp");

        Assert.assertEquals(transactionRepository.count(), 0);

        Transaction a = transactionRepository.save(r1);

        Transaction b = transactionRepository.findOne(r1.getId());

        Assert.assertEquals(a.toString(), b.toString());

        Assert.assertEquals(transactionRepository.count(), 1);
        transactionRepository.delete(r1.getId());
        Assert.assertEquals(transactionRepository.count(), 0);

    }

    private Transaction mockTransaction(String prefix) {
        Transaction r = new Transaction();
        r.setAmount(Double.valueOf(new Random().nextInt(9999)));
        r.setTimestamp(new Random().nextLong());
        return r;
    }

}
