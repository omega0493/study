package com.study.auth.service;

import com.study.auth.model.UserModel;
import com.study.common.exception.BusinessError;
import com.study.common.exception.BusinessException;
import com.study.entity.user.User;
import com.study.entity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    public UserModel login(UserModel model) {

        // model -> entity
        User user = model.toEntity();

        // 회원 정보 조회
        Optional<User> byUserName = userRepository.findByUserName(user.getUserName());

        if(byUserName.isEmpty()) {
            throw new BusinessException(BusinessError.NO_REGISTERED_USER);
        }

        // entity -> model
        return UserModel.fromEntity(byUserName.get());
    }

    @Transactional
    public UserModel join(UserModel model) {

        // model -> entity
        User user = model.toEntity();

        // 회원 존재 여부 확인
        boolean isUserJoined = userRepository.findByUserName(user.getUserName()).isPresent();

        // 중복 회원 존재
        if (isUserJoined) {
            throw new BusinessException(BusinessError.REGISTERED_USER);
        }

        userRepository.save(user);

        // entity -> model
        return UserModel.fromEntity(user);
    }
}
