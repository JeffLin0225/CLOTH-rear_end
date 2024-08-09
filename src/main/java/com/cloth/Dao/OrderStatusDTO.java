package com.cloth.Dao;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderStatusDTO {
	
	private Integer orderId;
    private Integer status;
    private Integer productId;

    public OrderStatusDTO(Integer orderId, Integer status, Integer productId) {
        this.orderId = orderId;
        this.status = status;
        this.productId = productId;
    }
    
    
}