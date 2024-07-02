package com.example.demo.Service.Impl;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.User;
import com.example.demo.Entity.WishList;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.WishListRepository;
import com.example.demo.Service.WhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishListServiceImpl implements WhiteListService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WishListRepository wishListRepository;
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
        return wishListRepository.save(whiteLis);

    }
    @Override
    public void DeleteWhiteList(int id){
        wishListRepository.deleteById(id);
    }

    @Override
    public List<WishList> ViewWhiteList(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    List<WishList> wishLists = wishListRepository.findByUserID(user.getUserId());
    if(wishLists.size() ==0){
        throw new AppException(ErrorCode.NOT_FOUND);
        }
    return wishLists;
    }

}
