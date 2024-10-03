document.addEventListener("DOMContentLoaded", function() {
	fetchUserData();
	fetchWeeklyData();
});

function fetchUserData() {
	fetch('/pickflo/api/chart/getUserData') // 데이터 API를 호출
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok');
			}
			return response.json();
		})
		.then(data => {
			// 데이터를 가져와서 차트를 그린다
			drawChart(data, 'pageView', 'pageViewChart');
            drawChart(data, 'scrollCount', 'scrollCountChart');
            drawChart(data, 'likeCount', 'likeCountChart');
            drawChart(data, 'unlikeCount', 'unlikeCountChart');
            
            drawWeeklyChart(data, 'pageView', 'pageViewChartWeek');
            drawWeeklyChart(data, 'scrollCount', 'scrollCountChartWeek');
            drawWeeklyChart(data, 'likeCount', 'likeCountChartWeek');
            drawWeeklyChart(data, 'unlikeCount', 'unlikeCountChartWeek');
		})
		.catch(error => {
			console.error('Error fetching user data:', error);
		});
}


// 차트를 그리는 함수
function drawChart(userStatistics, metric, elementId) {
    const groups = userStatistics.reduce((acc, stat) => {
        if (!acc[stat.userGroup]) {
            acc[stat.userGroup] = { pageView: 0, scrollCount: 0, likeCount: 0, unlikeCount: 0 };
        }
        acc[stat.userGroup][metric] += stat[metric]; // 동적으로 메트릭에 따라 값 업데이트
        return acc;
    }, {});

    const sortedGroups = Object.keys(groups).sort((a, b) => a.localeCompare(b));

    const data = new google.visualization.DataTable();
    data.addColumn('string', 'User Group');
    data.addColumn('number', metric.charAt(0).toUpperCase() + metric.slice(1)); // 첫 글자 대문자

    sortedGroups.forEach(group => {
		const displayGroup = group === 'A' ? 'A그룹' : group === 'B' ? 'B그룹' : group;
        data.addRow([displayGroup, groups[group][metric]]);
    });

	const options = {
		title: `${metric.charAt(0).toUpperCase() + metric.slice(1)}`, // 제목 설정
		backgroundColor: '#141414',
		colors: ['#ff9999', '#66b3ff'],
		titleTextStyle: {
			color: '#FFFFFF',
			fontSize: 24
		},
		legend: {
			textStyle: {
				color: '#FFFFFF',
				fontSize: 18
			}
		}
	};

    const chart = new google.visualization.PieChart(document.getElementById(elementId));
    chart.draw(data, options);
}

function drawWeeklyChart(weeklyStatistics, metric, elementId) {
    const data = new google.visualization.DataTable();
    data.addColumn('string', '주');
    data.addColumn('number', metric.charAt(0).toUpperCase() + metric.slice(1)); // 첫 글자 대문자

    weeklyStatistics.forEach(stat => {
        const weekLabel = `${stat.weekStartDate} ~ ${stat.weekEndDate}`;
        data.addRow([weekLabel, stat[metric]]);
    });

	const options = {
		title: `${metric.charAt(0).toUpperCase() + metric.slice(1)} 주간 통계`, // 제목 설정
		backgroundColor: '#141414',
		colors: ['#ff9999', '#66b3ff'],
		titleTextStyle: {
			color: '#FFFFFF',
			fontSize: 24
		},
		legend: {
			textStyle: {
				color: '#FFFFFF',
				fontSize: 18
			}
		}
	};

    const chart = new google.visualization.ColumnChart(document.getElementById(elementId)); // 변경: 막대 차트로 변경
    chart.draw(data, options);
}


function fetchWeeklyData() {
	fetch('/pickflo/api/chart/getUserData') // 주간 통계 데이터 API 호출
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok');
			}
			return response.json();
		})
		.then(data => {
			// 주간 통계로 차트를 그린다
			drawWeeklyChart(data, 'pageView', 'pageViewChart');
            drawWeeklyChart(data, 'scrollCount', 'scrollCountChart');
            drawWeeklyChart(data, 'likeCount', 'likeCountChart');
            drawWeeklyChart(data, 'unlikeCount', 'unlikeCountChart');
		})
		.catch(error => {
			console.error('Error fetching weekly data:', error);
		});
}

function drawWeeklyChart(weeklyStatistics, metric, elementId) {
    const data = new google.visualization.DataTable();
    data.addColumn('string', '주');
    data.addColumn('number', metric.charAt(0).toUpperCase() + metric.slice(1)); // 첫 글자 대문자

    weeklyStatistics.forEach(stat => {
        const weekLabel = `${stat.weekStartDate} ~ ${stat.weekEndDate}`;
        data.addRow([weekLabel, stat[metric]]);
    });

	const options = {
		title: `${metric.charAt(0).toUpperCase() + metric.slice(1)} 주간 통계`, // 제목 설정
		backgroundColor: '#141414',
		colors: ['#ff9999', '#66b3ff'],
		titleTextStyle: {
			color: '#FFFFFF',
			fontSize: 24
		},
		legend: {
			textStyle: {
				color: '#FFFFFF',
				fontSize: 18
			}
		}
	};

    const chart = new google.visualization.ColumnChart(document.getElementById(elementId)); // 막대 차트로 변경
    chart.draw(data, options);
}

// Google Charts 로드 후 차트 그리기
google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(fetchUserData);