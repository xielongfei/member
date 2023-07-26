package com.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.CheckInRecords;
import com.wechat.entity.Members;
import com.wechat.entity.request.CheckInRequest;
import com.wechat.result.Response;
import com.wechat.result.ResultCode;
import com.wechat.service.ICheckInRecordsService;
import com.wechat.service.IMembersService;
import com.wechat.util.DateUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 打卡链接表 前端控制器
 * </p>
 *
 * @author 
 * @since 2023-07-25
 */
@Controller
@RequestMapping("/checkLink")
public class CheckLinkController {

    @Autowired
    private ICheckInRecordsService checkInRecordsService;

    @Autowired
    private IMembersService membersService;

    @ApiOperation(value = "导出")
    @GetMapping(value = "/export")
    public Object export(CheckInRequest inRequest) {
        //先查数据库，是否存在记录，存在返回链接，不存在生成链接并+1

        boolean isCheckInMonth = StringUtils.hasLength(inRequest.getCheckInMonth());
        boolean isSearchCheckInDate = StringUtils.hasLength(inRequest.getSearchCheckInDate());
        LambdaQueryWrapper wrapper = Wrappers.<CheckInRecords>lambdaQuery()
                .func(isCheckInMonth,
                        f -> f.apply("DATE_FORMAT(check_in_date, '%Y-%m') = {0}", inRequest.getCheckInMonth()))
                .func(isSearchCheckInDate,
                        f -> f.apply("DATE_FORMAT(check_in_date, '%Y-%m-%d') = {0}", inRequest.getSearchCheckInDate()));
        List<CheckInRecords> list = checkInRecordsService.list(wrapper);
        if (list.size() == 0) {
            return Response.failure(ResultCode.NOT_FOUND);
        }
        String param = isCheckInMonth ? inRequest.getCheckInMonth() : inRequest.getSearchCheckInDate();
        //原始路径
        String originPath;
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            originPath = "D:/images/checkIn";
        } else {
            originPath = "/images/checkIn";
        }

        //循环压缩
        exportCheckInRecords(list, originPath + File.separator + param, param);
        return Response.success();
    }

    /**
     * 根据打卡日期查询打卡记录，并生成压缩文件
     * @param list
     * @param outputFolder
     * @param param
     */
    public void exportCheckInRecords(List<CheckInRecords> list, String outputFolder, String param) {

        // 使用Stream API根据会员ID分组
        Map<Integer, List<CheckInRecords>> groupedByMemberId = list.stream()
                .collect(Collectors.groupingBy(CheckInRecords::getMemberId));

        //遍历每个分组，或者根据会员ID查找对应的打卡记录列表
        for (Map.Entry<Integer, List<CheckInRecords>> entry : groupedByMemberId.entrySet()) {
            int memberId = entry.getKey();
            List<CheckInRecords> records = entry.getValue();
            Members members = membersService.getById(memberId);
            String memberName = members.getName();
            String shopId = members.getShopId();
            //创建会员编号文件夹
            File dateFolder = new File(outputFolder, memberName + "_" + shopId);
            if (!dateFolder.exists()) {
                dateFolder.mkdirs();
            }
            // 生成 Excel 文件
            String excelFilePath = dateFolder.getAbsolutePath() + File.separator + "打卡信息.xlsx";
            generateExcel(records, memberName, excelFilePath);

            // 获取该会员的所有图片路径
            List<String> imageFilePaths = records.stream()
                    .filter(f -> StringUtils.hasLength(f.getImageUrl()))
                    .map(CheckInRecords::getImageUrl)
                    .collect(Collectors.toList());
            if (imageFilePaths.size() == 0) {
                continue;
            }
            // 将所有图片打包为一个 ZIP 文件
            try {
                String zipFileName = dateFolder.getAbsolutePath() + File.separator + "打卡照片.zip";
                try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName))) {
                    for (String imageFilePath : imageFilePaths) {
                        Path sourcePath = Paths.get(imageFilePath);
                        ZipEntry zipEntry = new ZipEntry(sourcePath.getFileName().toString());
                        zipOutputStream.putNextEntry(zipEntry);
                        Files.copy(sourcePath, zipOutputStream);
                        zipOutputStream.closeEntry();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 最终将所有会员文件夹打包为一个 ZIP 文件
        try {
            String zipFileName = String.format("%s%s打卡信息_%s.zip", outputFolder, File.separator, param);
            Path sourcePath = Paths.get(outputFolder);
            Path targetPath = Paths.get(zipFileName);

            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(targetPath.toFile()))) {
                Files.walk(sourcePath).forEach(path -> {
                    if (!Files.isDirectory(path)) {
                        ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            Files.copy(path, zipOutputStream);
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成 Excel 文件
     * @param records
     * @param name
     * @param filePath
     */
    private void generateExcel(List<CheckInRecords> records, String name, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("打卡记录");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("打卡编号");
            headerRow.createCell(1).setCellValue("姓名");
            headerRow.createCell(2).setCellValue("打卡日期");
            headerRow.createCell(3).setCellValue("省");
            headerRow.createCell(4).setCellValue("市");
            headerRow.createCell(5).setCellValue("区/县");
            headerRow.createCell(6).setCellValue("详细地址");

            // 写入数据
            int rowIndex = 1;
            for (CheckInRecords record : records) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(record.getId());
                dataRow.createCell(1).setCellValue(name);
                dataRow.createCell(2).setCellValue(DateUtil.formatter1.format(record.getCheckInDate()));
                dataRow.createCell(3).setCellValue(record.getProvince());
                dataRow.createCell(4).setCellValue(record.getCity());
                dataRow.createCell(5).setCellValue(record.getCounty());
                dataRow.createCell(6).setCellValue(record.getAddress());
            }

            // 将数据写入文件
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
