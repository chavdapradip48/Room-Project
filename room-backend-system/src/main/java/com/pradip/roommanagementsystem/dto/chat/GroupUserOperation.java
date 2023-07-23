package com.pradip.roommanagementsystem.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class GroupUserOperation {
    private List<Long> userIds;
}
