package com.fei.feiojbackenduserservice.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fei.feiojbackendmodel.model.dto.user.UserAddRequest;
import com.fei.feiojbackendmodel.model.dto.user.UserQueryRequest;
import com.fei.feiojbackendmodel.model.entity.User;
import com.fei.feiojbackendmodel.model.vo.LoginUserVO;
import com.fei.feiojbackendmodel.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author a'u
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-08-14 16:53:39
*/
public interface UserService extends IService<User> {

    User getLoginUser(HttpServletRequest request);

    long userRegister(String userAccount, String userPassword, String checkPassword);

    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    LoginUserVO getLoginUserVO(User user);

    UserVO getUserVO(User user);

    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 是否是管理員
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    boolean isAdmin(HttpServletRequest request);

    /**
     * 注销
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);


    Wrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 管理员添加新用户
     * @param userAddRequest
     * @return
     */
    long addUser(UserAddRequest userAddRequest);

}




