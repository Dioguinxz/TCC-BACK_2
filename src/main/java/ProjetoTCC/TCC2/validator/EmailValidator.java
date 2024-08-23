package ProjetoTCC.TCC2.validator;

import java.util.regex.Pattern;

public class EmailValidator {
    public static boolean isValid(String email) {
        String emailRegex = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,63}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) return false;
        return pat.matcher(email).matches();
    }
}
