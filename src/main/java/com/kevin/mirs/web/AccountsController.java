package com.kevin.mirs.web;


import com.kevin.mirs.dto.MIRSResult;
import com.kevin.mirs.entity.User;
import com.kevin.mirs.service.UserService;
import com.kevin.mirs.vo.UserProfile;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/accounts")
public class AccountsController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    UserService userService;


    /**
     * 返回用户个人信息
     * @return 成功返回用户信息，失败返回错误原因
     */
    @ResponseBody
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @ApiOperation(value = "/profile", notes = "返回用户个人信息")
    public MIRSResult<UserProfile> getProfile(HttpServletRequest request) {
        logger.info("--------------------GET:/accounts/profile--------------------");

        // 检查SESSION信息
        if (request.getSession().getAttribute(UserService.USER_ID) == null) {
            return new MIRSResult<UserProfile>(false, "请先登录!");
        }
        
        int id = (int) request.getSession().getAttribute(UserService.USER_ID);

        UserProfile userProfile = userService.getUserProfileByUserId(id);

        if (userProfile != null) {
            return new MIRSResult<UserProfile>(true, userProfile);
        }

        return new MIRSResult<UserProfile>(false, "获取信息时出错!");
    }

    /**
     * 更新用户提交的信息
     * @return 返回是否修改成功
     */
    @ResponseBody
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    @ApiOperation(value = "/profile", notes = "更新用户提交的信息")
    public MIRSResult<Boolean> setProfile(@RequestBody UserProfile userProfile,
                             HttpServletRequest request) {
        logger.info("--------------------POST:/accounts/profile--------------------");

        // 检查SESSION信息
        if (request.getSession().getAttribute(UserService.USER_ID) == null) {
            return new MIRSResult<Boolean>(false, "请先登录!");
        }
        // 检查ID是否匹配
        int id = (int) request.getSession().getAttribute(UserService.USER_ID);
        if (userProfile.getId() != id) {
            return new MIRSResult<Boolean>(false, "数据被篡改了!");
        }

        if (userService.updateUserProfile(userProfile) == 1) {
            return new MIRSResult<Boolean>(true, true);
        }
        return new MIRSResult<Boolean>(false, "更新失败了!");
    }


    @ResponseBody
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ApiOperation(value = "/password", notes = "执行更改密码操作")
    public MIRSResult<Boolean> updatePassword(@RequestParam(value = "email") String email,
                                 @RequestParam(value = "password") String password) {
        logger.info("--------------------POST:/accounts/password--------------------");

        //返回更新状态
        return new MIRSResult<Boolean>(false, false);
    }


    @ResponseBody
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    @ApiOperation(value = "/reset-password", notes = "重置用户密码")
    public MIRSResult<Boolean> resetPassword(@RequestParam(value = "email") String email,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "verification") String verification) {
        logger.info("--------------------POST:/accounts/reset-password--------------------");

        return new MIRSResult<Boolean>(false, false);
    }

}
