import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class SimpleGUI {
    // instancia do servico de orcamento
    private OrcamentoService orcamentoService = new OrcamentoService();

    // metodo para exibir a interface grafica
    public void exibir() {
        // cria a janela principal
        JFrame frame = new JFrame("Sistema de Orçamentos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout(10, 10));

        // painel do topo com titulo e descricao
        JPanel topo = new JPanel(new GridLayout(2, 1));
        JLabel titulo = new JLabel("Sistema de Orçamentos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel descricao = new JLabel("Permite criar, salvar e consultar orçamentos por cliente.", SwingConstants.CENTER);
        topo.add(titulo);
        topo.add(descricao);

        // painel central com campos e botoes
        JPanel centro = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField campoCliente = new JTextField();
        JTextField campoValor = new JTextField();
        centro.add(new JLabel("Nome do Cliente:"));
        centro.add(campoCliente);
        centro.add(new JLabel("Valor do Orçamento:"));
        centro.add(campoValor);

        // botoes principais
        JButton botaoGerar = new JButton("Gerar Orçamento");
        JButton botaoConsultar = new JButton("Consultar Orçamentos");
        centro.add(botaoGerar);
        centro.add(botaoConsultar);

        // campo e botao para excluir orcamento
        JTextField campoExcluir = new JTextField();
        centro.add(new JLabel("Excluir Cliente:"));
        centro.add(campoExcluir);
        JButton botaoExcluir = new JButton("Excluir Orçamento");
        centro.add(botaoExcluir);

        // area para exibir resultado da consulta
        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);

        // adiciona os paineis na janela
        frame.add(topo, BorderLayout.NORTH);
        frame.add(centro, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        // acao do botao gerar orcamento
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
                JOptionPane.showMessageDialog(frame, "Valor invalido.");
            }
        });

        // acao do botao consultar orcamentos
        botaoConsultar.addActionListener(e -> {
            try {
                String conteudo = Files.readString(Path.of("orcamentos.txt"));
                resultadoArea.setText(conteudo);
            } catch (IOException ex) {
                resultadoArea.setText("Nenhum orcamento salvo ainda.");
            }
        });

        // acao do botao excluir orcamento
        botaoExcluir.addActionListener(e -> {
            String clienteExcluir = campoExcluir.getText().trim();
            if (clienteExcluir.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Informe o nome do cliente para excluir.");
                return;
            }

            try {
                Path caminho = Path.of("orcamentos.txt");
                if (!Files.exists(caminho)) {
                    JOptionPane.showMessageDialog(frame, "Arquivo de orcamentos nao encontrado.");
                    return;
                }

                // filtra as linhas que nao comecam com o nome do cliente
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
                    JOptionPane.showMessageDialog(frame, "Orcamento excluido com sucesso.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Cliente nao encontrado.");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Erro ao tentar excluir.");
            }
        });

        // centraliza a janela na tela e exibe
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
