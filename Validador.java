import java.util.regex.Pattern;

public class Validador {
    public static boolean validarCPF(String cpf) {
    return cpf != null && cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }
    public static boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regex).matcher(email).matches();
    }
    public static boolean validarSenha(String senha) {
        return senha != null && senha.length() >= 6;
    }
}
