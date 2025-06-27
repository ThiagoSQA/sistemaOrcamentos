import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileExporter {
    public static void exportarParaArquivo(List<Orcamento> orcamentos) {
        try (FileWriter writer = new FileWriter("orcamentos.txt", true)) {
            for (Orcamento o : orcamentos) {
                writer.write(o.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}