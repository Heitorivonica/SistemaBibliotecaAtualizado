package project.controller.dto.Update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookRequest {
    private String name;
    private String gender;
    private String author;
    private Integer publicationYear;
}
