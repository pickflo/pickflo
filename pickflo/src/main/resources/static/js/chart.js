/**
 * chart.html
 */
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

	// 차트 데이터를 준비합니다.
	const labels = Object.keys(groups);
	const pageViewData = labels.map(group => groups[group].pageView);
	const scrollCountData = labels.map(group => groups[group].scrollCount);

	const ctx = document.getElementById('userStatsChart').getContext('2d');
	const userStatsChart = new Chart(ctx, {
		type: 'bar', // 차트 유형
		data: {
			labels: labels, // 그룹 A와 B의 레이블
			datasets: [
				{
					label: 'Page Views',
					data: pageViewData, // 페이지 방문 횟수 데이터
					backgroundColor: 'rgba(75, 192, 192, 0.2)',
					borderColor: 'rgba(75, 192, 192, 1)',
					borderWidth: 1
				},
				{
					label: 'Scroll Counts',
					data: scrollCountData, // 스크롤 횟수 데이터
					backgroundColor: 'rgba(153, 102, 255, 0.2)',
					borderColor: 'rgba(153, 102, 255, 1)',
					borderWidth: 1
				}
			]
		},
		options: {
			scales: {
				y: {
					beginAtZero: true
				}
			}
		}
	});
}

