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

		// 하트 아이콘 클릭 시 호출되는 함수
		const iconHeart = document.getElementById('iconHeart');
		iconHeart.addEventListener('click', FavoriteBtn);

		function FavoriteBtn() {
			//const movieId = iconHeart.getAttribute('data-movie-id'); // 영화 ID 가져오기
			//const userId = document.getElementById('userId').value; // 유저 ID 가져오기

			// 영화 ID를 로그로 확인
			console.log("Movie ID:", movieId);
			//유저 ID 확인
			console.log("User ID:", userId);

			// 현재 하트 아이콘 상태에 따라 AJAX 요청을 보냅니다.
			if (iconHeart.classList.contains('fa-regular')) {
				// 찜 추가
				axios.get('/pickflo/api/movie/like', {
					params: {
						movieId: movieId,
						userId: userId
					},
					/*headers: {
						'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
					}*/
				})
					.then(response => {
						iconHeart.classList.remove('fa-regular', 'fa-heart');
						iconHeart.classList.add('fa-solid', 'fa-heart');
						iconHeart.style.color = 'red'; // 찜 상태일 때 색상
					})
					.catch(error => console.error('Error:', error));
			} else {
				// 찜 해제
				axios.get('/pickflo/api/movie/unlike', {
					params: {
						movieId: movieId,
						userId: userId
					},
					/*headers: {
						'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
					}*/
				})
					.then(response => {
						// 서버에서 "no" 응답이 오면 경고창을 띄웁니다.
						if (response.data === 'no') {
							alert('찜한 콘텐츠는 3개이상이어야 합니다.');
						} else {
							// 정상적으로 해제되었을 때 아이콘 업데이트
							iconHeart.classList.remove('fa-solid', 'fa-heart');
							iconHeart.classList.add('fa-regular', 'fa-heart');
							iconHeart.style.color = '#ffffff'; // 찜 해제 상태일 때 색상
						}
					})
					.catch(error => console.error('Error:', error));
			}
		}

	}


}