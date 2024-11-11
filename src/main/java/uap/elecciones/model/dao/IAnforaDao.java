package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import uap.elecciones.model.entity.Anfora;
import uap.elecciones.model.entity.Frente;

public interface IAnforaDao extends CrudRepository<Anfora, Long> {

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

        @Query(value = """
        select 
                sum(a.cant_voto_nulo) as total_voto_nulo,
                sum(a.cant_voto_blanco) as total_voto_blanco,
                sum(a.cant_voto_valido) as total_voto_valido,
                sum(a.cant_voto_habilitado) as total_voto_habilitado,
                sum(a.cant_voto_emitido) as total_voto_emitido,
                (select count(ah.id_asignacion_habilitado)
                from asignacion_habilitado ah 
                left join mesa m2 on ah.id_mesa = m2.id_mesa 
                left join votante_habilitado vh on vh.id_votante_habilitado = ah.id_votante_habilitado
                where (vh.id_estudiante is null and :esNulo = true) 
                or (vh.id_estudiante is not null and :esNulo = false)) as total_habilitado,
                count(m.id_mesa) as total_actas_computadas,
                (select count(m3.id_mesa) 
                from mesa m3
                where m3.nombre_mesa like concat('%', :nombreMesa, '%')
                ) as total_actas_habilitadas
        from anfora a
        join mesa m on a.id_mesa = m.id_mesa
        where m.nombre_mesa like concat('%', :nombreMesa, '%') and m.estado = 'COMPLETADO'
        """, nativeQuery = true)
        public Object votosGenerales(@Param("esNulo") boolean esNulo, @Param("nombreMesa") String nombreMesa);


        @Query(value = """
        select 
                sum(a.cant_voto_nulo) as total_voto_nulo,
                sum(a.cant_voto_blanco) as total_voto_blanco,
                sum(a.cant_voto_valido) as total_voto_valido,
                sum(a.cant_voto_habilitado) as total_voto_habilitado,
                sum(a.cant_voto_emitido) as total_voto_emitido,
                (select count(ah.id_asignacion_habilitado)
                from asignacion_habilitado ah 
                join mesa m2 on ah.id_mesa = m2.id_mesa) as total_habilitado,
                count(m.id_mesa) as total_actas_computadas,
                (select count(m3.id_mesa) from mesa m3) as total_actas_habilitadas
        from anfora a
        join mesa m on a.id_mesa = m.id_mesa
        where m.estado = 'COMPLETADO'"""
        , nativeQuery = true)
        public Object votosGeneral();

        @Query(value = """
        select
        sum(a.cant_voto_nulo) as total_voto_nulo,
        sum(a.cant_voto_blanco) as total_voto_blanco,
        sum(a.cant_voto_valido) as total_voto_valido,
        sum(a.cant_voto_habilitado) as total_voto_habilitado,
        sum(a.cant_voto_emitido) as total_voto_emitido,
        COALESCE((
                select count(ah.id_asignacion_habilitado)
                from asignacion_habilitado ah 
                left join mesa m2 on ah.id_mesa = m2.id_mesa 
                left join votante_habilitado vh on vh.id_votante_habilitado = ah.id_votante_habilitado
                left join estudiante e on e.id_estudiante = vh.id_estudiante
                left join docente d on d.id_docente = vh.id_docente
                left join carrera_estudiante ce on ce.id_estudiante = e.id_estudiante 
                left join carrera c_estudiante on c_estudiante.id_carrera = ce.id_carrera 
                left join carrera_docente cd on cd.id_docente = d.id_docente 
                left join carrera c_docente on c_docente.id_carrera = cd.id_carrera 
                where 
                (c_estudiante.id_facultad = :idFacultad or c_docente.id_facultad = :idFacultad)
                and (
                        (:esNulo = true and vh.id_estudiante is null) 
                        or 
                        (:esNulo = false and vh.id_estudiante is not null)
                )
        ), 0) as total_habilitado,
        count(m.id_mesa) as total_actas_computadas,
        (select count(m3.id_mesa) 
        from mesa m3 
        left join facultad f2 on f2.id_facultad = m3.if_facultad
        where f2.id_facultad = :idFacultad 
        and m3.nombre_mesa like concat('%', :sigla, '%')) as total_actas_habilitadas  
        from anfora a
        inner join mesa m on a.id_mesa = m.id_mesa
        inner join facultad f on m.if_facultad = f.id_facultad
        where f.id_facultad = :idFacultad 
        and m.nombre_mesa like concat('%', :sigla, '%') and m.estado = 'COMPLETADO';
        """, nativeQuery = true)
        public Object votosGeneralFacultad(@Param("idFacultad") Long idFacultad, @Param("sigla") String sigla , @Param("esNulo")Boolean esNulo);


        @Query(value = """
                select
                sum(a.cant_voto_nulo) as total_voto_nulo,
        sum(a.cant_voto_blanco) as total_voto_blanco,
        sum(a.cant_voto_valido) as total_voto_valido,
        sum(a.cant_voto_habilitado) as total_voto_habilitado,
        sum(a.cant_voto_emitido) as total_voto_emitido,
        (select count(ah.id_asignacion_habilitado)
                from asignacion_habilitado ah 
                left join mesa m2 on ah.id_mesa = m2.id_mesa 
                left join votante_habilitado vh on vh.id_votante_habilitado = ah.id_votante_habilitado
                left join estudiante e on e.id_estudiante = vh.id_estudiante
                left join carrera_estudiante ce on ce.id_estudiante = e.id_estudiante
                left join carrera c2 on c2.id_carrera = ce.id_carrera
                where vh.id_estudiante is not null and c2.id_carrera = :id_carrera) as total_habilitado,
                count(m.id_mesa) as total_actas_computadas,
                (
                SELECT COUNT(m3.id_mesa) 
                FROM mesa m3 
                WHERE m3.id_carrera = :id_carrera 
                ) AS total_actas_habilitadas 
        from anfora a, mesa m, carrera c 
        where a.id_mesa = m.id_mesa and m.id_carrera = c.id_carrera 
        and c.id_carrera = :id_carrera and m.estado = 'COMPLETADO'
        """,nativeQuery = true)
        public Object votosGeneralCarrera(@Param("id_carrera")Long id_carrera);
}
