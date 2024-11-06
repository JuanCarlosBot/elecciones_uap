package uap.elecciones.model.service;

import org.springframework.web.multipart.MultipartFile;

public interface UtilidadService {
    public String guardarArchivo(MultipartFile archivo);
}
