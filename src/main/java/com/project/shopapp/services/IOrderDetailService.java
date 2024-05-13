package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {

    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;

    OrderDetailResponse getOrderDetailById(Long id) throws Exception;

    OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception;

    void deleteOrderDetail(Long id) throws Exception;

    List<OrderDetailResponse> getAllOrderDetailsByOrderId(Long orderId) throws Exception;
}
