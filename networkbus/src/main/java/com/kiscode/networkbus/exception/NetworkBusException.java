package com.kiscode.networkbus.exception;

/**
 * Description: NetworkBusException
 * Author: keno
 * Date : 2020/9/7 23:19
 **/
public class NetworkBusException extends RuntimeException {
    public NetworkBusException(String message) {
        super(message);
    }

    public NetworkBusException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkBusException(Throwable cause) {
        super(cause);
    }
}