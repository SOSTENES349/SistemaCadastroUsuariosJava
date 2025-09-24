import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;

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
class SistemaCadastroUsuarios {
    private List<Usuario> usuarios;
    
    public SistemaCadastroUsuarios() {
        usuarios = new ArrayList<>(); // Uso de coleção (ArrayList)
        
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
    public boolean cadastrarUsuario(String nomeCompleto, String cpf, String email, 
                                  String cargo, String login, String senha, PerfilUsuario perfil) {
        
        if (!Validador.validarCPF(cpf)) {
            return false;
        }
        
        if (!Validador.validarEmail(email)) {
            return false;
        }
        
        if (!Validador.validarSenha(senha)) {
            return false;
        }
        
        if (!Validador.validarLogin(login, usuarios)) {
            return false;
        }
        
        // Criar e adicionar o usuário
        Usuario novoUsuario = new Usuario(nomeCompleto, cpf, email, cargo, login, senha, perfil);
        usuarios.add(novoUsuario);
        
        return true;
    }
    
    // Método para listar todos os usuários
    public String listarUsuarios() {
        if (usuarios.isEmpty()) {
            return "Nenhum usuário cadastrado.";
        }
        
        StringBuilder lista = new StringBuilder();
        for (int i = 0; i < usuarios.size(); i++) {
            lista.append("Usuário #").append(i + 1).append("\n");
            lista.append(usuarios.get(i).toString()).append("\n");
        }
        
        return lista.toString();
    }
    
    // Método para autenticar usuário
    public String autenticarUsuario(String login, String senha) {
        Usuario usuarioAutenticado = Autenticacao.autenticar(login, senha, usuarios);
        
        if (usuarioAutenticado != null) {
            return "Autenticação bem-sucedida!\nBem-vindo, " + usuarioAutenticado.getNomeCompleto() +
                   "\nSeu perfil: " + usuarioAutenticado.getPerfil();
        } else {
            return "Falha na autenticação! Login ou senha incorretos.";
        }
    }
    
    // Método para obter a lista de usuários (para uso interno)
    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}

// Interface gráfica
public class InterfaceGrafica extends JFrame {
    private final SistemaCadastroUsuarios sistema;
    private JTextArea areaTexto;

    public InterfaceGrafica() {
            sistema = new SistemaCadastroUsuarios();
        configurarJanela();
    }

    private void configurarJanela() {
        setTitle("Sistema de Cadastro de Usuários");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Painel principal
        JPanel painel = new JPanel(new BorderLayout());
        
        // Área de texto para exibir resultados
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaTexto);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new GridLayout(2, 2, 5, 5));
        
        JButton btnCadastrar = new JButton("Cadastrar Usuário");
        JButton btnListar = new JButton("Listar Usuários");
        JButton btnAutenticar = new JButton("Autenticar");
        JButton btnSair = new JButton("Sair");
        
        // Adicionar ações aos botões
    btnCadastrar.addActionListener(_ -> cadastrarUsuario());
    btnListar.addActionListener(_ -> listarUsuarios());
    btnAutenticar.addActionListener(_ -> autenticarUsuario());
    btnSair.addActionListener(_ -> System.exit(0));
        
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnAutenticar);
        painelBotoes.add(btnSair);
        
        painel.add(painelBotoes, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        
        add(painel);
        setVisible(true);
    }

    private void cadastrarUsuario() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        
        JTextField nomeField = new JTextField();
        JTextField cpfField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField cargoField = new JTextField();
        JTextField loginField = new JTextField();
        JPasswordField senhaField = new JPasswordField();
        
        JComboBox<PerfilUsuario> perfilCombo = new JComboBox<>(PerfilUsuario.values());
        
        panel.add(new JLabel("Nome completo:"));
        panel.add(nomeField);
        panel.add(new JLabel("CPF (000.000.000-00):"));
        panel.add(cpfField);
        panel.add(new JLabel("E-mail:"));
        panel.add(emailField);
        panel.add(new JLabel("Cargo:"));
        panel.add(cargoField);
        panel.add(new JLabel("Login:"));
        panel.add(loginField);
        panel.add(new JLabel("Senha:"));
        panel.add(senhaField);
        panel.add(new JLabel("Perfil:"));
        panel.add(perfilCombo);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Cadastrar Usuário", 
                                                 JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            boolean sucesso = sistema.cadastrarUsuario(
                nomeField.getText(),
                cpfField.getText(),
                emailField.getText(),
                cargoField.getText(),
                loginField.getText(),
                new String(senhaField.getPassword()),
                (PerfilUsuario) perfilCombo.getSelectedItem()
            );
            
            if (sucesso) {
                areaTexto.append("Usuário cadastrado com sucesso!\n");
            } else {
                areaTexto.append("Erro ao cadastrar usuário. Verifique os dados.\n");
            }
        }
    }

    private void listarUsuarios() {
        String lista = sistema.listarUsuarios();
        areaTexto.setText("=== LISTA DE USUÁRIOS ===\n\n" + lista);
    }

    private void autenticarUsuario() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        JTextField loginField = new JTextField();
        JPasswordField senhaField = new JPasswordField();
        
        panel.add(new JLabel("Login:"));
        panel.add(loginField);
        panel.add(new JLabel("Senha:"));
        panel.add(senhaField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Autenticar Usuário", 
                                                 JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String resultado = sistema.autenticarUsuario(
                loginField.getText(),
                new String(senhaField.getPassword())
            );
            
            areaTexto.setText("=== RESULTADO DA AUTENTICAÇÃO ===\n\n" + resultado);
        }
    }

    public static void main(String[] args) {
        // Executar a interface gráfica
        SwingUtilities.invokeLater(() -> new InterfaceGrafica());
    }
}