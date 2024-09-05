
document.addEventListener('DOMContentLoaded', function() {
	let startRow = 0;
	let endRow = startRow + 21;
	let isLoading = false;

	function loadMovies() {
		if (isLoading) return;
		isLoading = true;

		axios.get(`/pickflo/api/home/recMovies?startRow=${startRow}&endRow=${endRow}`)
			.then(response => {
				console.log('API 응답 데이터:', response.data); // 응답 데이터 구조 확인
				const movies = response.data;
				//const movies = data.movies; // 배열을 포함하는 속성 추출
				const movieListDiv = document.querySelector('.movie-list');

				if (!movieListDiv) {
					console.error('Error: .movie-list element not found.');
					return;
				}

				if (Array.isArray(movies)) {
					movies.forEach(movie => {
						const movieCard = document.createElement('div');
						movieCard.className = 'movie-card';

						const img = document.createElement('img');
						img.src = movie.movieImg;
						img.alt = 'Movie Image';
						img.className = 'poster-image';

						img.setAttribute('data-movie-id', movie.movieId);
						img.setAttribute('data-bs-toggle', 'modal');
						img.setAttribute('data-bs-target', '#modalMovieDetails');

						movieCard.appendChild(img);

						movieListDiv.appendChild(movieCard);
					});
					startRow = endRow+1;
                    endRow = startRow + 20;
				} else {
					console.error('Error: 응답 데이터의 content가 배열이 아닙니다.');
				}

				isLoading = false;
			})
			.catch(error => {
				console.error('Error fetching movies:', error);
				isLoading = false;
			});
	}

	// 초기 로드
	loadMovies();

	// 스크롤 이벤트 리스너
	window.addEventListener('scroll', () => {
		if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {
			loadMovies();
		}
	});
});
