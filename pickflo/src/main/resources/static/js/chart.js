/**
 * chart.html
 */
google.charts.load('current', { packages: ['corechart', 'bar'] });
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    axios.get('/api/getUserData') // 데이터를 가져오는 API
        .then(response => {
            const data = new google.visualization.DataTable();
            data.addColumn('string', '유저 그룹');
            data.addColumn('number', '페이지 방문 수');
            data.addColumn('number', '스크롤 수');

            // 응답 데이터를 사용하여 DataTable에 데이터 추가
            response.data.forEach(item => {
                data.addRow([item.userGroup, item.pageViews, item.scrollCount]);
            });

            const options = {
                title: 'A 그룹과 B 그룹의 통계',
                hAxis: { title: '유저 그룹' },
                vAxis: { title: '수치' },
                seriesType: 'bars',
                series: { 1: { type: 'line' } },
            };

            const chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
            chart.draw(data, options);
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
        });
}
