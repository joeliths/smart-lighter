package com.gunnarsson.smartlighter.ui.controller;

import com.gunnarsson.smartlighter.service.UserService;
import com.gunnarsson.smartlighter.shared.dto.UserDto;
import com.gunnarsson.smartlighter.ui.model.UserRequestModel;
import com.gunnarsson.smartlighter.ui.model.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;


    @PostMapping
    public UserResponseModel createUser(@RequestBody UserRequestModel userRequestModel){
        UserDto userDto = new ModelMapper().map(userRequestModel,UserDto.class);
        UserDto createdUser = userService.createUser(userDto);
        return new ModelMapper().map(createdUser,UserResponseModel.class);
    }
}
