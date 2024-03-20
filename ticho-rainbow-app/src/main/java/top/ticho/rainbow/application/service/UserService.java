package top.ticho.rainbow.application.service;

import top.ticho.boot.view.core.PageResult;
import top.ticho.rainbow.interfaces.dto.ImgCodeDTO;
import top.ticho.rainbow.interfaces.dto.ImgCodeEmailDTO;
import top.ticho.rainbow.interfaces.dto.UserDTO;
import top.ticho.rainbow.interfaces.dto.UserLoginDTO;
import top.ticho.rainbow.interfaces.dto.UserPasswordDTO;
import top.ticho.rainbow.interfaces.dto.UserRoleDTO;
import top.ticho.rainbow.interfaces.dto.UserRoleMenuDtlDTO;
import top.ticho.rainbow.interfaces.dto.UserSignUpOrResetDTO;
import top.ticho.rainbow.interfaces.query.UserQuery;

import java.io.IOException;

/**
 * 用户信息 服务接口
 *
 * @date 2023-12-17 20:12
 */
public interface UserService {

    /**
     * 图片验证码校验
     *
     * @param imgCodeDTO 图片验证码DTO
     */
    void imgCodeValid(ImgCodeDTO imgCodeDTO);


    /**
     * 注册验证码邮箱发送
     *
     * @param imgCodeEmailDTO 邮箱验证码发送时图片验证码DTO
     */
    void signUpEmailSend(ImgCodeEmailDTO imgCodeEmailDTO);

    /**
     * 注册
     *
     * @param userSignUpOrResetDTO 注册dto
     */
    UserLoginDTO signUp(UserSignUpOrResetDTO userSignUpOrResetDTO);

    /**
     * 重置邮箱验证码发送
     *
     * @param imgCodeEmailDTO 邮箱验证码发送时图片验证码DTO
     */
    String resetPasswordEmailSend(ImgCodeEmailDTO imgCodeEmailDTO);

    /**
     * 重置用户密码(用户使用)
     *
     * @param userSignUpOrResetDTO 重置密码DTO
     */
    UserLoginDTO resetPassword(UserSignUpOrResetDTO userSignUpOrResetDTO);

    /**
     * 重置用户密码(管理员使用)
     */
    void resetPassword(String username);

    /**
     * 保存用户信息
     *
     * @param userDTO 用户信息DTO 对象
     */
    void save(UserDTO userDTO);

    /**
     * 删除用户信息
     *
     * @param username 用户名
     */
    void removeByUsername(String username);

    /**
     * 修改用户信息
     *
     * @param userDTO 用户信息DTO 对象
     */
    void updateById(UserDTO userDTO);

    /**
     * 修改用户密码信息
     *
     * @param userPassworUpdDTO 用户信息DTO 对象
     */
    void updatePassword(UserPasswordDTO userPassworUpdDTO);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return {@link UserDTO}
     */
    UserDTO getByUsername(String username);

    /**
     * 分页查询用户信息列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link UserDTO}>
     */
    PageResult<UserDTO> page(UserQuery query);

    /**
     * 根据用户名查询用户角色菜单功能号信息
     *
     * @param username 用户名
     * @return {@link UserRoleMenuDtlDTO}
     */
    UserRoleMenuDtlDTO getUserDtl(String username);

    /**
     * 绑定角色
     *
     * @param userRoleDTO 用户角色dto
     */
    void bindRole(UserRoleDTO userRoleDTO);

    /**
     * 验证码
     */
    void imgCode(String imgKey) throws IOException;

}

