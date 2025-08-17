package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.application.dto.excel.UserExcelExport;
import top.ticho.rainbow.application.dto.excel.UserExcelImport;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.entity.vo.UserModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.UserStatus;
import top.ticho.rainbow.interfaces.command.LoginUserModifyCommand;
import top.ticho.rainbow.interfaces.command.UseModifyCommand;
import top.ticho.rainbow.interfaces.command.UseSaveCommand;
import top.ticho.rainbow.interfaces.command.UserSignUpCommand;
import top.ticho.rainbow.interfaces.dto.LoginUserDTO;
import top.ticho.rainbow.interfaces.dto.LoginUserDetailDTO;
import top.ticho.rainbow.interfaces.dto.UserDTO;
import top.ticho.tool.core.TiIdUtil;

/**
 * 用户信息 转换
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Mapper(componentModel = "spring", imports = {UserStatus.class, TiIdUtil.class})
public interface UserAssembler {

    @Mapping(target = "id", expression = "java(TiIdUtil.snowId())")
    @Mapping(target = "status", expression = "java(UserStatus.NORMAL.code())")
    User toEntity(UseSaveCommand useSaveCommand);

    @Mapping(target = "id", expression = "java(TiIdUtil.snowId())")
    @Mapping(target = "status", expression = "java(UserStatus.NORMAL.code())")
    @Mapping(target = "nickname", source = "username")
    User toEntity(UserSignUpCommand userSignUpCommand);

    @Mapping(target = "id", expression = "java(TiIdUtil.snowId())")
    @Mapping(target = "status", expression = "java(UserStatus.NORMAL.code())")
    User toEntity(UserExcelImport imp, String password, Integer sex);

    UserModifyVO toModifyVo(UseModifyCommand useModifyCommand);

    UserModifyVO toModifyVo(LoginUserModifyCommand useModifyCommand);

    UserDTO toDTO(User user);

    LoginUserDTO toLoginUserDTO(User user);

    LoginUserDetailDTO toLoginUserDetailDTO(User user);

    UserExcelExport toExcelExport(UserDTO userDTO);

}