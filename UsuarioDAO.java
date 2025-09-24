import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public UsuarioDAO() {
        criarTabela();
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "cpf TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "cargo TEXT," +
                "login TEXT NOT NULL UNIQUE," +
                "senha TEXT NOT NULL," +
                "perfil TEXT NOT NULL)";
        try (Connection conn = ConexaoSQLite.conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, cpf, email, cargo, login, senha, perfil) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLite.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNomeCompleto());
            pstmt.setString(2, usuario.getCpf());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getCargo());
            pstmt.setString(5, usuario.getLogin());
            pstmt.setString(6, usuario.getSenha());
            pstmt.setString(7, usuario.getPerfil().name());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT nome, cpf, email, cargo, login, senha, perfil FROM usuarios";
        try (Connection conn = ConexaoSQLite.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("email"),
                    rs.getString("cargo"),
                    rs.getString("login"),
                    rs.getString("senha"),
                    PerfilUsuario.valueOf(rs.getString("perfil"))
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT nome, cpf, email, cargo, login, senha, perfil FROM usuarios WHERE login = ? AND senha = ?";
        try (Connection conn = ConexaoSQLite.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, senha);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("email"),
                    rs.getString("cargo"),
                    rs.getString("login"),
                    rs.getString("senha"),
                    PerfilUsuario.valueOf(rs.getString("perfil"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean loginExiste(String login) {
        String sql = "SELECT 1 FROM usuarios WHERE login = ?";
        try (Connection conn = ConexaoSQLite.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}
