package com.baopen753.weatherapiproject.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ErrorDTO {
    private Date timeStamp;
    private Integer status;
    private String path;
    public Set<String> error = new HashSet<>();

    public void addError(String error) {
        this.error.add(error);
    }

}
