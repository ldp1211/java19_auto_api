package com.lemon.homework;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.utiles.ExcelUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DanpingLi
 * @date 2020/6/23 - 18:06
 */
public class ExcelUtils1 {
    public static List<WriteBackDate1>wbd1List = new ArrayList<>();

    public static Object[] getDatas(int sheetIndex,int sheetNum,Class clazz){
        try {
            List <CaseInfo> list = ExcelUtils1.read(sheetIndex, sheetNum, CaseInfo1.class);
            //集合转成数组
            Object[] datas = list.toArray();
            return  datas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param sheetIndex  sheet开始位置
     * @param sheetNum  sheet个数
     * @param clazz   映射关系字节码
     * @return
     * @throws Exception
     */
    public static List read(int sheetIndex, int sheetNum, Class clazz) throws Exception {
        //1.加载excel文件
        FileInputStream fis = new FileInputStream(Constants1.EXCEL_PATH);
        //导入参数  easypoi
        ImportParams params = new ImportParams();
        //默认从第一个sheet开始读取(0)
        params.setStartSheetIndex(sheetIndex);
        //默认每次读取一个sheet
        params.setSheetNum(sheetNum);
        //importExcel(EXCEL文件流，映射关系字节码对象，导入参数)
        List caseInfo1List = ExcelImportUtil.importExcel(fis, clazz, params);

        return caseInfo1List;
    }

    public static void batchWrite(){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(Constants1.EXCEL_PATH);
        //解析数据必须用poi提供对象
        Workbook excel = WorkbookFactory.create(fis);
        //循环 批量回写集合 wbd1List
            for (WriteBackDate1 writeBackDate1 : wbd1List) {
                //取出sheetIndex
                int sheetIndex = writeBackDate1.getSheetIndex();
                //取出行号
                int rownum = writeBackDate1.getRownum();
                //取出列号
                int cellnum = writeBackDate1.getCellnum();
                //取出响应内容
                String content = writeBackDate1.getContent();
                //选择sheet
                Sheet sheet = excel.getSheetAt(sheetIndex);
                //读取每一行
                Row row = sheet.getRow(rownum);
                //读取每一个单元格
                Cell cell = row.getCell(cellnum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //修改
                cell.setCellValue(content);
            }
        fos = new FileOutputStream(Constants1.EXCEL_PATH);
        //写回去
        excel.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关流
            try {
                if(fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
