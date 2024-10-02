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
			drawPageViewChart(data);
			drawScrollCountChart(data);
		})
		.catch(error => {
			console.error('Error fetching user data:', error);
		});
}

// 페이지 뷰 차트를 그리는 함수
function drawPageViewChart(userStatistics) {
	const groups = userStatistics.reduce((acc, stat) => {
		if (!acc[stat.userGroup]) {
			acc[stat.userGroup] = { pageView: 0, scrollCount: 0 };
		}
		acc[stat.userGroup].pageView += stat.pageView;
		return acc;
	}, {});

	const sortedGroups = Object.keys(groups).sort((a, b) => a.localeCompare(b));

	const data = new google.visualization.DataTable();
	data.addColumn('string', 'User Group');
	data.addColumn('number', 'Page Views');

	sortedGroups.forEach(group => {
		data.addRow([group, groups[group].pageView]);
	});

	const options = {
		title: 'Page Views by User Group',
	};

	const chart = new google.visualization.PieChart(document.getElementById('pageViewChart'));
	chart.draw(data, options);
}

// 스크롤 횟수 차트를 그리는 함수
function drawScrollCountChart(userStatistics) {
	const groups = userStatistics.reduce((acc, stat) => {
		if (!acc[stat.userGroup]) {
			acc[stat.userGroup] = { pageView: 0, scrollCount: 0 };
		}
		acc[stat.userGroup].scrollCount += stat.scrollCount;
		return acc;
	}, {});

	const sortedGroups = Object.keys(groups).sort((a, b) => a.localeCompare(b));

	const data = new google.visualization.DataTable();
	data.addColumn('string', 'User Group');
	data.addColumn('number', 'Scroll Counts');

	sortedGroups.forEach(group => {
		data.addRow([group, groups[group].scrollCount]);
	});

	const options = {
		title: 'Scroll Counts by User Group',
	};

	const chart = new google.visualization.PieChart(document.getElementById('scrollCountChart'));
	chart.draw(data, options);
}

// Google Charts 로드 후 차트 그리기
google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(fetchUserData);