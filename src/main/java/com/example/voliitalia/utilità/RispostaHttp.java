package com.example.voliitalia.utilità;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class RispostaHttp {
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String messaggio;

    public RispostaHttp(int httpStatusCode,HttpStatus httpStatus,String messaggio){
        this.httpStatus=httpStatus;
        this.httpStatusCode=httpStatusCode;
        this.messaggio=messaggio;
    }
}
