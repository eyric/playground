package org.example.api.demo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DemoRequest {
    @NotNull(message = "id不允许为空")
    private String id;
}
