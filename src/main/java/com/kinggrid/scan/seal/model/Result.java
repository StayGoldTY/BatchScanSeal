package com.kinggrid.scan.seal.model;

import java.io.Serializable;

/**
 *
 * @author xionglei
 * @version 1.0.0
 * 2021年09月09日 09:13
 */
public class Result<T> implements Serializable {

    private String code;

    private String message;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
