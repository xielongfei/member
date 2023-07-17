package com.wechat.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.wechat.entity.Members;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * @date: 2023/07/17
 * @description:
 */
public class ExportUtils {

    public static void exportMembersToExcel(List<Members> members, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("会员_" + LocalDate.now() + ".xlsx", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream(), Members.class);
        ExcelWriterSheetBuilder sheetBuilder = writerBuilder.sheet();
        sheetBuilder.doWrite(members);
        writerBuilder.build().finish();
    }
}
