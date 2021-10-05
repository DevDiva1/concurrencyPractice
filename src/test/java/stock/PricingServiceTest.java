package stock;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceTest {

    @Test
    public void givenStockData_whenAddedToMap_thenMapHasLatestStockPrice() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<String> stockData = Arrays.asList(
                "0231351065 433.72 2021-01-02",
                "0231351065 500.72 2021-01-01",

                "0378331006 100.43 2021-02-01",
                "0378331006 101.43 2021-01-01",
                "0378331006 102.43 2021-03-01",

                "9497461011 10.00 2021-07-01",
                "9497461011 11.00 2021-06-01",

                "30303M1027 33.00 2021-03-01",
                "30303M1027 31.00 2021-02-01");

        ConcurrentHashMap<String, CurrentPrice> map = new ConcurrentHashMap<>();

        for (String stockInfo : stockData) {
            String[] stock = stockInfo.split(" ");
            PricingService.addStockPrice(new Stock(stock[0], new CurrentPrice(Double.parseDouble(stock[1]),
                            LocalDate.parse(stock[2], formatter))), map);

        }
        assertEquals(433.72, map.get("0231351065").price, "Latest stock price");
        assertEquals(102.43, map.get("0378331006").price,"Latest stock price");
        assertEquals(10.00, map.get("9497461011").price,"Latest stock price");
        assertEquals(33.00, map.get("30303M1027").price,"Latest stock price");
    }


    public void testAddStockDataInMultiThreadedEnv(final int threadCount) throws ExecutionException, InterruptedException {
        //set up
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<PricingService> fileLists = new ArrayList<>();
        Future<ConcurrentHashMap<String, CurrentPrice>> futureResult = null;
        ConcurrentHashMap<String, CurrentPrice> resultMap;

        //adding files to fileLists
        String folderPath = "src/main/resources/stock";
        File file = new File(folderPath);
        File[] listOfFiles = {};
        if (!file.isFile()) {
            listOfFiles = file.listFiles();
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            fileLists.add(new PricingService(listOfFiles[i].getAbsolutePath()));
        }
        //submitting task with callable
        for (int i = 0; i < fileLists.size(); i++) {
            futureResult = executorService.submit(fileLists.get(i));
        }
        resultMap = futureResult.get();

        //assertions
        assertEquals(433.72, resultMap.get("0231351065").price, "Latest price for Cusip code : 0231351065");
        assertEquals(20.43, resultMap.get("0378331006").price, "Latest price for Cusip code : 0378331006");
        assertEquals(10.00, resultMap.get("30303M1027").price, "Latest price for Cusip code : 30303M1027");
        assertEquals(122.23, resultMap.get("5949181049").price, "Latest price for Cusip code : 5949181049");
        assertEquals(80.00, resultMap.get("9497461011").price, "Latest price for Cusip code : 9497461011");


        resultMap.clear();
        executorService.shutdown();
    }

    @Test
    public void test01() throws ExecutionException, InterruptedException {
        testAddStockDataInMultiThreadedEnv(4);
    }
}