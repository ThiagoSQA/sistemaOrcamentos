import java.util.ArrayList;
import java.util.List;

public class OrcamentoService {
    // lista que armazena os orcamentos temporariamente
    private List<Orcamento> orcamentos = new ArrayList<>();

    // salva um orcamento, exporta para arquivo e limpa a lista
    public void salvarOrcamento(Orcamento o) {
        orcamentos.add(o);
        FileExporter.exportarParaArquivo(orcamentos);
        orcamentos.clear();
    }

    // remove um orcamento da lista
    public void excluirorcamento(Orcamento a) {
        orcamentos.remove(a);
    }
}
