/**
 * picker.html에 포함.
 */
document.addEventListener('DOMContentLoaded', () => {
    const layout = document.querySelector('div#layout');
    const goToHome = document.querySelector('div#home');

    let currentPage = 0; // 현재 페이지 번호
    const itemsPerPage = 18; // 한 페이지당 항목 수
    let allMovies = []; // 서버에서 받아온 전체 영화 데이터
    const rating = 6.0; // 영화 인기도
    let isLoading = false; // 로딩 상태 추적

    // 영화 데이터를 서버에서 가져오는 함수
    async function loadMovies() {
        if (isLoading) return; // 이미 로딩 중이면 중단
        isLoading = true; // 로딩 상태로 설정

        try {
            if (allMovies.length === 0) { // 처음 로드할 때만 서버에서 데이터를 가져옴
                const response = await axios.get(`/pickflo/api/picker/listByGenre`, {
                    params: { rating }
                });
                allMovies = response.data; // 서버에서 받아온 전체 영화 데이터를 저장
            }

            // 현재 페이지에 해당하는 데이터만 슬라이스
            const start = currentPage * itemsPerPage;
            const end = start + itemsPerPage;
            const moviesToDisplay = allMovies.slice(start, end);

            showMovies(moviesToDisplay); // 화면에 영화 표시

            if (moviesToDisplay.length > 0) {
                currentPage++; // 페이지 증가
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

        // 새로 추가된 영화 요소에 이벤트 리스너를 등록
        const likeMovies = document.querySelectorAll('.overlay');
        likeMovies.forEach(movie => {
            movie.removeEventListener('click', handleMovieClick); // 기존 이벤트 리스너 제거
            movie.addEventListener('click', handleMovieClick); // 새로운 이벤트 리스너 등록
        });
    }

    function handleMovieClick(event) {
        const movie = event.currentTarget;
        const movieContainer = movie.closest('.movie');
        if (movieContainer.classList.contains('selected')) {
            movieContainer.classList.remove('selected');
        } else {
            movieContainer.classList.add('selected');
        }
        goToHomeVisibility();
    }

    // 선택한 영화가 3개 이상일 경우 홈으로 가기 표시
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
        const selectedMoviesCode = Array.from(document.querySelectorAll('.selected')).map(movie => movie.getAttribute('movie-id'));

        const userId = document.getElementById('userId').value;

        const data = selectedMoviesCode.map(movieId => ({ userId, movieId }));

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
                console.log(error);
            });
    });
});

