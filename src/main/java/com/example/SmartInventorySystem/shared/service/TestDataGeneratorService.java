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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    // Для хранения сгенерированных данных для удобного доступа
    private List<Category> categories = new ArrayList<>();
    private List<Supplier> suppliers = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private List<User> users = new ArrayList<>();

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
        // Очистка таблиц для чистого тестового набора (опционально)
        cleanup();
        
        // Создание базовых данных
        createCategories(5);
        createSuppliers(10);
        createProducts(50);
        createUsers(3);
        
        // Создание транзакционных данных
        LocalDateTime startDate = LocalDateTime.now().minusMonths(12); // данные за последний год
        createBatchArrivals(20, startDate);
        createSalesData(1000, startDate);
        
        System.out.println("Тестовые данные успешно созданы!");
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
        // Не очищаем пользователей, так как могут быть существующие учетные записи
    }

    private void createCategories(int count) {
        String[] categoryNames = {
            "Напитки", "Молочные продукты", "Мясо", "Овощи", "Фрукты", 
            "Выпечка", "Консервы", "Снеки", "Кондитерские изделия", "Бакалея"
        };
        
        IntStream.range(0, Math.min(count, categoryNames.length))
            .forEach(i -> {
                Category category = new Category();
                category.setName(categoryNames[i]);
                category.setDescription("Описание для категории " + categoryNames[i]);
                categories.add(categoryRepository.save(category));
            });
    }

    private void createSuppliers(int count) {
        String[] supplierNames = {
            "ООО 'ПродуктМастер'", "ИП Иванов И.И.", "АО 'Свежие продукты'", 
            "ООО 'МегаФуд'", "ОАО 'ПродуктСервис'", "ИП Петров П.П.", 
            "ООО 'ФудДистрибьюшн'", "ЗАО 'АгроХолдинг'", "ООО 'ФудСистемс'", 
            "ООО 'ЛогистикаПродукт'"
        };
        
        IntStream.range(0, Math.min(count, supplierNames.length))
            .forEach(i -> {
                Supplier supplier = new Supplier();
                supplier.setName(supplierNames[i]);
                supplier.setAddress("Адрес поставщика " + (i+1));
                supplier.setContactInfo("Контакт: +7 (900) " + String.format("%03d", i) + "-" + String.format("%04d", i));
                suppliers.add(supplierRepository.save(supplier));
            });
    }
    
    private void createProducts(int count) {
        String[] productNames = {
            "Молоко", "Хлеб", "Яблоки", "Бананы", "Говядина", 
            "Курица", "Сыр", "Йогурт", "Макароны", "Рис", 
            "Гречка", "Картофель", "Морковь", "Лук", "Помидоры", 
            "Огурцы", "Творог", "Сметана", "Кефир", "Масло", 
            "Мука", "Сахар", "Соль", "Печенье", "Конфеты",
            "Чай", "Кофе", "Сок", "Вода", "Газировка",
            "Чипсы", "Сухарики", "Мороженое", "Колбаса", "Ветчина",
            "Шоколад", "Мед", "Орехи", "Капуста", "Яйца",
            "Тушенка", "Сосиски", "Пельмени", "Вареники", "Замороженные овощи",
            "Пиво", "Вино", "Зелень", "Дрожжи", "Баклажаны"
        };
        
        String[] units = {"кг", "л", "шт", "упак", "бут"};
        
        IntStream.range(0, Math.min(count, productNames.length))
            .forEach(i -> {
                Product product = new Product();
                product.setProductName(productNames[i]);
                product.setBarcode(String.format("%013d", 1000000000000L + i));
                product.setCategory(categories.get(random.nextInt(categories.size())));
                product.setSupplier(suppliers.get(random.nextInt(suppliers.size())));
                product.setIsPerishable(random.nextBoolean());
                product.setIsComposite(random.nextBoolean());
                product.setUnitOfMeasure(units[random.nextInt(units.length)]);
                product.setDescription("Описание продукта " + productNames[i]);
                
                // Генерируем цену от 10 до 1000 руб
                BigDecimal price = BigDecimal.valueOf(10 + random.nextInt(991))
                                             .add(BigDecimal.valueOf(random.nextInt(100) / 100.0))
                                             .setScale(2, RoundingMode.HALF_UP);
                product.setPrice(price);
                
                // Устанавливаем объем от 0.1 до 10 единиц
                BigDecimal volume = BigDecimal.valueOf(0.1 + random.nextInt(100) / 10.0)
                                              .setScale(2, RoundingMode.HALF_UP);
                product.setVolume(volume);
                
                // Устанавливаем порог от 5 до 50 единиц
                BigDecimal threshold = BigDecimal.valueOf(5 + random.nextInt(46))
                                                 .setScale(2, RoundingMode.HALF_UP);
                product.setThreshold(threshold);
                
                products.add(productRepository.save(product));
            });
    }
    
    private void createUsers(int count) {
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setUsername("testuser" + (i+1));
            user.setPasswordHash("$2a$10$dL4JXvMvAWe/uEg4z5ni5OEvSp4S48U9qwWW9.juWnFECXl5hPpBW"); // password
            user.setRole("USER");
            users.add(userRepository.save(user));
        }
        
        // Добавим одного администратора
        User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordHash("$2a$10$dL4JXvMvAWe/uEg4z5ni5OEvSp4S48U9qwWW9.juWnFECXl5hPpBW"); // admin
        admin.setRole("ADMIN");
        users.add(userRepository.save(admin));
    }
    
    private void createBatchArrivals(int count, LocalDateTime startDate) {
        // Распределим поставки на протяжении всего периода
        long daysBetween = ChronoUnit.DAYS.between(startDate, LocalDateTime.now());
        
        for (int i = 0; i < count; i++) {
            BatchArrival batchArrival = new BatchArrival();
            
            // Случайная дата в заданном диапазоне
            long randomDays = ThreadLocalRandom.current().nextLong(0, daysBetween + 1);
            LocalDateTime arrivalDate = startDate.plusDays(randomDays);
            
            batchArrival.setArrivalDate(arrivalDate);
            batchArrival.setNotes("Тестовая поставка #" + (i+1));
            batchArrival.setSupplier(suppliers.get(random.nextInt(suppliers.size())));
            batchArrival.setAddedBy(users.get(random.nextInt(users.size())));
            
            // Сохраняем партию
            BatchArrival savedBatchArrival = batchArrivalRepository.save(batchArrival);
            
            // Создаем от 1 до 10 позиций в партии
            int itemsCount = 1 + random.nextInt(10);
            for (int j = 0; j < itemsCount; j++) {
                BatchArrivalItem item = new BatchArrivalItem();
                item.setBatchArrival(savedBatchArrival);
                
                // Выбираем случайный продукт
                Product randomProduct = products.get(random.nextInt(products.size()));
                item.setProduct(randomProduct);
                
                // Устанавливаем количество от 10 до 1000 единиц
                BigDecimal quantity = BigDecimal.valueOf(10 + random.nextInt(991))
                                                .setScale(2, RoundingMode.HALF_UP);
                item.setQuantityReceived(quantity);
                item.setQuantityRemaining(quantity); // Изначально остаток равен полученному количеству
                
                // Устанавливаем себестоимость (80% от цены продажи)
                BigDecimal unitCost = randomProduct.getPrice()
                                                    .multiply(BigDecimal.valueOf(0.8))
                                                    .setScale(2, RoundingMode.HALF_UP);
                item.setUnitCost(unitCost);
                
                // Для скоропортящихся продуктов устанавливаем срок годности
                if (randomProduct.getIsPerishable()) {
                    // Срок годности от 7 до 365 дней от даты поставки
                    LocalDate expiryDate = arrivalDate.toLocalDate().plusDays(7 + random.nextInt(359));
                    item.setExpiryDate(expiryDate);
                }
                
                batchArrivalItemRepository.save(item);
            }
        }
    }
    
    private void createSalesData(int count, LocalDateTime startDate) {
        // Распределим продажи на протяжении всего периода
        long daysBetween = ChronoUnit.DAYS.between(startDate, LocalDateTime.now());
        
        for (int i = 0; i < count; i++) {
            SalesTransaction transaction = new SalesTransaction();
            
            // Случайная дата в заданном диапазоне
            long randomDays = ThreadLocalRandom.current().nextLong(0, daysBetween + 1);
            LocalDateTime transactionDate = startDate.plusDays(randomDays);
            
            transaction.setTransactionDate(transactionDate);
            transaction.setTotalAmount(BigDecimal.ZERO); // Будет обновлено после создания позиций
            
            // Создаем от 1 до 5 позиций в транзакции
            int itemsCount = 1 + random.nextInt(5);
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            // Сохраняем транзакцию
            SalesTransaction savedTransaction = salesTransactionRepository.save(transaction);
            
            // Набор продуктов для этой транзакции (чтобы избежать дубликатов)
            Set<Integer> usedProductIndexes = new HashSet<>();
            
            for (int j = 0; j < itemsCount; j++) {
                // Выбираем случайный продукт, которого еще нет в этой транзакции
                int productIndex;
                do {
                    productIndex = random.nextInt(products.size());
                } while (usedProductIndexes.contains(productIndex));
                
                usedProductIndexes.add(productIndex);
                Product randomProduct = products.get(productIndex);
                
                SalesItem item = new SalesItem();
                item.setSalesTransaction(savedTransaction);
                item.setProduct(randomProduct);
                
                // Устанавливаем количество от 1 до 10 единиц
                BigDecimal quantity = BigDecimal.valueOf(1 + random.nextInt(10))
                                               .setScale(2, RoundingMode.HALF_UP);
                item.setQuantity(quantity);
                
                // Добавляем к общей сумме
                BigDecimal itemAmount = randomProduct.getPrice().multiply(quantity)
                                                               .setScale(2, RoundingMode.HALF_UP);
                totalAmount = totalAmount.add(itemAmount);
                
                // Добавляем случайный срок годности для проданного продукта (если это скоропортящийся продукт)
                if (randomProduct.getIsPerishable()) {
                    LocalDate expiryDate = transactionDate.toLocalDate().plusDays(random.nextInt(30) + 1);
                    item.setExpiryDate(expiryDate);
                }
                
                salesItemRepository.save(item);
                
                // Имитируем уменьшение остатка (опционально)
                // Это можно сделать более сложно, с учетом партий, но для теста упростим
                updateRemainingStock(randomProduct.getProductId(), quantity);
            }
            
            // Обновляем общую сумму транзакции
            savedTransaction.setTotalAmount(totalAmount);
            salesTransactionRepository.save(savedTransaction);
        }
    }
    
    private void updateRemainingStock(Long productId, BigDecimal quantity) {
        // Находим партии с этим продуктом и остатком > 0, сортируем по сроку годности (FIFO)
        List<BatchArrivalItem> items = batchArrivalItemRepository.findAll().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId) 
                        && item.getQuantityRemaining().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(item -> 
                        item.getExpiryDate() != null ? item.getExpiryDate() : LocalDate.MAX))
                .collect(Collectors.toList());
        
        BigDecimal remainingToDeduct = quantity;
        
        for (BatchArrivalItem item : items) {
            if (remainingToDeduct.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            
            BigDecimal currentRemaining = item.getQuantityRemaining();
            
            if (currentRemaining.compareTo(remainingToDeduct) >= 0) {
                // Достаточно остатка в этой партии
                item.setQuantityRemaining(currentRemaining.subtract(remainingToDeduct));
                remainingToDeduct = BigDecimal.ZERO;
            } else {
                // Используем весь остаток из этой партии и продолжаем
                item.setQuantityRemaining(BigDecimal.ZERO);
                remainingToDeduct = remainingToDeduct.subtract(currentRemaining);
            }
            
            batchArrivalItemRepository.save(item);
        }
    }

    @Transactional
    public void generateAITestData() {
        // Очистка только транзакционных данных
        salesItemRepository.deleteAll();
        salesTransactionRepository.deleteAll();
        
        // Проверяем наличие базовых данных, если их нет - создаем
        if (categoryRepository.count() == 0) {
            createCategories(5);
        } else {
            categories = categoryRepository.findAll();
        }
        
        if (supplierRepository.count() == 0) {
            createSuppliers(5);
        } else {
            suppliers = supplierRepository.findAll();
        }
        
        if (productRepository.count() == 0) {
            createProducts(30);
        } else {
            products = productRepository.findAll();
        }
        
        if (userRepository.count() == 0) {
            createUsers(2);
        } else {
            users = userRepository.findAll();
        }
        
        // Создаем данные для AI тестирования - большой набор продаж за год
        // с четкими сезонными тенденциями и трендами
        
        LocalDateTime startDate = LocalDateTime.now().minusMonths(12);
        createPatternedSalesData(startDate);
        
        System.out.println("Тестовые данные для AI успешно созданы!");
    }

    private void createPatternedSalesData(LocalDateTime startDate) {
        // Создаем хорошие паттерны данных для AI
        // Выберем 10 продуктов, которые будут иметь четкие паттерны
        
        List<Product> selectedProducts = new ArrayList<>();
        if (products.size() >= 10) {
            // Берем 10 случайных продуктов
            Random random = new Random();
            Set<Integer> selectedIndexes = new HashSet<>();
            while (selectedIndexes.size() < 10) {
                selectedIndexes.add(random.nextInt(products.size()));
            }
            
            for (Integer index : selectedIndexes) {
                selectedProducts.add(products.get(index));
            }
        } else {
            // Если у нас меньше 10 продуктов, берем все что есть
            selectedProducts.addAll(products);
        }
        
        // Продукты с сезонными продажами
        createSeasonalSalesPattern(selectedProducts.get(0), startDate, "summer"); // Летний продукт
        createSeasonalSalesPattern(selectedProducts.get(1), startDate, "winter"); // Зимний продукт
        
        // Продукты с еженедельными пиками (выходные)
        createWeeklyPattern(selectedProducts.get(2), startDate, "weekend"); // Больше продаж на выходных
        createWeeklyPattern(selectedProducts.get(3), startDate, "weekday"); // Больше продаж в будни
        
        // Продукты с ежемесячными пиками (начало/конец месяца)
        createMonthlyPattern(selectedProducts.get(4), startDate, "start"); // Больше в начале месяца
        createMonthlyPattern(selectedProducts.get(5), startDate, "end"); // Больше в конце месяца
        
        // Продукты с четким трендом (рост/падение)
        createTrendPattern(selectedProducts.get(6), startDate, "growing"); // Растущие продажи
        createTrendPattern(selectedProducts.get(7), startDate, "declining"); // Падающие продажи
        
        // Продукты со стабильными продажами
        createStablePattern(selectedProducts.get(8), startDate, "high"); // Стабильно высокие
        createStablePattern(selectedProducts.get(9), startDate, "low"); // Стабильно низкие
    }

    private void createSeasonalSalesPattern(Product product, LocalDateTime startDate, String season) {
        LocalDateTime current = startDate;
        LocalDateTime endDate = LocalDateTime.now();
        
        while (current.isBefore(endDate)) {
            int month = current.getMonthValue();
            
            // Определяем "сезонность" продаж
            double multiplier;
            if (season.equals("summer")) {
                // Летний продукт: пик в июне-августе
                if (month >= 6 && month <= 8) {
                    multiplier = 5.0; // В 5 раз больше летом
                } else if (month >= 4 && month <= 9) {
                    multiplier = 2.0; // Весна и осень - средний сезон
                } else {
                    multiplier = 1.0; // Зима - низкий сезон
                }
            } else { // winter
                // Зимний продукт: пик в декабре-феврале
                if (month == 12 || month <= 2) {
                    multiplier = 5.0; // В 5 раз больше зимой
                } else if (month >= 10 || month <= 4) {
                    multiplier = 2.0; // Осень и весна - средний сезон
                } else {
                    multiplier = 1.0; // Лето - низкий сезон
                }
            }
            
            // Создаем продажи с учетом сезонности
            createSalesForDay(product, current, (int)(1 + random.nextInt(5) * multiplier));
            
            // Переходим к следующему дню
            current = current.plusDays(1);
        }
    }

    private void createWeeklyPattern(Product product, LocalDateTime startDate, String pattern) {
        LocalDateTime current = startDate;
        LocalDateTime endDate = LocalDateTime.now();
        
        while (current.isBefore(endDate)) {
            int dayOfWeek = current.getDayOfWeek().getValue(); // 1 (Monday) - 7 (Sunday)
            
            // Определяем шаблон недельных продаж
            double multiplier;
            if (pattern.equals("weekend")) {
                // Больше продаж на выходных
                if (dayOfWeek >= 6) { // Суббота и воскресенье
                    multiplier = 3.0;
                } else if (dayOfWeek == 5) { // Пятница
                    multiplier = 2.0;
                } else {
                    multiplier = 1.0;
                }
            } else { // weekday
                // Больше продаж в будни
                if (dayOfWeek <= 5) { // Понедельник-пятница
                    multiplier = 2.5;
                } else {
                    multiplier = 1.0;
                }
            }
            
            // Создаем продажи с учетом дня недели
            createSalesForDay(product, current, (int)(1 + random.nextInt(5) * multiplier));
            
            // Переходим к следующему дню
            current = current.plusDays(1);
        }
    }

    private void createMonthlyPattern(Product product, LocalDateTime startDate, String pattern) {
        LocalDateTime current = startDate;
        LocalDateTime endDate = LocalDateTime.now();
        
        while (current.isBefore(endDate)) {
            int dayOfMonth = current.getDayOfMonth();
            int lastDayOfMonth = current.getMonth().length(current.toLocalDate().isLeapYear());
            
            // Определяем шаблон месячных продаж
            double multiplier;
            if (pattern.equals("start")) {
                // Больше продаж в начале месяца
                if (dayOfMonth <= 5) {
                    multiplier = 4.0;
                } else if (dayOfMonth <= 10) {
                    multiplier = 2.0;
                } else {
                    multiplier = 1.0;
                }
            } else { // end
                // Больше продаж в конце месяца
                if (dayOfMonth >= lastDayOfMonth - 4) {
                    multiplier = 4.0;
                } else if (dayOfMonth >= lastDayOfMonth - 9) {
                    multiplier = 2.0;
                } else {
                    multiplier = 1.0;
                }
            }
            
            // Создаем продажи с учетом дня месяца
            createSalesForDay(product, current, (int)(1 + random.nextInt(4) * multiplier));
            
            // Переходим к следующему дню
            current = current.plusDays(1);
        }
    }

    private void createTrendPattern(Product product, LocalDateTime startDate, String trend) {
        LocalDateTime current = startDate;
        LocalDateTime endDate = LocalDateTime.now();
        
        // Общее количество дней для расчета тренда
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        long currentDay = 0;
        
        while (current.isBefore(endDate)) {
            // Рассчитываем прогресс (от 0 до 1) для текущего дня
            double progress = (double) currentDay / totalDays;
            
            // Определяем множитель в зависимости от тренда
            double multiplier;
            if (trend.equals("growing")) {
                // Растущие продажи: от 1 до 5
                multiplier = 1.0 + 4.0 * progress;
            } else { // declining
                // Падающие продажи: от 5 до 1
                multiplier = 5.0 - 4.0 * progress;
            }
            
            // Создаем продажи с учетом тренда
            createSalesForDay(product, current, (int)(1 + random.nextInt(3) * multiplier));
            
            // Переходим к следующему дню
            current = current.plusDays(1);
            currentDay++;
        }
    }

    private void createStablePattern(Product product, LocalDateTime startDate, String level) {
        LocalDateTime current = startDate;
        LocalDateTime endDate = LocalDateTime.now();
        
        // Базовый множитель в зависимости от уровня продаж
        double baseMultiplier = level.equals("high") ? 5.0 : 1.0;
        
        while (current.isBefore(endDate)) {
            // Добавляем небольшую случайность для реалистичности
            double randomFactor = 0.8 + (random.nextDouble() * 0.4); // 0.8 - 1.2
            
            // Создаем продажи с учетом уровня и случайности
            createSalesForDay(product, current, (int)(1 + random.nextInt(3) * baseMultiplier * randomFactor));
            
            // Переходим к следующему дню
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
        
        // Вычисляем и обновляем общую сумму
        BigDecimal totalAmount = product.getPrice().multiply(quantityBD)
                .setScale(2, RoundingMode.HALF_UP);
        savedTransaction.setTotalAmount(totalAmount);
        salesTransactionRepository.save(savedTransaction);
    }
} 