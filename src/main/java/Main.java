import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Map<UUID, Order> orderMap = new HashMap<>();
        URI ordersURI = getResourceURI("orders.csv");
        File ordersFile = new File(ordersURI);

        CSVReader reader = new CSVReader(new FileReader(ordersFile), ',', '"', 1);

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            orderMap.put(UUID.fromString(nextLine[0]), new Order(UUID.fromString(nextLine[0]), LocalDateTime.parse(nextLine[1]).toLocalDate()));
        }
        reader.close();

        Map<UUID, Product> productMap = new HashMap<>();
        URI productsURI = getResourceURI("products.csv");
        File productsFile = new File(productsURI);

        reader = new CSVReader(new FileReader(productsFile), ',', '"', 1);

        while ((nextLine = reader.readNext()) != null) {
            productMap.put(UUID.fromString(nextLine[0]), new Product(UUID.fromString(nextLine[0]), nextLine[1], Integer.parseInt(nextLine[2])));
        }
        reader.close();

        Map<LocalDate, Map<Product, Integer>> result = new HashMap<>();
        URI orderItemsURI = getResourceURI("order_items.csv");
        File orderItemsFile = new File(orderItemsURI);
        reader = new CSVReader(new FileReader(orderItemsFile), ',', '"', 1);

        while ((nextLine = reader.readNext()) != null) {
            Order order = orderMap.get(UUID.fromString(nextLine[0]));
            Product product = productMap.get(UUID.fromString(nextLine[1]));
            int count = Integer.parseInt(nextLine[2]);
            order.putProduct(product, count);
        }
        reader.close();

        orderMap.forEach(((uuid, order) -> {
            if (!result.containsKey(order.getDate())) {
                result.put(order.getDate(), new HashMap<>());
            }

            Map<Product, Integer> productIntegerMap = result.get(order.getDate());

            order.getProductMap().forEach(((product, count) -> {
                if (!productIntegerMap.containsKey(product)) {
                    productIntegerMap.put(product, count * product.getPrice());
                } else {
                    productIntegerMap.put(product, productIntegerMap.get(product) + count * product.getPrice());
                }
            }));
        }));

        for (Map.Entry<LocalDate, Map<Product, Integer>> entry : result.entrySet()) {
            Product productKey = Collections.max(entry.getValue().entrySet(), Map.Entry.comparingByValue()).getKey();
            System.out.println(entry.getKey() + ": " + productKey.getName());
        }
    }

    public static URI getResourceURI(String path) throws URISyntaxException {
        return Main.class.getClassLoader().getResource(path).toURI();
    }
}