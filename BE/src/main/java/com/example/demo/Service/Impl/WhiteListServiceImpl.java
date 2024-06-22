package com.example.demo.Service.Impl;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.User;
import com.example.demo.Entity.WishList;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.WhiteListRepository;
import com.example.demo.Service.WhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WhiteListServiceImpl implements WhiteListService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WhiteListRepository whiteListRepository;
    @Autowired
    private ProductRepository productRepository;


    @Override
    public WishList AddWhiteList(int product_id){
        WishList whiteLis = new WishList();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        Products products = productRepository.findById(product_id);
        whiteLis.setProduct(products);
        whiteLis.setUser(user);
        return whiteListRepository.save(whiteLis);

    }
    @Override
    public void DeleteWhiteList(int id){
        whiteListRepository.deleteById(id);
    }

    @Override
    public List<WishList> ViewWhiteList(int user_id){
    List<WishList> wishLists = whiteListRepository.findByUserID(user_id);
    if(wishLists.size() ==0){
        throw new AppException(ErrorCode.NOT_FOUND);
        }
    return wishLists;
    }

}
