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
			drawChart(data, 'pageView', 'pageViewChart');
            drawChart(data, 'scrollCount', 'scrollCountChart');
            drawChart(data, 'likeCount', 'likeCountChart');
		})
		.catch(error => {
			console.error('Error fetching user data:', error);
		});
}


// 차트를 그리는 함수
function drawChart(userStatistics, metric, elementId) {
    const groups = userStatistics.reduce((acc, stat) => {
        if (!acc[stat.userGroup]) {
            acc[stat.userGroup] = { pageView: 0, scrollCount: 0, likeCount: 0 };
        }
        acc[stat.userGroup][metric] += stat[metric]; // 동적으로 메트릭에 따라 값 업데이트
        return acc;
    }, {});

    const sortedGroups = Object.keys(groups).sort((a, b) => a.localeCompare(b));

    const data = new google.visualization.DataTable();
    data.addColumn('string', 'User Group');
    data.addColumn('number', metric.charAt(0).toUpperCase() + metric.slice(1)); // 첫 글자 대문자

    sortedGroups.forEach(group => {
        data.addRow([group, groups[group][metric]]);
    });

    const options = {
        title: `${metric.charAt(0).toUpperCase() + metric.slice(1)} by User Group`, // 제목 설정
        backgroundColor: '#141414', // 검정 배경
        colors: ['#ff9999', '#66b3ff'], // 파스텔 톤 색상
        titleTextStyle: { color: '#FFFFFF' }, // 폰트 색상 흰색
        legend: {
            textStyle: { color: '#FFFFFF' } // 범례 색상 흰색
        }
    };

    const chart = new google.visualization.PieChart(document.getElementById(elementId));
    chart.draw(data, options);
}

// Google Charts 로드 후 차트 그리기
google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(fetchUserData);