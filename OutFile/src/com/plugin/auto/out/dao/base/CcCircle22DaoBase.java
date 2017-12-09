package com.plugin.auto.out.dao.base;

import com.plugin.auto.out.model.CcCircle22Model;
import java.util.List;
import java.util.Map;

public interface CcCircle22DaoBase {

    public int saveCcCircle22(CcCircle22Model ccCircle22Model);

    public boolean updateCcCircle22(CcCircle22Model ccCircle22Model);

    public boolean deleteById(int id);

    public CcCircle22Model getCcCircle22ById(int id);

    public List<CcCircle22Model> getAll();

    public List<CcCircle22Model> getCcCircle22List(List<Integer> idList);

}
