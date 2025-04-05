package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import top.ticho.rainbow.application.dto.command.UseModifyCommand;
import top.ticho.rainbow.application.dto.command.UseModifySelfCommand;
import top.ticho.rainbow.application.dto.command.UseSaveCommand;
import top.ticho.rainbow.application.dto.excel.UserExcelExport;
import top.ticho.rainbow.application.dto.excel.UserExcelImport;
import top.ticho.rainbow.application.dto.response.UserDTO;
import top.ticho.rainbow.application.dto.response.UserRoleMenuDtlDTO;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.entity.vo.UserModifyVO;

/**
 * 用户信息 转换
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Mapper(componentModel = "spring")
public interface UserAssembler {

    /**
     * 用户信息
     */
    User toEntity(UseSaveCommand useSaveCommand);

    UserModifyVO toModifyVo(UseModifyCommand useModifyCommand);

    UserModifyVO toModifyVo(UseModifySelfCommand useModifyCommand);

    UserDTO toDTO(User entity);

    UserRoleMenuDtlDTO toDtlDTO(User user);

    UserExcelExport toExp(User user);

    default User toEntity(UserExcelImport imp, String password, Integer status, Integer sex) {
        if (imp == null) {
            return null;
        }
        User.UserBuilder user = User.builder();
        user.username(imp.getUsername());
        user.nickname(imp.getNickname());
        user.realname(imp.getRealname());
        user.idcard(imp.getIdcard());
        user.age(imp.getAge());
        user.birthday(imp.getBirthday());
        user.address(imp.getAddress());
        user.education(imp.getEducation());
        user.email(imp.getEmail());
        user.qq(imp.getQq());
        user.wechat(imp.getWechat());
        user.mobile(imp.getMobile());
        user.password(password);
        user.sex(sex);
        user.status(status);
        return user.build();
    }


}