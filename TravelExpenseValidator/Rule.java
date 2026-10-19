package TravelExpenseValidator;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Rule {
    private final String category;
    private final String field;
    private final String constraint;
}
