import java.util.ArrayList;
import java.util.List;

public class OrcamentoService {
    private List<Orcamento> orcamentos = new ArrayList<>();

    public void salvarOrcamento(Orcamento o) {
        orcamentos.add(o);
        FileExporter.exportarParaArquivo(orcamentos);
        orcamentos.clear();
    }
    public void excluirorcamento(Orcamento a){
        orcamentos.remove(a);
    }
}