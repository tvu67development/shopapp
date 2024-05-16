package com.project.shopapp.controllers;

import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.IOrderDetailService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

    private final IOrderDetailService iOrderDetailService;
    private final LocalizationUtils localizationUtils;

    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderDetailResponse orderDetailResponse = iOrderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(orderDetailResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{order_detail_id}")
    public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable("order_detail_id") Long orderDetailId) {
        try {
            OrderDetailResponse orderDetailResponse = iOrderDetailService.getOrderDetailById(orderDetailId);
            return ResponseEntity.status(HttpStatus.OK).body(orderDetailResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getAllOrderDetailsByOrderId(@Valid @PathVariable("order_id") Long orderId) {
        try {
            List<OrderDetailResponse> orderDetailResponses = iOrderDetailService.getAllOrderDetailsByOrderId(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(orderDetailResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{order_detail_id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("order_detail_id") Long orderDetailId,
                                              @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetailResponse orderDetailResponse = iOrderDetailService.updateOrderDetail(orderDetailId,orderDetailDTO);
            return ResponseEntity.status(HttpStatus.OK).body(orderDetailResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{order_detail_id}")
    public ResponseEntity<String> deleteOrderDetail(@Valid @PathVariable("order_detail_id") Long orderDetailId) {
        try {
            iOrderDetailService.deleteOrderDetail(orderDetailId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    localizationUtils.getLocalizeMessage(MessageKeys.DELETE_ORDER_DETAIL_SUCCESSFULLY, orderDetailId)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
