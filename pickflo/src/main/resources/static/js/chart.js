document.addEventListener("DOMContentLoaded", function() {
    fetchUserData();
});

function fetchUserData() {
    fetch('/pickflo/api/chart/getUserData') // 데이터 API를 호출합니다.
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // 데이터를 가져와서 차트를 그립니다.
            drawChart(data);
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
        });
}

function drawChart(userStatistics) {
    // 각 그룹의 페이지 방문 및 스크롤 횟수 계산
    const groups = userStatistics.reduce((acc, stat) => {
        if (!acc[stat.userGroup]) {
            acc[stat.userGroup] = { pageView: 0, scrollCount: 0 };
        }
        acc[stat.userGroup].pageView += stat.pageView;
        acc[stat.userGroup].scrollCount += stat.scrollCount;
        return acc;
    }, {});

    // Google Charts에 전달할 데이터 준비
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'User Group');
    data.addColumn('number', 'Page Views');
    data.addColumn('number', 'Scroll Counts');

    Object.keys(groups).forEach(group => {
        data.addRow([group, groups[group].pageView, groups[group].scrollCount]);
    });

    // 차트 생성
    const options = {
        title: 'User Statistics',
        hAxis: {
            title: 'User Group'
        },
        vAxis: {
            title: 'Count',
            minValue: 0
        },
        legend: { position: 'top', alignment: 'center' }
    };

    const chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
    chart.draw(data, options);
}

// Google Charts 로드 후 차트 그리기
google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(fetchUserData);
