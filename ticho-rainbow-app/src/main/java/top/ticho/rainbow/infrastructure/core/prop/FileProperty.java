package top.ticho.rainbow.infrastructure.core.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import java.io.File;

/**
 * @author zhajianjun
 * @date 2024-04-22 10:33
 */
@Data
@ConfigurationProperties(prefix = "rainbow.file")
@Component
public class FileProperty {

    /** 域名或者ip地址 */
    private String domain;

    /** 地址 */
    private String rootPath = System.getProperty("user.dir");

    private String mvcResourcePath = "/static/data/public/**";

    /** 文件大小限制，默认20MB */
    private DataSize maxFileSize = DataSize.ofMegabytes(20L);

    /** 分段上传大小限制 */
    private DataSize maxPartSize = DataSize.ofMegabytes(5L);


    /**
     * 开放路径，相对路径为 /data/public/
     */
    public String getPublicPath() {
        return this.rootPath + File.separator + "data" + File.separator + "public" + File.separator;
    }

    /**
     * 私有，相对路径为 /data/private/
     */
    public String getPrivatePath() {
        return this.rootPath + File.separator + "data" + File.separator + "private" + File.separator;
    }

    public String getTmpPath() {
        return this.rootPath + File.separator + "data" + File.separator + "tmp" + File.separator;
    }

}
