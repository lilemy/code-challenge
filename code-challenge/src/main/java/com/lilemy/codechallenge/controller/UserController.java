package com.lilemy.codechallenge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.codechallenge.common.BaseResponse;
import com.lilemy.codechallenge.common.DeleteRequest;
import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.common.ResultUtils;
import com.lilemy.codechallenge.constant.CommonConstant;
import com.lilemy.codechallenge.constant.UserConstant;
import com.lilemy.codechallenge.exception.BusinessException;
import com.lilemy.codechallenge.exception.ThrowUtils;
import com.lilemy.codechallenge.model.dto.user.*;
import com.lilemy.codechallenge.model.entity.User;
import com.lilemy.codechallenge.model.vo.LoginUserVO;
import com.lilemy.codechallenge.model.vo.UserVO;
import com.lilemy.codechallenge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "UserController")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    // region 登录相关
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword);
        return ResultUtils.success(loginUserVO);
    }

    @Operation(summary = "用户注销")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        boolean result = userService.userLogout();
        return ResultUtils.success(result);
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser() {
        User user = userService.getLoginUser();
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // endregion

    // region 用户增删改

    @Operation(summary = "创建用户")
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(), ResultCode.NO_AUTH_ERROR);
        if (userAddRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 123456
        String defaultPassword = UserConstant.DEFAULT_PASSWORD;
        String encryptPassword = DigestUtils.md5DigestAsHex((CommonConstant.SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(), ResultCode.NO_AUTH_ERROR);
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(), ResultCode.NO_AUTH_ERROR);
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion

    // region 用户查询

    @Operation(summary = "根据 id 获取用户（仅管理员）")
    @GetMapping("/get")
    public BaseResponse<User> getUserById(Long id) {
        ThrowUtils.throwIf(!userService.isAdmin(), ResultCode.NO_AUTH_ERROR);
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ResultCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    @Operation(summary = "根据 id 获取脱敏用户信息")
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(Long id) {
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        User user = userService.getById(id);
        return ResultUtils.success(userService.getUserVO(user));
    }

    @Operation(summary = "分页获取用户列表")
    @PostMapping("/list")
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(), ResultCode.NO_AUTH_ERROR);
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    @Operation(summary = "分页获取用户封装列表")
    @PostMapping("/list/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ResultCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    // endregion
}
