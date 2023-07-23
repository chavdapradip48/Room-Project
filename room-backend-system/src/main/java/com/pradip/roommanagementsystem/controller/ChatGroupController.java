package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.chat.ChatGroupDTO;
import com.pradip.roommanagementsystem.dto.chat.GroupUserOperation;
import com.pradip.roommanagementsystem.service.ChatGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/chat/group")
@CrossOrigin(origins = "*")
@Slf4j
public class ChatGroupController {
    @Autowired
    private ChatGroupService groupService;

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getGroups() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Groups fetched successfully.", groupService.getAllGroups())
        );
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Object> getGroupById(@PathVariable Long groupId) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Group fetched successfully.", groupService.getOptimizedGroupDataById(groupId))
        );
    }

    @PostMapping
    public ResponseEntity<Object> addGroup(@RequestBody ChatGroupDTO group){
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value(),
                "Group saved successfully.", groupService.save(group, false))
        );
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Object> updateGroup(@RequestBody ChatGroupDTO group, @PathVariable Long groupId){
        group.setId(groupId);
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value(),
                "Group updated successfully.", groupService.save(group, true))
        );
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Object> deleteGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value()
                , "Group deleted successfully.",groupService.deleteGroup(groupId))
        );
    }

    @PutMapping("/{groupId}/add-user/{userId}")
    public ResponseEntity<Object> addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId){
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value(),
                "Users assigned successfully in Group.", groupService.addOrRemoveUserFromGroup(groupId, userId, true))
        );
    }

    @DeleteMapping("/{groupId}/remove-user/{userId}")
    public ResponseEntity<Object>   removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId){
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value(),
                "User Removed Successfully From Group", groupService.addOrRemoveUserFromGroup(groupId,userId, false))
        );
    }

}


