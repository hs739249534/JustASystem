package com.neuqsoft.justAsystem.dao;

import com.neuqsoft.justAsystem.pojo.Department;
import com.neuqsoft.justAsystem.pojo.Employee;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class EmployeeDao {

    private static Map<Integer, Employee> employeeMap = null;

    @Autowired
    private DepartmentDao departmentDao;

    static {
        employeeMap = new HashMap<Integer, Employee>();
        employeeMap.put(1001, new Employee(1001, "AA", "A789456123@qq.com", 1, new Department(1, "人力资源部")));
        employeeMap.put(1002, new Employee(1002, "BB", "B789456123@qq.com", 0, new Department(2, "行政管理部")));
        employeeMap.put(1003, new Employee(1003, "CC", "C789456123@qq.com", 1, new Department(3, "质量管理部")));
        employeeMap.put(1004, new Employee(1004, "DD", "D789456123@qq.com", 0, new Department(4, "财务部")));
        employeeMap.put(1005, new Employee(1005, "EE", "E789456123@qq.com", 1, new Department(5, "技术部")));

    }

    private static Integer initId = 1006;
    public void save(Employee employee){
        if (employee.getId() == null) {
            employee.setId(initId++);
        }

        employee.setDepartment(departmentDao.getDepartmentById(employee.getDepartment().getId()));
        employeeMap.put(employee.getId(), employee);
    }

    public Collection<Employee> getAll() {
        return employeeMap.values();
    }

    public Employee getEmployeeById(Integer id) {
        return employeeMap.get(id);
    }

    public void delete(Integer id) {
        employeeMap.remove(id);
    }
}
