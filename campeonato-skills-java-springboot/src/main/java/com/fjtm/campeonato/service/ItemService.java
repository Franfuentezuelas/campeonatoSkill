package com.fjtm.campeonato.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjtm.campeonato.modelo.Item;
import com.fjtm.campeonato.repository.ItemRepository;
import com.fjtm.campeonato.service.base.BaseService;
import com.fjtm.campeonato.modelo.Evaluacion;

@Service
public class ItemService extends BaseService<Item, Long, ItemRepository> {

    @Autowired
    private ItemRepository itemRepositorio;
    

}
