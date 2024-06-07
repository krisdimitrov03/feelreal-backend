package com.feelreal.api.service.wellnessChecks;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.common.ResultStatus;
import com.feelreal.api.dto.wellnesschecks.WellnessCheckRequest;
import com.feelreal.api.dto.wellnesschecks.WellnessCheckResponse;
import com.feelreal.api.model.User;
import com.feelreal.api.model.WellnessCheck;
import com.feelreal.api.repository.WellnessCheckRepository;
import com.feelreal.api.service.authentication.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    public OperationResult<List<WellnessCheckResponse>> getForUser(UUID userId) {
        Optional<User> user = userService.getById(userId);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        List<WellnessCheckResponse> checks = wellnessCheckRepository
                .findAllByUserId(userId)
                .stream()
                .map(check -> new WellnessCheckResponse(
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
    public OperationResult<WellnessCheckResponse> getById(UUID id, UUID userId) {
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

        WellnessCheckResponse response = new WellnessCheckResponse(
                check.get().getId(),
                check.get().getType().ordinal(),
                check.get().getValue(),
                check.get().getDate().toString(),
                check.get().getUser().getId()
        );

        return new OperationResult<>(ResultStatus.SUCCESS, response);
    }

    @Override
    public OperationResult<UUID> create(WellnessCheckRequest data, UUID userId) {
        Optional<User> user = userService.getById(userId);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        try {

            WellnessCheck wellnessCheck = createWellnessCheck(data, user.get());
            wellnessCheckRepository.saveAndFlush(wellnessCheck);

            return new OperationResult<>(ResultStatus.SUCCESS, wellnessCheck.getId());

        } catch (DateTimeParseException e) {
            return new OperationResult<>(ResultStatus.INVALID_INPUT, null);
        } catch (Exception e) {
            return new OperationResult<>(ResultStatus.INTERNAL_ERROR, null);
        }
    }

    @Override
    public OperationResult<UUID> update(UUID id, WellnessCheckRequest data, UUID userId) {
        Optional<User> user = userService.getById(userId);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        Optional<WellnessCheck> checkFromDb = wellnessCheckRepository.findById(id);

        if (checkFromDb.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        try {

            WellnessCheck check = updateWellnessCheck(checkFromDb.get(), data);
            wellnessCheckRepository.saveAndFlush(check);

            return new OperationResult<>(ResultStatus.SUCCESS, check.getId());

        } catch (DateTimeParseException e) {
            return new OperationResult<>(ResultStatus.INVALID_INPUT, null);
        } catch (Exception e) {
            return new OperationResult<>(ResultStatus.INTERNAL_ERROR, null);
        }
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

    private WellnessCheck createWellnessCheck(WellnessCheckRequest data, User user) {
        return new WellnessCheck(user, data.getType(), data.getValue(), LocalDate.parse(data.getDate()));
    }

    private WellnessCheck updateWellnessCheck(WellnessCheck check, WellnessCheckRequest data) {
        check.setType(data.getType());
        check.setValue(data.getValue());
        check.setDate(LocalDate.parse(data.getDate()));

        return check;
    }
}

