package com.micro.user.dto.userDTO;

import com.micro.user.dto.addressDTO.AddressRes;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserRes {

    private Long id;
    private String keycloakId;
    private String name;
    private String email;
    private String phone;
    private boolean isActive;
    private LocalDateTime createdAt;
    private List<AddressRes> addressRes;

}
