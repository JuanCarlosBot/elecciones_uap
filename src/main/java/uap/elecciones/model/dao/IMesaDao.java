package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.ConteoTotal;
import uap.elecciones.model.entity.Mesa;

public interface IMesaDao extends CrudRepository<Mesa, Long> {

  @Query(value = "SELECT " +
      "   m.id_mesa, " +
      "   MAX(m.nombre_mesa) AS nombre_mesa, " +
      "   MAX(m.estado) AS estado, " +
      "   count(ah.id_asignacion_habilitado) as cantidad " +
      "FROM asignacion_habilitado ah " +
      "   LEFT JOIN votante_habilitado vh ON ah.id_votante_habilitado = vh.id_votante_habilitado " +
      "   LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante " +
      "   LEFT JOIN carrera_estudiante ce ON e.id_estudiante = ce.id_estudiante " +
      "   LEFT JOIN carrera c ON ce.id_carrera = c.id_carrera " +
      "   LEFT JOIN facultad f ON c.id_facultad = f.id_facultad " +
      "   LEFT JOIN mesa m ON ah.id_mesa = m.id_mesa " +
      "   LEFT JOIN persona p ON e.id_persona = p.id_persona " +
      "WHERE c.id_carrera = ?1 " +
      "GROUP BY m.id_mesa " +
      "ORDER BY m.id_mesa;", nativeQuery = true)
  public List<Object[]> lista_mesas_por_carrera(Long id_carrera);

  @Query(value = "SELECT " +
      "   m.id_mesa, " +
      "   MAX(m.nombre_mesa) AS nombre_mesa, " +
      "   MAX(m.estado) AS estado, " +
      "   count(ah.id_asignacion_habilitado) as cantidad " +
      "FROM asignacion_habilitado ah " +
      "   LEFT JOIN votante_habilitado vh ON ah.id_votante_habilitado = vh.id_votante_habilitado " +
      "   LEFT JOIN docente d ON vh.id_docente = d.id_docente " +
      "   LEFT JOIN carrera_docente cd ON d.id_docente = cd.id_docente " +
      "   LEFT JOIN carrera c ON cd.id_carrera = c.id_carrera " +
      "   LEFT JOIN facultad f ON c.id_facultad = f.id_facultad " +
      "   LEFT JOIN mesa m ON ah.id_mesa = m.id_mesa " +
      "    LEFT JOIN persona p ON d.id_persona = p.id_persona " +
      "WHERE f.id_facultad = ?1 " +
      "GROUP BY m.id_mesa " +
      "order by m.id_mesa ASC;", nativeQuery = true)
  public List<Object[]> lista_mesas_por_facultad_docente(Long id_facultad);

  @Query(value = "SELECT distinct m.id_mesa, m.nombre_mesa, c.nombre_carrera, f.nombre_facultad, c.id_carrera from mesa m \n"
      + //
      "\tleft join asignacion_habilitado ah on m.id_mesa = ah.id_mesa\n" + //
      "\tleft join votante_habilitado vh on ah.id_votante_habilitado = vh.id_votante_habilitado\n" + //
      "\tleft join estudiante est on vh.id_estudiante = est.id_estudiante\n" + //
      "\tleft join carrera_estudiante ce on est.id_estudiante = ce.id_estudiante\n" + //
      "\tleft join carrera c on ce.id_carrera = c.id_carrera\n" + //
      "\tleft join facultad f on c.id_facultad = f.id_facultad\n" + //
      "\twhere  m.id_mesa = ?1", nativeQuery = true)
  Object mesaPorCarrera(Long idMesa);

  @Query(value = " select * from mesa m where m.id_carrera  = ?1", nativeQuery = true)
  public List<Mesa> mesasPorCarrera(Long idMesa);

  @Query("SELECT m from Mesa m where m.facultad.id_facultad = ?1")
  List<Mesa> listarMesasPorIdFacultad(Long idMesa);

  @Query(value = "SELECT * FROM mesa m ORDER BY m.id_mesa ASC",  nativeQuery = true)
  List<Mesa> listarMesasOrdenadas();
}
