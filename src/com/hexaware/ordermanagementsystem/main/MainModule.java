package com.hexaware.ordermanagementsystem.main;
import com.hexaware.ordermanagementsystem.dao.IOrderManagementRepository;
import com.hexaware.ordermanagementsystem.dao.OrderProcessor;
import com.hexaware.ordermanagementsystem.entity.Product;
import com.hexaware.ordermanagementsystem.entity.User;
import com.hexaware.ordermanagementsystem.exception.OrderNotFoundException;
import com.hexaware.ordermanagementsystem.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainModule {

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        IOrderManagementRepository op = new OrderProcessor();

        while (true) {
            System.out.println("\nOrder Management System Menu");
            System.out.println("1. Create Order");
            System.out.println("2. Cancel Order");
            System.out.println("3. Create Product");
            System.out.println("4. Create User");
            System.out.println("5. View All Products");
            System.out.println("6. View Orders by User");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("Enter User ID:");
                    int orderUserId = sc.nextInt();
                    User orderUser = new User(orderUserId, "", "", "User");

                    List<Product> products = new ArrayList<>();

                    while (true) {
                        System.out.println("Enter Product ID:");
                        int prodId = sc.nextInt();
                        products.add(new Product(prodId, "", "", 0.0, 0, ""));

                        System.out.println("Add another product? (yes/no):");
                        sc.nextLine();
                        if (sc.nextLine().equals("no")) {
                            break;
                        }
                    }

                    try {
                        op.createOrder(orderUser, products);
                        System.out.println("Order created successfully.");
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Enter User ID:");
                    int cancelUserId = sc.nextInt();
                    System.out.println("Enter Order ID:");
                    int cancelOrderId = sc.nextInt();
                    sc.nextLine();
                    try {
                        op.cancelOrder(cancelUserId, cancelOrderId);
                        System.out.println("Order cancelled successfully.");
                    } catch (UserNotFoundException | OrderNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("Enter Admin User ID:");
                    int adminId = sc.nextInt();
                    sc.nextLine();
                    User admin = new User(adminId, "", "", "Admin");

                    System.out.println("Enter Product ID:");
                    int productId = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter Product Name:");
                    String productName = sc.nextLine();
                    System.out.println("Enter Description:");
                    String description = sc.nextLine();
                    System.out.println("Enter Price:");
                    double price = sc.nextDouble();
                    System.out.println("Enter Quantity:");
                    int quantity = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter Type:");
                    String type = sc.nextLine();

                    Product product = new Product(productId, productName, description, price, quantity, type);
                    op.createProduct(admin, product);
                    break;

                case 4:
                    System.out.println("Enter User ID:");
                    int userId = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter Username:");
                    String username = sc.nextLine();
                    System.out.println("Enter Password:");
                    String password = sc.nextLine();

                    User user = new User(userId, username, password, "User");
                    op.createUser(user);
                    break;

                case 5:
                    List<Product> allProducts = op.getAllProducts();
                    System.out.println("All Products");
                    for (Product p : allProducts) {
                        System.out.println(p.toString());
                    }
                    break;

                case 6:
                    System.out.println("Enter User ID:");
                    int viewUserId = sc.nextInt();
                    sc.nextLine();
                    User viewUser = new User(viewUserId, "", "", "User");
                    try {
                        List<Product> userOrders = op.getOrderByUser(viewUser);
                        System.out.println("Products ordered by User ID: " + viewUserId );
                        for (Product p : userOrders) {
                            System.out.println(p.toString());
                        }
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 7:

                    System.out.println("Exiting application");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
