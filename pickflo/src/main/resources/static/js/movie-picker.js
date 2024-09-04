/**
 * picker.html에 포함.
 */

document.addEventListener('DOMContentLoaded', () => {
	
    const layout = document.querySelector('div#layout');
    const goToHome = document.querySelector('div#home');

    let rn1 = 1;
    let rn2 = rn1 + 1;
    const rating = 6.5; // 영화 평점
    let isLoading = false; // 로딩 상태 추적
    let moviesBuffer = []; // 남은 영화를 저장할 배열
    const displayCount = 21; // 처음에 표시할 영화 수additionalDisplayCount

    // 영화 데이터를 서버에서 가져오는 함수
    async function loadMovies() {
        if (isLoading) return; // 이미 로딩 중이면 중단
        isLoading = true; // 로딩 상태로 설정

        try {
            const response = await axios.get(`/pickflo/api/picker/listByGenre`, {
                params: { rating, rn1, rn2 }
            });

            console.log("서버 응답:", response.data); // 응답 데이터 확인

            const moviesToDisplay = response.data; // 서버에서 받아온 영화 데이터

            if (moviesToDisplay.length > 0) {
                if (moviesBuffer.length === 0) {
                    // 처음 로드 시
                    moviesBuffer = moviesToDisplay.slice(displayCount); // 22번째부터 끝까지 저장
                    showMovies(moviesToDisplay.slice(0, displayCount)); // 처음 21개 영화 표시
                } else {
                    // 버퍼가 이미 있을 경우
                    moviesBuffer = moviesBuffer.concat(moviesToDisplay);
                    showMoviesFromBuffer(); // 버퍼에서 추가로 영화 표시
                }
                rn1 += 2;
            } else {
                console.log("더 이상 로드할 영화가 없습니다.");
            }

        } catch (error) {
            console.error("Error loading movies:", error);
        } finally {
            isLoading = false; // 로딩 상태 해제
        }
    }

    // 무한 스크롤 감지 함수
    window.addEventListener("scroll", () => {
        if ((window.innerHeight + window.scrollY) >= (document.documentElement.scrollHeight - 100)) {
            console.log("페이지 하단에 도달, 영화 데이터를 로드합니다."); 
            // 스크롤이 거의 끝에 도달하면 추가 데이터를 로드
            //loadMovies();
            
            // 남은 버퍼의 영화를 추가로 화면에 표시하거나, 버퍼가 비었으면 서버에서 추가 데이터를 불러옴
            if (moviesBuffer.length > 0) {
                showMoviesFromBuffer();
            } else {
                loadMovies();
            }
        }
    });
     // 버퍼에서 영화 데이터를 가져와 화면에 표시하는 함수
    function showMoviesFromBuffer() {
        const countToShow = Math.min(34 - moviesBuffer.length, moviesBuffer.length); // 남은 버퍼 수만큼 또는 남은 화면 수 만큼 표시
        const moviesToShow = moviesBuffer.splice(0, countToShow); // 버퍼에서 countToShow 개수만큼 잘라냄
        console.log(`보여줄 영화: ${moviesToShow}`);
        showMovies(moviesToShow);
    }

    // 초기 데이터 로드
    loadMovies();

    function showMovies(data) {
        let htmlStr = '';
        data.forEach(movie => {
            const id = movie.movieId;
            const title = movie.movieTitle;
            const img = movie.movieImg;

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
        });

        layout.insertAdjacentHTML('beforeend', htmlStr);
    }

    // 이벤트 위임을 사용하여 영화 선택 처리
    layout.addEventListener('click', (event) => {
        const target = event.target;

        // 클릭한 요소가 .overlay가 포함된 요소일 경우 처리
        if (target.closest('.overlay')) {
            const movieContainer = target.closest('.movie');
            if (movieContainer.classList.contains('selected')) {
                movieContainer.classList.remove('selected');
            } else {
                movieContainer.classList.add('selected');
            }
            goToHomeVisibility();
        }
    });

    // 선택한 영화가 3개 이상일 경우 홈으로 가기 텍스트 표시
    function goToHomeVisibility() {
        const selectedMovies = document.querySelectorAll('.selected');
        if (selectedMovies.length >= 3) {
            goToHome.classList.remove('d-none');
        } else {
            goToHome.classList.add('d-none');
        }
    }

    // 홈으로 가기 텍스트 클릭 이벤트
    goToHome.addEventListener('click', () => {
        const selectedMoviesId = Array.from(document.querySelectorAll('.selected')).map(movie => movie.getAttribute('movie-id'));

        const userId = document.getElementById('userId').value;

        const data = selectedMoviesId.map(movieId => ({ userId, movieId }));

		const uri = `/pickflo/api/usermovie/saveAll`;
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
				console.log(error);
			});
    });
});





