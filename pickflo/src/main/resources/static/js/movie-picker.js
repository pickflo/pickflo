/**
 * picker.html에 포함.
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
			const id = data[i].id;
			const title = data[i].movieTitle;
			const img = data[i].movieImg;

			htmlStr += `
				<div class="movie" movie-id="${id}">
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

	// 홈으로 가기 버튼 클릭 이벤트 
	goToHome.addEventListener('click', () => {
		// 선택된 영화 코드 배열로 가져오기
		const selectedMoviesCode = Array.from(document.querySelectorAll('.selected')).map(movie => movie.getAttribute('movie-id'));
		
		// TODO: 로그인 유저 아이디 가져오기
		const userId=1;	

		// 사용자가 선택한 영화 객체 생성
		const data = selectedMoviesCode.map(movieId => ({ userId, movieId }));

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
				window.location.href = '/pickflo';
			})
			.catch((error) => {
				console.log(error)
			});

});


});