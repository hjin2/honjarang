package com.example.honjarang.domain.jointdelivery.dto;

import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointDeliveryCartListDto {
    private Long id;
    private String menuId;
    private String menuName;
    private Integer menuPrice;
    private String menuImage;
    private Integer quantity;
    private Long userId;
    private String userNickname;

    public JointDeliveryCartListDto(JointDeliveryCart jointDeliveryCart, Menu menu) {
        this.id = jointDeliveryCart.getId();
        this.menuId = jointDeliveryCart.getMenuId();
        this.menuName = menu.getName();
        this.menuPrice = menu.getPrice();
        this.menuImage = menu.getImage();
        this.quantity = jointDeliveryCart.getQuantity();
        this.userId = jointDeliveryCart.getUser().getId();
        this.userNickname = jointDeliveryCart.getUser().getNickname();
    }
}
