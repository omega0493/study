package com.study.auth.service;

import com.study.auth.model.UserModel;
import com.study.common.exception.BusinessError;
import com.study.common.exception.BusinessException;
import com.study.entity.user.User;
import com.study.entity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    public UserModel login(UserModel model) {

        // model -> entity
        User user = model.toEntity();

        // user information
        Optional<User> byUserName = userRepository.findByUserName(user.getUserName());

        if(byUserName.isEmpty()) {
            throw new BusinessException(BusinessError.NO_REGISTERED_USER);
        }

        // entity -> model
        return UserModel.fromEntity(byUserName.get());
    }
}
