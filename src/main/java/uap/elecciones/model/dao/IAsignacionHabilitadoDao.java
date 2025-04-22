package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import uap.elecciones.model.entity.AsignacionHabilitado;
import uap.elecciones.model.entity.DelegadoDto;
import uap.elecciones.model.entity.Mesa;

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


    @Query(value = """
    SELECT f.nombre_facultad , c.nombre_carrera, e.ru , p.apellidos ,concat(m.nombre_mesa,' - ',m.ubicacion,' - ',m.descripcion)as lugarVotacion,
ah.delegado, p.id_persona, vh.id_votante_habilitado,concat(m2.nombre_mesa,' - ',m2.ubicacion,' - ',m2.descripcion)as lugarJurado, td.nombre_tipo_delegado
    FROM asignacion_habilitado ah 
    LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado 
    LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante 
    LEFT JOIN carrera_estudiante ce ON	e.id_estudiante = ce.id_estudiante 
    LEFT JOIN carrera c ON	ce.id_carrera = c.id_carrera 
    LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad 
    LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa 
    LEFT JOIN persona p ON e.id_persona = p.id_persona 
    left join delegado d2 on vh.id_votante_habilitado = d2.id_votante_habilitado
	left join tipo_delegado td on d2.id_tipo_delegado = td.id_tipo_delegado 
	left join mesa m2 on d2.id_mesa = m2.id_mesa 
    WHERE ah.delegado = 'Delegado' and vh.id_estudiante is not null ORDER BY m.id_mesa  asc""",nativeQuery = true)
    List<Object[]> lista_asignados_delegados();

    @Query(value = """
    SELECT f.nombre_facultad , c.nombre_carrera, d.rd , p.apellidos ,concat(m.nombre_mesa,' - ',m.ubicacion,' - ',m.descripcion)as lugarVotacion,
ah.delegado, p.id_persona, vh.id_votante_habilitado, concat(m2.nombre_mesa,' - ',m2.ubicacion,' - ',m2.descripcion)as lugarJurado, td.nombre_tipo_delegado
    FROM asignacion_habilitado ah 
    LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado 
    LEFT JOIN docente d ON vh.id_docente = d.id_docente 
    LEFT JOIN carrera_docente cd ON	d.id_docente = cd.id_docente 
    LEFT JOIN carrera c ON	cd.id_carrera = c.id_carrera 
    LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad 
    LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa 
    LEFT JOIN persona p ON d.id_persona = p.id_persona 
    left join delegado d2 on vh.id_votante_habilitado = d2.id_votante_habilitado
	left join tipo_delegado td on d2.id_tipo_delegado = td.id_tipo_delegado 
	left join mesa m2 on d2.id_mesa = m2.id_mesa 
WHERE ah.delegado = 'Delegado' and vh.id_docente is not null ORDER BY m.id_mesa  ASC""",nativeQuery = true)
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

    @Query(value = "SELECT f.nombre_facultad , c.nombre_carrera ,e.ru, p.apellidos ,m.nombre_mesa ,ah.delegado, m.ubicacion, m.descripcion, "+
        "m2.nombre_mesa AS mesadelegado, "+
        "m2.ubicacion as ubicacio, "+
        "m2.descripcion as descrip "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante "+
    "LEFT JOIN carrera_estudiante ce ON	e.id_estudiante = ce.id_estudiante "+
    "LEFT JOIN carrera c ON	ce.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON e.id_persona = p.id_persona "+
    "left join delegado d2 on vh.id_votante_habilitado = d2.id_votante_habilitado "+
    "LEFT JOIN mesa m2 ON d2.id_mesa = m2.id_mesa "+
    "WHERE e.ru = ?1 "+
    "ORDER BY p.apellidos ASC;",nativeQuery = true)
    Object asignado_habilitado(String ru);

    /*@Query(value = "SELECT f.nombre_facultad , c.nombre_carrera ,d.rd, p.apellidos ,m.nombre_mesa ,ah.delegado, m.ubicacion, m.descripcion "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN docente d ON vh.id_docente = d.id_docente "+
    "LEFT JOIN carrera_docente cd ON	d.id_docente = cd.id_docente "+
    "LEFT JOIN carrera c ON	cd.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON d.id_persona = p.id_persona "+
    "WHERE d.rd = ?1",nativeQuery = true)*/

    @Query(value = "SELECT f.nombre_facultad, "+
       "c.nombre_carrera, "+
       "d.rd, "+
       "p.apellidos, "+
       "m.nombre_mesa AS mesa_asignada, "+
       "ah.delegado, "+
       "m.ubicacion, "+
       "m.descripcion, "+
       "m2.nombre_mesa AS mesadelegado, "+
       "m2.ubicacion as ubicacio, "+
       "m2.descripcion as descrip "+
"FROM asignacion_habilitado ah "+
"LEFT JOIN votante_habilitado vh ON ah.id_votante_habilitado = vh.id_votante_habilitado "+
"LEFT JOIN docente d ON vh.id_docente = d.id_docente "+
"LEFT JOIN carrera_docente cd ON d.id_docente = cd.id_docente "+
"LEFT JOIN carrera c ON cd.id_carrera = c.id_carrera "+
"LEFT JOIN facultad f ON c.id_facultad = f.id_facultad "+
"LEFT JOIN mesa m ON ah.id_mesa = m.id_mesa "+
"LEFT JOIN persona p ON d.id_persona = p.id_persona "+
"LEFT JOIN delegado d2 ON vh.id_votante_habilitado = d2.id_votante_habilitado "+
"LEFT JOIN mesa m2 ON d2.id_mesa = m2.id_mesa "+
"WHERE p.ci LIKE %?1% "+
"ORDER BY p.apellidos ASC;",nativeQuery = true)
    Object asignado_habilitadoDocente(String rd);

    @Query(value = "select * from asignacion_habilitado ah "+
    "where ah.id_mesa = ?1",nativeQuery = true)
   List<AsignacionHabilitado> lista_asignados_habilitados_por_mesa(Long id_mesa);

    @Query(value = "select * from asignacion_habilitado ah "+
    "where ah.id_mesa = ?1 and ah.delegado = 'Delegado'",nativeQuery = true)
    List<AsignacionHabilitado> listaHabilitadosMesas(Long id_mesa);//cambiar en un futuro
    
    @Query(value = "SELECT vh.id_votante_habilitado FROM votante_habilitado vh "
             + "LEFT JOIN docente d ON vh.id_docente = d.id_docente "
             + "LEFT JOIN carrera_docente cd ON d.id_docente = cd.id_docente "
             + "LEFT JOIN carrera c ON cd.id_carrera = c.id_carrera "
             + "WHERE c.id_facultad = ?1", nativeQuery = true)
    List<Long> listaDocentesFac(Long id_fac);//cambiar en un futuro


    @Query(value = """
        SELECT
    COALESCE(
        (SELECT f2.nombre_facultad 
         FROM votante_habilitado vh2
         LEFT JOIN docente d ON vh2.id_docente = d.id_docente
         left join carrera_docente cd on  d.id_docente = cd.id_docente
         LEFT JOIN carrera c ON cd.id_carrera  = c.id_carrera
         left join facultad f2 on c.id_facultad = f2.id_facultad
         WHERE vh2.id_votante_habilitado = vh.id_votante_habilitado),
        f.nombre_facultad
    ) AS nombre_facultad,
    COALESCE(
        (SELECT c2.nombre_carrera 
         FROM votante_habilitado vh2
         LEFT JOIN docente d ON vh2.id_docente = d.id_docente
         left join carrera_docente cd on  d.id_docente = cd.id_docente
         LEFT JOIN carrera c2 ON cd.id_carrera  = c2.id_carrera
         WHERE vh2.id_votante_habilitado = vh.id_votante_habilitado),
        c.nombre_carrera
    ) AS nombre_carrera,
    COALESCE(
        (SELECT d2.rd FROM votante_habilitado vh2
         LEFT JOIN docente d2 ON vh2.id_docente = d2.id_docente
         WHERE vh2.id_votante_habilitado = vh.id_votante_habilitado),
        e.ru
    ) AS ru_rd,
     CASE 
        WHEN e.ru IS NULL THEN 'Docente'
        ELSE 'Estudiante'
    END AS tipo_persona,
    COALESCE(
        (SELECT p2.apellidos 
         FROM votante_habilitado vh2
         LEFT JOIN docente d ON vh2.id_docente = d.id_docente
         LEFT JOIN persona p2 ON d.id_persona = p2.id_persona
         WHERE vh2.id_votante_habilitado = vh.id_votante_habilitado),
        p.apellidos
    ) AS apellidos,
    COALESCE(
        (SELECT p2.id_persona 
         FROM votante_habilitado vh2
         LEFT JOIN docente d ON vh2.id_docente = d.id_docente
         LEFT JOIN persona p2 ON d.id_persona = p2.id_persona
         WHERE vh2.id_votante_habilitado = vh.id_votante_habilitado),
        p.id_persona
    ) AS id_persona,
    m.nombre_mesa, 
    td.nombre_tipo_delegado, 
    vh.id_votante_habilitado
FROM delegado dd 
LEFT JOIN votante_habilitado vh ON dd.id_votante_habilitado = vh.id_votante_habilitado
LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante
LEFT JOIN carrera_estudiante ce ON e.id_estudiante = ce.id_estudiante
LEFT JOIN carrera c ON ce.id_carrera = c.id_carrera 
LEFT JOIN facultad f ON c.id_facultad = f.id_facultad 
LEFT JOIN mesa m ON dd.id_mesa = m.id_mesa 
LEFT JOIN persona p ON e.id_persona = p.id_persona 
LEFT JOIN tipo_delegado td ON dd.id_tipo_delegado = td.id_tipo_delegado
WHERE m.id_mesa = ?1
ORDER BY td.id_tipo_delegado ASC;
    """,nativeQuery = true)
    List<Object[]> listarDelegadosPorMesa(Long idMesa);

    
    @Query (value = """
    SELECT
    COALESCE(
        (SELECT f2.nombre_facultad 
         FROM votante_habilitado vh2
         LEFT JOIN docente d ON vh2.id_docente = d.id_docente
         LEFT JOIN carrera_docente cd ON d.id_docente = cd.id_docente
         LEFT JOIN carrera c ON cd.id_carrera = c.id_carrera
         LEFT JOIN facultad f2 ON c.id_facultad = f2.id_facultad
         WHERE vh2.id_votante_habilitado = vh.id_votante_habilitado),
        f.nombre_facultad
    ) AS nombre_facultad,
    COALESCE(
        (SELECT c2.nombre_carrera 
         FROM votante_habilitado vh2
         LEFT JOIN docente d ON vh2.id_docente = d.id_docente
         LEFT JOIN carrera_docente cd ON d.id_docente = cd.id_docente
         LEFT JOIN carrera c2 ON cd.id_carrera = c2.id_carrera
         WHERE vh2.id_votante_habilitado = vh.id_votante_habilitado),
        c.nombre_carrera
    ) AS nombre_carrera,
    COALESCE(
        (SELECT d2.rd 
         FROM votante_habilitado vh2
         LEFT JOIN docente d2 ON vh2.id_docente = d2.id_docente
         WHERE vh2.id_votante_habilitado = vh.id_votante_habilitado),
        e.ru
    ) AS ru_rd,
    CASE 
        WHEN e.ru IS NULL THEN 'Docente'
        ELSE 'Estudiante'
    END AS tipo_persona,
    COALESCE(
        (SELECT p2.apellidos 
         FROM votante_habilitado vh2
         LEFT JOIN docente d ON vh2.id_docente = d.id_docente
         LEFT JOIN persona p2 ON d.id_persona = p2.id_persona
         WHERE vh2.id_votante_habilitado = vh.id_votante_habilitado),
        p.apellidos
    ) AS apellidos,
    m.nombre_mesa,
    COALESCE(
        (SELECT d3.celular 
         FROM votante_habilitado vh3
         LEFT JOIN docente d3 ON vh3.id_docente = d3.id_docente
         WHERE vh3.id_votante_habilitado = vh.id_votante_habilitado),
        e.celular
    ) AS celular
FROM votante_habilitado vh
LEFT JOIN asignacion_habilitado ah ON ah.id_votante_habilitado = vh.id_votante_habilitado
LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante
LEFT JOIN carrera_estudiante ce ON e.id_estudiante = ce.id_estudiante
LEFT JOIN carrera c ON ce.id_carrera = c.id_carrera 
LEFT JOIN facultad f ON c.id_facultad = f.id_facultad 
LEFT JOIN mesa m ON ah.id_mesa = m.id_mesa 
LEFT JOIN persona p ON e.id_persona = p.id_persona 
WHERE m.id_mesa = ?1
ORDER BY p.apellidos ASC;
    """,nativeQuery = true)
    public List<Object[]> lista_votantes_por_mesa(Long id_mesa);


    @Query("SELECT a FROM AsignacionHabilitado a WHERE a.mesa IN :mesas")
    List<AsignacionHabilitado> findByMesaIn(@Param("mesas") List<Mesa> mesas);

}

