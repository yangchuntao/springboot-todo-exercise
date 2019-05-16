
package com.tdwl.wife.sql.utils;

import com.tdwl.wife.sql.exception.ExceptionMsg;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultMap extends HashMap<String, Object> {

    public final static String CODE = "code";
    public final static String MESSAGE = "message";
    public final static String FORM_ERROR_LIST = "formErrorList";
    public final static String FIELD_ERROR_MAP = "fieldErrorMap";

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 7419449915536552454L;

    public static ResultMap newInstanceOfDefaultError() {
        ResultMap rm = new ResultMap();
        rm.put(ExceptionMsg.DEFAULT_ERROR);
        return rm;
    }

    public static ResultMap newInstanceOfDefaultError(String msg) {
        ResultMap rm = new ResultMap();
        rm.put(ExceptionMsg.BUSINESS_ERROR);
        rm.put(MESSAGE, msg);
        return rm;
    }

    public static ResultMap newInstanceOfDefaultError(Model model) {
        ResultMap rm = new ResultMap();
        rm.put(model);
        rm.put(ExceptionMsg.DEFAULT_ERROR);
        return rm;
    }

    /**
     * 
     * newInstanceOfInvalid: 创建并返回带有表单参数校验非法的map
     *
     * @param @param bindingResult
     * @return 
     *         ResultMap返回格式{"code":"10003","message":"请求参数无效","formErrorList":["xxx","xxx","xxx"],"fieldErrorMap":[{"xx"
     *         :"xxx"},{...}]}.
     * @throws ResultMap
     *             DOM对象
     * @since JDK 1.8
     */
    public static ResultMap newInstanceOfInvalid(BindingResult bindingResult) {
        ResultMap rm = new ResultMap();
        rm.put(ExceptionMsg.PARAM_INVALID);
        rm.put(bindingResult);
        return rm;
    }

    /**
     * 
     * newInstanceOfInvalidSimpleMsg: 创建并返回带有简单的表单参数校验非法的map，返回格式见@return
     *
     * @param @param bindingResult
     * @return ResultMap返回格式{"code":"10003","message":"xxxxxx"}.
     * @throws ResultMap
     *             DOM对象
     * @since JDK 1.8
     */
    public static ResultMap newInstanceOfInvalidSimpleMsg(BindingResult bindingResult) {
        ResultMap rm = new ResultMap();
        rm.put(ExceptionMsg.PARAM_INVALID);
        rm.putSimpleMsg(bindingResult);
        return rm;
    }

    public static ResultMap newInstanceOfInvalid(BindingResult bindingResult, Model model) {
        ResultMap rm = newInstanceOfInvalid(bindingResult);
        rm.put(model);
        return rm;
    }

    public static ResultMap newInstanceOfSuccess() {
        ResultMap rm = new ResultMap();
        rm.put(ExceptionMsg.SUCCESS);
        return rm;
    }

    public static ResultMap newInstanceOfSuccess(Model model) {
        ResultMap rm = new ResultMap();
        rm.put(model);
        rm.put(ExceptionMsg.SUCCESS);
        return rm;
    }

    public static ResultMap newInstanceOfSuccess(String msg) {
        ResultMap rm = new ResultMap();
        rm.put(ExceptionMsg.SUCCESS);
        rm.put(MESSAGE, msg);
        return rm;
    }

    public static ResultMap newInstance(Model model) {
        ResultMap rm = new ResultMap();
        rm.put(model);
        return rm;
    }

    public static ResultMap newInstance() {
        return new ResultMap();
    }

    public static ResultMap newInstance(String code, String message) {
        ResultMap resultMap = new ResultMap();
        resultMap.put(CODE, code);
        resultMap.put(MESSAGE, message);
        return resultMap;
    }

    public static ResultMap newInstance(ExceptionMsg exceptionMsg) {
        ResultMap rm = new ResultMap();
        rm.put(exceptionMsg);
        return rm;
    }

    public ResultMap() {
        super();
    }

    public ResultMap(Model model) {
        super();
        put(model);
    }

    public void put(Model model) {
        if (model != null) {
            putAll(model.asMap());
        }
    }

    public void put(BindingResult bindingResult) {
        List<String> formErrorList = new ArrayList<String>();
        Map<String, String> fieldErrorMap = new HashMap<String, String>();
        for (ObjectError oe : bindingResult.getAllErrors()) {
            if (oe instanceof FieldError) {
                FieldError fe = (FieldError) oe;
                fieldErrorMap.put(fe.getField(), oe.getDefaultMessage());
            } else {
                formErrorList.add(oe.getDefaultMessage());
            }
        }
        put(FORM_ERROR_LIST, formErrorList);
        put(FIELD_ERROR_MAP, fieldErrorMap);
    }

    public void putSimpleMsg(BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        // StringBuilder sb = new StringBuilder();
        for (ObjectError oe : allErrors) {
            // sb.append(oe.getDefaultMessage());
            put(MESSAGE, oe.getDefaultMessage());
            return;
        }
        // put(MESSAGE,sb.toString());
    }

    public void put(ExceptionMsg resultMsg) {
        put(CODE, resultMsg.getCode());
        put(MESSAGE, resultMsg.getMsg());
    }

    public static ResultMap dataNewInstanceOfSuccess(ExceptionMsg msg) {
        ResultMap rm = ResultMap.newInstanceOfSuccess();
        rm.put("data", putData(msg));
        return rm;
    }

    public static ResultMap dataNewInstanceOfDefaultError(ExceptionMsg msg) {
        ResultMap rm = ResultMap.newInstanceOfDefaultError();
        rm.put("data", putData(msg));
        return rm;
    }

    private static ResultMap putData(ExceptionMsg msg){
        ResultMap resultMap = ResultMap.newInstance();
        resultMap.put(msg);
        return resultMap;
    }

    public static ResultMap dataNewInstanceOfInvalidSimpleMsg(BindingResult bindingResult) {
        ResultMap rm = new ResultMap();
        if(bindingResult.hasFieldErrors("captcha")){
            rm.put(ExceptionMsg.VCODE_INVALID);
        }else if(bindingResult.hasFieldErrors("phone")){
            rm.put(ExceptionMsg.USERNAME_NOT_FOUND);
        }else if (bindingResult.hasFieldErrors("password")){
            rm.put(ExceptionMsg.PASSWORD_INVALID);
        }else{
            rm.put(ExceptionMsg.PARAM_INVALID);
        }
        return rm;
    }

    public String getResultMsg() {
        return (String) get(MESSAGE);
    }

    public String getResultCode() {
        return (String) get(CODE);
    }

    public boolean isErrorResult() {
        return !isSuccessResult();
    }

    public boolean isSuccessResult() {
        return ExceptionMsg.SUCCESS.equals(getResultCode());
    }

}
