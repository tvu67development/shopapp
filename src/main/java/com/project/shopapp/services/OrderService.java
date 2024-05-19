package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(
                () -> new DataNotFoundException("Not found User with id " + orderDTO.getUserId())
        );
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        if (orderDTO.getShippingDate() == null) {
            order.setShippingDate(LocalDate.now());
        } else {
            if (orderDTO.getShippingDate().isBefore(LocalDate.now())) {
                throw new DataNotFoundException("Shipping date must be at least today");
            }
            order.setShippingDate(orderDTO.getShippingDate());
        }
        order.setActive(true);
        orderRepository.save(order);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrderByOrderId(Long orderId) throws Exception {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new DataNotFoundException("Not found Order with id "+orderId)
        );
        return modelMapper.map(existingOrder, OrderResponse.class);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderDTO orderDTO) throws Exception {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new DataNotFoundException("Not found Order with id "+orderId)
        );
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(
                () -> new DataNotFoundException("Not found User with id " + orderDTO.getUserId())
        );
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, existingOrder);
        existingOrder.setUser(existingUser);
        orderRepository.save(existingOrder);
        return modelMapper.map(existingOrder, OrderResponse.class);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) throws Exception {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new DataNotFoundException("Not found Order with id "+orderId)
        );
        existingOrder.setActive(false);
        orderRepository.save(existingOrder);
    }

    @Override
    public List<OrderResponse> getAllOrdersByUserId(Long userId) throws Exception {
        userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("Not found User with id " + userId)
        );
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderResponse> orderResponseList = new ArrayList<>();
        orders.forEach(order ->
                orderResponseList.add(modelMapper.map(order, OrderResponse.class))
        );
        return orderResponseList;
    }
}
