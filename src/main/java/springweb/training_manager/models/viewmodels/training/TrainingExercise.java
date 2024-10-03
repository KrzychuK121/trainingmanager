package springweb.training_manager.models.viewmodels.training;

import lombok.NoArgsConstructor;
import springweb.training_manager.models.entities.Training;
import springweb.training_manager.models.schemas.TrainingSchema;
import springweb.training_manager.models.viewmodels.Castable;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class TrainingExercise extends TrainingSchema implements Castable<Training> {
    public TrainingExercise(Training training) {
        super(
            training.getId(),
            training.getTitle(),
            training.getDescription()
        );
    }

    public static List<TrainingExercise> toTrainingExerciseList(final List<Training> list) {
        return list.stream().map(
            TrainingExercise::new
        ).collect(Collectors.toList());
    }

    public static List<Training> toTrainingList(final List<TrainingExercise> list) {
        return list.stream().map(
            TrainingExercise::toEntity
        ).collect(Collectors.toList());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Training toEntity() {
        var toReturn = new Training(
            title,
            description
        );
        toReturn.setId(id);

        return toReturn;
    }
}
