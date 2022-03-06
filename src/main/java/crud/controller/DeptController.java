package crud.controller;

import crud.bean.Department;
import crud.bean.Msg;
import crud.service.DepartmenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 处理和部门有关的请求
 */
@Controller
public class DeptController {

    @Autowired
    private DepartmenService departmenService;

    /**
     * 返回所有部门的信息
     */
    @RequestMapping("/depts")
    @ResponseBody
    public Msg getDepts(){
        //查出的所有部门
        List<Department> list = departmenService.getDepts();

        return Msg.success().add("depts", list);
    }
}
