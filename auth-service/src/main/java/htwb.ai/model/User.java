package htwb.ai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "appUser")
public class User {

    @Id
    @JsonProperty("userId")
    private String id;
    private String firstname;
    private String lastname;
    private String password;
}