package com.kevinchwong.example.test;

import com.kevinchwong.example.Application;
import com.kevinchwong.example.dao.jpa.StatInfoRepository;
import com.kevinchwong.example.domain.StatInfo;
import com.kevinchwong.example.domain.Statistic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class StatInfoControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    StatInfoRepository statInfoRepository;

    private MockMvc mvc;

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    //@Test
    public void ShouldCreateRetrieveDeleteThruResp() throws Exception {
        StatInfo r1 = mockStatInfo("ShouldCreateRetrieveDeleteThruResp");
        //byte[] r1Json = toJson(r1);

        Assert.assertEquals(statInfoRepository.count(), 0);

        StatInfo a = statInfoRepository.save(r1);

        Optional<StatInfo> b = statInfoRepository.findByTimestampAndLevel(a.getTimestamp(), a.getLevel());

        Assert.assertEquals(a.toString(), b.get().toString());

        Assert.assertEquals(statInfoRepository.count(), 1);
        statInfoRepository.delete(a);
        Assert.assertEquals(statInfoRepository.count(), 0);

    }

    @Test
    public void test1(){
        statInfoRepository.createByTimestampAndDurationAndAmount(1525578430941L, 60000, 10);
        Long ts = 1525578430941L;
        Optional<Statistic> x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
    }

    @Test
    public void ShouldCreateRetrieveDeleteStatInfo() {

        // Send 3 transactions and created 3 batches of statInfo for these
        statInfoRepository.createByTimestampAndDurationAndAmount(1111111, 60000, 10);
        statInfoRepository.createByTimestampAndDurationAndAmount(1111888, 60000, 20);
        statInfoRepository.createByTimestampAndDurationAndAmount(1111555, 60000, 100);

        Long ts = 1111112L;
        Optional<Statistic> x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        String ans = "Optional[Statistic{sum=10.0, avg=10.0, max=10.0, min=10.0, count=1}]";
        Assert.assertEquals(ans, x.toString());

        ts = 1111677L;
        x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        ans = "Optional[Statistic{sum=110.0, avg=55.0, max=100.0, min=10.0, count=2}]";
        Assert.assertEquals(ans, x.toString());

        ts = 1111999L;
        x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        ans = "Optional[Statistic{sum=130.0, avg=43.333333333333336, max=100.0, min=10.0, count=3}]";
        Assert.assertEquals(ans, x.toString());

        ts = 1116112L;
        x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        ans = "Optional[Statistic{sum=130.0, avg=43.333333333333336, max=100.0, min=10.0, count=3}]";
        Assert.assertEquals(ans, x.toString());

        ts = 1171110L;
        x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        ans = "Optional[Statistic{sum=130.0, avg=43.333333333333336, max=100.0, min=10.0, count=3}]";
        Assert.assertEquals(ans, x.toString());

        ts = 1171111L;
        x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        ans = "Optional[Statistic{sum=120.0, avg=60.0, max=100.0, min=20.0, count=2}]";
        Assert.assertEquals(ans, x.toString());

        ts = 1171112L;
        x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        ans = "Optional[Statistic{sum=120.0, avg=60.0, max=100.0, min=20.0, count=2}]";
        Assert.assertEquals(ans, x.toString());

        ts = 1171777L;
        x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        ans = "Optional[Statistic{sum=20.0, avg=20.0, max=20.0, min=20.0, count=1}]";
        Assert.assertEquals(ans, x.toString());

        ts = 1171887L;
        x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        ans = "Optional[Statistic{sum=20.0, avg=20.0, max=20.0, min=20.0, count=1}]";
        Assert.assertEquals(ans, x.toString());

        ts = 1171888L;
        x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        ans = "Optional.empty";
        Assert.assertEquals(ans, x.toString());

        ts = 99999999999L;
        x = statInfoRepository.loadStatisticByTimestamp(ts, 4);
        System.out.println(ts + ":" + x);
        ans = "Optional.empty";
        Assert.assertEquals(ans, x.toString());

        statInfoRepository.deleteAll();
        Assert.assertEquals(statInfoRepository.count(), 0);
    }

    private StatInfo mockStatInfo(String prefix) {
        StatInfo r = new StatInfo();
        r.setSum(Double.valueOf(new Random().nextInt(9999)));
        r.setMax(Double.valueOf(new Random().nextInt(9999)));
        r.setMin(Double.valueOf(new Random().nextInt(9999)));
        r.setCount(new Random().nextLong());
        r.setTimestamp(new Random().nextLong());
        r.setLevel(new Random().nextInt());
        return r;
    }
}
