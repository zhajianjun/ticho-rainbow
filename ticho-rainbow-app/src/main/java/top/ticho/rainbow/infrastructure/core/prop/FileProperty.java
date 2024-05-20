package top.ticho.rainbow.infrastructure.core.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.util.Objects;

/**
 * @author zhajianjun
 * @date 2024-04-22 10:33
 */
@Data
@ConfigurationProperties(prefix = "ticho.rainbow.file")
@Component
public class FileProperty {

    /** 域名或者ip地址 */
    private String domain;

    /** 地址 */
    private String rootPath = System.getProperty("user.dir");

    private String mvcResourcePath = "/static/file/**";

    /** 文件大小限制，默认20MB */
    private DataSize maxFileSize = DataSize.ofMegabytes(20L);

    /** 分段上传大小限制 */
    private DataSize maxPartSize = DataSize.ofMegabytes(5L);

    /** 大文件大小限制，默认1GB */
    private DataSize maxBigFileSize = DataSize.ofGigabytes(1L);

    public String getRootPath() {
        if (Objects.isNull(this.rootPath) || Objects.equals(this.rootPath, File.separator)) {
            return File.separator;
        }
        return this.rootPath + File.separator;
    }

    /**
     * 开放路径，相对路径为 /data/public/
     */
    public String getPublicPath() {
        return getRootPath() + "data" + File.separator + "public" + File.separator;
    }

    /**
     * 私有，相对路径为 /data/private/
     */
    public String getPrivatePath() {
        return getRootPath() + "data" + File.separator + "private" + File.separator;
    }

    public String getTmpPath() {
        return getRootPath() + "data" + File.separator + "tmp" + File.separator;
    }

}
