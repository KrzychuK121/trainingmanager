package springweb.trainingmanager.models.viewmodels.user;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;
import springweb.trainingmanager.models.entities.User;
import springweb.trainingmanager.models.schemas.UserSchema;

public class UserWrite extends UserSchema {
    @Transient
    @NotBlank(message = "Powtórz hasło.")
    private String passwordRepeat;
    @Transient
    private final PasswordEncoder encoder;

    public UserWrite(final PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public User toUser(){
        User toReturn = new User();

        toReturn.setUsername(username);
        toReturn.setFirstName(firstName);
        toReturn.setLastName(lastName);
        toReturn.setPasswordHashed(encoder.encode(password));

        return toReturn;
    }
}
