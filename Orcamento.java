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
        return cliente + ": R$ " + valor;
    }
}