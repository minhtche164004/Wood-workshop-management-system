package com.example.demo.Dto.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
// cai nay` dung` de gop san pham request va san pham co san
public class ProductItem {
    private int id;
    private int quantity;
    private BigDecimal price;
}
