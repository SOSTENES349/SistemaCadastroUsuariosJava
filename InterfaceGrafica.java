import java.awt.*;
import javax.swing.*;
import java.util.List;


// Interface gráfica
public class InterfaceGrafica extends JFrame {
    private final UsuarioDAO usuarioDAO;
    private JTextArea areaTexto;

        public InterfaceGrafica() {
        usuarioDAO = new UsuarioDAO();
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
        btnCadastrar.addActionListener(_ -> {
            try {
                cadastrarUsuario();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        btnListar.addActionListener(_ -> {
            try {
                listarUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar usuários: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        btnAutenticar.addActionListener(_ -> {
            try {
                autenticarUsuario();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao autenticar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
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
            String nome = nomeField.getText();
            String cpf = cpfField.getText();
            String email = emailField.getText();
            String cargo = cargoField.getText();
            String login = loginField.getText();
            String senha = new String(senhaField.getPassword());
            PerfilUsuario perfil = (PerfilUsuario) perfilCombo.getSelectedItem();

            if (!Validador.validarCPF(cpf)) {
                JOptionPane.showMessageDialog(this, "CPF inválido! Use o formato: 000.000.000-00", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Validador.validarEmail(email)) {
                JOptionPane.showMessageDialog(this, "E-mail inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Validador.validarSenha(senha)) {
                JOptionPane.showMessageDialog(this, "Senha muito curta! Mínimo 6 caracteres.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (usuarioDAO.loginExiste(login)) {
                JOptionPane.showMessageDialog(this, "Login já existe! Escolha outro.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario novoUsuario = new Usuario(nome, cpf, email, cargo, login, senha, perfil);
            boolean sucesso = usuarioDAO.inserirUsuario(novoUsuario);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                nomeField.setText("");
                cpfField.setText("");
                emailField.setText("");
                cargoField.setText("");
                loginField.setText("");
                senhaField.setText("");
                perfilCombo.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Erro inesperado ao cadastrar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listarUsuarios() {
        List<Usuario> lista = usuarioDAO.listarUsuarios();
        StringBuilder texto = new StringBuilder("=== LISTA DE USUÁRIOS ===\n\n");
        if (lista.isEmpty()) {
            texto.append("Nenhum usuário cadastrado.\n");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                texto.append("Usuário #").append(i + 1).append("\n");
                texto.append(lista.get(i).toString()).append("\n");
            }
        }
        areaTexto.setText(texto.toString());
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
            Usuario usuario = usuarioDAO.autenticar(
                loginField.getText(),
                new String(senhaField.getPassword())
            );
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Autenticação bem-sucedida!\nBem-vindo, " + usuario.getNomeCompleto() +
                            "\nSeu perfil: " + usuario.getPerfil(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loginField.setText("");
                senhaField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Falha na autenticação! Login ou senha incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        // Executar a interface gráfica
        SwingUtilities.invokeLater(() -> new InterfaceGrafica());
    }
}