package com.vchicken.rabbitmq.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMessBody {

    private static final long serialVersionUID = 1L;

    private String title;

    private String body;
}
