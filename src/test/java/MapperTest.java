//
//import crud.bean.Department;
//import crud.bean.Employee;
//import crud.dao.DepartmentMapper;
//import crud.dao.EmployeeMapper;
//import org.apache.ibatis.session.SqlSession;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.UUID;
//
///*@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
//public class MapperTest {
//
//    @Autowired
//    DepartmentMapper departmentMapper;
//
//    @Autowired
//    EmployeeMapper employeeMapper;
//
//    @Autowired
//    SqlSession sqlSession;
//
//    *//**
//     * 测试Department的Mapper
//     * 推荐Spring的项目就可以使用Sring的单元测试，可以自动注入需要的组件
//     * 1、导入SpringTest模块
//     * 2、@ContextConfiguration指定Spring配置文件的位置
//     * 3、直接autowired即可
//     *//*
//    @org.junit.Test
//    public void testCRUDDept(){
//        *//*//*/1、创建SpringIOC容器
//        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
//        //2、从容器中拿mapper
//        DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);*//*
//
//        System.out.println(departmentMapper);
//
//        //1、插入几个部门
//        departmentMapper.insertSelective(new Department(null, "开发部"));
//        departmentMapper.insertSelective(new Department(null, "测试部"));
//
//    }
//
//
//    @Test
//    public void testCRUDEmp(){
//
//        //2、生成员工数据，测试员工插入
//        employeeMapper.insertSelective(new Employee(null, "Jerry", "M", "Jerry@li.com",1));
//
//        //2、批量插入多名员工：可以使用执行批量操作的sqlSession
//        *//*for(){
//            employeeMapper.insertSelective(new Employee(null, "Jerry", "M", "Jerry@li.com",1));
//
//        }*//*
//        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//        for (int i = 0; i < 1000; i++) {
//            String uid = UUID.randomUUID().toString().substring(0, 5) + i;
//            mapper.insertSelective(new Employee(null, uid, "M", uid+"@li.com", 1));
//        }
//    }
//
//}*/
