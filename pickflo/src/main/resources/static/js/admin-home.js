let selectedDate = new Date(); 

document.addEventListener("DOMContentLoaded", function() {
    // 페이지 로드 시 이번 주 통계 가져오기
    updateDateInfo(selectedDate); // 주차 정보 표시
    fetchUserData(selectedDate);

/*
    // 날짜 선택 버튼에 이벤트 리스너 추가
    document.getElementById('datePicker').addEventListener('change', (event) => {
        selectedDate = new Date(event.target.value); // 선택한 날짜로 설정
        updateDateInfo(selectedDate);
        fetchUserData(selectedDate);
    });
    */
});

// 날짜 정보 업데이트 함수
function updateDateInfo(date) {
    const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
    const formattedDate = date.toLocaleDateString(undefined, options); // 날짜 형식화
    const dateInfoElement = document.getElementById('dateInfo');
    dateInfoElement.textContent = `선택된 날짜: ${formattedDate}`; // 선택한 날짜 정보 표시
}

function fetchUserData(weekOffset) {
 const formattedDate = date.toISOString().split('T')[0]; // 'YYYY-MM-DD' 형식으로 변환
    fetch(`/pickflo/api/chart/getUserData?date=${formattedDate}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('네트워크 응답이 올바르지 않습니다.');
            }
            return response.json();
        })
        .then(data => {
			
            // 데이터를 배열이 아닌 객체로 받아올 경우도 처리
            if (!Array.isArray(data)) {
                data = [data]; // 객체를 배열로 변환
            }
            clearCharts(); // 새로운 차트를 그리기 전에 이전 차트 지우기
            updateCharts(data); // 새로운 데이터로 차트 업데이트
        })
        .catch(error => {
            console.error('Fetch 오류:', error);
        });
}

// 이전 차트를 지우는 함수
function clearCharts() {
    document.getElementById('pageViewChart').innerHTML = '';
    document.getElementById('scrollCountChart').innerHTML = '';
    document.getElementById('likeCountChart').innerHTML = '';
    document.getElementById('unlikeCountChart').innerHTML = '';
}

// 메트릭을 기반으로 차트를 업데이트하는 함수
function updateCharts(userStatistics) {
    drawChart(userStatistics, 'pageView', 'pageViewChart');
    drawChart(userStatistics, 'scrollCount', 'scrollCountChart');
    drawChart(userStatistics, 'likeCount', 'likeCountChart');
    drawChart(userStatistics, 'unlikeCount', 'unlikeCountChart');
}

// 개별 메트릭을 그리는 함수
function drawChart(userStatistics, metric, elementId) {
    const groups = {}; 

    // userStatistics 배열을 순회하며 각 그룹의 메트릭을 합산
    userStatistics.forEach(stat => {
        if (!groups[stat.userGroup]) {
            groups[stat.userGroup] = { [metric]: 0 };
        }
        groups[stat.userGroup][metric] += stat[metric]; // 메트릭에 따라 값 업데이트
    });

    // 차트를 그리기 위한 데이터 구성
    const data = new google.visualization.DataTable();
    data.addColumn('string', '사용자 그룹');
    data.addColumn('number', metric.charAt(0).toUpperCase() + metric.slice(1)); // 첫 글자 대문자

    Object.keys(groups).forEach(group => {
        const displayGroup = group === 'A' ? 'A그룹' : group === 'B' ? 'B그룹' : group;
        data.addRow([displayGroup, groups[group][metric]]);
    });

    const options = {
        title: `${metric.charAt(0).toUpperCase() + metric.slice(1)}`, // 제목
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

    const chart = new google.visualization.PieChart(document.getElementById(elementId)); // 원형 차트
    chart.draw(data, options);
}

// Google Charts 로드 후 차트 그리기
google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(() => fetchUserData(currentWeekOffset)); // 데이터 가져오기 
