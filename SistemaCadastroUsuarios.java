import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

// Enum para os perfis de usuário
enum PerfilUsuario {
    ADMINISTRADOR,
    GERENTE,
    COLABORADOR
}

// Classe base Pessoa (para demonstrar herança)
class Pessoa {
    protected String nomeCompleto;
    protected String cpf;
    protected String email;
    
    public Pessoa(String nomeCompleto, String cpf, String email) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.email = email;
    }
    
    // Método que será sobrescrito (para demonstrar polimorfismo)
    public String getIdentificacao() {
        return "Pessoa: " + nomeCompleto;
    }
    
    // Getters e Setters
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

// Classe que representa um usuário (herda de Pessoa)
class Usuario extends Pessoa {
    private String cargo;
    private String login;
    private String senha;
    private PerfilUsuario perfil;
    
    // Construtor parametrizado
    public Usuario(String nomeCompleto, String cpf, String email, String cargo, 
                  String login, String senha, PerfilUsuario perfil) {
        super(nomeCompleto, cpf, email); // Chamada ao construtor da classe pai
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }
    
    // Sobrescrita de método (polimorfismo)
    @Override
    public String getIdentificacao() {
        return "Usuário: " + nomeCompleto + " (" + perfil + ")";
    }
    
    // Sobrecarga de método (overload) - versão simplificada do construtor
    public Usuario(String nomeCompleto, String cpf, String email) {
        this(nomeCompleto, cpf, email, "", "", "", PerfilUsuario.COLABORADOR);
    }
    
    // Getters e Setters
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

// Classe para validação de dados
class Validador {
    // Método para validar CPF (formato básico)
    public static boolean validarCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }
    
    // Método para validar e-mail
    public static boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regex).matcher(email).matches();
    }
    
    // Método para validar força da senha
    public static boolean validarSenha(String senha) {
        return senha != null && senha.length() >= 6;
    }
    
    // Método para verificar se login já existe (sobrecarga)
    public static boolean validarLogin(String login, List<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equals(login)) {
                return false; // Login já existe
            }
        }
        return true; // Login disponível
    }
}

// Classe para gerenciar a autenticação de usuários
class Autenticacao {
    // Método para autenticar usuário
    public static Usuario autenticar(String login, String senha, List<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equals(login) && usuario.getSenha().equals(senha)) {
                return usuario;
            }
        }
        return null; // Autenticação falhou
    }
}

// Classe principal do sistema
public class SistemaCadastroUsuarios {
    private List<Usuario> usuarios;
    private Scanner scanner;
    
    public SistemaCadastroUsuarios() {
        usuarios = new ArrayList<>(); // Uso de coleção (ArrayList)
        scanner = new Scanner(System.in);
        
        // Adicionar um usuário administrador padrão
        usuarios.add(new Usuario(
            "Administrador Padrão", 
            "123.456.789-00", 
            "admin@empresa.com", 
            "Administrador", 
            "admin", 
            "admin123", 
            PerfilUsuario.ADMINISTRADOR
        ));
    }
    
    // Método para cadastrar um novo usuário
    public void cadastrarUsuario() {
        System.out.println("\n=== CADASTRO DE NOVO USUÁRIO ===");
        
        // Nome completo
        System.out.print("Nome completo: ");
        String nomeCompleto = scanner.nextLine();
        
        // CPF
        String cpf;
        do {
            System.out.print("CPF (formato: 000.000.000-00): ");
            cpf = scanner.nextLine();
            if (!Validador.validarCPF(cpf)) {
                System.out.println("CPF inválido! Use o formato: 000.000.000-00");
            }
        } while (!Validador.validarCPF(cpf));
        
        // E-mail
        String email;
        do {
            System.out.print("E-mail: ");
            email = scanner.nextLine();
            if (!Validador.validarEmail(email)) {
                System.out.println("E-mail inválido!");
            }
        } while (!Validador.validarEmail(email));
        
        // Cargo
        System.out.print("Cargo: ");
        String cargo = scanner.nextLine();
        
        // Login
        String login;
        do {
            System.out.print("Login: ");
            login = scanner.nextLine();
            if (!Validador.validarLogin(login, usuarios)) {
                System.out.println("Login já existe! Escolha outro.");
            }
        } while (!Validador.validarLogin(login, usuarios));
        
        // Senha
        String senha;
        do {
            System.out.print("Senha (mínimo 6 caracteres): ");
            senha = scanner.nextLine();
            if (!Validador.validarSenha(senha)) {
                System.out.println("Senha muito curta! Mínimo 6 caracteres.");
            }
        } while (!Validador.validarSenha(senha));
        
        // Perfil
        PerfilUsuario perfil = selecionarPerfil();
        
        // Criar e adicionar o usuário
        Usuario novoUsuario = new Usuario(nomeCompleto, cpf, email, cargo, login, senha, perfil);
        usuarios.add(novoUsuario);
        
        System.out.println("\nUsuário cadastrado com sucesso!");
        
        // Demonstrando polimorfismo
        System.out.println("Identificação: " + novoUsuario.getIdentificacao());
    }
    
