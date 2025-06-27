public class Orcamento {
    // atributos do orcamento: nome do cliente e valor
    private String cliente;
    private double valor;

    // construtor que recebe cliente e valor
    public Orcamento(String cliente, double valor) {
        this.cliente = cliente;
        this.valor = valor;
    }

    // retorna o nome do cliente
    public String getCliente() {
        return cliente;
    }

    // retorna o valor do orcamento
    public double getValor() {
        return valor;
    }

    // representa o orcamento como texto
    @Override
    public String toString() {
        return cliente + ": R$ " + valor;
    }
}
