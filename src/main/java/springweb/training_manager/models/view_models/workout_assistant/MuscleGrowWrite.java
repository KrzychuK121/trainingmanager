package springweb.training_manager.models.view_models.workout_assistant;

import lombok.Getter;
import lombok.Setter;
import springweb.training_manager.models.entities.BodyPart;

import java.util.Map;

@Getter
@Setter
public class MuscleGrowWrite {
    private Map<BodyPart, BodyPartWorkoutStatistics> bodyPartWorkoutStatistics;
}