    // Método para selecionar o perfil do usuário
    private PerfilUsuario selecionarPerfil() {
        System.out.println("\nSelecione o perfil:");
        System.out.println("1 - Administrador");
        System.out.println("2 - Gerente");
        System.out.println("3 - Colaborador");
        
        int opcao;
        do {
            System.out.print("Opção (1-3): ");
            
            // Tratamento de erro para entrada inválida
            try {
                opcao = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Erro: Digite um número válido!");
                opcao = 0;
            } finally {
                scanner.nextLine(); // Limpar o buffer em qualquer caso
            }
            
        } while (opcao < 1 || opcao > 3);
        
        switch (opcao) {
            case 1: return PerfilUsuario.ADMINISTRADOR;
            case 2: return PerfilUsuario.GERENTE;
            case 3: return PerfilUsuario.COLABORADOR;
            default: return PerfilUsuario.COLABORADOR;
        }
    }
    
    // Método para listar todos os usuários
    public void listarUsuarios() {
        System.out.println("\n=== LISTA DE USUÁRIOS CADASTRADOS ===");
        
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println("Usuário #" + (i + 1));
            System.out.println(usuarios.get(i));
        }
    }
    
    // Método para autenticar usuário
    public void autenticarUsuario() {
        System.out.println("\n=== AUTENTICAÇÃO ===");
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        Usuario usuarioAutenticado = Autenticacao.autenticar(login, senha, usuarios);
        
        if (usuarioAutenticado != null) {
            System.out.println("Autenticação bem-sucedida!");
            System.out.println("Bem-vindo, " + usuarioAutenticado.getNomeCompleto());
            System.out.println("Seu perfil: " + usuarioAutenticado.getPerfil());
        } else {
            System.out.println("Falha na autenticação! Login ou senha incorretos.");
        }
    }
    
    // Método para demonstrar herança e polimorfismo
    public void demonstrarHerancaPolimorfismo() {
        System.out.println("\n=== DEMONSTRAÇÃO DE HERANÇA E POLIMORFISMO ===");
        
        // Criando objetos das diferentes classes
        Pessoa pessoa = new Pessoa("João Silva", "111.222.333-44", "joao@email.com");
        Usuario usuario = new Usuario("Maria Santos", "555.666.777-88", "maria@empresa.com", 
                                    "Analista", "maria", "senha123", PerfilUsuario.GERENTE);
        
        // Demonstrando polimorfismo - mesmo método, comportamentos diferentes
        System.out.println(pessoa.getIdentificacao());
        System.out.println(usuario.getIdentificacao());
        
        // Usando a classe base para referenciar objeto derivado
        Pessoa pessoaComoUsuario = usuario;
        System.out.println("Polimorfismo: " + pessoaComoUsuario.getIdentificacao());
    }
    
    // Método para exibir o menu principal
    public void exibirMenu() {
        int opcao;
        
        do {
            System.out.println("\n=== SISTEMA DE CADASTRO DE USUÁRIOS ===");
            System.out.println("1 - Cadastrar novo usuário");
            System.out.println("2 - Listar todos os usuários");
            System.out.println("3 - Autenticar usuário");
            System.out.println("4 - Demonstrar herança e polimorfismo");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");
            
            // Tratamento de erro para entrada inválida
            try {
                opcao = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Erro: Digite um número válido!");
                opcao = 0;
            } finally {
                scanner.nextLine(); // Limpar o buffer em qualquer caso
            }
            
            switch (opcao) {
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    listarUsuarios();
                    break;
                case 3:
                    autenticarUsuario();
                    break;
                case 4:
                    demonstrarHerancaPolimorfismo();
                    break;
                case 5:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 5);
    }
    
    // Método principal
    public static void main(String[] args) {
        SistemaCadastroUsuarios sistema = new SistemaCadastroUsuarios();
        sistema.exibirMenu();
    }
}