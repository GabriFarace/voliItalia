package com.example.voliitalia.utilità;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pagina {
    private List<?> elementi;
    private int numPagine;

    public Pagina(List<?> elementi,int n){
        this.elementi=elementi;
        this.numPagine=n;
    }
}
