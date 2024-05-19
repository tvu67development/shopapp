package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                () -> new DataNotFoundException("Not found Order with id "+orderDetailDTO.getOrderId())
        );
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                () -> new DataNotFoundException("Not found Product with id " + orderDetailDTO.getProductId()));

//        modelMapper.typeMap(OrderDetailDTO.class, OrderDetail.class)
//                .addMappings(mapper -> mapper.skip(OrderDetail::setId));
//        OrderDetail orderDetail = new OrderDetail();
//        modelMapper.map(orderDetailDTO, orderDetail);
        OrderDetail orderDetail = OrderDetail.builder()
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .color(orderDetailDTO.getColor())
                .build();
        orderDetail.setOrder(existingOrder);
        orderDetail.setProduct(existingProduct);
        orderDetailRepository.save(orderDetail);
        return modelMapper.map(orderDetail, OrderDetailResponse.class);
    }

    @Override
    public OrderDetailResponse getOrderDetailById(Long id) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Not found Order Detail with id "+id)
        );
        return modelMapper.map(orderDetail, OrderDetailResponse.class);
    }

    @Override
    @Transactional
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Not found Order Detail with id "+id)
        );
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                () -> new DataNotFoundException("Not found Order with id "+orderDetailDTO.getOrderId())
        );
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                () -> new DataNotFoundException("Not found Product with id " + orderDetailDTO.getProductId()));
//        modelMapper.typeMap(OrderDetailDTO.class, OrderDetail.class)
//                .addMappings(mapper -> mapper.skip(OrderDetail::setId));
//        modelMapper.map(orderDetailDTO,orderDetail);
        orderDetail.setColor(orderDetailDTO.getColor());
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        orderDetail.setOrder(existingOrder);
        orderDetail.setProduct(existingProduct);
        orderDetailRepository.save(orderDetail);
        return modelMapper.map(orderDetail, OrderDetailResponse.class);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(Long id) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Not found Order Detail with id "+id)
        );
        orderDetailRepository.delete(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> getAllOrderDetailsByOrderId(Long orderId) throws Exception {
        orderRepository.findById(orderId).orElseThrow(
                () -> new DataNotFoundException("Not found Order with id "+orderId)
        );
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
        orderDetails.forEach(
                orderDetail ->
                        orderDetailResponseList.add(modelMapper.map(orderDetail,OrderDetailResponse.class)));
        return orderDetailResponseList;
    }
}
