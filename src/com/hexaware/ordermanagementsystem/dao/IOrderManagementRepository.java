package com.hexaware.ordermanagementsystem.dao;
import com.hexaware.ordermanagementsystem.entity.Product;
import com.hexaware.ordermanagementsystem.entity.User;
import com.hexaware.ordermanagementsystem.exception.OrderNotFoundException;
import com.hexaware.ordermanagementsystem.exception.UserNotFoundException;

import java.util.List;

public interface IOrderManagementRepository {
    void createOrder(User user, List<Product> products) throws UserNotFoundException;
    void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException;
    void createProduct(User user, Product product);
    void createUser(User user);
    List<Product> getAllProducts();
    List<Product> getOrderByUser(User user) throws UserNotFoundException;
}



