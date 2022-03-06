package crud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import crud.bean.Employee;
import crud.bean.Msg;
import crud.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理员工CRUD请求
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 单个、批量二合一
     * 批量删除：1-2-3
     * 单个删除：1
     * @param ids
     * @return
     */
    @RequestMapping( value = "/emp/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmp(@PathVariable("ids") String ids){
        if (ids.contains("-")){
            //批量删除
            List<Integer> del_id = new ArrayList<>();
            String[] str_ids = ids.split("-");
            //组装id的集合
            for (String string: str_ids
                 ) {
                del_id.add(Integer.parseInt(string));
            }
            employeeService.deleteBatch(del_id);
        }else {
            //单个删除
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }
        return Msg.success();
    }

    /**
     * 员工更新方法
     * 如果直接发送ajax=PUT请求，封装的数据除了id全是null
     *
     * 问题：
     * 请求体中有数据，但是Employee对象封装不上：sql语句就是如下
     * update tbl_emp where emp_id = #{empId,jdbcType=INTEGER}
     *
     * 原因：
     * Tomcat：
     *  1、将请求体中的数据：封装成一个map
     *  2、request.getParameter("empName)就会从这个map中取值
     *  3、SpringMVC封装POJO对象时
     *      会把POJO中每个属性的值：request.getParameter("email");
     *
     * Ajax发送PUT请求引发的问题：
     *  request.getParameter("gender"))也拿不到
     *  Tomcat一看是PUT不会封装请求体中的数据为map，只有POST形式的请求才封装请求体为map
     *
     *org.apache.catalina.connector.Request -- parseParameters()
     *
     * protected String parseBodyMethods = "POST"
     * if (!getConnector().isParseBodyMethod(getMethod()){
     *     success = true;
     *     return;
     * })
     *
     * 解决方案：
     *  1、配置上FormContentFilter；
     *  2、它的作用：将请求体中的数据解析包装为一个map；
     *  3、request被重新包装，request.getParameter()被重写，就会从自己封装的map中取数据
     *
     * @param employee
     * @return
     */
    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
    @ResponseBody
    public Msg saveEmp(Employee employee, HttpServletRequest request){
        System.out.println("请求体中的值:"+request.getParameter("gender"));

        System.out.println(employee);
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){

        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp", employee);
    }


    /**
     * 检查用户名是否可用
     * @param empName
     * @return
     */
    @RequestMapping("/checkuser")
    @ResponseBody
    public Msg checkuser(@RequestParam("empName") String empName){
        //先判断用户名是否是合法的表达式
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if (!empName.matches(regx)){
            return Msg.fail().add("va_msg", "用户名必须是2-5位中文或者是6-16位英文和数字的组合");
        }

        //用户名数据库校验
        boolean b = employeeService.checkUser(empName);
        if (b){
            return Msg.success();
        }else {
            return Msg.fail().add("va_msg", "用户名不可用");
        }
    }


    /**
     * 员工保存
     * 1、支持JSR303校验
     * 2、导入Hibernate-Validator
     * @return
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result){
        if (result.hasErrors()){
            //校验失败，应该返回失败，在模态框中显示校验失败的信息
            Map<String, Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError fildError :
                    errors) {
                System.out.println("错误的字段名：" + fildError.getField());
                System.out.println("错误信息：" + fildError.getDefaultMessage());
                map.put(fildError.getField(), fildError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields", map);
        }else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }
    }


    /**
     * 导入jackson包
     * @param pn
     * @return
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn){
        //这还不是一个分页查询
        //引入PageHepler插件
        //在查询之前只需要调用startPage方法，传入页码以及每页的大小
        PageHelper.startPage(pn, 5);
        //startPage后面紧跟的查询就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询结果，只需要将pageInfo交给页面即可
        //封装了详细的信息，包括有查询出来的数据，传入连续显示的页数
        PageInfo<Employee> page = new PageInfo<>(emps, 5);

        return Msg.success().add("pageInfo", page);
    }

    /**
     * 查询员工数据（分页查询）
     * @return
     */
    //@RequestMapping("/emps")
    private String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model){
        //这还不是一个分页查询
        //引入PageHepler插件
        //在查询之前只需要调用startPage方法，传入页码以及每页的大小
        PageHelper.startPage(pn, 5);
        //startPage后面紧跟的查询就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询结果，只需要将pageInfo交给页面即可
        //封装了详细的信息，包括有查询出来的数据，传入连续显示的页数
        PageInfo<Employee> page = new PageInfo<>(emps, 5);
        model.addAttribute("pageInfo", page);

        return "list";
    }
}
