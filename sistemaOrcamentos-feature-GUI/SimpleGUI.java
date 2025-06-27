import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

public class SimpleGUI extends JFrame {

    private OrcamentoService orcamentoService = new OrcamentoService();

    public SimpleGUI() {
        this.setTitle("Sistema de Orçamentos");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 400);
        this.setLayout(new BorderLayout(10, 10));

        setup();
        this.setVisible(true);
    }

    private void setup() {
        // Painel superior
        JPanel topo = new JPanel(new GridLayout(2, 1));
        JLabel titulo = new JLabel("Sistema de Orçamentos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel descricao = new JLabel("Permite criar, salvar e consultar orçamentos por cliente.", SwingConstants.CENTER);
        topo.add(titulo);
        topo.add(descricao);

        // Painel central
        JPanel centro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        JTextField campoCliente = new JTextField(20);
        JTextField campoValor = new JTextField(20);
        campoValor.setHorizontalAlignment(JTextField.RIGHT);
        JTextField campoExcluir = new JTextField(20);
        JButton botaoGerar = new JButton("Gerar Orçamento");
        JButton botaoConsultar = new JButton("Consultar Orçamentos");
        JButton botaoExcluir = new JButton("Excluir Orçamento");
        JTextArea resultadoArea = new JTextArea(8, 30);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        centro.add(new JLabel("Nome do Cliente:"), gbc);
        gbc.gridx = 1;
        centro.add(campoCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centro.add(new JLabel("Valor do Orçamento:"), gbc);
        gbc.gridx = 1;
        centro.add(campoValor, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel botoesPanel = new JPanel();
        botoesPanel.add(botaoGerar);
        botoesPanel.add(botaoConsultar);
        centro.add(botoesPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        centro.add(new JLabel("Excluir Cliente:"), gbc);
        gbc.gridx = 1;
        centro.add(campoExcluir, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        centro.add(botaoExcluir, gbc);

        // Action Listeners
        botaoGerar.addActionListener(e -> {
            String cliente = campoCliente.getText().trim();
            String valorTexto = campoValor.getText().trim();

            if (cliente.isEmpty() || valorTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }

            try {
                double valor = Double.parseDouble(valorTexto);
                Orcamento orcamento = new Orcamento(cliente, valor);
                orcamentoService.salvarOrcamento(orcamento);
                JOptionPane.showMessageDialog(this, "Orçamento salvo com sucesso.");
                campoCliente.setText("");
                campoValor.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido.");
            }
        });

        botaoConsultar.addActionListener(e -> {
            try {
                Path caminho = Path.of("orcamentos.txt");
                if (!Files.exists(caminho)) {
                    resultadoArea.setText("Nenhum orçamento cadastrado ainda.");
                    return;
                }
                
                String conteudo = Files.readString(caminho);
                resultadoArea.setText(conteudo.isEmpty() ? 
                    "Nenhum orçamento cadastrado." : conteudo);
            } catch (IOException ex) {
                resultadoArea.setText("Erro ao ler arquivo.");
                ex.printStackTrace();
            }
        });

        botaoExcluir.addActionListener(e -> {
            String clienteExcluir = campoExcluir.getText().trim();
            if (clienteExcluir.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o nome do cliente.");
                return;
            }

            try {
                Path caminho = Path.of("orcamentos.txt");
                if (!Files.exists(caminho)) {
                    JOptionPane.showMessageDialog(this, "Arquivo não encontrado.");
                    return;
                }

                List<String> linhas = Files.readAllLines(caminho);
                List<String> restantes = new ArrayList<>();
                boolean encontrado = false;

                for (String linha : linhas) {
                    if (!linha.toLowerCase().contains(clienteExcluir.toLowerCase() + ":")) {
                        restantes.add(linha);
                    } else {
                        encontrado = true;
                    }
                }

                if (encontrado) {
                    Files.write(caminho, restantes);
                    campoExcluir.setText("");
                    String novoConteudo = Files.readString(caminho);
                    resultadoArea.setText(novoConteudo.isEmpty() ? 
                        "Nenhum orçamento cadastrado." : novoConteudo);
                    JOptionPane.showMessageDialog(this, "Orçamento excluído.");
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir.");
                ex.printStackTrace();
            }
        });

        // Adicionando componentes ao JFrame
        this.add(topo, BorderLayout.NORTH);
        this.add(centro, BorderLayout.CENTER);
        this.add(new JScrollPane(resultadoArea), BorderLayout.SOUTH);
    }
}