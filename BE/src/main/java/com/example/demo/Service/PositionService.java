package com.example.demo.Service;

import com.example.demo.Entity.Position;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PositionService {
    List<String> getListNamePosition();
    List<Position> getListPosition();
}
