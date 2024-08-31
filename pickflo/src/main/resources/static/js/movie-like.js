/*
 * like.html 
 */

document.addEventListener('DOMContentLoaded', function() {
	function fetchMovies() {
	    // 찜한 영화 목록을 서버에서 가져오기
	    axios.get('/api/user/movies') // 사용자 찜한 영화 목록을 반환하는 API 경로
	        .then(response => {
	            const movies = response.data;
	            showMovieList(movies); // 영화 목록을 화면에 표시
	        })
	        .catch(error => {
	            console.error('Error fetching movies:', error);
	        });
	}

	function showMovieList(movies) {
	    const movieList = document.querySelector('main'); // 영화 목록을 넣을 곳 선택
	    movieList.innerHTML = ''; // 기존 영화 목록 초기화

	    // 영화 목록을 화면에 추가
	    movies.forEach(movie => {
	        const movieCard = document.createElement('div');
	        movieCard.classList.add('movie-card');
	        movieCard.innerHTML = `
	            <img src="${movie.movieImg}" alt="${movie.movieTitle}" class="poster-image">
	            <p>${movie.movieTitle}</p>
	        `;
	        movieList.appendChild(movieCard);
	    });
	}

	// 페이지 로드 시 찜한 영화 목록을 가져옴
	fetchMovies();
});