/*document.addEventListener('DOMContentLoaded', () => {
	
    const layout = document.querySelector('div#layout');
    const goToHome = document.querySelector('div#home');

    let rn1 = 1;
    let rn2 = rn1 + 1;
    const rating = 6.5; // 영화 평점
    let isLoading = false; // 로딩 상태 추적

    // 영화 데이터를 서버에서 가져오는 함수
    async function loadMovies() {
        if (isLoading) return; // 이미 로딩 중이면 중단
        isLoading = true; // 로딩 상태로 설정

        try {
            const response = await axios.get(`/pickflo/api/picker/listByGenre`, {
                params: { rating, rn1, rn2 }
            });

            console.log("서버 응답:", response.data); // 응답 데이터 확인

            const moviesToDisplay = response.data; // 서버에서 받아온 영화 데이터

            if (moviesToDisplay.length > 0) {
                showMovies(moviesToDisplay); // 화면에 영화 표시
                rn1 += 2;
            } else {
                console.log("더 이상 로드할 영화가 없습니다.");
            }

        } catch (error) {
            console.error("Error loading movies:", error);
        } finally {
            isLoading = false; // 로딩 상태 해제
        }
    }

    // 무한 스크롤 감지 함수
    window.addEventListener("scroll", () => {
        if ((window.innerHeight + window.scrollY) >= (document.documentElement.scrollHeight - 100)) {
            console.log("페이지 하단에 도달, 영화 데이터를 로드합니다."); 
            // 스크롤이 거의 끝에 도달하면 추가 데이터를 로드
            loadMovies();
        }
    });

    // 초기 데이터 로드
    loadMovies();

    function showMovies(data) {
        let htmlStr = '';
        data.forEach(movie => {
            const id = movie.movieId;
            const title = movie.movieTitle;
            const img = movie.movieImg;

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
        });

        layout.insertAdjacentHTML('beforeend', htmlStr);
    }

    // 이벤트 위임을 사용하여 영화 선택 처리
    layout.addEventListener('click', (event) => {
        const target = event.target;

        // 클릭한 요소가 .overlay가 포함된 요소일 경우 처리
        if (target.closest('.overlay')) {
            const movieContainer = target.closest('.movie');
            if (movieContainer.classList.contains('selected')) {
                movieContainer.classList.remove('selected');
            } else {
                movieContainer.classList.add('selected');
            }
            goToHomeVisibility();
        }
    });

    // 선택한 영화가 3개 이상일 경우 홈으로 가기 텍스트 표시
    function goToHomeVisibility() {
        const selectedMovies = document.querySelectorAll('.selected');
        if (selectedMovies.length >= 3) {
            goToHome.classList.remove('d-none');
        } else {
            goToHome.classList.add('d-none');
        }
    }

    // 홈으로 가기 텍스트 클릭 이벤트
    goToHome.addEventListener('click', () => {
        const selectedMoviesId = Array.from(document.querySelectorAll('.selected')).map(movie => movie.getAttribute('movie-id'));

        const userId = document.getElementById('userId').value;

        const data = selectedMoviesId.map(movieId => ({ userId, movieId }));

		const uri = `/pickflo/api/usermovie/saveAll`;
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
				console.log(error);
			});
    });
});
*/

