package com.plugin.auto.out.service.impl;

import java.util.List;
import java.util.Map;

import com.plugin.auto.out.dto.CcCircle22DTO;
import com.plugin.auto.out.model.CcCircle22Model;
import javax.annotation.Resource;
import java.util.HashMap;
import com.plugin.auto.out.service.CcCircle22Service;
import com.plugin.auto.out.dao.CcCircle22Dao;
import com.plugin.auto.out.utils.DtoConvert;

public class CcCircle22ServiceImpl implements CcCircle22Service {

    @Resource
    private CcCircle22Dao ccCircle22Dao;

    public boolean saveCcCircle22(CcCircle22DTO ccCircle22DTO) {
        return ccCircle22Dao.saveCcCircle22(DtoConvert.convert(ccCircle22DTO, CcCircle22Model.class)) > 0;
    }

    public boolean updateCcCircle22(CcCircle22DTO ccCircle22DTO) {
        return ccCircle22Dao.updateCcCircle22(DtoConvert.convert(ccCircle22DTO, CcCircle22Model.class));
    }

    public boolean deleteById(int id) {
        return ccCircle22Dao.deleteById(id);
    }

    public CcCircle22DTO getCcCircle22ById(int id) {
        return DtoConvert.convert(ccCircle22Dao.getCcCircle22ById(id), CcCircle22DTO.class);
    }

    public List<CcCircle22DTO> getAll() {
        return DtoConvert.convert2List(ccCircle22Dao.getAll(), CcCircle22DTO.class);
    }

    public List<CcCircle22DTO> getCcCircle22List(List<Integer> idList) {
        return DtoConvert.convert2List(ccCircle22Dao.getCcCircle22List(idList), CcCircle22DTO.class);
    }

    public Map<Integer, CcCircle22DTO> getCcCircle22MapById(List<Integer> idList) {
        List<CcCircle22DTO> list = getCcCircle22List(idList);
        if (list != null && list.size() > 0) {
            Map<Integer, CcCircle22DTO> map = new HashMap<>();
            for (CcCircle22DTO dto : list) {
                map.put(dto.getId(), dto);
            }
            return map;
        }
        return null;
    
    }

}
