# N26 Project

## How to Build and Run
```
        mvn clean install
        mvn spring-boot:run
```

## O(1) Space and Time complexity

#### 1. Pre-calculate Statistic

- To save time and space complexity, we need to **pre-calculate** statistic when we add transaction to DB.
- StatInfo object is used to store the pre-calculated statistic results (min, max, sum, count) at a certain timestamp.
- We know that inserting one new transaction will affect the statistic results along next 60000 milliseconds. But, if we update all 60000 StatInfo along 60000 milliseconds, it would be very time-consuming.
- To resolve this, we apply **Level of detail** concept on StatInfo.
- Now, each StatInfo contains sum, count, min and max of all transactions within a given level and time range.

- For example: for range [123 to 321), we can break it into 3 levels:
```
       level 2: 200-299 (1 save)
       level 1: 130-139, 140-149, 150-159, 160-169, 170-179, 180-189, 190-199, 300-309, 310-319 (9 saves)
       level 0: 123, 124, 125, 126, 127, 128, 129, 320 (8 saves; 321 not included in open ending)
```
- **StatInfoSavingMap.findMap(timestamp,duration)** is used to calculate how many/how to save in each levels.

- Similarly, we can reduce 60000 save operations into 90 or less save operations. This is implemented in function **StatInfoRepository.createByTimestampAndDurationAndAmount(timestamp, duration, amount)**

#### 2. Load the statistics
- After we have inserted a certain amount of transactions, if we want to obtain the statistic, we can just combine the statistic along all levels of that time.

- For example: if we want to get statistic at timestamp 123456, we just combine :
```
       statInfo(123456,0) :: statInfo(123450,1) :: statInfo(123400,2) :: statInfo(123000,3) :: statInfo(120000,4) :: ...
```
- If the window size is about 60000ms, we could just combine first 5 levels of the statInfo. Since this level is limited, the time and space complexity is still O(1).
   
- This part is implemented in function **StatInfoRepository.loadStatisticByTimestamp(timestamp, levelLimit=4)**


