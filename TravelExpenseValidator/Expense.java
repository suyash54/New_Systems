package TravelExpenseValidator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Expense implements ValidationResult{
    private final Map<String,String> data;
    private boolean isValid = true;
    private String failureReason;

    public Expense(Map<String, String> row) {
        this.data = row;
    }

    public String get(String key){
        return data.get(key);
    }

    @Override
    public boolean isValid(){
        return this.isValid;
    }

    @Override
    public String getFailureReason(){
        return this.failureReason;
    }

    @Override
    public String getResultType(){
        return "Expense";
    }

    public void markInvalid(String reason){
        this.isValid = false;
        this.failureReason = reason;
    }

    @Override
    public String toString(){
        return "Expense{data= "+data+", valid= "+isValid+(failureReason!=null?", failureReason= "+failureReason:"")+ "}";
    }

}
