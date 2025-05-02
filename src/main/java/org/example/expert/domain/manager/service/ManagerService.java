package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.security.CustomUserDetails;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.log.entity.DomainType;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.entity.LogStatus;
import org.example.expert.domain.log.service.LogService;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    private final LogService logService;

    @Transactional
    public ManagerSaveResponse saveManager(CustomUserDetails userDetails, long todoId, ManagerSaveRequest managerSaveRequest) {

        // 일정을 만든 유저
        User user = User.fromAuthUser(userDetails.getUser());
        Optional<Todo> findOptTodo = todoRepository.findById(todoId);
        if (findOptTodo.isEmpty()) {
            Log saveLog = logService.failLogSave(new Log("saveManager", DomainType.MANAGER, LogStatus.FAIL));
            log.info("methodName: {}, domain: {}, 로깅 시각: {}", saveLog.getMethodName(), saveLog.getType(), saveLog.getCreateBy());
            throw new InvalidRequestException("Todo not found");
        }

        Todo todo = findOptTodo.get();

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            Log saveLog = logService.failLogSave(new Log("saveManager", DomainType.MANAGER, LogStatus.FAIL));
            log.info("methodName: {}, domain: {}, 로깅 시각: {}", saveLog.getMethodName(), saveLog.getType(), saveLog.getCreateBy());
            throw new InvalidRequestException("담당자를 등록하려고 하는 유저가 유효하지 않거나, 일정을 만든 유저가 아닙니다.");
        }

        Optional<User> findOptUser = userRepository.findById(managerSaveRequest.getManagerUserId());
        if (findOptUser.isEmpty()) {
            Log saveLog = logService.failLogSave(new Log("saveManager", DomainType.MANAGER, LogStatus.FAIL));
            log.info("methodName: {}, domain: {}, 로깅 시각: {}", saveLog.getMethodName(), saveLog.getType(), saveLog.getCreateBy());
            throw new InvalidRequestException("등록하려고 하는 담당자 유저가 존재하지 않습니다.");
        }

        User managerUser = findOptUser.get();

        if (ObjectUtils.nullSafeEquals(user.getId(), managerUser.getId())) {
            Log saveLog = logService.failLogSave(new Log("saveManager", DomainType.MANAGER, LogStatus.FAIL));
            log.info("methodName: {}, domain: {}, 로깅 시각: {}", saveLog.getMethodName(), saveLog.getType(), saveLog.getCreateBy());
            throw new InvalidRequestException("일정 작성자는 본인을 담당자로 등록할 수 없습니다.");
        }

        Manager newManagerUser = new Manager(managerUser, todo);
        Manager savedManagerUser = managerRepository.save(newManagerUser);

        Log saveLog = logService.successLogSave(new Log("saveManager", DomainType.MANAGER, LogStatus.SUCCESS));
        log.info("methodName: {}, domain: {}, 로깅 시각: {}", saveLog.getMethodName(), saveLog.getType(), saveLog.getCreateBy());

        return new ManagerSaveResponse(
                savedManagerUser.getId(),
                new UserResponse(managerUser.getId(), managerUser.getEmail(), managerUser.getNickname())
        );
    }

    public List<ManagerResponse> getManagers(long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        List<Manager> managerList = managerRepository.findByTodoIdWithUser(todo.getId());

        List<ManagerResponse> dtoList = new ArrayList<>();
        for (Manager manager : managerList) {
            User user = manager.getUser();
            dtoList.add(new ManagerResponse(
                    manager.getId(),
                    new UserResponse(user.getId(), user.getEmail(), user.getNickname())
            ));
        }
        return dtoList;
    }

    @Transactional
    public void deleteManager(CustomUserDetails userDetails, long todoId, long managerId) {
        User user = User.fromAuthUser(userDetails.getUser());

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new InvalidRequestException("해당 일정을 만든 유저가 유효하지 않습니다.");
        }

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new InvalidRequestException("Manager not found"));

        if (!ObjectUtils.nullSafeEquals(todo.getId(), manager.getTodo().getId())) {
            throw new InvalidRequestException("해당 일정에 등록된 담당자가 아닙니다.");
        }

        managerRepository.delete(manager);
    }
}
