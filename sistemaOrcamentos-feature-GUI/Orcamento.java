public class Orcamento {
    private String cliente;
    private double valor;

    public Orcamento(String cliente, double valor) {
        this.cliente = cliente;
        this.valor = valor;
    }

    public String getCliente() {
        return cliente;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        // Formata o valor para 2 casas decimais e alinha em 10 caracteres
        return String.format("%-30s R$ %10.2f", cliente, valor);
    }
}