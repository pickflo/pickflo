/**
 * admin/home.html
 */

document.addEventListener("DOMContentLoaded", function() {
    fetchUserData();
});

function fetchUserData() {
    fetch('/pickflo/api/user-statistics/getUserData') // 데이터 API를 호출합니다.
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
    // 각 그룹의 통계 합계 계산
    const groups = userStatistics.reduce((acc, stat) => {
        if (!acc[stat.userGroup]) {
            acc[stat.userGroup] = { timeSpentSeconds: 0, scrollCount: 0, likeCount: 0, unlikeCount: 0, pageTimes: {} };
        }
        acc[stat.userGroup].timeSpentSeconds += stat.timeSpentSeconds; // timeSpentSeconds 합산
        acc[stat.userGroup].scrollCount += stat.scrollCount; // scrollCount 합산
        acc[stat.userGroup].likeCount += stat.likeCount; // likeCount 합산
        acc[stat.userGroup].unlikeCount += stat.unlikeCount; // unlikeCount 합산

        return acc;
    }, {});

    console.log('Grouped Statistics:', groups); // 그룹 통계 출력

    // 총 체류 시간 비교 차트 생성
    drawTimeSpentComparisonChart(groups);
    
    // 원형 차트 데이터 생성
    const labels = ['A', 'B'];
    const datasets = createDatasets(labels, groups);
    
    // 각 원형 차트 그리기
    createCharts(labels, datasets);
}

// 총 사이트 이용시간
function drawTimeSpentComparisonChart(groups) {
    const labels = ['A그룹', 'B그룹'];
    const data = [
        groups['A']?.timeSpentSeconds || 0,
        groups['B']?.timeSpentSeconds || 0
    ];

    const ctx = document.getElementById('timeSpentComparisonChart').getContext('2d');
    new Chart(ctx, {
        type: 'pie', // 원형 차트 유형
        data: {
            labels: labels, // A그룹과 B그룹 레이블
            datasets: [{
                label: '사이트 이용 시간 (초)',
                data: data,
                backgroundColor: ['rgba(60, 179, 113, 0.6)', 'rgba(255, 165, 0, 0.6)'],
                borderColor: ['rgb(60, 179, 113)', 'rgb(255, 165, 0)'],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: true,
                    text: '총 사이트 이용시간',
                    color: 'white',
                    font: {
                        size: 25,
                        weight: 'bold'
                    }
                },
                legend: {
                    display: true, // 범례 표시
                    labels: {
                        color: 'white', // 범례 텍스트 색상
                        font: {
                            size: 13,
                        }
                    }
                },
                datalabels: {
                    formatter: (value, context) => {
                        const total = context.chart.data.datasets[0].data.reduce((acc, val) => acc + val, 0);
                        const percentage = ((value / total) * 100).toFixed(2) + '%';
                        return percentage; // 퍼센트 포맷으로 리턴
                    },
                    color: 'black',
                    font: {
                        size: 20,
                        weight: 'bold',
                    },
                    anchor: 'center',
                    align: 'center'
                }
            } 
        },
        plugins: [ChartDataLabels] // Datalabels 플러그인 추가
    });
}


// 차트 색상 정의
const CHART_COLORS = {
    background: 'rgba(255, 115, 102, 0.2)',
    border: 'rgba(0, 0, 0, 0)',
    groupColors: ['rgb(60, 179, 113)', 'rgb(255, 165, 0)']
};

function createDataset(label, data) {
    return {
        label: label,
        data: data,
        backgroundColor: CHART_COLORS.groupColors.map(color => `${color.replace(')', ', 0.6)')}`), // 투명도 추가
        borderColor: CHART_COLORS.border,
        borderWidth: 1
    };
}

function createDatasets(labels, groups) {
    return [
        createDataset('스크롤 횟수', labels.map(group => groups[group].scrollCount)),
        createDataset('좋아요 클릭 수', labels.map(group => groups[group].likeCount)),
        createDataset('좋아요 해제 수', labels.map(group => groups[group].unlikeCount))
    ];  
}

function createCharts(labels, datasets) {
    const chartIds = ['scrollCountChart', 'likeCountChart', 'unlikeCountChart'];
    const chartTitles = ['스크롤 횟수', '좋아요 클릭 수', '좋아요 해제 수']; // 각 차트의 제목
    const orderedLabels = ['A 그룹', 'B 그룹']; // A 그룹 -> B 그룹 순으로 레이블 정렬

    // 그룹 데이터 순서를 'A 그룹', 'B 그룹' 순으로 맞추기 위한 데이터 처리
    const orderedDatasets = datasets.map(dataset => {
        // A 그룹, B 그룹 순서대로 데이터 배열
        const orderedData = [
            dataset.data[labels.indexOf('A')], // A 그룹 데이터
            dataset.data[labels.indexOf('B')]  // B 그룹 데이터
        ];

        return {
            ...dataset,  // 기존 dataset 객체의 모든 속성을 복사
            data: orderedData // 데이터를 A -> B 순서로 설정
        };
    });

    orderedDatasets.forEach((dataset, index) => {
        const ctx = document.getElementById(chartIds[index]).getContext('2d');
        new Chart(ctx, {
            type: 'pie', // 차트 유형
            data: {
                labels: orderedLabels, // A 그룹이 먼저 나오도록 레이블 정렬
                datasets: [dataset]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false, // 비율 유지하지 않음
                plugins: {
                    title: {
                        display: true,
                        text: chartTitles[index],
                        color: 'white',
                        font: {
                            size: 25,
                            weight: 'bold'
                        }
                    },
                    legend: {
                        labels: {
                            color: 'white',
                            font: {
                                size: 13,
                            }
                        }
                    },
                    datalabels: {
                        formatter: (value, context) => {
                            const total = context.chart.data.datasets[0].data.reduce((acc, val) => acc + val, 0);
                            const percentage = ((value / total) * 100).toFixed(2) + '%';
                            return percentage; // 퍼센트 포맷으로 리턴
                        },
                        color: 'black',
                        font: {
                            size: 20,
                            weight: 'bold',
                        },
                        anchor: 'center',
                        align: 'center'
                    }
                }
            },
            plugins: [ChartDataLabels] // Datalabels 플러그인 추가
        });
    });
}
