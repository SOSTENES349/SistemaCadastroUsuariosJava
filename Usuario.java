public class Usuario extends Pessoa {
    private String cargo;
    private String login;
    private String senha;
    private PerfilUsuario perfil;

    public Usuario(String nomeCompleto, String cpf, String email, String cargo, String login, String senha, PerfilUsuario perfil) {
        super(nomeCompleto, cpf, email);
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public Usuario(String nomeCompleto, String cpf, String email) {
        this(nomeCompleto, cpf, email, "", "", "", PerfilUsuario.COLABORADOR);
    }

    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public PerfilUsuario getPerfil() {
        return perfil;
    }
    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }
    @Override
    public String getIdentificacao() {
        return "Usuário: " + nomeCompleto + " (" + perfil + ")";
    }
    @Override
    public String toString() {
        return "Nome: " + nomeCompleto + 
               "\nCPF: " + cpf + 
               "\nE-mail: " + email + 
               "\nCargo: " + cargo + 
               "\nLogin: " + login + 
               "\nPerfil: " + perfil + 
               "\n------------------------";
    }
}
