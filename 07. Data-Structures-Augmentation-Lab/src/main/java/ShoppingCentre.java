import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingCentre {

    Map<String, List<Product>> productsByName = new HashMap<>();
    Map<String, List<Product>> productsByProducer = new HashMap<>();

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);
        productsByName.putIfAbsent(name, new ArrayList<>());
        productsByName.get(name).add(product);
        productsByProducer.putIfAbsent(producer, new ArrayList<>());
        productsByProducer.get(producer).add(product);
        return "Product added\n";
    }

    public String delete(String name, String producer) {
        int removedProducts = 0;
        if (productsByProducer.size() > 0) {
            System.out.println();
            List<Product> products = productsByProducer.get(producer);
            if (products != null && !products.isEmpty()) {
                List<Product> productsByNameAndProducer = products.stream().filter(product -> product.getName().equals(name)).collect(Collectors.toList());
                if (!productsByNameAndProducer.isEmpty())
                removedProducts += productsByNameAndProducer.size();
                productsByProducer.remove(producer);
                productsByName.remove(name);
                /*notWorking
                    productsByProducer.values().removeAll(productsByNameAndProducer);
                productsByName.values().removeAll(productsByNameAndProducer);
                 */
            }
        }
        return getRemovedMessage(removedProducts);
    }

    private String getRemovedMessage(int removedProducts) {
        return removedProducts == 0 ? "No products found\n" : String.format("%d products deleted\n", removedProducts);
    }

    public String delete(String producer) {
        int removedProducts = 0;
        if (productsByProducer.size() > 0) {
            List<Product> products = productsByProducer.get(producer);
            if (!products.isEmpty()) {
                removedProducts += products.size();
                productsByProducer.remove(producer);
            }
        }
        return getRemovedMessage(removedProducts);
    }

    public String findProductsByName(String name) {
        List<Product> filteredProducts = productsByName.get(name);
        if (filteredProducts != null) {
            return getProductMessage(filteredProducts);
        }
        return "No products found\n";
    }

    public String findProductsByProducer(String producer) {
        List<Product> filteredProducts = productsByProducer.get(producer);
        if (filteredProducts != null) {
            return getProductMessage(filteredProducts);
        }
        return "No products found\n";
    }


    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        List<Product> productsByPrice = new ArrayList<>();
        if (!productsByName.isEmpty()) {
            for (List<Product> value : productsByName.values()) {
                productsByPrice.addAll(value.stream().filter(product -> product.getPrice() >= priceFrom && product.getPrice() <= priceTo)
                        .collect(Collectors.toList()));
            }
        }
        return getProductMessage(productsByPrice);
    }


    private String getProductMessage(List<Product> productByParameter) {
        StringBuilder sb = new StringBuilder();
        if (productByParameter.isEmpty()) {
            return "No products found\n";
        }
        productByParameter = productByParameter.stream().sorted(Product::compareTo).collect(Collectors.toList());

        for (Product prod : productByParameter) {
            sb.append(String.format("{%s;%s;%.2f}", prod.getName(), prod.getProducer(), prod.getPrice()))
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}


// Slow solution
//  List<Product> products = new ArrayList<>();
//    public String addProduct(String name, double price, String producer) {
//        Product product = new Product(name, price, producer);
//        products.add(product);
//        return "Product added\n";
//    }
//
//    public String delete(String name, String producer) {
//        int initialSize = products.size();
//        boolean removed = this.products.removeIf(p -> p.getProducer().equals(producer) && p.getName().equals(name));
//
//        return getRemovedMessage(initialSize, removed);
//    }
//
//    private String getRemovedMessage(int initialSize, boolean removed) {
//        return !removed ? "No products found\n" : String.format("%d products deleted\n", initialSize - products.size());
//    }
//
//    public String delete(String producer) {
//        int initialSize = products.size();
//        boolean removed = this.products.removeIf(p -> p.getProducer().equals(producer));
//
//        return getRemovedMessage(initialSize, removed);
//    }
//
//    public String findProductsByName(String name) {
//        List<Product> productByName = new ArrayList<>();
//        if (!products.isEmpty()) {
//            for (Product product : products) {
//                if (product.getName().equals(name)) {
//                    productByName.add(product);
//                }
//            }
//        }
//        return getProductMessage(productByName);
//    }
//
//    public String findProductsByProducer(String producer) {
//        List<Product> productByName = new ArrayList<>();
//        if (!products.isEmpty()) {
//            for (Product product : products) {
//                if (product.getProducer().equals(producer)) {
//                    productByName.add(product);
//                }
//            }
//        }
//
//        return getProductMessage(productByName);
//    }
//
//    public String findProductsByPriceRange(double priceFrom, double priceTo) {
//        List<Product> productByName = new ArrayList<>();
//        if (!products.isEmpty()) {
//            for (Product product : products) {
//                if ((product.getPrice() >= priceFrom && product.getPrice() <= priceTo)) {
//                    productByName.add(product);
//                }
//            }
//        }
//        return getProductMessage(productByName);
//    }
//
//    private String getProductMessage(List<Product> productByName) {
//        StringBuilder sb = new StringBuilder();
//        if (productByName.isEmpty()) {
//            return "No products found\n";
//        }
//        productByName = productByName.stream().sorted(Product::compareTo).collect(Collectors.toList());
//
//        for (Product prod : productByName) {
//            sb.append(String.format("{%s;%s;%.2f}", prod.getName(), prod.getProducer(), prod.getPrice()))
//                    .append(System.lineSeparator());
//        }
//        return sb.toString();
//    }

