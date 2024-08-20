package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.DTOS.UsuarioNovo;
import gogood.gogoodapi.domain.models.usuarios.Usuarios;
import gogood.gogoodapi.service.OcorrenciaService;
import gogood.gogoodapi.service.UsuarioService;
import gogood.gogoodapi.utils.StringHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/transfer")
@Tag(name = "Transferência de Dados", description = "Transferir dados de usuários")
public class TransferDataController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private OcorrenciaService ocorrenciaService;


    @Operation(summary = "Download de dados de usuários em formato CSV", description = "Exporta os dados de usuários do banco de dados em um arquivo CSV")
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Void> downloadData(HttpServletResponse response) throws IOException {
        Iterable<Usuarios> allData = usuarioService.get();

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"usuarios.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,Nome,Email,Senha,DataNascimento,Genero,GoogleId,CreatedAt");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (Usuarios usuario : allData) {
                Date date = java.sql.Date.valueOf(usuario.getDt_nascimento());
                String formattedDate = dateFormat.format(date);

                Date timestamp = java.sql.Timestamp.valueOf(usuario.getCreated_at().atStartOfDay());
                String formattedTimestamp = timestampFormat.format(timestamp);
                writer.println(String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                        usuario.getID(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getSenha(),
                        formattedDate,
                        usuario.getGenero(),
                        usuario.getGoogle_id(),
                        formattedTimestamp
                ));
            }
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Download de dados de ocorrências por bairro", description = "Download de dados de ocorrências por bairro")
    @GetMapping(value = "/relatorio", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Void> gerarRelatorioBairro(HttpServletResponse response, @RequestParam String bairro,  @RequestParam String cidade) throws IOException {

        var data = LocalDate.now();

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio_%s_%d-%d-%d.csv\"".formatted(
        bairro, data.getDayOfYear(), data.getMonth().getValue(), data.getYear()
        ));


        var historicoBairro = ocorrenciaService.
                obterHistoricoQuantidadeOcorrenciasBairro(StringHelper.normalizar(cidade), StringHelper.normalizar(bairro));
        try (PrintWriter writer = response.getWriter()) {
           writer.println("LOGRADOURO,QUANTIDADE_OCORRENCIAS_2023,QUANTIDADE_OCORRENCIAS_2024,QUANTIDADE_OCORRENCIAS_TOTAL");
            for (int i = 0; i < historicoBairro.length; i++) {
                writer.println(String.format("%s,%s,%s,%s",
                        historicoBairro[i][0],
                        historicoBairro[i][1],
                        historicoBairro[i][2],
                        historicoBairro[i][3]
                ));

            }

        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Upload de dados de usuários em formato CSV", description = "Importa os dados de usuários de um arquivo CSV para o banco de dados")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadData(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Por favor anexe um arquivo CSV";
        }

        List<Usuarios> usuarios = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            br.readLine(); // Ignora a primeira linha (cabeçalho)

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Usuarios usuario = new Usuarios();
                usuario.setID(Integer.parseInt(data[0]));
                usuario.setNome(data[1]);
                usuario.setEmail(data[2]);
                usuario.setSenha(data[3]);
                usuario.setDt_nascimento(LocalDate.parse(data[4]));
                usuario.setGenero(data[5]);
                usuario.setGoogle_id(data[6]);

                usuarios.add(usuario);
            }

            for (Usuarios usuario : usuarios) {
                UsuarioNovo usuarioNovo = new UsuarioNovo(
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getSenha(),
                        usuario.getGenero(),
                        usuario.getDt_nascimento(),
                        LocalDate.now()
                );

               usuarioService.criar(usuarioNovo);
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }

        return "Dados importados com sucesso!";
    }
}
