package com.project.shopapp.controllers;

import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.responses.OrderResponse;
import com.project.shopapp.services.IOrderService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService iOrderService;
    private final LocalizationUtils localizationUtils;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO, BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderResponse orderResponse = iOrderService.createOrder(orderDTO);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getAllOrdersByUserId(@Valid @PathVariable("user_id") Long userId) {
        try {
            List<OrderResponse> orderResponseList = iOrderService.getAllOrdersByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(orderResponseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<?> getOrderByOrderId(@Valid @PathVariable("order_id") Long orderId) {
        try {
            OrderResponse existingOrder = iOrderService.getOrderByOrderId(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(existingOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{order_id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("order_id") Long orderId,
                                              @Valid @RequestBody OrderDTO orderDTO) {
        try {
            OrderResponse orderResponse = iOrderService.updateOrder(orderId, orderDTO);
            return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable("order_id") Long orderId) {
        try {
            iOrderService.deleteOrder(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    localizationUtils.getLocalizeMessage(MessageKeys.DELETE_ORDER_SUCCESSFULLY, orderId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
