package com.example.demo.Service;

import com.example.demo.Entity.WishList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WhiteListService {
    WishList AddWhiteList(int product_id);
    void DeleteWhiteList(int id);
    List<WishList> ViewWhiteList(int user_id);
}
