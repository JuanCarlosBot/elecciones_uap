function cargarTablaPorMesa(idMesa) {
    $.ajax({
        type: 'POST',
        url: "/tabla-mesa/" + idMesa,  // Ruta del metodo del controlador en Spring Boot
        success: function (response) {
            $("#tablaRegistros").html(response);  // Actualiza el contenido del div con la respuesta del servidor
        }
    });
}

function listarCarreras() {
    const facultadId = $('#lfacultad').val();
    $.ajax({
        type: 'POST',
        url: "/cargarCarreras/" + facultadId,  // Ruta del metodo del controlador en Spring Boot
        success: function (response) {

            const selectCarrera = $('#lcarreras');
            selectCarrera.empty();  // Limpiar las opciones existentes

            response.forEach((carrera, index) => {
                const option = $('<option></option>')
                    .val(carrera[0])  // El valor del option es el ID 
                    .text(carrera[1]);  // El texto del option es el nombre completo 

                // Agregar la opción al <select>
                selectCarrera.append(option);
            });

            // Opcional: Seleccionar la primera opción y ejecutar `cargarTablaCarrera`
            if (response.length > 0) {
                selectCarrera.val(response[0][0]);  // Selecciona el primer ID de carrera
                cargarTablaCarrera();  // Llama cargarTablaCarrera solo después de haber llenado el select
            }

        }, error: function (xhr, status, error) {
            console.error('Hubo un problema con la solicitud AJAX:', status, error);
        }
    });

}

function cargarGeneraTotal() {

    $.ajax({
        type: 'POST',
        url: "/cargarGeneralTotal",  // Ruta del metodo del controlador en Spring Boot
        success: function (response) {
            updateChart(response.chartData, response.totalHabilitados, 'chart6');
            // $("#tablaGeneralTotal").html(response.html);
            console.log(response.chartData);
        }, error: function (xhr, status, error) {
            console.error('Hubo un problema con la solicitud AJAX:', status, error);
        }
    });
}

function cargarTablaGeneral() {
    $.ajax({
        type: 'POST',
        url: "/cargarGeneral",
        contentType: "application/json",
        data: JSON.stringify({}),  // Enviar un cuerpo vacío
        success: function (response) {
            updateChart(response.chartDataDoc, response.totalHabilitadosDoc, 'chart');
            updateChart(response.chartDataEst, response.totalHabilitadosEst, 'chart5');
            updateChart(response.chartDataTotal, 100, 'chart6');
            updatePieChart(response.chartDataTotal, 'chart6Torta')

            $("#tablaGeneralDoc").html(response.htmlDoc);
            $("#tablaGeneralEst").html(response.htmlEst);
            $("#tablaGeneralTotal").html(response.htmlTotalGeneral);
            $("#suma").html("RENOVACION + BLANCOS : " +response.suma_validos_blanco +" %");
            // console.log(response.suma_validos_blanco);

        },
        error: function (xhr, status, error) {
            console.error('Hubo un problema con la solicitud AJAX:', status, error);
        }
    });
}

function cargarTablaEstDoc() {

    const sigla = $('#sigla').val();
    const isnull = (sigla === "D");

    $.ajax({
        type: 'POST',
        url: "/cargarDatosEstDoc/" + sigla + "/" + isnull,  // Ruta del metodo del controlador en Spring Boot
        success: function (response) {
            updateChart(response.chartData, response.totalHabilitados, 'chart2');
            $("#tablaEstDoc").html(response.html);
        }, error: function (xhr, status, error) {
            console.error('Hubo un problema con la solicitud AJAX:', status, error);
        }
    });
}

function cargarTablaFacultad() {

    const id_facultad = $('#facultades').val();
    const sigla = $('#siglaf').val();
    const isnull = (sigla === "D");

    $.ajax({
        type: 'POST',
        url: "/cargarDatosFacultad/" + id_facultad + "/" + sigla + "/" + isnull,  // Ruta del metodo del controlador en Spring Boot
        success: function (response) {
            updateChart(response.chartData, response.totalHabilitados, 'chart3');
            $("#tablaFacultad").html(response.html);
        }, error: function (xhr, status, error) {
            console.error('Hubo un problema con la solicitud AJAX:', status, error);
        }
    });
}

