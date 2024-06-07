package com.feelreal.api.service.wellnessChecks;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.common.ResultStatus;
import com.feelreal.api.dto.wellnesschecks.WellnessCheckDetailsResponse;
import com.feelreal.api.model.User;
import com.feelreal.api.model.WellnessCheck;
import com.feelreal.api.model.enumeration.WellnessCheckType;
import com.feelreal.api.repository.UserRepository;
import com.feelreal.api.repository.WellnessCheckRepository;
import com.feelreal.api.service.authentication.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WellnessCheckServiceImpl implements WellnessCheckService {

    private final WellnessCheckRepository wellnessCheckRepository;

    private final UserService userService;

    @Autowired
    public WellnessCheckServiceImpl(
            WellnessCheckRepository wellnessCheckRepository,
            UserService userService) {
        this.wellnessCheckRepository = wellnessCheckRepository;
        this.userService = userService;
    }

    @Override
    public OperationResult<List<WellnessCheckDetailsResponse>> getForUser(UUID userId) {
        Optional<User> user = userService.getById(userId);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        List<WellnessCheckDetailsResponse> checks = wellnessCheckRepository
                .findAllByUserId(userId)
                .stream()
                .map(check -> new WellnessCheckDetailsResponse(
                        check.getId(),
                        check.getType().ordinal(),
                        check.getValue(),
                        check.getDate().toString(),
                        check.getUser().getId()
                ))
                .toList();

        return new OperationResult<>(ResultStatus.SUCCESS, checks);
    }

    @Override
    public OperationResult<WellnessCheckDetailsResponse> getById(UUID id, UUID userId) {
        Optional<User> user = userService.getById(userId);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        Optional<WellnessCheck> check = wellnessCheckRepository.findById(id);

        if (check.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        if (check.get().getUser().equals(user.get())) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        WellnessCheckDetailsResponse response = new WellnessCheckDetailsResponse(
                check.get().getId(),
                check.get().getType().ordinal(),
                check.get().getValue(),
                check.get().getDate().toString(),
                check.get().getUser().getId()
        );

        return new OperationResult<>(ResultStatus.SUCCESS, response);
    }

    @Override
    public OperationResult<UUID> create(WellnessCheckDetailsResponse data, UUID userId) {
        Optional<User> user = userService.getById(userId);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        WellnessCheck wellnessCheck = createWellnessCheck(data, user.get());

        wellnessCheckRepository.saveAndFlush(wellnessCheck);

        return new OperationResult<>(ResultStatus.SUCCESS, wellnessCheck.getId());
    }

    @Override
    public OperationResult<UUID> update(UUID id, WellnessCheckDetailsResponse data, UUID userId) {
        Optional<User> user = userService.getById(userId);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        Optional<WellnessCheck> checkFromDb = wellnessCheckRepository.findById(id);

        if (checkFromDb.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        WellnessCheck check = updateCheck(checkFromDb.get(), data);
        wellnessCheckRepository.saveAndFlush(check);

        return new OperationResult<>(ResultStatus.SUCCESS, check.getId());
    }

    @Override
    public OperationResult<UUID> delete(UUID id, UUID userId) {
        Optional<User> user = userService.getById(userId);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        Optional<WellnessCheck> check = wellnessCheckRepository.findById(id);

        if (check.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        if (!check.get().getUser().equals(user.get())) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        wellnessCheckRepository.deleteById(id);
        wellnessCheckRepository.flush();

        return new OperationResult<>(ResultStatus.SUCCESS, id);
    }

    private WellnessCheck createWellnessCheck(WellnessCheckDetailsResponse data, User user) {
        WellnessCheck wellnessCheck = new WellnessCheck();
        wellnessCheck.setUser(user);
        wellnessCheck.setType(WellnessCheckType.fromOrdinal(data.getType()));
        // TODO: Fix this
//        wellnessCheck.setValue(data.getValue());
//        wellnessCheck.setDate(LocalDate.parse(data.getDate()));
        return wellnessCheck;
    }

    private WellnessCheck updateCheck(WellnessCheck check, WellnessCheckDetailsResponse data) {
        return check;
    }
}

