package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.models.MapData;
import gogood.gogoodapi.domain.models.MapList;
import gogood.gogoodapi.configuration.JdbcConfig;
import gogood.gogoodapi.domain.models.Ocorrencia;
import gogood.gogoodapi.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableCaching
@CrossOrigin(origins = "https://gogood.azurewebsites.net")
@RequestMapping("/consultar")
@Tag(name = "Mapa", description = "Consultar dados de ocorrências")

public class MapController {
    @Autowired
    private MapService mapService;
    @Operation(summary = "Obter dados de ocorrências", description = "Obtém os dados de ocorrências do banco de dados")
    @GetMapping
    public ResponseEntity<Map<String, Object>> get() {
        Map<String, Object> response = mapService.getDadosOcorrencia();
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Obter dados de ocorrências por localização", description = "Obtém os dados de ocorrências do banco de dados por localização")
    @GetMapping("/local/{latitude}/{longitude}")
    public Map<String, Object> getLocation(@PathVariable Double latitude, @PathVariable Double longitude) {
        return mapService.getAndSaveByLocation(latitude, longitude);
    }

}
