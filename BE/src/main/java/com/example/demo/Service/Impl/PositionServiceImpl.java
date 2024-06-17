package com.example.demo.Service.Impl;

import com.example.demo.Entity.Position;
import com.example.demo.Repository.PositionRepository;
import com.example.demo.Service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionRepository positionRepository;
    @Override
    public List<String> getListNamePosition(){
        return positionRepository.getListName();
    }
    @Override
    public List<Position> getListPosition(){
        return positionRepository.findAll();
    }

}
