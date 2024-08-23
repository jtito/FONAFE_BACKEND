/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.models;

/**
 *
 * @author CANVIA
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author CANVIA
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetalleGrafico implements Serializable {
    
    private String des;
    private Integer cant;
    private Integer datc;
    private String datt;
    private String nom;
    private String color;
}
