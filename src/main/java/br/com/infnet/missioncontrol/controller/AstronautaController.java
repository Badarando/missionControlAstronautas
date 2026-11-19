package br.com.infnet.missioncontrol.controller;
import br.com.infnet.missioncontrol.model.Astronauta;
import br.com.infnet.missioncontrol.model.ErrorResponse;
import br.com.infnet.missioncontrol.model.StatusAstronauta;
import br.com.infnet.missioncontrol.service.AstronautaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.List;

@RestController
public class AstronautaController {
    private final AstronautaService astronautaService;

    public AstronautaController(AstronautaService astronautaService) {
        this.astronautaService = astronautaService;
    }

    @GetMapping("/astronautas")
    public ResponseEntity<List<Astronauta>> listarAstronautas(@RequestParam(required = false) StatusAstronauta status){
        if(status == null){
            return ResponseEntity.ok(astronautaService.listar());
        }
        return ResponseEntity.ok(astronautaService.filtrarPorStatus(status));
    }



    @GetMapping("/astronautas/{id}")
    public ResponseEntity<?> pegarId(@PathVariable("id") Long id){
        if(id <= 0){
            return ResponseEntity.badRequest().body(new ErrorResponse("Id inválido"));
        }
        Astronauta encontrado = astronautaService.buscarPorId(id);

        if(encontrado == null){
            return ResponseEntity.status(404).body(new ErrorResponse(("Id não econtrado")));
        }

        return ResponseEntity.ok(encontrado);
    }

    @PostMapping("/astronautas")
    public ResponseEntity cadastrar(@RequestBody Astronauta astronauta){
        if(astronauta.getNome() == null || astronauta.getNome().isBlank()){
            return  ResponseEntity.badRequest().body(new ErrorResponse("Nome não pode estar vazio"));
        }
        if(astronauta.getPais() == null || astronauta.getPais().isBlank()){
            return  ResponseEntity.badRequest().body(new ErrorResponse("Pais não pode estar vazio"));
        }
        if(astronauta.getIdade() == null || astronauta.getIdade() < 18){
            return  ResponseEntity.badRequest().body(new ErrorResponse("Idade nao pode ser vazio ou menor de 18 anos"));
        }
        if(astronauta.getAnosExperiencia() == null || astronauta.getAnosExperiencia() < 0){
            return  ResponseEntity.badRequest().body(new ErrorResponse("Anos de experiência nao pode ser vazio ou negativo"));
        }
        if(astronauta.getStatus() == null){
            return  ResponseEntity.badRequest().body(new ErrorResponse("Status não pode ser vazio"));
        }
        Astronauta cadastrado = astronautaService.cadastrar(astronauta);
        if(cadastrado == null){
            return  ResponseEntity.badRequest().body(new ErrorResponse("Nome não pode ser repetido!"));
        }
        return ResponseEntity.status(201).body(cadastrado);
    }

    @DeleteMapping("/astronautas/{id}")
    public ResponseEntity remover(@PathVariable("id") Long id){
        if(id <= 0){
            return ResponseEntity.badRequest().body(new ErrorResponse("Id não pode ser menor ou igual a 0"));
        }
        boolean removido = astronautaService.remover(id);
        if(removido == false){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Astronauta não localizado"));
        }
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/astronautas/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody Astronauta entrada) {
        if (id <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Id não pode ser menor ou igual a 0"));
        }

        if (astronautaService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Astronauta não localizado"));
        }

        if (entrada.getNome() == null || entrada.getNome().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Nome não pode estar vazio"));
        }

        if (entrada.getPais() == null || entrada.getPais().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("País não pode estar vazio"));
        }

        if (entrada.getIdade() == null || entrada.getIdade() < 18) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Idade é obrigatória e deve ser no mínimo 18"));
        }

        if (entrada.getAnosExperiencia() == null || entrada.getAnosExperiencia() < 0) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Anos de experiência são obrigatórios e não podem ser negativos"));
        }

        if (entrada.getStatus() == null) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Status não pode estar vazio"));
        }

        try {
            Astronauta atualizado = astronautaService.atualizar(id, entrada);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(erro.getMessage()));
        }
    }

}
