package com.example.demo.Repository;

import com.example.demo.Entity.UserInfor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInforRepository extends JpaRepository<UserInfor,Integer> {

}
