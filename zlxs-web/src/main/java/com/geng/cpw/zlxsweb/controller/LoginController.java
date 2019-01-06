package com.geng.cpw.zlxsweb.controller;

import com.geng.cpw.zlxsweb.base.BaseResult;
import com.geng.cpw.zlxsweb.base.ResultConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public Object login(){
        Map<String,Object> data = new HashMap<>();
        data.put("token","1111");
        return new BaseResult(ResultConstant.SUCCESS,data);
    }

    @RequestMapping("/get_info")
    public Object getUserInfo(){
        /*
        *{
    name: 'super_admin',
    user_id: '1',
    access: ['super_admin', 'admin'],
    token: 'super_admin',
    avator: 'https://file.iviewui.com/dist/a0e88e83800f138b94d2414621bd9704.png'
  }
        * */
        Map<String,Object> data = new HashMap<>();
        List list = Collections.EMPTY_LIST;
        list.add("super_admin");
        list.add("admin");
        data.put("name","super_admin");
        data.put("user_id","1");
        data.put("access",list);
        data.put("token","super_admin");
        data.put("avator","https://file.iviewui.com/dist/a0e88e83800f138b94d2414621bd9704.png");
        return new BaseResult(ResultConstant.SUCCESS,data);
    }

}
