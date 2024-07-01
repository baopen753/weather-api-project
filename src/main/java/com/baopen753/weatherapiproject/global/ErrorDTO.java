package com.baopen753.weatherapiproject.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ErrorDTO {
    private Date timeStamp;
    private Integer status;
    private String path;
    public List<String> error = new ArrayList<>();

    public void addError(String error) {
        this.error.add(error);
    }

}
