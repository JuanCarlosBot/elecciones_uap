package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Anfora;
import uap.elecciones.model.entity.Frente;



public interface IAnforaDao extends CrudRepository<Anfora,Long>{
    
    @Query(value = "select m.nombre_mesa, f.nombre_frente, n.nombre_nivel, f.id_frente " +
               "from mesa m " +
               "left join detalle_asignacion_mesa d on m.id_mesa = d.id_mesa " +
               "left join asignacion_eleccion ae on d.id_asignacion_eleccion = ae.id_asignacion_eleccion " +
               "left join frente f on ae.id_frente = f.id_frente " +
               "left join nivel n on ae.id_nivel = n.id_nivel " +
               "where m.id_mesa = ?1 and n.id_nivel = ?2", nativeQuery = true)
List<Object[]> getDatosDeMesaYFrenteYNivelPorIdMesaYNivel(Long idMesa, Long idNivel);

@Query(value = "select distinct fac.nombre_facultad  from mesa m \n" + //
        "left join asignacion_habilitado ah on m.id_mesa = ah.id_mesa\n" + //
        "left join votante_habilitado vh on ah.id_votante_habilitado = vh.id_votante_habilitado\n" + //
        "left join estudiante est on vh.id_estudiante = est.id_estudiante\n" + //
        "left join carrera_estudiante cares on cares.id_estudiante = est.id_estudiante\n" + //
        "left join carrera car on car.id_carrera = cares.id_carrera\n" + //
        "left join facultad fac on fac.id_facultad = car.id_facultad\n" + //
        "where m.id_mesa = ?1", nativeQuery = true)
Object mesaPorFacultad(Long idMesa);




}
