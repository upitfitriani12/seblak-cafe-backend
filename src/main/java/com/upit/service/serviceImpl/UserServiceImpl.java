package com.upit.service.serviceImpl;

import com.upit.constants.CafeConstants;
import com.upit.model.User;
import com.upit.repository.UserRepository;
import com.upit.service.UserService;
import com.upit.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> request) {
        log.info("Inside signup {}", request);
        try {
            if (validateSignUp(request)){
                User user = userRepository.findByEmailId(request.get("email"));
                if(Objects.isNull(user)){
                    userRepository.save(getUserFrom(request));
                    return CafeUtils.getResponseEntity("Pendaftaran Akun Berhasil.", HttpStatus.OK);
                }else {
                    return CafeUtils.getResponseEntity("Email Sudah Ada.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUp(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password"))
        {
            return true;
        }
        return false;
    }

    private User getUserFrom(Map<String, String> request){
        User user = new User();
        user.setName(request.get("name"));
        user.setContactNumber(request.get("contactNumber"));
        user.setEmail(request.get("email"));
        user.setPassword(request.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

}
