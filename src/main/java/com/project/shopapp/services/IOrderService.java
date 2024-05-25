package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.responses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {

    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;

    OrderResponse getOrderByOrderId(Long orderId) throws Exception;

    OrderResponse updateOrder(Long orderId, OrderDTO orderDTO) throws Exception;

    void deleteOrder(Long orderId) throws Exception;

    List<OrderResponse> getAllOrdersByUserId(Long userId) throws Exception;

    Page<OrderResponse> getOrdersByKeyword(String keyword, Pageable pageable);
}
