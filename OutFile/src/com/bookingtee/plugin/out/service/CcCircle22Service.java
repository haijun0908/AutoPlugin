package com.bookingtee.plugin.out.service;

import java.util.List;
import java.util.Map;

import com.bookingtee.plugin.out.dto.CcCircle22DTO;
import com.bookingtee.plugin.out.model.CcCircle22Model;

public interface CcCircle22Service {

    public int saveCcCircle22(CcCircle22DTO ccCircle22DTO);

    public boolean updateCcCircle22(CcCircle22DTO ccCircle22DTO);

    public boolean deleteById(int id);

    public CcCircle22DTO getCcCircle22ById(int id);

    public List<CcCircle22DTO> getAll();

    public List<CcCircle22DTO> getCcCircle22List(List<Integer> idList);

    public Map<Integer, CcCircle22DTO> getCcCircle22MapById(List<Integer> idList);

}
