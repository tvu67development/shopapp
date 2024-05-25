package com.project.shopapp.responses;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderListResponse {

    private List<OrderResponse> orders;

    private int totalPages;
}
