package com.example.mianshi.response;

import com.example.mianshi.domain.ReturnCode;
import lombok.Data;

@Data
public class ResponseWrapper {

    /**是否成功*/
    private String status;
    /**返回码*/
    private String code;
    /**返回数据*/
    private Object data;

    public static ResponseWrapper markCustom(String status,String code,Object data){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setStatus(status);
        responseWrapper.setCode(code);
        responseWrapper.setData(data);
        return responseWrapper;
    }

    /**
     * 参数为空或者参数格式错误
     * @return
     */
    public static ResponseWrapper markParamError(){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setStatus("1004");
        responseWrapper.setCode(ReturnCode.PARAMS_ERROR.getCode());
        return responseWrapper;
    }

    /**
     * 查询失败
     * @return
     */
    public static ResponseWrapper markError(){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setStatus("0002");
        responseWrapper.setCode(ReturnCode.FAILED.getCode());
        responseWrapper.setData(null);
        return responseWrapper;
    }

    /**
     * 查询成功但无数据
     * @return
     */
    public static ResponseWrapper markStatusButNoData(){
        ResponseWrapper responseWrapper  = new ResponseWrapper();
        responseWrapper.setStatus("1");
        responseWrapper.setCode(ReturnCode.NODATA.getCode());
        responseWrapper.setData(null);
        return responseWrapper;
    }

    /**
     * 查询成功且有数据
     * @param data
     * @return
     */
    public static ResponseWrapper markStatus(Object data){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setStatus("1");
        responseWrapper.setCode(ReturnCode.SUCCESS.getCode());
        responseWrapper.setData(data);
        return  responseWrapper;
    }

    public static ResponseWrapper markStatus0(Object data){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setStatus("0");
        responseWrapper.setCode(ReturnCode.PARAMS_ERROR.getCode());
        responseWrapper.setData(data);
        return  responseWrapper;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status= status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResponseWrapper{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}