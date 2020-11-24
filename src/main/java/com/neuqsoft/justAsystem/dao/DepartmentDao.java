package com.neuqsoft.justAsystem.dao;

import com.neuqsoft.justAsystem.pojo.Department;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DepartmentDao {
    private static Map<Integer,Department> departmentMap = null;
    static {
        departmentMap = new HashMap<Integer, Department>();
        departmentMap.put(1,new Department(1, "人力资源部"));
        departmentMap.put(2,new Department(2, "行政管理部"));
        departmentMap.put(3,new Department(3, "质量管理部"));
        departmentMap.put(4,new Department(4, "财务部"));
        departmentMap.put(5,new Department(5, "技术部"));
        departmentMap.put(6,new Department(6, "医疗卫生研发部"));
        departmentMap.put(7,new Department(7, "社会保险研发部"));
        departmentMap.put(8,new Department(8, "电子政务研发部"));
        departmentMap.put(9,new Department(9, "产创中心"));
    }

    public Collection<Department> getDepartments() {
        return departmentMap.values();
    }

    public Department getDepartmentById(Integer id) {
        return departmentMap.get(id);
    }
}
