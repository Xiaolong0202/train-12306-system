package com.lxl.business.req;


import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class WebDailyTrainTicketQueryReq  implements Serializable {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    private String start;

    private String end;
}
