package uap.elecciones.model.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import uap.elecciones.model.service.UtilidadService;

@Service
public class UtilidadServiceImpl implements UtilidadService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String guardarArchivo(MultipartFile archivo) {
        String uniqueFilename = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();

        Path rootPath = Paths.get("uploads/").resolve(uniqueFilename);
        Path rootAbsolutPath = rootPath.toAbsolutePath();
        System.out.println("LA DIRECCION ES: " + rootAbsolutPath);
        log.info("rootPath: " + rootPath);
        log.info("rootAbsolutPath: " + rootAbsolutPath);

        try {
            System.out.println("CUARDAR EN EL DIRECCTORIO");
            Files.copy(archivo.getInputStream(), rootAbsolutPath);

        } catch (IOException e) {
            System.out.println("ERROR AL GUARDAR EL ARCHIVO: " + e.getMessage());
            e.printStackTrace();
        }

        return uniqueFilename;
    }
}
