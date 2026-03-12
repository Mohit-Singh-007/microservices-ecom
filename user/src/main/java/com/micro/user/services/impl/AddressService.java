package com.micro.user.services.impl;

import com.micro.user.dto.addressDTO.AddressReq;
import com.micro.user.dto.addressDTO.AddressRes;
import com.micro.user.exceptions.AddressNotFoundException;
import com.micro.user.exceptions.AddressOwnershipException;
import com.micro.user.models.Address;
import com.micro.user.models.User;
import com.micro.user.repository.AddressRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.jose.jwk.JWK;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {


    private final UserService userService;
    private final AddressRepo addressRepo;


    public List<AddressRes> getAllAddresses(Jwt jwt){
        User user = userService.getOrCreateUser(jwt);

        return user.getAddressList().stream()
                .map(this::mapToAddressRes).toList();

    }

    public AddressRes addAddress(Jwt jwt,AddressReq req){
        // user hona chahiye to add
        User user = userService.getOrCreateUser(jwt);

        Address address = new Address();
        address.setUser(user);
        address.setStreet(req.getStreet());
        address.setCity(req.getCity());
        address.setState(req.getState());
        address.setPincode(req.getPincode());
        address.setCountry(req.getCountry());

        return mapToAddressRes(address);
    }

    @Transactional
    public void deleteAddress(Jwt jwt , Long addressId){
        User user = userService.getOrCreateUser(jwt);

        Address address = verifyAddressOwnership(addressId,user.getId());

        addressRepo.delete(address);
    }

    @Transactional
    public AddressRes setDefaultAddress(Jwt jwt , Long addressId){
        User user = userService.getOrCreateUser(jwt);

        Address address = verifyAddressOwnership(addressId, user.getId());
        addressRepo.clearDefaultByUserId(user.getId());
        address.setDefault(true);

        return mapToAddressRes(addressRepo.save(address));
    }


    @Transactional
    public AddressRes updateAddress(Jwt jwt , Long addressId,AddressReq req){
        User user = userService.getOrCreateUser(jwt);
        Address address = verifyAddressOwnership(addressId,user.getId());

        address.setStreet(req.getStreet());
        address.setCity(req.getCity());
        address.setState(req.getState());
        address.setCountry(req.getCountry());
        address.setPincode(req.getPincode());

        return mapToAddressRes(addressRepo.save(address));

    }

    private Address verifyAddressOwnership(Long addressId,Long userId){
       Address address =  addressRepo.findById(addressId)
               .orElseThrow(()-> new AddressNotFoundException(addressId));

       if(!address.getUser().getId().equals(userId)){
           throw new AddressOwnershipException(addressId);
       }
        return address;
    }

    private AddressRes mapToAddressRes(Address a) {
        AddressRes res = new AddressRes();
        res.setId(a.getId());
        res.setStreet(a.getStreet());
        res.setCity(a.getCity());
        res.setState(a.getState());
        res.setPincode(a.getPincode());
        res.setCountry(a.getCountry());
        res.setDefault(a.isDefault());
        res.setCreatedAt(a.getCreatedAt());
        return res;
    }
}
