package com.kevinchwong.example.dao.jpa;

import com.kevinchwong.example.domain.StatInfo;
import com.kevinchwong.example.domain.Statistic;
import com.kevinchwong.example.util.StatInfoSavingMap;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Stack;

public interface StatInfoRepository extends PagingAndSortingRepository<StatInfo, Long> {

    @Transactional
    Optional<StatInfo> findByTimestampAndLevel(long timestamp, int level);

    @Transactional
    default Optional<Statistic> loadStatisticByTimestamp(long timestamp, int levelLimit) {

        // This function's time complexity is O(1) because we just consider the statistic in last 60000 millis second
        // and levelLimit must be fixed as 4

        // The space complexity of this function is O(1)

        Statistic res = null;
        for (int l = 0; l <= levelLimit; l++) {
            long base = (long) Math.pow(10, l);
            long ts = timestamp;
            if (l > 0)
                ts = ((long) (timestamp / base)) * base;

            // Should be O(1) since TimeStamp is primary key and Level Size is a constant (<=5).
            Optional<StatInfo> si = findByTimestampAndLevel(ts, l);

            if (si.isPresent()) {
                StatInfo sig = si.get();
                if (res == null) {
                    res = new Statistic();
                    res.setSum(sig.getSum());
                    res.setCount(sig.getCount());
                    res.setMax(sig.getMax());
                    res.setMin(sig.getMin());
                    res.setAvg(res.getSum() / res.getCount());
                } else {
                    res.setSum(sig.getSum() + res.getSum());
                    res.setCount(sig.getCount() + res.getCount());
                    res.setMax(Math.max(sig.getMax(), res.getMax()));
                    res.setMin(Math.min(sig.getMin(), res.getMin()));
                    res.setAvg(res.getSum() / res.getCount());
                }
            }
        }
        if (res == null) {
            return Optional.empty();
        }
        return Optional.of(res);
    }

    @Transactional
    default void createByTimestampAndDurationAndAmount(long timestamp, long duration, double amount) {

        Stack<StatInfo> stack = new Stack<>();

        // Since we just consider the transaction statistic within last 60 seconds,
        // the total # of StatInfo generated must be <= 5*18 = 90.
        // Hence, we can also consider time and space complexity of this function is O(1)
        StatInfoSavingMap.findMap(timestamp, duration).entrySet().stream().forEach(es -> {

            final int lvl = es.getKey();

            es.getValue().stream().forEach(ts -> {

                Optional<StatInfo> si = findByTimestampAndLevel(ts, lvl);

                if (!si.isPresent()) {
                    StatInfo r = new StatInfo();
                    r.setTimestamp(ts);
                    r.setLevel(lvl);
                    r.setCount(1L);
                    r.setSum(amount);
                    r.setMax(amount);
                    r.setMin(amount);
                    stack.push(r);

                } else {
                    StatInfo r = si.get();
                    r.setCount(r.getCount() + 1);
                    r.setSum(r.getSum() + amount);
                    if (amount < r.getMin())
                        r.setMin(amount);
                    if (amount > r.getMax())
                        r.setMax(amount);
                    stack.push(r);
                }
            });
        });

        while(!stack.empty())
            this.save(stack.pop());
    }
}
