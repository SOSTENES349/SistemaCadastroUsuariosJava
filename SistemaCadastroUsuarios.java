
import java.util.List;

public class SistemaCadastroUsuarios {
    private final UsuarioDAO usuarioDAO;

    public SistemaCadastroUsuarios() {
        usuarioDAO = new UsuarioDAO();
        // Adiciona usuário admin padrão se não existir
        if (!usuarioDAO.loginExiste("admin")) {
            Usuario admin = new Usuario(
                "Administrador Padrão",
                "123.456.789-00",
                "admin@empresa.com",
                "Administrador",
                "admin",
                "admin123",
                PerfilUsuario.ADMINISTRADOR
            );
            usuarioDAO.inserirUsuario(admin);
        }
    }

    public boolean cadastrarUsuario(String nomeCompleto, String cpf, String email, String cargo, String login, String senha, PerfilUsuario perfil) {
        if (!Validador.validarCPF(cpf)) return false;
        if (!Validador.validarEmail(email)) return false;
        if (!Validador.validarSenha(senha)) return false;
        if (usuarioDAO.loginExiste(login)) return false;
        Usuario novoUsuario = new Usuario(nomeCompleto, cpf, email, cargo, login, senha, perfil);
        return usuarioDAO.inserirUsuario(novoUsuario);
    }

    public String listarUsuarios() {
        List<Usuario> lista = usuarioDAO.listarUsuarios();
        if (lista.isEmpty()) return "Nenhum usuário cadastrado.";
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            texto.append("Usuário #").append(i + 1).append("\n");
            texto.append(lista.get(i).toString()).append("\n");
        }
        return texto.toString();
    }

    public String autenticarUsuario(String login, String senha) {
        Usuario usuarioAutenticado = usuarioDAO.autenticar(login, senha);
        if (usuarioAutenticado != null) {
            return "Autenticação bem-sucedida!\nBem-vindo, " + usuarioAutenticado.getNomeCompleto() +
                   "\nSeu perfil: " + usuarioAutenticado.getPerfil();
        } else {
            return "Falha na autenticação! Login ou senha incorretos.";
        }
    }

    // Método principal para teste simples
    public static void main(String[] args) {
        SistemaCadastroUsuarios sistema = new SistemaCadastroUsuarios();
        System.out.println(sistema.listarUsuarios());
    }
}