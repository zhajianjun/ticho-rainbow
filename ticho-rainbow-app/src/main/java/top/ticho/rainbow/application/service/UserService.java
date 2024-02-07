package top.ticho.rainbow.application.service;

import top.ticho.boot.view.core.PageResult;
import top.ticho.rainbow.interfaces.dto.UserDTO;
import top.ticho.rainbow.interfaces.dto.UserLoginDTO;
import top.ticho.rainbow.interfaces.dto.UserPasswordDTO;
import top.ticho.rainbow.interfaces.dto.UserRoleDTO;
import top.ticho.rainbow.interfaces.dto.UserRoleMenuDtlDTO;
import top.ticho.rainbow.interfaces.dto.UserSignUpDTO;
import top.ticho.rainbow.interfaces.query.UserQuery;

import java.io.IOException;

/**
 * 用户信息 服务接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public interface UserService {

    /**
     * 注册
     *
     * @param userSignUpDTO 注册dto
     */
    UserLoginDTO signUp(UserSignUpDTO userSignUpDTO);

    /**
     * 注册邮箱发送
     *
     * @param email 注册dto
     */
    void signUpEmailSend(String email);

    /**
     * 注册确认
     *
     * @param username 账户名称
     */
    void confirm(String username);

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

