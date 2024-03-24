package com.plx.admin_system.utils;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.util.ObjectUtil;
import lombok.SneakyThrows;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

/**
 * @author plx
 */
@Component
public class ClassExcelVerifyHandler implements IExcelVerifyHandler {
    @SneakyThrows
    @Override
    public ExcelVerifyHandlerResult verifyHandler(Object o) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if (ObjectUtil.isNotNull(o)) {
            //判断对象属性是否全部为空
            boolean b = ObjectIsNullUitl.checkFieldAllNull(o);
            result.setSuccess(!b);
        }
        return result;
    }
}


