package com.study.api.auth.service;

import com.study.api.auth.dto.LoginResponseDto;
import com.study.api.auth.model.UserModel;
import com.study.api.auth.repository.UserRepository;
import com.study.entity.user.User;
import com.study.infra.aop.UserIdAware;
import com.study.infra.common.exception.BusinessError;
import com.study.infra.common.exception.BusinessException;
import com.study.infra.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtProvider jwtProvider;

    public LoginResponseDto login(UserModel model) {

        // model -> entity
        User user = model.toEntity();

        // 회원 정보 조회
        Optional<User> byUserName = userRepository.findByUserName(user.getUserName());

        if (byUserName.isEmpty()) {
            throw new BusinessException(BusinessError.NO_REGISTERED_USER);
        }

        if (!encoder.matches(model.getUserPassword(), byUserName.get().getUserPassword())) {
            throw new BusinessException(BusinessError.NO_REGISTERED_USER);
        }

        String accessToken = jwtProvider.generateAccessToken(byUserName.get().getId());

        String refreshToken = jwtProvider.generateRefreshToken(byUserName.get().getId());

        // entity -> model
        UserModel userModel = UserModel.fromEntity(byUserName.get());

        return LoginResponseDto.fromModel(userModel, accessToken, refreshToken);
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

        user.setUserPassword(encoder.encode(user.getUserPassword()));

        user = userRepository.save(user);

        // entity -> model
        return UserModel.fromEntity(user);
    }

    public UserModel getUserById(Long id) {

        // 회원 정보 조회
        Optional<User> byUserName = userRepository.findById(id);

        // entity -> model
        return byUserName.map(UserModel::fromEntity).orElse(null);
    }
}
