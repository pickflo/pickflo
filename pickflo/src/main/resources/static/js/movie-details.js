/**
 * 
 */

function bindPosterImageClickEvent() {
	const posterImages = document.querySelectorAll('.poster-image');

	posterImages.forEach(image => {
		image.addEventListener('click', clickPosterImage);
	});

	function clickPosterImage() {
		const movieId = this.getAttribute('data-movie-id');

		axios.get(`/pickflo/api/movie/details`, { params: { movieId: movieId } })
			.then(response => {
				const movie = response.data;

				document.getElementById('title').textContent = movie.movieTitle;
				document.getElementById('synopsis').textContent = movie.movieOverview;
				document.getElementById('rating').textContent = movie.movieRating;
				document.getElementById('imgPoster').src = movie.movieImg;

				const releaseDate = new Date(movie.movieReleaseDate);
				const releaseYear = releaseDate.getFullYear();
				document.getElementById('releaseDate').textContent = releaseYear;

				const runtimeHours = Math.floor(movie.movieRuntime / 60);
				const runtimeMinutes = movie.movieRuntime % 60;
				const formattedRuntime = `${runtimeHours}시간 ${runtimeMinutes}분`;
				document.getElementById('runtime').textContent = formattedRuntime;

				const genreContainer = document.querySelector('.genre-badge-container');
				genreContainer.innerHTML = '';
				movie.genres.forEach(genre => {
					const span = document.createElement('span');
					span.className = 'badge';
					span.textContent = genre;
					genreContainer.appendChild(span);
				});

				const countryContainer = document.getElementById('country');
				countryContainer.innerHTML = movie.countries.join(' <span>∙</span> ');

				const actorContainer = document.querySelector('.actor-container');
				const directorContainer = document.querySelector('.director-container');

				const actors = [];
				const directors = [];

				movie.people.forEach(person => {
					if (person.job === 'Acting') {
						actors.push(person.personName);
					} else if (person.job === 'Director') {
						directors.push(person.personName);
					}
				});

				actorContainer.textContent = actors.join(', ') || '출연진 정보 없음';
				directorContainer.textContent = directors.join(', ') || '감독 정보 없음';
			})
			.catch(error => {
				console.error('Error fetching movie details:', error);
			});

		// 찜 상태 확인
		const userId = document.getElementById('userId').value;
		console.log('User ID:', userId); // 로그를 통해 userId 확인
		axios.get(`/pickflo/api/movie/like-status`, { params: { userId: userId, movieId: movieId } })
			.then(response => {
				const isFavorite = response.data;

				const iconHeart = document.getElementById('iconHeart');
				if (isFavorite) {
					// 찜 상태일 때
					iconHeart.classList.remove('fa-regular', 'fa-heart');
					iconHeart.classList.add('fa-solid', 'fa-heart');
					iconHeart.style.color = 'red'; // 찜 상태일 때 색상 변경
				} else {
					// 찜 상태가 아닐 때
					iconHeart.classList.remove('fa-solid', 'fa-heart');
					iconHeart.classList.add('fa-regular', 'fa-heart');
					iconHeart.style.color = 'white'; // 찜 상태가 아닐 때 색상 변경
				}
			})
			.catch(error => {
				console.error('Error checking favorite status:', error);
			});
	}
}
