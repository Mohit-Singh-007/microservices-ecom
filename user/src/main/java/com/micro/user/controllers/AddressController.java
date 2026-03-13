package com.micro.user.controllers;

import com.micro.user.dto.addressDTO.AddressReq;
import com.micro.user.dto.addressDTO.AddressRes;
import com.micro.user.services.AddressServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/address")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AddressController {

    private final AddressServiceInterface addressService;

    @GetMapping
    public ResponseEntity<List<AddressRes>> getAllAddress(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(addressService.getAllAddresses(jwt));
    }

    @PostMapping
    public ResponseEntity<AddressRes> addAddress(@AuthenticationPrincipal Jwt jwt ,
                                                 @Valid @RequestBody AddressReq req){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.addAddress(jwt,req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressRes> updateAddress(@AuthenticationPrincipal Jwt jwt ,
                                                    @PathVariable Long id ,
                                                    @RequestBody AddressReq req){

        return ResponseEntity.ok(addressService.updateAddress(jwt,id,req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal Jwt jwt ,
                                              @PathVariable Long id){
        addressService.deleteAddress(jwt,id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<AddressRes> setDefaultAddress(@AuthenticationPrincipal Jwt jwt ,
                                                        @PathVariable Long id){
        return ResponseEntity.ok(addressService.setDefaultAddress(jwt,id));
    }

}


