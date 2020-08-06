package com.foreign.exchange.service;


import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.NumberFormat;

/**
 * 处理excel公共类
 * @author
 * @create 2020-08-06-10:19
 */
public class AbstractParseExcelService {

    //返回String类型
    protected  String getStringValue(XSSFCell cell){
        String value = null;
        if (cell != null){
            int cellType = cell.getCellType();
            if (cellType == 3){
                value = null;
                //小数类型
            }else  if (cellType ==0){
                double cv = cell.getNumericCellValue();
                String numericStr = String.valueOf(cv);
                if (numericStr.indexOf("E") == -1){
                    //不含E的浮点型
                    value = numericStr;
                }else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    value = nf.format(cv);
                }
                //String类型
            }else if(cellType ==1){
                value = cell.getStringCellValue();
            }
        }
        return  value;
    }

    //返回Double类型
    protected  Double getNumericValue(XSSFCell cell){
        if (cell == null){
            return  null;
        }else {
            int cellType = cell.getCellType();
            if (cellType == 3){
                return  null;
            }else  if (cellType == 0){
                return cell.getNumericCellValue();
            }else if (cellType == 1){
                return  Double.parseDouble(cell.getStringCellValue());
            }else {
                return  null;
            }
        }
    }

    //返回Int类型
    protected  Integer getIntValue(XSSFCell cell){
        Double cv = cell.getNumericCellValue();
        return  cv==null?null:cv.intValue();
    }
}
