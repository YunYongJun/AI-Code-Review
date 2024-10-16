import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "codes")  // MongoDB의 컬렉션 이름
@Data
public class Code {

    @Id
    private String id;
    private String name;
    private String email;

}
