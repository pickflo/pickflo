/**
 * home.html
 */

document.addEventListener('DOMContentLoaded', function() {
	axios.get('/api/home-recommendations', {
		params: { userId } // 실제로는 로그인 사용자 ID를 사용해야 함
	})
		.then(response => {
			const movies = response.data; // 서버에서 가져온 영화 데이터
			const movieListContainer = document.querySelector('.movie-list');

			// 영화 데이터를 movie-list에 동적으로 추가하기
			movies.forEach(movie => {
				const movieItem = document.createElement('div');
				movieItem.classList.add('movie-item');

				const movieImage = document.createElement('img');
				movieImage.src = movie.movieImg;
				movieImage.alt = 'Movie Image';
				movieImage.classList.add('movie-image');

				movieItem.appendChild(movieImage);
				movieListContainer.appendChild(movieItem);
			});
		})
		.catch(error => {
			console.error('There was an error fetching the movie data!', error);
		});
});