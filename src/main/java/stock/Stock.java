package stock;

public class Stock {
    String cusipCode;
    CurrentPrice currPrice;

    Stock(String cusipCode, CurrentPrice currPrice) {
        this.cusipCode = cusipCode;
        this.currPrice = currPrice;
    }
}
