package springweb.training_manager.models.view_models.training_routine;

import lombok.Getter;
import springweb.training_manager.models.entities.TrainingRoutine;
import springweb.training_manager.models.schemas.TrainingRoutineSchema;
import springweb.training_manager.models.view_models.user.UserRead;

@Getter
public class TrainingRoutineRead extends TrainingRoutineSchema {
    private final UserRead owner;

    public TrainingRoutineRead(TrainingRoutine trainingRoutine) {
        super(
            trainingRoutine.getId(),
            trainingRoutine.isActive()
        );
        this.owner = new UserRead(trainingRoutine.getOwner());
    }

}
