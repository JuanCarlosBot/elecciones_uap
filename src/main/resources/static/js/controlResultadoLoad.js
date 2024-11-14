$(document).ready(function () {
    google.charts.load('current', {
        packages: ['corechart', 'bar']
    });

    // Asegurarse de que el código solo se ejecute cuando Google Charts esté listo
    google.charts.setOnLoadCallback(function () {
        $('.js-example-basic-single').select2({
            placeholder: "Seleccione..."
        });
        // cargarGeneraTotal();
        cargarTablaGeneral();

        setInterval(cargarTablaGeneral, 25000);
        // cargarTablaPorMesa($('#tabla_resultado_mesa').val());

        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            const target = $(e.target).attr("href"); // Obtiene el ID del tab destino
            console.log(target);
            if (target === "#pie-chart") {
                // Llama a la función updatePieChart nuevamente para redibujar el gráfico de torta
                $.ajax({
                    type: 'POST',
                    url: "/cargarGeneral",
                    contentType: "application/json",
                    data: JSON.stringify({}),
                    success: function (response) {
                        updateChart(response.chartDataDoc, response.totalHabilitadosDoc, 'chart');
                        updateChart(response.chartDataEst, response.totalHabilitadosEst, 'chart5');
                        updatePieChart(response.chartDataTotal, 'chart6Torta');
                    },
                    error: function (xhr, status, error) {
                        console.error('Hubo un problema con la solicitud AJAX:', status, error);
                    }
                });
            }else{
                $.ajax({
                    type: 'POST',
                    url: "/cargarGeneral",
                    contentType: "application/json",
                    data: JSON.stringify({}),
                    success: function (response) {
                        updateChart(response.chartDataDoc, response.totalHabilitadosDoc, 'chart');
                        updateChart(response.chartDataEst, response.totalHabilitadosEst, 'chart5');
                        updateChart(response.chartDataTotal, 100, 'chart6');
                    },
                    error: function (xhr, status, error) {
                        console.error('Hubo un problema con la solicitud AJAX:', status, error);
                    }
                }); 
            }
        });
    });
});