package stock;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PricingService implements Callable<ConcurrentHashMap<String, CurrentPrice>> {
    final String fileName;
    private static ConcurrentHashMap<String, CurrentPrice> stockMap = new ConcurrentHashMap<>();

    public PricingService(String fileName) {
        this.fileName = fileName;
    }

    public static CurrentPrice addStockPrice(Stock stockData, ConcurrentHashMap<String, CurrentPrice> map) {
        //compute is atomic operations in the ConcurrentHashMap class
        return map.compute(stockData.cusipCode, (k, v) -> v == null ? stockData.currPrice : latestStockPrice(v, stockData));
    }

    private static CurrentPrice latestStockPrice(CurrentPrice v, Stock stockData) {
        if (v.localDate.isAfter(stockData.currPrice.localDate)) {
            return new CurrentPrice(v.price, v.localDate);
        }
        return new CurrentPrice(stockData.currPrice.price, stockData.currPrice.localDate);
    }

    @Override
    public ConcurrentHashMap<String, CurrentPrice> call() throws Exception {
        //Read files in different threads line by line
        Stream<String> stream = Files.lines(Paths.get(this.fileName));
        stream.forEach(
                line -> {
                    String data[] = line.split(" ");
                    addStockPrice(stockPriceBuild(data), stockMap);
                });
        return stockMap;
    }

    private Stock stockPriceBuild(String[] data) {
        if (data.length != 3) {
            System.out.println("Input File Error");
        }
        return new Stock(data[0], new CurrentPrice(Double.parseDouble(data[1]), LocalDate.parse(data[2])));
    }
}