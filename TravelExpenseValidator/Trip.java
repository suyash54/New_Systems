package TravelExpenseValidator;

import jdk.jshell.ExpressionSnippet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Trip implements ValidationResult{
    private final Map<String,String> metaData;
    private final List<Expense> expenseList;
    private boolean isValid = true;
    private String failureReason;

    public Trip(Map<String, String> tripMetaData, List<Expense> expenseList) {
        this.metaData = tripMetaData;
        this.expenseList = expenseList;
    }

    public String get(String key){
        return metaData.get(key);
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
        return "Trip";
    }

    public void markInvalid(String reason){
        this.isValid = false;
        this.failureReason = reason;
    }

    @Override
    public String toString(){
        return "Expense{metaData= "+this.metaData+", expenses= " +this.expenseList+ ", valid= "+isValid+(failureReason!=null?", failureReason= "+failureReason:"")+ "}";

    }


    public List<Expense> getExpenses() {
       return  this.getExpenseList();
    }
}
