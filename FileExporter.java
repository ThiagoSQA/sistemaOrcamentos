import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileExporter {
    // metodo que exporta uma lista de orcamentos para um arquivo
    public static void exportarParaArquivo(List<Orcamento> orcamentos) {
        // tenta abrir o arquivo em modo de acrescentar conteudo
        try (FileWriter writer = new FileWriter("orcamentos.txt", true)) {
            // escreve cada orcamento no arquivo
            for (Orcamento o : orcamentos) {
                writer.write(o.toString() + "\n");
            }
        } catch (IOException e) {
            // imprime erro caso ocorra problema ao escrever no arquivo
            e.printStackTrace();
        }
    }
}
