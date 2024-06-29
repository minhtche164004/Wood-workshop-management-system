package com.example.demo.Dto.SubMaterialDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class Product_SubmaterialDTO {
    private int id;
    private String sub_name;
    private int sub_id;
    private double quantity;
    private String sub_type;

    public Product_SubmaterialDTO(int id, String sub_name, int sub_id, double quantity, String sub_type) {
        this.id = id;
        this.sub_name = sub_name;
        this.sub_id = sub_id;
        this.quantity = quantity;
        this.sub_type = sub_type;
    }
}
