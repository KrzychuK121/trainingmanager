package springweb.training_manager.controllers.apis;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springweb.training_manager.exceptions.NotOwnedByUserException;
import springweb.training_manager.models.entities.Training;
import springweb.training_manager.models.schemas.RoleSchema;
import springweb.training_manager.models.viewmodels.training.TrainingCreate;
import springweb.training_manager.models.viewmodels.training.TrainingRead;
import springweb.training_manager.models.viewmodels.training.TrainingWriteAPI;
import springweb.training_manager.services.TrainingService;
import springweb.training_manager.services.UserService;

import java.net.URI;
import java.util.List;

@RestController
@Secured({RoleSchema.ROLE_ADMIN, RoleSchema.ROLE_USER})
@RequestMapping(
    value = "/api/training",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
@Slf4j
public class TrainingControllerAPI {
    private final TrainingService service;

    @GetMapping("/paged")
    @ResponseBody
    ResponseEntity<Page<TrainingRead>> getAll(
        @PageableDefault(size = 2) Pageable page,
        Authentication auth
    ) {
        var user = UserService.getUserByAuth(auth);
        return ResponseEntity.ok(
            service.getPagedForUser(
                page,
                user
            )
        );
    }

    @GetMapping("/all")
    @ResponseBody
    ResponseEntity<List<TrainingRead>> getAll(
        Authentication auth
    ) {
        var user = UserService.getUserByAuth(auth);
        if (UserService.userIsInRole(user, RoleSchema.ROLE_ADMIN))
            return ResponseEntity.ok(service.getAll());
        return ResponseEntity.ok(service.getPublicOrOwnerBy(user));
    }

    @GetMapping("/{id}")
    @ResponseBody
    ResponseEntity<TrainingRead> getById(
        @PathVariable int id,
        Authentication auth
    ) {
        Training found = null;
        try {
            found = service.getById(id, UserService.getUserIdByAuth(auth));
        } catch (IllegalArgumentException e) {
            log.error("Wystąpił wyjątek: {}", e.getMessage());
            if (e.getMessage()
                .contains("Nie masz dostępu"))
                return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
            return ResponseEntity.notFound()
                .build();
        }
        return ResponseEntity.ok(new TrainingRead(found));
    }

    @GetMapping(value = {"/createModel", "/createModel/{id}"})
    public ResponseEntity<TrainingCreate> getCreateModel(
        @PathVariable(required = false) Integer id,
        Authentication auth
    ) {
        var loggedUser = UserService.getUserByAuth(auth);
        return ResponseEntity.ok(service.getCreateModel(id, loggedUser));
    }

    @PostMapping()
    @ResponseBody
    ResponseEntity<?> create(
        @RequestBody @Valid TrainingWriteAPI toCreate,
        BindingResult result,
        Authentication auth
    ) {
        var validationErrors = service.validateAndPrepareTraining(toCreate, result);
        if (validationErrors != null)
            return ResponseEntity.badRequest()
                .body(validationErrors);

        var userId = UserService.getUserIdByAuth(auth);
        Training created = service.create(toCreate.getToSave(), userId);
        var trainingRead = new TrainingRead(created);
        return ResponseEntity.created(
                URI.create("/api/training/" + created.getId())
            )
            .body(trainingRead);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> edit(
        @RequestBody @Valid TrainingWriteAPI data,
        BindingResult result,
        @PathVariable int id,
        Authentication auth
    ) {
        var userId = UserService.getUserIdByAuth(auth);
        var validationErrors = service.validateAndPrepareTraining(data, result);
        if (validationErrors != null)
            return ResponseEntity.badRequest()
                .body(validationErrors);
        try {
            var toEdit = data.getToSave();
            service.edit(toEdit, id, userId);
        } catch (IllegalArgumentException e) {
            log.error("Wystąpił wyjątek: " + e.getMessage());
            return ResponseEntity.notFound()
                .build();
        }

        return ResponseEntity.noContent()
            .build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(
        @PathVariable int id,
        Authentication auth
    ) {
        var userId = UserService.getUserIdByAuth(auth);
        try {
            service.delete(id, userId);
        } catch (NotOwnedByUserException e) {
            return ResponseEntity.badRequest()
                .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                .build();
        } catch (Exception ex) {
            log.error("Wystąpił nieoczekiwany wyjątek: {}", ex.getMessage(), ex);
        }

        return ResponseEntity.noContent()
            .build();
    }
}
