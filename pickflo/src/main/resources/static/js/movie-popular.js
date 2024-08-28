/**
 * 
 */
document.addEventListener('DOMContentLoaded', () => {

	const layout=document.querySelector('div#layout');
	const goToHome=document.querySelector('div#home');
	
	
	// 인기영화 DB data 불러오기
	const uri = `/pickflo/api/popular/list`;
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
			const movieId = data[i].movieId;
			const title = data[i].title;
			const img = data[i].img;
			
			htmlStr += `
				<div class="movie">
					<div class="poster"> 
						<img class="poster-image" src="${img}" alt="${title}" />
						<div class="overlay"></div>
					</div>
				</div>
			`;
		}
		layout.insertAdjacentHTML('beforeend', htmlStr);

		// 선택 아이콘
		const likeMovies = document.querySelectorAll('.overlay');
		likeMovies.forEach(movie => {
			movie.addEventListener('click', () => {
				const movieContainer=movie.closest('.movie');
				if (movieContainer.classList.contains('selected')) {
					movieContainer.classList.remove('selected');
				} else {
					movieContainer.classList.add('selected');
				}
				goToHomeVisibility();
			});
		});
	}
	
	function goToHomeVisibility() {
		// 선태한 영화가 3개 이상일 경우 홈 아이콘 표시
		const selectedMovie = document.querySelectorAll('.selected');
		if (selectedMovie.length >= 3) {
			goToHome.classList.remove('d-none');
		} else {
			goToHome.classList.add('d-none');
		}
	}


});
	