/**
 *
 */
document.addEventListener('DOMContentLoaded', () => {

	const layout = document.querySelector('div#layout');
	const goToHome = document.querySelector('div#home');


	// 영화 DB data 불러오기
	const uri = `/pickflo/api/picker/list`;
	axios
		.get(uri)
		.then((response) => {
			getMovies(response.data);
		})
		.catch((error) => {
			console.log(error);
		});

	function getMovies(data) {

		let htmlStr = '';
		for (let i = 0; i < data.length; i++) {
			const code = data[i].movieCode;
			const title = data[i].movieTitle;
			const img = data[i].movieImg;

			htmlStr += `
				<div class="movie" code="${code}">
					<div class="poster"> 
						<img class="poster-image" src="${img}" alt="${title}" />
						<div class="overlay">
							<i class="bi bi-check-circle"></i>
						</div>
					</div>
				</div>
			`;
		}
		layout.insertAdjacentHTML('beforeend', htmlStr);

		// 영화 선택 시, 클래스 selected 추가
		const likeMovies = document.querySelectorAll('.overlay');
		likeMovies.forEach(movie => {
			movie.addEventListener('click', () => {
				const movieContainer = movie.closest('.movie');
				if (movieContainer.classList.contains('selected')) {
					movieContainer.classList.remove('selected');
				} else {
					movieContainer.classList.add('selected');
				}
				goToHomeVisibility();
			});
		});
	}

	// 선태한 영화가 3개 이상일 경우 홈으로 가기 표시
	function goToHomeVisibility() {
		const selectedMovies = document.querySelectorAll('.selected');
		if (selectedMovies.length >= 3) {
			goToHome.classList.remove('d-none');
		} else {
			goToHome.classList.add('d-none');
		}
	}

const title=document.querySelector('.intro-title');
	// 홈으로 가기 버튼 클릭 이벤트 
	goToHome.addEventListener('click', (event) => {
		// 선택된 영화 코드 배열로 가져오기
		const selectedMoviesCode = Array.from(document.querySelectorAll('.selected')).map(movie => movie.getAttribute('code'));
		console.log(`!!!!!!!!!!${selectedMoviesCode}`);
		// 사용자가 선택한 영화 객체 생성
		const userId=1;	

		const data = selectedMoviesCode.map(movieId => ({ userId, movieId }));
		console.log(`----------------data=${JSON.stringify(data[0])}`);

		// 선택된 영화 목록 DB 저장
		const uri = `/pickflo/api/usermovie/save`;
		axios
			.post(uri, data, {
				headers: {
					'Content-Type': 'application/json'
				}
			})
			.then((response) => {
				console.log(response.data);
				window.location.href = '/pickflo/home';
			})
			.catch((error) => {
				if (error.response) {
					// 서버가 상태 코드로 응답했으나 오류가 발생한 경우
					console.log('Error Response:', error.response.data);
					console.log('Error Status:', error.response.status);
					console.log('Error Headers:', error.response.headers);
				} else if (error.request) {
					// 요청이 전송되었으나 응답을 받지 못한 경우
					console.log('Error Request:', error.request);
				} else {
					// 오류를 발생시킨 요청 설정
					console.log('Error Message:', error.message);
				}
				console.log('Error Config:', error.config);
			});


});


});