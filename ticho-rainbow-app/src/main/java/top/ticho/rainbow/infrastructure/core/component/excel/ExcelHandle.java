package top.ticho.rainbow.infrastructure.core.component.excel;

import cn.hutool.core.util.URLUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.tool.json.util.JsonUtil;
import top.ticho.boot.view.core.BasePageQuery;
import top.ticho.boot.view.core.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * excel数据处理
 * <p>easy excel</p>
 *
 * @author zhajianjun
 * @date 2024-05-09 18:41
 */
@Slf4j
public class ExcelHandle {

    /**
     * 读取excel
     *
     * @param file 文件
     * @param handlerDataBatch 对读取的数据处理的逻辑
     * @param claz excel读取类
     * @return {@link List}<{@link M}> 读取数据
     */
    public static <M extends ExcelBaseImp> List<M> read(
        MultipartFile file,
        BiConsumer<List<M>, Consumer<M>> handlerDataBatch,
        Class<M> claz
    ) throws IOException {
        List<M> allDatas = new ArrayList<>();
        BiConsumer<List<M>, Consumer<M>> handlerDataBatchProxy = (s, t) -> {
            handlerDataBatch.accept(s, t);
            allDatas.addAll(s);
        };
        ExcelListener<M> readListener = new ExcelListener<>(handlerDataBatchProxy);
        EasyExcel.read(file.getInputStream(), claz, readListener).sheet().doRead();
        return allDatas;
    }

    /**
     * 数据写入流
     *
     * @param datas 数据
     * @param title 标题
     * @param claz  excel类
     */
    public static <M> void write(
        OutputStream outputStream,
        List<M> datas,
        String title,
        Class<M> claz
    ) {
        try (ExcelWriter excelWriter = EasyExcel.write(outputStream, claz).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
            excelWriter.write(datas, writeSheet);
        }
    }

    /**
     * 数据写入流
     *
     * @param datas 数据
     * @param title 标题
     * @param claz  excel类
     * @return {@link ByteArrayOutputStream} 返回流
     */
    public static <M> ByteArrayOutputStream write(
        List<M> datas,
        String title,
        Class<M> claz
    ) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ExcelWriter excelWriter = EasyExcel.write(out, claz).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
            excelWriter.write(datas, writeSheet);
            return out;
        }
    }

    /**
     * 数据写入HttpServletResponse
     *
     * @param response HttpServletResponse
     */
    public static <M> void writeToResponse(
        List<M> datas,
        String fileName,
        String sheetName,
        Class<M> claz,
        HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLUtil.encodeAll(fileName + ".xlsx"));
        try(ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), claz).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            excelWriter.write(datas, writeSheet);
        } catch (Exception e) {
            log.error("{}下载失败，{}", sheetName, e.getMessage(), e);
            downloadFileErrRes(response, e.getMessage());
        }
    }

    /**
     * 数据写入HttpServletResponse
     *
     * @param handlerDataBatch 批查询逻辑
     * @param query            查询条件
     * @param fileName         文件名称
     * @param sheetName        sheetName
     * @param claz             excel类
     * @param response         HttpServletResponse
     */
    public static <Q extends BasePageQuery, R> void writeToResponseBatch(
        Function<Q, Collection<R>> handlerDataBatch,
        Q query,
        String fileName,
        String sheetName,
        Class<R> claz,
        HttpServletResponse response
    ) throws IOException {
        writeToResponseBatch(handlerDataBatch, query, fileName, sheetName, claz, 50, 10000L, response);
    }

    /**
     * 空数据写入HttpServletResponse
     *
     * @param fileName         文件名称
     * @param sheetName        sheetName
     * @param claz             excel类
     * @param response         HttpServletResponse
     */
    public static <R> void writeEmptyToResponseBatch(
        String fileName,
        String sheetName,
        Class<R> claz,
        HttpServletResponse response
    ) throws IOException {
        writeToResponseBatch(null, null, fileName, sheetName, claz, 50, 10000L, response);
    }

    /**
     * 数据写入HttpServletResponse
     *
     * @param handlerDataBatch 批查询逻辑
     * @param query            查询条件
     * @param fileName         文件名称
     * @param sheetName        sheetName
     * @param claz             excel类
     * @param batchSize        批数量
     * @param maxTotal         最大数量
     * @param response         HttpServletResponse
     */
    public static <Q extends BasePageQuery, R> void writeToResponseBatch(
        Function<Q, Collection<R>> handlerDataBatch,
        Q query,
        String fileName,
        String sheetName,
        Class<R> claz,
        Integer batchSize,
        Long maxTotal,
        HttpServletResponse response
    ) throws IOException {
        if (batchSize == null || batchSize <=0) {
            batchSize = 50;
        }
        if (maxTotal == null || maxTotal <=0) {
            maxTotal = 10000L;
        }
        if (sheetName == null || sheetName.isEmpty()) {
            sheetName = "sheet";
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLUtil.encodeAll(fileName + ".xlsx"));
        try(ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), claz).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            long total = 0;
            if (query == null) {
                // 要执行write,不然没有标题
                excelWriter.write(Collections.emptyList(), writeSheet);
                return;
            }
            query.setPageNum(1);
            query.setPageSize(batchSize);
            while (true) {
                Collection<R> apply = handlerDataBatch.apply(query);
                // 如果第一次执行是空数据，也要执行write,不然没有标题
                excelWriter.write(apply, writeSheet);
                if (apply.isEmpty()) {
                    break;
                }
                total = total + apply.size();
                // 最大数量保护，防止死循环
                if (total > maxTotal) {
                     break;
                }
                // 当每页条数等于 Integer.MAX_VALUE 打断循环
                if (Objects.equals(query.getPageSize(), Integer.MAX_VALUE)) {
                     break;
                }
                query.setPageNum(query.getPageNum() + 1);
            }
        } catch (Exception e) {
            log.error("{}下载失败，{}", sheetName, e.getMessage(), e);
            downloadFileErrRes(response, e.getMessage());
        }
    }

    /**
     * 读取excel,把结果写入到新的excel中
     *
     * @param file 文件
     * @param handlerDataBatch 对读取的数据处理的逻辑
     * @param claz excel导入类
     */
    public static <M extends ExcelBaseImp> void readAndWriteToResponse(
        BiConsumer<List<M>, Consumer<M>> handlerDataBatch,
        MultipartFile file,
        String fileName,
        String sheetName,
        Class<M> claz,
        HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLUtil.encodeAll(fileName + ".xlsx"));
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), claz).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            excelWriter.write(Collections.emptyList(), writeSheet);
            BiConsumer<List<M>, Consumer<M>> handlerDataBatchProxy = (s, t) -> {
                handlerDataBatch.accept(s, t);
                excelWriter.write(s, writeSheet);
            };
            ExcelListener<M> readListener = new ExcelListener<>(handlerDataBatchProxy);
            EasyExcel.read(file.getInputStream(), claz, readListener).sheet().doRead();
        }
    }

    /**
     * 下载文件错误返回
     *
     * @param response 响应
     * @param errMsg   犯错味精
     * @throws IOException ioexception
     */
    public static void downloadFileErrRes(HttpServletResponse response, String errMsg) throws IOException {
        // 重置response
        response.reset();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Result<String> fail = Result.of(500, "下载文件失败，" + errMsg);
        PrintWriter writer = response.getWriter();
        writer.println(JsonUtil.toJsonString(fail));
    }

}