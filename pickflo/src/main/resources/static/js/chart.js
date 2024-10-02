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
         	drawPageViewChart(data);
            drawScrollCountChart(data);
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
        });
}

function drawPageViewChart(userStatistics) {
    // 각 그룹의 페이지 방문 횟수 계산
    const groups = userStatistics.reduce((acc, stat) => {
        if (!acc[stat.userGroup]) {
            acc[stat.userGroup] = { pageView: 0, scrollCount: 0 };
        }
        acc[stat.userGroup].pageView += stat.pageView;
        return acc;
    }, {});

    // Google Charts에 전달할 데이터 준비 (페이지 뷰 차트)
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'User Group');
    data.addColumn('number', 'Page Views');

    Object.keys(groups).forEach(group => {
        data.addRow([group, groups[group].pageView]);
    });

    // 차트 옵션 설정 (페이지 뷰 차트)
    const options = {
        title: 'Page Views by User Group',
        hAxis: { title: 'User Group' },
        vAxis: { title: 'Page Views', minValue: 0 },
        legend: { position: 'none' }
    };

    const pageViewChart = new google.visualization.ColumnChart(document.getElementById('pageViewChart_div'));
    pageViewChart.draw(data, options);
}

function drawScrollCountChart(userStatistics) {
    // 각 그룹의 스크롤 횟수 계산
    const groups = userStatistics.reduce((acc, stat) => {
        if (!acc[stat.userGroup]) {
            acc[stat.userGroup] = { pageView: 0, scrollCount: 0 };
        }
        acc[stat.userGroup].scrollCount += stat.scrollCount;
        return acc;
    }, {});

    // Google Charts에 전달할 데이터 준비 (스크롤 카운트 차트)
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'User Group');
    data.addColumn('number', 'Scroll Counts');

    Object.keys(groups).forEach(group => {
        data.addRow([group, groups[group].scrollCount]);
    });

    // 차트 옵션 설정 (스크롤 카운트 차트)
    const options = {
        title: 'Scroll Counts by User Group',
        hAxis: { title: 'User Group' },
        vAxis: { title: 'Scroll Counts', minValue: 0 },
        legend: { position: 'none' }
    };

    const scrollCountChart = new google.visualization.ColumnChart(document.getElementById('scrollCountChart_div'));
    scrollCountChart.draw(data, options);
}

// Google Charts 로드 후 차트 그리기
google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(fetchUserData);
