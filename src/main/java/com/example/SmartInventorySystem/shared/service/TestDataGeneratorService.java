package com.example.SmartInventorySystem.shared.service;

import com.example.SmartInventorySystem.batcharrival.entity.BatchArrival;
import com.example.SmartInventorySystem.batcharrival.repository.BatchArrivalRepository;
import com.example.SmartInventorySystem.batcharrivalitem.entity.BatchArrivalItem;
import com.example.SmartInventorySystem.batcharrivalitem.repository.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.category.entity.Category;
import com.example.SmartInventorySystem.category.repository.CategoryRepository;
import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.product.repository.ProductRepository;
import com.example.SmartInventorySystem.saleitem.entity.SalesItem;
import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import com.example.SmartInventorySystem.salestransaction.repository.SalesTransactionRepository;
import com.example.SmartInventorySystem.supplier.entity.Supplier;
import com.example.SmartInventorySystem.supplier.repository.SupplierRepository;
import com.example.SmartInventorySystem.user.entity.User;
import com.example.SmartInventorySystem.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TestDataGeneratorService {

    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final BatchArrivalRepository batchArrivalRepository;
    private final BatchArrivalItemRepository batchArrivalItemRepository;
    private final SalesTransactionRepository salesTransactionRepository;
    private final SalesItemRepository salesItemRepository;

    private final Random random = new Random();

    // For storing generated data for easy access
    private List<Category> categories = new ArrayList<>();
    private List<Supplier> suppliers = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private User adminUser;

    public TestDataGeneratorService(CategoryRepository categoryRepository, 
                                   SupplierRepository supplierRepository,
                                   ProductRepository productRepository, 
                                   UserRepository userRepository,
                                   BatchArrivalRepository batchArrivalRepository,
                                   BatchArrivalItemRepository batchArrivalItemRepository,
                                   SalesTransactionRepository salesTransactionRepository,
                                   SalesItemRepository salesItemRepository) {
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.batchArrivalRepository = batchArrivalRepository;
        this.batchArrivalItemRepository = batchArrivalItemRepository;
        this.salesTransactionRepository = salesTransactionRepository;
        this.salesItemRepository = salesItemRepository;
    }

    @Transactional
    public void generateTestData() {
        // Clean tables for a fresh test dataset
        cleanup();
        
        // Create base data for a small cafe
        createCafeCategories();
        createCafeSuppliers();
        createCafeProducts();
        createAdminUser();
        
        // Create transaction data (only for the last month)
        LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
        createBatchArrivals(startDate);
        createSalesData(startDate);
        
        System.out.println("Cafe test data successfully created!");
    }
    
    @Transactional
    public void cleanup() {
        salesItemRepository.deleteAll();
        salesTransactionRepository.deleteAll();
        batchArrivalItemRepository.deleteAll();
        batchArrivalRepository.deleteAll();
        productRepository.deleteAll();
        supplierRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll(); // Also cleaning up users
    }

    private void createCafeCategories() {
        String[] categoryNames = {
            "Coffee Beans", "Tea", "Milk & Dairy", "Syrups", "Pastries", 
            "Desserts", "Coffee Equipment", "Disposables", "Snacks", "Beverages"
        };
        
        String[] descriptions = {
            "Various coffee beans and blends",
            "Different types of tea leaves and tea bags",
            "Milk, cream, and other dairy products",
            "Flavored syrups for coffee and other drinks",
            "Fresh baked goods and pastries",
            "Cakes, cookies, and other sweet treats",
            "Coffee machines, grinders, and accessories",
            "Cups, lids, napkins, and other disposable items",
            "Savory snacks and light meals",
            "Bottled and canned drinks"
        };
        
        for (int i = 0; i < categoryNames.length; i++) {
            Category category = new Category();
            category.setName(categoryNames[i]);
            category.setDescription(descriptions[i]);
            categories.add(categoryRepository.save(category));
        }
    }

    private void createCafeSuppliers() {
        String[] supplierNames = {
            "Bean Masters Coffee Co.", "Dairy Fresh", "Sweet Delights Bakery", 
            "Tea Treasures", "Café Equipment Pro", "Eco-Friendly Packaging", 
            "Flavor Fusion", "Beverage Distributors Inc."
        };

        for (int i = 0; i < supplierNames.length; i++) {
            Supplier supplier = new Supplier();
            supplier.setName(supplierNames[i]);
            supplier.setAddress("123 Supplier St, Coffee Town, CT " + (10000 + i));
            supplier.setContactInfo("Phone: (555) 123-45" + (10 + i) + ", Email: contact@" + supplierNames[i].toLowerCase().replaceAll("[^a-z]", "") + ".com");
            suppliers.add(supplierRepository.save(supplier));
        }
    }
    
    private void createCafeProducts() {
        // Coffee products
        Category coffeeCat = categories.get(0);
        Supplier coffeeSupplier = suppliers.get(0);
        createCoffeeProducts(coffeeCat, coffeeSupplier);
        
        // Tea products
        Category teaCat = categories.get(1);
        Supplier teaSupplier = suppliers.get(3);
        createTeaProducts(teaCat, teaSupplier);
        
        // Milk products
        Category milkCat = categories.get(2);
        Supplier milkSupplier = suppliers.get(1);
        createDairyProducts(milkCat, milkSupplier);
        
        // Syrup products
        Category syrupCat = categories.get(3);
        Supplier syrupSupplier = suppliers.get(6);
        createSyrupProducts(syrupCat, syrupSupplier);
        
        // Pastry products
        Category pastryCat = categories.get(4);
        Supplier pastrySupplier = suppliers.get(2);
        createPastryProducts(pastryCat, pastrySupplier);
        
        // Equipment products
        Category equipCat = categories.get(6);
        Supplier equipSupplier = suppliers.get(4);
        createEquipmentProducts(equipCat, equipSupplier);
        
        // Disposable products
        Category dispCat = categories.get(7);
        Supplier dispSupplier = suppliers.get(5);
        createDisposableProducts(dispCat, dispSupplier);
        
        // Beverage products
        Category bevCat = categories.get(9);
        Supplier bevSupplier = suppliers.get(7);
        createBeverageProducts(bevCat, bevSupplier);
    }
    
    private void createCoffeeProducts(Category category, Supplier supplier) {
        String[][] coffeeData = {
            {"Ethiopian Yirgacheffe", "Premium single-origin coffee with fruity notes", "kg", "25.99", "1.0", "10.0"},
            {"Colombian Supremo", "Medium roast with chocolate notes", "kg", "22.50", "1.0", "15.0"},
            {"Espresso Blend", "Dark roast perfect for espresso", "kg", "23.99", "1.0", "12.0"},
            {"Decaf House Blend", "Smooth decaffeinated medium roast", "kg", "24.50", "1.0", "8.0"},
            {"Breakfast Blend", "Light roast with citrus notes", "kg", "21.99", "1.0", "15.0"}
        };
        
        for (String[] data : coffeeData) {
            Product product = new Product();
            product.setProductName(data[0]);
            product.setDescription(data[1]);
            product.setUnitOfMeasure(data[2]);
            product.setPrice(new BigDecimal(data[3]));
            product.setVolume(new BigDecimal(data[4]));
            product.setThreshold(new BigDecimal(data[5]));
            product.setBarcode(String.format("%013d", 1000000000000L + products.size()));
            product.setCategory(category);
            product.setSupplier(supplier);
            product.setIsPerishable(true);
            product.setIsComposite(false);
            
            products.add(productRepository.save(product));
        }
    }
    
    private void createTeaProducts(Category category, Supplier supplier) {
        String[][] teaData = {
            {"Earl Grey", "Classic black tea with bergamot", "kg", "18.99", "0.25", "5.0"},
            {"Green Tea", "Japanese sencha green tea", "kg", "22.50", "0.25", "5.0"},
            {"Chamomile", "Relaxing herbal tea", "kg", "16.99", "0.25", "4.0"},
            {"Chai Mix", "Spiced chai tea blend", "kg", "19.50", "0.25", "5.0"},
            {"Peppermint", "Refreshing herbal tea", "kg", "15.99", "0.25", "4.0"}
        };
        
        for (String[] data : teaData) {
            Product product = new Product();
            product.setProductName(data[0]);
            product.setDescription(data[1]);
            product.setUnitOfMeasure(data[2]);
            product.setPrice(new BigDecimal(data[3]));
            product.setVolume(new BigDecimal(data[4]));
            product.setThreshold(new BigDecimal(data[5]));
            product.setBarcode(String.format("%013d", 1000000000000L + products.size()));
            product.setCategory(category);
            product.setSupplier(supplier);
            product.setIsPerishable(true);
            product.setIsComposite(false);
            
            products.add(productRepository.save(product));
        }
    }
    
    private void createDairyProducts(Category category, Supplier supplier) {
        String[][] dairyData = {
            {"Whole Milk", "Fresh organic whole milk", "l", "4.99", "1.0", "20.0"},
            {"Skim Milk", "Fat-free milk", "l", "4.50", "1.0", "20.0"},
            {"Almond Milk", "Plant-based alternative", "l", "5.99", "1.0", "15.0"},
            {"Oat Milk", "Creamy plant-based milk", "l", "5.50", "1.0", "15.0"},
            {"Heavy Cream", "For whipping and coffee", "l", "6.99", "0.5", "10.0"}
        };
        
        for (String[] data : dairyData) {
            Product product = new Product();
            product.setProductName(data[0]);
            product.setDescription(data[1]);
            product.setUnitOfMeasure(data[2]);
            product.setPrice(new BigDecimal(data[3]));
            product.setVolume(new BigDecimal(data[4]));
            product.setThreshold(new BigDecimal(data[5]));
            product.setBarcode(String.format("%013d", 1000000000000L + products.size()));
            product.setCategory(category);
            product.setSupplier(supplier);
            product.setIsPerishable(true);
            product.setIsComposite(false);
            
            products.add(productRepository.save(product));
        }
    }
    
    private void createSyrupProducts(Category category, Supplier supplier) {
        String[][] syrupData = {
            {"Vanilla Syrup", "Classic vanilla flavor", "bott", "9.99", "0.75", "8.0"},
            {"Caramel Syrup", "Rich caramel flavor", "bott", "9.99", "0.75", "8.0"},
            {"Hazelnut Syrup", "Nutty hazelnut flavor", "bott", "10.50", "0.75", "6.0"},
            {"Chocolate Syrup", "Premium chocolate flavor", "bott", "8.99", "0.75", "10.0"},
            {"Pumpkin Spice", "Seasonal favorite", "bott", "11.99", "0.75", "4.0"}
        };
        
        for (String[] data : syrupData) {
            Product product = new Product();
            product.setProductName(data[0]);
            product.setDescription(data[1]);
            product.setUnitOfMeasure(data[2]);
            product.setPrice(new BigDecimal(data[3]));
            product.setVolume(new BigDecimal(data[4]));
            product.setThreshold(new BigDecimal(data[5]));
            product.setBarcode(String.format("%013d", 1000000000000L + products.size()));
            product.setCategory(category);
            product.setSupplier(supplier);
            product.setIsPerishable(false);
            product.setIsComposite(false);
            
            products.add(productRepository.save(product));
        }
    }
    
    private void createPastryProducts(Category category, Supplier supplier) {
        String[][] pastryData = {
            {"Croissant", "Buttery French pastry", "pcs", "2.50", "1.0", "20.0"},
            {"Blueberry Muffin", "Fresh-baked muffin", "pcs", "2.99", "1.0", "15.0"},
            {"Cinnamon Roll", "Sweet breakfast pastry", "pcs", "3.50", "1.0", "12.0"},
            {"Bagel", "Plain New York style", "pcs", "2.25", "1.0", "25.0"},
            {"Chocolate Croissant", "Pastry with chocolate filling", "pcs", "3.25", "1.0", "15.0"}
        };
        
        for (String[] data : pastryData) {
            Product product = new Product();
            product.setProductName(data[0]);
            product.setDescription(data[1]);
            product.setUnitOfMeasure(data[2]);
            product.setPrice(new BigDecimal(data[3]));
            product.setVolume(new BigDecimal(data[4]));
            product.setThreshold(new BigDecimal(data[5]));
            product.setBarcode(String.format("%013d", 1000000000000L + products.size()));
            product.setCategory(category);
            product.setSupplier(supplier);
            product.setIsPerishable(true);
            product.setIsComposite(false);
            
            products.add(productRepository.save(product));
        }
    }
    
    private void createEquipmentProducts(Category category, Supplier supplier) {
        String[][] equipData = {
            {"Coffee Filter", "Paper filters for drip coffee", "pack", "5.99", "100.0", "10.0"},
            {"Tamper", "Stainless steel espresso tamper", "pcs", "24.99", "1.0", "2.0"},
            {"Milk Pitcher", "Stainless steel frothing pitcher", "pcs", "15.99", "1.0", "5.0"},
            {"Coffee Grinder Burr", "Replacement burr for grinder", "pcs", "39.99", "1.0", "2.0"},
            {"Cleaning Tablets", "For espresso machine maintenance", "pack", "12.99", "10.0", "5.0"}
        };
        
        for (String[] data : equipData) {
            Product product = new Product();
            product.setProductName(data[0]);
            product.setDescription(data[1]);
            product.setUnitOfMeasure(data[2]);
            product.setPrice(new BigDecimal(data[3]));
            product.setVolume(new BigDecimal(data[4]));
            product.setThreshold(new BigDecimal(data[5]));
            product.setBarcode(String.format("%013d", 1000000000000L + products.size()));
            product.setCategory(category);
            product.setSupplier(supplier);
            product.setIsPerishable(false);
            product.setIsComposite(false);
            
            products.add(productRepository.save(product));
        }
    }
    
    private void createDisposableProducts(Category category, Supplier supplier) {
        String[][] disposableData = {
            {"Paper Cup 12oz", "Compostable hot cups", "pack", "24.99", "100.0", "15.0"},
            {"Paper Cup 16oz", "Compostable hot cups", "pack", "29.99", "100.0", "15.0"},
            {"Cup Lid", "Recyclable plastic lids", "pack", "14.99", "100.0", "20.0"},
            {"Paper Napkin", "Recycled paper napkins", "pack", "9.99", "500.0", "10.0"},
            {"Straw", "Compostable straws", "pack", "12.99", "200.0", "8.0"}
        };
        
        for (String[] data : disposableData) {
            Product product = new Product();
            product.setProductName(data[0]);
            product.setDescription(data[1]);
            product.setUnitOfMeasure(data[2]);
            product.setPrice(new BigDecimal(data[3]));
            product.setVolume(new BigDecimal(data[4]));
            product.setThreshold(new BigDecimal(data[5]));
            product.setBarcode(String.format("%013d", 1000000000000L + products.size()));
            product.setCategory(category);
            product.setSupplier(supplier);
            product.setIsPerishable(false);
            product.setIsComposite(false);
            
            products.add(productRepository.save(product));
        }
    }
    
    private void createBeverageProducts(Category category, Supplier supplier) {
        String[][] beverageData = {
            {"Bottled Water", "Spring water", "bott", "1.99", "1.0", "50.0"},
            {"Sparkling Water", "Carbonated mineral water", "bott", "2.50", "1.0", "30.0"},
            {"Orange Juice", "Fresh-squeezed juice", "bott", "3.99", "0.5", "20.0"},
            {"Iced Tea", "Bottled unsweetened tea", "bott", "2.99", "0.5", "25.0"},
            {"Lemonade", "Fresh-squeezed lemonade", "bott", "3.50", "0.5", "20.0"}
        };
        
        for (String[] data : beverageData) {
            Product product = new Product();
            product.setProductName(data[0]);
            product.setDescription(data[1]);
            product.setUnitOfMeasure(data[2]);
            product.setPrice(new BigDecimal(data[3]));
            product.setVolume(new BigDecimal(data[4]));
            product.setThreshold(new BigDecimal(data[5]));
            product.setBarcode(String.format("%013d", 1000000000000L + products.size()));
            product.setCategory(category);
            product.setSupplier(supplier);
            product.setIsPerishable(true);
            product.setIsComposite(false);
            
            products.add(productRepository.save(product));
        }
    }
    
    private void createAdminUser() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordHash("$2a$10$dL4JXvMvAWe/uEg4z5ni5OEvSp4S48U9qwWW9.juWnFECXl5hPpBW"); // password: admin
        admin.setRole("ROLE_ADMIN");
        adminUser = userRepository.save(admin);
    }
    
    private void createBatchArrivals(LocalDateTime startDate) {
        // Create batch arrivals within the last month
        long daysBetween = ChronoUnit.DAYS.between(startDate, LocalDateTime.now());
        
        // Create one batch arrival per supplier to ensure all products are in stock
        for (Supplier supplier : suppliers) {
            // Random date within the last month
            long randomDays = ThreadLocalRandom.current().nextLong(0, daysBetween + 1);
            LocalDateTime arrivalDate = startDate.plusDays(randomDays);
            
            BatchArrival batchArrival = new BatchArrival();
            batchArrival.setArrivalDate(arrivalDate);
            batchArrival.setNotes("Regular delivery from " + supplier.getName());
            batchArrival.setSupplier(supplier);
            batchArrival.setAddedBy(adminUser);
            
            BatchArrival savedBatchArrival = batchArrivalRepository.save(batchArrival);
            
            // Add batch arrival items for all products from this supplier
            List<Product> supplierProducts = products.stream()
                .filter(p -> p.getSupplier().getSupplierId().equals(supplier.getSupplierId()))
                .toList();
                
            for (Product product : supplierProducts) {
                BatchArrivalItem item = new BatchArrivalItem();
                item.setBatchArrival(savedBatchArrival);
                item.setProduct(product);
                
                // Random quantity based on product threshold
                BigDecimal quantity = product.getThreshold()
                    .multiply(BigDecimal.valueOf(2 + random.nextDouble() * 3)) // 2-5x threshold
                    .setScale(2, RoundingMode.HALF_UP);
                    
                item.setQuantityReceived(quantity);
                item.setQuantityRemaining(quantity);
                
                // Cost is 60-80% of retail price
                BigDecimal unitCost = product.getPrice()
                    .multiply(BigDecimal.valueOf(0.6 + random.nextDouble() * 0.2))
                    .setScale(2, RoundingMode.HALF_UP);
                item.setUnitCost(unitCost);
                
                // Set expiry date for perishable products
                if (product.getIsPerishable()) {
                    // Expiry date 7-90 days from arrival date
                    LocalDate expiryDate = arrivalDate.toLocalDate()
                        .plusDays(7 + random.nextInt(84));
                    item.setExpiryDate(expiryDate);
                }
                
                batchArrivalItemRepository.save(item);
            }
        }
        
        // Create a few more random batch arrivals if needed
        int additionalBatchArrivals = Math.max(0, 15 - suppliers.size());
        for (int i = 0; i < additionalBatchArrivals; i++) {
            Supplier supplier = suppliers.get(random.nextInt(suppliers.size()));
            
            // Random date within the last month
            long randomDays = ThreadLocalRandom.current().nextLong(0, daysBetween + 1);
            LocalDateTime arrivalDate = startDate.plusDays(randomDays);
            
            BatchArrival batchArrival = new BatchArrival();
            batchArrival.setArrivalDate(arrivalDate);
            batchArrival.setNotes("Additional delivery from " + supplier.getName());
            batchArrival.setSupplier(supplier);
            batchArrival.setAddedBy(adminUser);
            
            BatchArrival savedBatchArrival = batchArrivalRepository.save(batchArrival);
            
            // Add 1-5 random products from this supplier
            List<Product> supplierProducts = products.stream()
                .filter(p -> p.getSupplier().getSupplierId().equals(supplier.getSupplierId()))
                .toList();
                
            int itemCount = 1 + random.nextInt(Math.min(5, supplierProducts.size()));
            
            // Avoid duplicate products in the same batch arrival
            Set<Integer> usedProductIndices = new HashSet<>();
            
            for (int j = 0; j < itemCount; j++) {
                int productIndex;
                do {
                    productIndex = random.nextInt(supplierProducts.size());
                } while (usedProductIndices.contains(productIndex));
                
                usedProductIndices.add(productIndex);
                Product product = supplierProducts.get(productIndex);
                
                BatchArrivalItem item = new BatchArrivalItem();
                item.setBatchArrival(savedBatchArrival);
                item.setProduct(product);
                
                // Random quantity
                BigDecimal quantity = BigDecimal.valueOf(1 + random.nextInt(20))
                    .setScale(2, RoundingMode.HALF_UP);
                item.setQuantityReceived(quantity);
                item.setQuantityRemaining(quantity);
                
                // Cost is 60-80% of retail price
                BigDecimal unitCost = product.getPrice()
                    .multiply(BigDecimal.valueOf(0.6 + random.nextDouble() * 0.2))
                    .setScale(2, RoundingMode.HALF_UP);
                item.setUnitCost(unitCost);
                
                // Set expiry date for perishable products
                if (product.getIsPerishable()) {
                    LocalDate expiryDate = arrivalDate.toLocalDate()
                        .plusDays(7 + random.nextInt(84));
                    item.setExpiryDate(expiryDate);
                }
                
                batchArrivalItemRepository.save(item);
            }
        }
    }
    
    private void createSalesData(LocalDateTime startDate) {
        // Distribute sales throughout the period (last month)
        long daysBetween = ChronoUnit.DAYS.between(startDate, LocalDateTime.now());
        
        for (int i = 0; i < 200; i++) {
            SalesTransaction transaction = new SalesTransaction();
            
            // Random date within the period
            long randomDays = ThreadLocalRandom.current().nextLong(0, daysBetween + 1);
            // Weigh towards business hours (8am-8pm)
            int hour = 8 + random.nextInt(12);
            int minute = random.nextInt(60);
            
            LocalDateTime transactionDate = startDate.plusDays(randomDays)
                .withHour(hour).withMinute(minute);
            
            transaction.setTransactionDate(transactionDate);
            transaction.setTotalAmount(BigDecimal.ZERO); // Will be updated after creating items
            
            // Save transaction
            SalesTransaction savedTransaction = salesTransactionRepository.save(transaction);
            
            // Create 1-5 items per transaction
            int itemsCount = 1 + random.nextInt(5);
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            // Avoid duplicate products in same transaction
            Set<Integer> usedProductIndices = new HashSet<>();
            
            for (int j = 0; j < itemsCount; j++) {
                // Select random product not yet in this transaction
                int productIndex;
                do {
                    productIndex = random.nextInt(products.size());
                } while (usedProductIndices.contains(productIndex));
                
                usedProductIndices.add(productIndex);
                Product product = products.get(productIndex);
                
                SalesItem item = new SalesItem();
                item.setSalesTransaction(savedTransaction);
                item.setProduct(product);
                
                // Random quantity (1-3 for most items)
                BigDecimal quantity = BigDecimal.valueOf(1 + random.nextInt(3))
                    .setScale(2, RoundingMode.HALF_UP);
                item.setQuantity(quantity);
                
                // Calculate item amount
                BigDecimal itemAmount = product.getPrice()
                    .multiply(quantity)
                    .setScale(2, RoundingMode.HALF_UP);
                totalAmount = totalAmount.add(itemAmount);
                
                // For perishable products, add expiry date
                if (product.getIsPerishable()) {
                    LocalDate expiryDate = transactionDate.toLocalDate()
                        .plusDays(random.nextInt(30) + 1);
                    item.setExpiryDate(expiryDate);
                }
                
                salesItemRepository.save(item);
                
                // Update stock levels
                updateRemainingStock(product.getProductId(), quantity);
            }
            
            // Update transaction total
            savedTransaction.setTotalAmount(totalAmount);
            salesTransactionRepository.save(savedTransaction);
        }
    }
    
    private void updateRemainingStock(Long productId, BigDecimal quantity) {
        // Find batch items with this product and remaining quantity > 0, sort by expiry date (FIFO)
        List<BatchArrivalItem> items = batchArrivalItemRepository.findAll().stream()
            .filter(item -> item.getProduct().getProductId().equals(productId) 
                    && item.getQuantityRemaining().compareTo(BigDecimal.ZERO) > 0)
            .sorted(Comparator.comparing(item -> 
                    item.getExpiryDate() != null ? item.getExpiryDate() : LocalDate.MAX))
            .toList();
        
        BigDecimal remainingToDeduct = quantity;
        
        for (BatchArrivalItem item : items) {
            if (remainingToDeduct.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            
            BigDecimal currentRemaining = item.getQuantityRemaining();
            
            if (currentRemaining.compareTo(remainingToDeduct) >= 0) {
                // Enough stock in this batch
                item.setQuantityRemaining(currentRemaining.subtract(remainingToDeduct));
                remainingToDeduct = BigDecimal.ZERO;
            } else {
                // Use all remaining in this batch and continue
                item.setQuantityRemaining(BigDecimal.ZERO);
                remainingToDeduct = remainingToDeduct.subtract(currentRemaining);
            }
            
            batchArrivalItemRepository.save(item);
        }
    }

    @Transactional
    public void generateAITestData() {
        // Clean transaction data
        salesItemRepository.deleteAll();
        salesTransactionRepository.deleteAll();
        
        // Check for base data, create if none exists
        if (categoryRepository.count() == 0) {
            createCafeCategories();
        } else {
            categories = categoryRepository.findAll();
        }
        
        if (supplierRepository.count() == 0) {
            createCafeSuppliers();
        } else {
            suppliers = supplierRepository.findAll();
        }
        
        if (productRepository.count() == 0) {
            createCafeProducts();
        } else {
            products = productRepository.findAll();
        }
        
        if (userRepository.count() == 0) {
            createAdminUser();
        } else {
            adminUser = userRepository.findAll().get(0);
        }
        
        // Create AI test data - large sales data set with clear seasonal trends and trends
        
        LocalDateTime startDate = LocalDateTime.now().minusMonths(12);
        createPatternedSalesData(startDate);
        
        System.out.println("AI test data successfully created!");
    }

    private void createPatternedSalesData(LocalDateTime startDate) {
        // Create good data patterns for AI
        // Select 10 products that will have clear patterns
        
        List<Product> selectedProducts = new ArrayList<>();
        if (products.size() >= 10) {
            // Take 10 random products
            Random random = new Random();
            Set<Integer> selectedIndexes = new HashSet<>();
            while (selectedIndexes.size() < 10) {
                selectedIndexes.add(random.nextInt(products.size()));
            }
            
            for (Integer index : selectedIndexes) {
                selectedProducts.add(products.get(index));
            }
        } else {
            // If we have less than 10 products, take all we have
            selectedProducts.addAll(products);
        }
        
        // Products with seasonal sales
        createSeasonalSalesPattern(selectedProducts.get(0), startDate, "summer"); // Summer product
        createSeasonalSalesPattern(selectedProducts.get(1), startDate, "winter"); // Winter product
        
        // Products with weekly peaks (weekends)
        createWeeklyPattern(selectedProducts.get(2), startDate, "weekend"); // More sales on weekends
        createWeeklyPattern(selectedProducts.get(3), startDate, "weekday"); // More sales on weekdays
        
        // Products with monthly peaks (start/end of month)
        createMonthlyPattern(selectedProducts.get(4), startDate, "start"); // More in the beginning of the month
        createMonthlyPattern(selectedProducts.get(5), startDate, "end"); // More in the end of the month
        
        // Products with clear trends (growth/decline)
        createTrendPattern(selectedProducts.get(6), startDate, "growing"); // Growing sales
        createTrendPattern(selectedProducts.get(7), startDate, "declining"); // Declining sales
        
        // Products with stable sales
        createStablePattern(selectedProducts.get(8), startDate, "high"); // Stable high
        createStablePattern(selectedProducts.get(9), startDate, "low"); // Stable low
    }

    private void createSeasonalSalesPattern(Product product, LocalDateTime startDate, String season) {
        LocalDateTime current = startDate;
        LocalDateTime endDate = LocalDateTime.now();
        
        while (current.isBefore(endDate)) {
            int month = current.getMonthValue();
            
            // Determine "seasonality" of sales
            double multiplier;
            if (season.equals("summer")) {
                // Summer product: peak in June-August
                if (month >= 6 && month <= 8) {
                    multiplier = 5.0; // 5x more in summer
                } else if (month >= 4 && month <= 9) {
                    multiplier = 2.0; // Spring and fall - average season
                } else {
                    multiplier = 1.0; // Winter - low season
                }
            } else {
                // Winter product: peak in December-February
                if (month == 12 || month <= 2) {
                    multiplier = 5.0; // 5x more in winter
                } else if (month >= 10 || month <= 4) {
                    multiplier = 2.0; // Fall and spring - average season
                } else {
                    multiplier = 1.0; // Summer - low season
                }
            }
            
            // Create sales with seasonality
            createSalesForDay(product, current, (int)(1 + random.nextInt(5) * multiplier));
            
            // Move to next day
            current = current.plusDays(1);
        }
    }

    private void createWeeklyPattern(Product product, LocalDateTime startDate, String pattern) {
        LocalDateTime current = startDate;
        LocalDateTime endDate = LocalDateTime.now();
        
        while (current.isBefore(endDate)) {
            int dayOfWeek = current.getDayOfWeek().getValue(); // 1 (Monday) - 7 (Sunday)
            
            // Determine weekly sales pattern
            double multiplier;
            if (pattern.equals("weekend")) {
                // More sales on weekends
                if (dayOfWeek >= 6) { // Saturday and Sunday
                    multiplier = 3.0;
                } else if (dayOfWeek == 5) { // Friday
                    multiplier = 2.0;
                } else {
                    multiplier = 1.0;
                }
            } else { // weekday
                // More sales on weekdays
                if (dayOfWeek <= 5) { // Monday-Friday
                    multiplier = 2.5;
                } else {
                    multiplier = 1.0;
                }
            }
            
            // Create sales with day of the week
            createSalesForDay(product, current, (int)(1 + random.nextInt(5) * multiplier));
            
            // Move to next day
            current = current.plusDays(1);
        }
    }

    private void createMonthlyPattern(Product product, LocalDateTime startDate, String pattern) {
        LocalDateTime current = startDate;
        LocalDateTime endDate = LocalDateTime.now();
        
        while (current.isBefore(endDate)) {
            int dayOfMonth = current.getDayOfMonth();
            int lastDayOfMonth = current.getMonth().length(current.toLocalDate().isLeapYear());
            
            // Determine monthly sales pattern
            double multiplier;
            if (pattern.equals("start")) {
                // More sales in the beginning of the month
                if (dayOfMonth <= 5) {
                    multiplier = 4.0;
                } else if (dayOfMonth <= 10) {
                    multiplier = 2.0;
                } else {
                    multiplier = 1.0;
                }
            } else { // end
                // More sales in the end of the month
                if (dayOfMonth >= lastDayOfMonth - 4) {
                    multiplier = 4.0;
                } else if (dayOfMonth >= lastDayOfMonth - 9) {
                    multiplier = 2.0;
                } else {
                    multiplier = 1.0;
                }
            }
            
            // Create sales with day of the month
            createSalesForDay(product, current, (int)(1 + random.nextInt(4) * multiplier));
            
            // Move to next day
            current = current.plusDays(1);
        }
    }

    private void createTrendPattern(Product product, LocalDateTime startDate, String trend) {
        LocalDateTime current = startDate;
        LocalDateTime endDate = LocalDateTime.now();
        
        // Total days for trend calculation
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        long currentDay = 0;
        
        while (current.isBefore(endDate)) {
            // Calculate progress (0 to 1) for current day
            double progress = (double) currentDay / totalDays;
            
            // Determine multiplier based on trend
            double multiplier;
            if (trend.equals("growing")) {
                // Growing sales: 1 to 5
                multiplier = 1.0 + 4.0 * progress;
            } else { // declining
                // Declining sales: 5 to 1
                multiplier = 5.0 - 4.0 * progress;
            }
            
            // Create sales with trend
            createSalesForDay(product, current, (int)(1 + random.nextInt(3) * multiplier));
            
            // Move to next day
            current = current.plusDays(1);
            currentDay++;
        }
    }

    private void createStablePattern(Product product, LocalDateTime startDate, String level) {
        LocalDateTime current = startDate;
        LocalDateTime endDate = LocalDateTime.now();
        
        // Base multiplier based on sales level
        double baseMultiplier = level.equals("high") ? 5.0 : 1.0;
        
        while (current.isBefore(endDate)) {
            // Add slight randomness for realism
            double randomFactor = 0.8 + (random.nextDouble() * 0.4); // 0.8 - 1.2
            
            // Create sales with level and randomness
            createSalesForDay(product, current, (int)(1 + random.nextInt(3) * baseMultiplier * randomFactor));
            
            // Move to next day
            current = current.plusDays(1);
        }
    }

    private void createSalesForDay(Product product, LocalDateTime date, int quantity) {
        if (quantity <= 0) return;
        
        SalesTransaction transaction = new SalesTransaction();
        transaction.setTransactionDate(date);
        transaction.setTotalAmount(BigDecimal.ZERO);
        
        SalesTransaction savedTransaction = salesTransactionRepository.save(transaction);
        
        SalesItem item = new SalesItem();
        item.setSalesTransaction(savedTransaction);
        item.setProduct(product);
        
        BigDecimal quantityBD = BigDecimal.valueOf(quantity).setScale(2, RoundingMode.HALF_UP);
        item.setQuantity(quantityBD);
        
        if (product.getIsPerishable()) {
            LocalDate expiryDate = date.toLocalDate().plusDays(random.nextInt(30) + 1);
            item.setExpiryDate(expiryDate);
        }
        
        salesItemRepository.save(item);
        
        // Calculate and update total
        BigDecimal totalAmount = product.getPrice().multiply(quantityBD)
                .setScale(2, RoundingMode.HALF_UP);
        savedTransaction.setTotalAmount(totalAmount);
        salesTransactionRepository.save(savedTransaction);
    }
} 