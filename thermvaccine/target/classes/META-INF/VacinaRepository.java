import com.faster.jackson.core.type.TypeReference;
import com.faster.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOExeception;
import java.util.ArrayList;
import java.util.List;

public class VacinaRepository {

    private final File arquivo =
            new File("vacinas.json");

    private final ObjectMapper mapper =
            new ObjectMapper();

    // READ FILE
    public List<Vacina> listar() {

        try {

            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    arquivo,
                    new TypeReference<List<Vacina>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // SAVE FILE
    public void salvar(List<Vacina> vacinas) {

        try {

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(arquivo, vacinas);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}