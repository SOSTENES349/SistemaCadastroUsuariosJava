public class Pessoa {
    protected String nomeCompleto;
    protected String cpf;
    protected String email;

    public Pessoa(String nomeCompleto, String cpf, String email) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.email = email;
    }

    public String getIdentificacao() {
        return "Pessoa: " + nomeCompleto;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
