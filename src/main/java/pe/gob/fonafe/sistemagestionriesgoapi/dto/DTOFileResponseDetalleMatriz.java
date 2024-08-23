/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dto;

/**
 *
 * @author CANVIA
 */
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DTOFileResponseDetalleMatriz {
    private String idFile;
    private String fileName;
    private String originalFileName;
    private List<DTODetalleMatrizRiesgo> listado;
}
