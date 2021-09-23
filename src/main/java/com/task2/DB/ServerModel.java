package com.task2.DB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerModel implements Model {
    @Id
    private Integer serverId;
    private Double total;
    private Double allocated;
    private Double free;
    private String state;
    private Date dateCreated;
}
