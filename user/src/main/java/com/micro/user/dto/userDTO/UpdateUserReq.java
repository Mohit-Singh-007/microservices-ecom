package com.micro.user.dto.userDTO;

import lombok.Data;

@Data
public class UpdateUserReq {

    private String name;
    private String phone;
    private String profileImageURL;

}
