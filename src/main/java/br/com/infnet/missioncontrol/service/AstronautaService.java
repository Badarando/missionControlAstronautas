package br.com.infnet.missioncontrol.service;
import br.com.infnet.missioncontrol.model.Astronauta;
import br.com.infnet.missioncontrol.model.StatusAstronauta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AstronautaService {
    private Long proximoId = 4L;
    private List<Astronauta> astronautas = new ArrayList<>(List.of(
            new Astronauta(
                    1L, "Ana Silva", "Brasil",
                    34, 8,
                    StatusAstronauta.DISPONIVEL),
            new Astronauta(
                    2L, "John Carter", "Estados Unidos",
                    41, 15,
                    StatusAstronauta.EM_MISSAO),
            new Astronauta(
                    3L,"Mei Lin","China",
                    29,5,
                    StatusAstronauta.AFASTADO)
    ));

    public List<Astronauta> listar(){ return astronautas; }

    public List<Astronauta> filtrarPorStatus(StatusAstronauta status){
        List<Astronauta> filtrados = new ArrayList<>();
        for (Astronauta astronauta : astronautas){
            if(astronauta.getStatus() == status){
                filtrados.add(astronauta);
            }
        }
        return filtrados;
    }

    public Astronauta buscarPorId(long id){
        for (Astronauta astronauta : astronautas){
            if(astronauta.getId().equals(id)){
                return astronauta;
            }
        }
        return null;
    }

    public Astronauta cadastrar(Astronauta astronauta){
        for (Astronauta astronauta1 : astronautas){
            if(astronauta1.getNome().equals(astronauta.getNome())){
                return null;
            }
        }
        astronauta.setId(this.proximoId);
        this.proximoId += 1;
        astronautas.add(astronauta);
        return astronauta;
    }

    public boolean remover(Long id){
        Astronauta astronauta = buscarPorId(id);
        if(astronauta == null){
            return false;
        }
        astronautas.remove(astronauta);
        return true;
    }
    public Astronauta atualizar(Long id, Astronauta entrada){
        Astronauta astronauta = buscarPorId(id);
        if(astronauta == null){
            return null;
        }
        for (Astronauta outro : astronautas) {
            if (!outro.getId().equals(id)
                    && outro.getNome().equals(entrada.getNome())) {
                throw new IllegalArgumentException("Nome não pode ser repetido!");
            }
        }
        astronauta.setNome(entrada.getNome());
        astronauta.setPais(entrada.getPais());
        astronauta.setIdade(entrada.getIdade());
        astronauta.setAnosExperiencia(entrada.getAnosExperiencia());
        astronauta.setStatus(entrada.getStatus());
        return astronauta;
    }
}
