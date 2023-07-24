package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.ChatUser;
import com.pradip.roommanagementsystem.dto.UserExpenseDTO;
import com.pradip.roommanagementsystem.dto.chat.ChatGroupDTO;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.entity.chat.ChatGroup;
import com.pradip.roommanagementsystem.exception.GroupException;
import com.pradip.roommanagementsystem.repository.ChatGroupRepository;
import com.pradip.roommanagementsystem.repository.UserRepository;
import com.pradip.roommanagementsystem.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatGroupService {

    @Autowired
    private ChatGroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralUtil generalUtil;

    public ChatGroupDTO save(ChatGroupDTO group, boolean isUpdate) {
        boolean isGroupNameExist = false;
        try {
            isGroupNameExist = ((List<ChatGroupDTO>) getAllGroups()).stream().anyMatch(perGroup -> perGroup.getName().equals(group.getName()));
        } catch (Exception e){}

        if (isGroupNameExist) {
            throw new GroupException("The Group name provided already exists. Please choose a different name");
        }

        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setName(group.getName());

//                ChatGroup chatGroup = generalUtil.convertObject(group, ChatGroup.class);

        if (isUpdate) chatGroup.setMembers(getGroupById(group.getId()).getMembers());
        else chatGroup.setMembers(group.getMembers().stream().map(member -> generalUtil.convertObject(member, User.class)).collect(Collectors.toSet()));

        ChatGroup savedGroup = groupRepository.save(chatGroup);

        return getOptimizedGroupDataById(savedGroup.getId());
    }

    public Object getAllGroups() {
        List<ChatGroup> all = groupRepository.findAll();

        if(all == null || all.isEmpty()){
            throw new EntityNotFoundException("Groups are not available");
        }

        return all.stream()
                .map(chatGroup -> {
                    ChatGroupDTO chatGroupDTO = generalUtil.convertObject(chatGroup, ChatGroupDTO.class);
                    chatGroupDTO.setMembers(chatGroup.getMembers().stream()
                            .map(user1 -> generalUtil.convertObject(user1, ChatUser.class)).collect(Collectors.toSet()));
                    return chatGroupDTO;
                }).collect(Collectors.toList());
    }

    public ChatGroup getGroupById(Long groupId) {

        Optional<ChatGroup> existingChatGroupOptional = groupRepository.findById(groupId);
        if(existingChatGroupOptional.isEmpty()){
            throw new EntityNotFoundException("Group does not Exist with "+groupId+" Id");
        }

        return existingChatGroupOptional.get();
    }

    public ChatGroupDTO getOptimizedGroupDataById(Long groupId) {

        ChatGroup groupById = getGroupById(groupId);

        ChatGroupDTO chatGroupDTO = generalUtil.convertObject(groupById, ChatGroupDTO.class);
        Set<User> members = groupById.getMembers();
        Set<ChatUser> optimizedmembers = null;
        if (!members.isEmpty()) {
            optimizedmembers = members.stream().map(member -> generalUtil.convertObject(member, ChatUser.class)).collect(Collectors.toSet());
        }

        chatGroupDTO.setMembers(optimizedmembers);

        return chatGroupDTO;
    }

    public ChatGroup deleteGroup(Long groupId) {
        ChatGroup groupById = getGroupById(groupId);
        groupRepository.deleteById(groupId);
        return groupById;
    }

    public Object addOrRemoveUserFromGroup(Long groupId, Long userId, boolean isAssign) {

        // Get group from database and check existance
        ChatGroup existingChatGroup = getGroupById(groupId);

        // Fetch user from Database so we can validate like user existance with our system
        Optional<User> userFromDBOptional = userRepository.findById(userId);
        if(userFromDBOptional.isEmpty()){
            throw new EntityNotFoundException("User does not Register with us");
        }

        Set<User> existingMembers = existingChatGroup.getMembers();
        User userFromDB = userFromDBOptional.get();
//        boolean userExistsInGroup = existingMembers.stream().anyMatch(user -> user.getId().equals(userId));
        User userExistsInGroup = existingMembers.stream().filter(user -> user.getId().equals(userId))
                .findFirst().orElse(null);
        // if request for add or remove user from group then check user already exist in grp or not
        if (isAssign){
            if (userExistsInGroup == null) existingMembers.add(userFromDB);
            else throw new EntityNotFoundException("The user is already available in the "+existingChatGroup.getName()+" Group");
        } else {
            if (userExistsInGroup != null) existingMembers.remove(userExistsInGroup);
            else throw new EntityNotFoundException("The user with userid "+userId+" is not available in the "+ existingChatGroup.getName() +" Group.");
        }

        // add that new set of users in group
        existingChatGroup.setMembers(existingMembers);
        groupRepository.save(existingChatGroup);

        return generalUtil.convertObject(userFromDB, UserExpenseDTO.class);
    }
}
