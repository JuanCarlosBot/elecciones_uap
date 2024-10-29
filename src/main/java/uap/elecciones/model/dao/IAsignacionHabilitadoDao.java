package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.AsignacionHabilitado;

public interface IAsignacionHabilitadoDao extends CrudRepository<AsignacionHabilitado,Long>{
    
    @Query(value = "SELECT f.nombre_facultad , c.nombre_carrera , p.apellidos ,m.nombre_mesa ,ah.delegado, ah.id_asignacion_habilitado ,e.ru "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante "+
    "LEFT JOIN carrera_estudiante ce ON	e.id_estudiante = ce.id_estudiante "+
    "LEFT JOIN carrera c ON	ce.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON e.id_persona = p.id_persona "+
    "WHERE c.id_carrera = ?1",nativeQuery = true)
    List<Object[]> lista_asignados_habilitados(Long id_carrera);

    @Query(value = "SELECT f.nombre_facultad , c.nombre_carrera , p.apellidos ,m.nombre_mesa ,ah.delegado, ah.id_asignacion_habilitado ,d.rd "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN docente d ON vh.id_docente = d.id_docente "+
    "LEFT JOIN carrera_docente cd ON d.id_docente = cd.id_docente "+
    "LEFT JOIN carrera c ON	cd.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON d.id_persona = p.id_persona "+
    "WHERE c.id_carrera = ?1;",nativeQuery = true)
    List<Object[]> lista_asignados_habilitadosF(Long id_facultad);


    @Query(value = "SELECT f.nombre_facultad , c.nombre_carrera, e.ru , p.apellidos ,m.nombre_mesa ,ah.delegado "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante "+
    "LEFT JOIN carrera_estudiante ce ON	e.id_estudiante = ce.id_estudiante "+
    "LEFT JOIN carrera c ON	ce.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON e.id_persona = p.id_persona "+
    "WHERE ah.delegado = 'Delegado' and vh.id_estudiante is not null ORDER BY m.id_mesa  ASC",nativeQuery = true)
    List<Object[]> lista_asignados_delegados();

    @Query(value = "SELECT f.nombre_facultad , c.nombre_carrera, d.rd , p.apellidos ,m.nombre_mesa ,ah.delegado "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN docente d ON vh.id_docente = d.id_docente "+
    "LEFT JOIN carrera_docente cd ON	d.id_docente = cd.id_docente "+
    "LEFT JOIN carrera c ON	cd.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON d.id_persona = p.id_persona "+
    "WHERE ah.delegado = 'Delegado' and vh.id_docente is not null ORDER BY m.id_mesa  ASC",nativeQuery = true)
    List<Object[]> lista_asignados_delegados_docentes();

    @Query(value = "SELECT ah.id_asignacion_habilitado, m.id_mesa, "+
    "    m.nombre_mesa, "+
    "    m.estado "+
    "FROM asignacion_habilitado ah "+
    "    LEFT JOIN votante_habilitado vh ON ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "    LEFT JOIN docente d ON vh.id_docente = d.id_docente "+
    "    LEFT JOIN carrera_docente cd ON d.id_docente = cd.id_docente "+ 
    "    LEFT JOIN carrera c ON cd.id_carrera = c.id_carrera "+
    "    LEFT JOIN facultad f ON c.id_facultad = f.id_facultad "+
    "    LEFT JOIN mesa m ON ah.id_mesa = m.id_mesa "+
    "    LEFT JOIN persona p ON d.id_persona = p.id_persona "+ 
    "WHERE f.id_facultad = ?1 "+
    "order by m.id_mesa ASC;",nativeQuery = true)
    public List<Object[]> lista_asignacion_por_mesa(Long id_facultad);

    @Query(value = "SELECT f.nombre_facultad , c.nombre_carrera ,e.ru, p.apellidos ,m.nombre_mesa ,ah.delegado "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante "+
    "LEFT JOIN carrera_estudiante ce ON	e.id_estudiante = ce.id_estudiante "+
    "LEFT JOIN carrera c ON	ce.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON e.id_persona = p.id_persona "+
    "WHERE e.ru = ?1",nativeQuery = true)
    Object asignado_habilitado(String ru);

    @Query(value = "SELECT f.nombre_facultad , c.nombre_carrera ,d.rd, p.apellidos ,m.nombre_mesa ,ah.delegado "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN docente d ON vh.id_docente = d.id_docente "+
    "LEFT JOIN carrera_docente cd ON	d.id_docente = cd.id_docente "+
    "LEFT JOIN carrera c ON	cd.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON d.id_persona = p.id_persona "+
    "WHERE d.rd = ?1",nativeQuery = true)
    Object asignado_habilitadoDocente(String rd);

    @Query(value = "select * from asignacion_habilitado ah "+
    "where ah.id_mesa = ?1",nativeQuery = true)
    List<AsignacionHabilitado> lista_asignados_habilitados_por_mesa(Long id_mesa);

}
