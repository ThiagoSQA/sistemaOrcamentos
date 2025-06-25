import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class SimpleGUI {
    private OrcamentoService orcamentoService = new OrcamentoService();

    public void exibir() {
        JFrame frame = new JFrame("Sistema de Orçamentos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel topo = new JPanel(new GridLayout(2, 1));
        JLabel titulo = new JLabel("Sistema de Orçamentos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel descricao = new JLabel("Permite criar, salvar e consultar orçamentos por cliente.", SwingConstants.CENTER);
        topo.add(titulo);
        topo.add(descricao);

        JPanel centro = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField campoCliente = new JTextField();
        JTextField campoValor = new JTextField();
        centro.add(new JLabel("Nome do Cliente:"));
        centro.add(campoCliente);
        centro.add(new JLabel("Valor do Orçamento:"));
        centro.add(campoValor);

        JButton botaoGerar = new JButton("Gerar Orçamento");
        JButton botaoConsultar = new JButton("Consultar Orçamentos");
        centro.add(botaoGerar);
        centro.add(botaoConsultar);
        JTextField campoExcluir = new JTextField();//excluir orcamento do cliente
        centro.add(new JLabel("Excluir Cliente:"));
        centro.add(campoExcluir);
        JButton botaoExcluir = new JButton("Excluir Orçamento");
        centro.add(botaoExcluir);


        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);

        frame.add(topo, BorderLayout.NORTH);
        frame.add(centro, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        botaoGerar.addActionListener(e -> {
            String cliente = campoCliente.getText().trim();
            String valorTexto = campoValor.getText().trim();

            if (cliente.isEmpty() || valorTexto.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Preencha todos os campos.");
                return;
            }

            try {
                double valor = Double.parseDouble(valorTexto);
                Orcamento orcamento = new Orcamento(cliente, valor);
                orcamentoService.salvarOrcamento(orcamento);
                JOptionPane.showMessageDialog(frame, "Orçamento salvo com sucesso.");
                campoCliente.setText("");
                campoValor.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Valor inválido.");
            }
        });

        botaoConsultar.addActionListener(e -> {
            try {
                String conteudo = Files.readString(Path.of("orcamentos.txt"));
                resultadoArea.setText(conteudo);
            } catch (IOException ex) {
                resultadoArea.setText("Nenhum orçamento salvo ainda.");
            }
        });
        botaoExcluir.addActionListener(e -> {
    String clienteExcluir = campoExcluir.getText().trim();
    if (clienteExcluir.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Informe o nome do cliente para excluir.");
        return;
    }

    try {
        Path caminho = Path.of("orcamentos.txt");
        if (!Files.exists(caminho)) {
            JOptionPane.showMessageDialog(frame, "Arquivo de orçamentos não encontrado.");
            return;
        }

        // Filtra as linhas que NÃO contêm o nome informado
        java.util.List<String> linhas = Files.readAllLines(caminho);
        java.util.List<String> restantes = new java.util.ArrayList<>();
        boolean encontrado = false;

        for (String linha : linhas) {
            if (!linha.toLowerCase().startsWith(clienteExcluir.toLowerCase() + ":")) {
                restantes.add(linha);
            } else {
                encontrado = true;
            }
        }

        Files.write(caminho, restantes);
        campoExcluir.setText("");

        if (encontrado) {
            JOptionPane.showMessageDialog(frame, "Orçamento excluído com sucesso.");
        } else {
            JOptionPane.showMessageDialog(frame, "Cliente não encontrado.");
        }

    } catch (IOException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Erro ao tentar excluir.");
    }
});

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}