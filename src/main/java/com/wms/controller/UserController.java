package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.User;
import com.wms.service.MenuService;
import com.wms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    /**
     *
     * @return
     */
    @GetMapping("/list")
    public List<User> list(){
        return userService.list();
    }

@GetMapping("/findByAccount")
    public Result findByAccount(@RequestParam String account){
       List list = userService.lambdaQuery().eq(User::getAccount,account).list();
       return list.size()>0?Result.success(list): Result.fail();
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @PostMapping("/save")
    public Result save (@RequestBody User user){
        return userService.save(user) ?Result.success():Result.fail();
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PostMapping("/update")
    public Result update (@RequestBody User user){

        return userService.updateById(user) ?Result.success():Result.fail();
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @PostMapping("/mod")
    public boolean mod (@RequestBody User user){
        return userService.updateById(user);
    }


    /**
     * 新增或修改用户
     * @param user
     * @return
     */
    @PostMapping("/saveORmod")
    public boolean saveORmod (@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Result delete (@RequestParam String id){
        return userService.removeById(id)?Result.success():Result.fail();
    }

    /**
     * 查询（模糊、匹配）
     * @param user
     * @return
     */
    @PostMapping("/query")
    public Result  query(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper =new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(user.getUsername())){
            lambdaQueryWrapper.like(User::getUsername, user.getUsername());
        }

        return Result.success(userService.list(lambdaQueryWrapper));
    }


    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result login (@RequestBody User user){
        List<User> list = userService.lambdaQuery()
                .eq(User::getAccount,user.getAccount())
                .eq(User::getPassword,user.getPassword()).list();


        if (list.size()>0){
            User user1 =list.get(0);
            List menuList = menuService.lambdaQuery().like(Menu::getMenuRight,user1.getRoleId()).list();

            HashMap res = new HashMap();
            res.put("user",user1);
            res.put("menu",menuList);
            return Result.success(res);
        }
        return Result.fail();
    }


    @PostMapping("/listPage")
    public List<User> listPage(@RequestBody QueryPageParam query){
        System.out.println(query);

        System.out.println("num=" + query.getPageNum());
        System.out.println("size=" + query.getPageSize());

        HashMap param =query.getParam();
        String name =(String) param.get("name");
        System.out.println("name=" +(String) param.get("name"));

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getUsername,name);

        IPage result =userService.page(page,lambdaQueryWrapper);
        System.out.println("total="+result.getTotal());

        return result.getRecords();

    }


    @PostMapping("/listPage1")
    public Result  listPage1(@RequestBody QueryPageParam query){


        HashMap param =query.getParam();
        String name =(String) param.get("name");
        String sex =(String) param.get("sex");
        String roleId =(String)param.get("roleId");
        System.out.println("name=" +(String) param.get("name"));

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(User::getUsername,name);
        }
        if (StringUtils.isNotBlank(sex)){
            lambdaQueryWrapper.eq(User::getSex,sex);
        }
        if (StringUtils.isNotBlank(roleId)){
            lambdaQueryWrapper.eq(User::getRoleId,roleId);
        }


        IPage result =userService.page(page,lambdaQueryWrapper);
        System.out.println("total="+result.getTotal());

        return Result.success(result.getRecords(), result.getTotal());

    }


}
