package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEmpresa;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EmpresaBean;

public interface IEmpresaService {

    DTOEmpresa registrarEmpresa(EmpresaBean empresaBean);
    DTOEmpresa obtenerEmpresa(Long idEmpresa);
    DTOGenerico listarEmpresas();
    DTOEmpresa actualizarEmpresa(EmpresaBean empresaBean);
    Byte anularEmpresa(EmpresaBean empresaBean);
}
