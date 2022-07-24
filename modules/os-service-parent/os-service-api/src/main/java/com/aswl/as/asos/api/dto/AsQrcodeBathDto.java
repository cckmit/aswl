package com.aswl.as.asos.api.dto;
import lombok.Data;
import java.io.Serializable;

/**
 * @author df
 * @date 2020/12/18 17:34
 */
@Data
public class AsQrcodeBathDto implements Serializable {
    private String[] ids;
    private String zipname;
}
