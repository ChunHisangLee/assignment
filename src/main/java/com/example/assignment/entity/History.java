package com.example.assignment.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class History extends Trade {
    String historyId;
}