function cargarTablaCarrera() {

    const id_carrera = $('#lcarreras').val();

    $.ajax({
        type: 'POST',
        url: "/cargarDatosCarrera/" + id_carrera,  // Ruta del metodo del controlador en Spring Boot
        success: function (response) {
            updateChart(response.chartData, response.totalHabilitados, 'chart4');
            $("#tablaCarrera").html(response.html);
        }, error: function (xhr, status, error) {
            console.error('Hubo un problema con la solicitud AJAX:', status, error);
        }
    });
}

function updateChart(chartData, totalHabilitados, chartId) {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Frente');
    data.addColumn('number', 'Votos');
    data.addColumn({ type: 'string', role: 'style' });
    data.addColumn({ type: 'string', role: 'annotation' });
    var titulo ;
    // Agregar los datos recibidos al DataTable de Google Charts
    // Validación según el chartId
    if (chartId === 'chart6') {
        // Si el chartId es 'chart6', agregar el símbolo de '%' a los votos
        chartData.forEach(item => {
            data.addRow([item.frente, item.votos, item.color, item.votos.toString() + " %"]);
        });
        titulo = "Ponderación de Votos"
    } else {
        // En otros casos, agregar los votos sin el símbolo de '%'
        chartData.forEach(item => {
            data.addRow([item.frente, item.votos, item.color, item.votos.toString()]);
        });
        titulo = "Distribución de Votos"
    }


    // Generar ticks del 0% al 100% en incrementos de 10%
    const ticks = [];
    for (let i = 0; i <= 10; i++) {
        const valor = (totalHabilitados / 10) * i; // Calcular cada 10%
        ticks.push({ v: valor, f: `${i * 10}%` });
    }

    const options = {
        title: titulo,
        titleTextStyle: {
            fontSize: 16, // Tamaño de fuente para el título
            bold: true,   // Negrita
        },
        backgroundColor: {
            fill: 'rgba(255, 255, 255, 0.1)', // Fondo blanco transparente
        },
        bars: 'horizontal',
        hAxis: {
            title: 'Votos',
            minValue: 0,
            maxValue: totalHabilitados,
            ticks: ticks,
            textStyle: {
                color: 'black' // Cambiar color de los textos del eje horizontal a blanco
            }
        },
        vAxis: {
            textStyle: {
                fontSize: 16,      // Tamaño del texto
                bold: true,        // Grosor del texto en negrita
                color: '#000000',  // Color del texto
            }
        },
        annotations: {
            textStyle: {
                fontSize: 16,      // Tamaño del texto
                bold: true,        // Grosor del texto en negrita
                color: '#000000',  // Color del texto
            }
        },
        legend: { position: 'none' },

    };

    // Generar el gráfico en el div correspondiente
    if (chartId) {
        const chart = new google.visualization.BarChart(document.getElementById(chartId));
        chart.draw(data, options);
    }
}

function updatePieChart(chartData, chartId) {
    // Crear un nuevo DataTable para el gráfico de torta
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Frente');
    data.addColumn('number', 'Votos');

    // Agregar los datos al DataTable de Google Charts
    chartData.forEach(item => {
        data.addRow([item.frente, item.votos]);
    });

    // Crear un formateador para los porcentajes
    const formatter = new google.visualization.NumberFormat({ pattern: '#,#0 %' });
    formatter.format(data, 1); // Formatear la segunda columna (valores de los votos)
    // Opciones del gráfico de torta
    const options = {
        title: 'Ponderación de Votos',
        titleTextStyle: {
            fontSize: 16, // Tamaño de fuente para el título
            bold: true,   // Negrita
        },
        pieHole: 0.3, // Agrega un agujero en el centro para un gráfico de tipo "donut" (opcional)
        legend: { position: 'right' },
        pieSliceText: 'value', // Muestra el valor en cada sección
        colors: chartData.map(item => item.color), // Colores personalizados para cada sección
        // Ajustar el tamaño dinámicamente en función de la ventana
        slices: chartData.map((item, index) => ({
            textStyle: { 
                color: 'black', 
                fontSize: window.innerWidth < 600 ? 10 : 16, // Tamaño de fuente ajustado para pantallas pequeñas
                bold: true 
            }
        })),
        width: '100%',
        height: window.innerWidth < 600 
            ? Math.max(300, window.innerHeight * 0.3) // Menor altura para pantallas pequeñas
            : Math.max(450, window.innerHeight * 0.4) // Altura para pantallas más grandes
    };

    // Generar el gráfico en el div correspondiente
    if (chartId) {
        const chart = new google.visualization.PieChart(document.getElementById(chartId));
        chart.draw(data, options);
    }
}