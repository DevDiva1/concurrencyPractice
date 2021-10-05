package stock;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CurrentPrice {
    Double price;
    LocalDate localDate;

    CurrentPrice(Double price, LocalDate localDate) {
        this.price = price;
        this.localDate = localDate;
    }
}
