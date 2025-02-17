package springweb.training_manager.models.view_models.workout_assistant;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import springweb.training_manager.models.entities.BodyPart;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@ToString
public class WorkoutAssistantWrite {
    private MuscleGrowWrite muscleGrow;
    private WeightReductionWrite weightReduction;
    @Range(min = 1, max = 7, message = "Liczba dni treningowych musi być w przedziale 1-7")
    private int workoutDays;
    @NotNull
    private LocalTime earliestTrainingStart;
    @NotNull
    private LocalTime latestTrainingStart;
    private List<BodyPart> bodyParts = List.of();

    public WorkoutType getWorkoutType() {
        return getMuscleGrow() != null
            ? WorkoutType.MUSCLE_GROW
            : WorkoutType.WEIGHT_REDUCTION;
    }
}