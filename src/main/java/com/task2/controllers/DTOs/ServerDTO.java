package com.task2.controllers.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.task2.DB.DTO;
import io.micrometer.core.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerDTO implements DTO {
    @Id
    @NonNull
    private Integer serverId;

    @Min(value = 0, message = "Capacity should not be less than 0")
    @Max(value = 100, message = "Capacity should not be greater than 100")
    @NotNull
    private Double total = 100.0;

    @NonNull
    private Double allocated = 0.0;

    @NonNull
    //@JsonIgnore
    private Double free = 0.0;

    @NonNull
    private String state;
    private Date dateCreated;
}
