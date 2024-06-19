package com.example.demo.Service;

import com.example.demo.Entity.WhiteList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WhiteListService {
    WhiteList AddWhiteList(int product_id);
    void DeleteWhiteList(int id);
    List<WhiteList> ViewWhiteList(int user_id);
}
