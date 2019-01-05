package com.geng.cpw.zlxsweb.controller;

import com.geng.cpw.zlxsweb.base.BaseResult;
import com.geng.cpw.zlxsweb.base.ResultConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public Object login(){
        Map<String,Object> data = new HashMap<>();
        data.put("token","1111");
        return new BaseResult(ResultConstant.SUCCESS,data);
    }

}
