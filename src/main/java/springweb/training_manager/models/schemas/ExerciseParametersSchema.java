package springweb.training_manager.models.schemas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import springweb.training_manager.models.entities.Difficulty;

import java.time.LocalTime;

@MappedSuperclass
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExerciseParametersSchema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    // ile serii
    @Range(min = 1, max = 10, message = "Ilość serii musi mieścić się między 1 a 10")
    protected int rounds;
    // powtórzenia
    @Range(min = 0, max = 100, message = "Ilość powtórzeń musi mieścić się między 0 a 100")
    protected int repetition;
    @Column(columnDefinition = "SMALLINT")
    @Range(min = 0, max = 300, message = "Ilość obciążenia musi mieścić się między 0 a 300")
    protected short weights;
    @DateTimeFormat(pattern = "mm:ss")
    protected LocalTime time;
    @Enumerated(EnumType.STRING)
    protected Difficulty difficulty;
}
