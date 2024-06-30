package com.example.demo.Repository;

import com.example.demo.Entity.Categories;
import com.example.demo.Entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
    @Query("SELECT u FROM Position u WHERE u.position_name = :query")
    Position findByName(String query);

    @Query("SELECT u FROM Position u WHERE u.position_id = :query")
    Position findById(int query);

    @Query("SELECT u.position_name FROM Position u") // Assuming the field is positionName
    List<String> getListName();

    @Query("SELECT u FROM Position u WHERE u.position_id = :query")
    Position findById1(int query);

}