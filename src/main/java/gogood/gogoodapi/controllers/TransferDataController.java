package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.models.Usuario;
import gogood.gogoodapi.configuration.JdbcConfig;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transfer")
public class TransferDataController {

    JdbcConfig jdbcConfig = new JdbcConfig();

    @Operation(summary = "Download de dados de usuários em formato CSV", description = "Exporta os dados de usuários do banco de dados em um arquivo CSV")
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Void> downloadData(HttpServletResponse response) throws IOException {
        List<Usuario> allData = jdbcConfig.getConexaoDoBanco().query(
                "SELECT * FROM usuarios",
                new BeanPropertyRowMapper<>(Usuario.class)
        );

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"usuarios.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,Nome,Email,Senha,DataNascimento,Genero,GoogleId,CreatedAt");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (Usuario usuario : allData) {
                writer.println(String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                        usuario.getID(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getSenha(),
                        dateFormat.format(usuario.getDt_nascimento()),
                        usuario.getGenero(),
                        usuario.getGoogle_id(),
                        timestampFormat.format(usuario.getCreated_at())));
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

        List<Usuario> usuarios = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            br.readLine(); // Ignora a primeira linha (cabeçalho)

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Usuario usuario = new Usuario();
                usuario.setID(Integer.parseInt(data[0]));
                usuario.setNome(data[1]);
                usuario.setEmail(data[2]);
                usuario.setSenha(data[3]);
                usuario.setDt_nascimento(new SimpleDateFormat("yyyy-MM-dd").parse(data[4]));
                usuario.setGenero(data[5]);
                usuario.setGoogle_id(data[6]);

                usuarios.add(usuario);
            }

            for (Usuario usuario : usuarios) {
                jdbcConfig.getConexaoDoBanco().update(
                        "INSERT INTO usuarios (ID, nome, email, senha, dt_nascimento, genero, google_id, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                        usuario.getID(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getDt_nascimento(),
                        usuario.getGenero(), usuario.getGoogle_id(), usuario.getCreated_at()
                );
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }

        return "Dados importados com sucesso!";
    }
}
