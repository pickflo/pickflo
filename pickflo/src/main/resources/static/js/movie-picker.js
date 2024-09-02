/**
 * picker.html에 포함.
 */
document.addEventListener('DOMContentLoaded', () => {

	const layout = document.querySelector('div#layout');
	const goToHome = document.querySelector('div#home');
    //let page = 1;
    let limit = 10; // 각 요청당 가져올 영화 수
    const rating = 6.0;
    let offset=0;
    //let page=1;
    let loadedMovieIds = new Set(); // 로드된 영화의 ID를 저장
	let isLoading = false; // 로딩 상태를 추적

	/*// 영화 DB data 불러오기
	const uri = `/pickflo/api/picker/list`;
	axios
		.get(uri)
		.then((response) => {
			getMovies(response.data);
		})
		.catch((error) => {
			console.log(error);
		});
*/
	
	// 영화 데이터를 서버에서 가져오는 함수
	async function loadMovies() { // 비동기 함수 선언
		if (isLoading) return; // 이미 로딩 중이라면 중단
	        isLoading = true; // 로딩 상태로 설정
		try {
			const excludedMovieIds = Array.from(loadedMovieIds).join(','); // 이미 로드된 영화 ID를 문자열로 변환

			// 서버로부터 데이터를 비동기적으로 가져오기
			const response = await axios.get(`/pickflo/api/picker/listByGenre`, {
				params: {
					rating, excludedMovieIds, offset, limit
				}
			});
			console.log("API response:", response.data); // API 응답을 확인
			const data = response.data; // axios는 응답 데이터를 response.data에 담습니다.
			getMovies(data);
			
			offset += limit;

		} catch (error) {
			console.error("Error loading movies:", error);
		} finally {
			isLoading = false; // 로딩 상태를 해제
		}
	}

	// 무한 스크롤 감지 함수
	window.addEventListener("scroll", () => {
		console.log("Scroll event triggered");
		if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {
			loadMovies();
		}
	});
	
	loadMovies();


   /* function loadMovies(rating, size) {
        isLoading = true;
        const uri = `/pickflo/api/picker/listByGenre?rating=${rating}&size=${size}`;
        axios
            .get(uri)
            .then((response) => {
                getMovies(response.data);
                isLoading = false;
            })
            .catch((error) => {
                console.log(error);
                isLoading = false;
            });
    }*/

	function getMovies(data) {

		let htmlStr = '';
		data.forEach(movie => {
			const id = movie.movieId;
			const title = movie.movieTitle;
			const img = movie.movieImg;

			 // 영화 ID가 이미 로드된 목록에 있으면 스킵
            if (loadedMovieIds.has(id)) return;
            
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

			// 영화 ID를 로드된 목록에 추가
			loadedMovieIds.add(id);
		});

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
		const userId = document.getElementById('userId').value;

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