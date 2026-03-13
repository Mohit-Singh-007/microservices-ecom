package com.micro.user.services;

import com.micro.user.dto.addressDTO.AddressReq;
import com.micro.user.dto.addressDTO.AddressRes;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface AddressServiceInterface {
    List<AddressRes> getAllAddresses(Jwt jwt);
    AddressRes addAddress(Jwt jwt, AddressReq req);
    void deleteAddress(Jwt jwt , Long addressId);
    AddressRes setDefaultAddress(Jwt jwt , Long addressId);
    AddressRes updateAddress(Jwt jwt , Long addressId,AddressReq req);
}
