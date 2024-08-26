/**
 * 
 */

document.addEventListener('DOMContentLoaded', function() {
    // TMDb API 키 및 기본 URL 설정
    const apiKey = 'a03e20d82852309465ec0d0f691d5ed9'; 
    const baseUrl = 'https://api.themoviedb.org/3';
    const imageBaseUrl = 'https://image.tmdb.org/t/p/w500';

    // DOM 요소 선택
    const searchInput = document.getElementById('search-input');
    const moviesContainer = document.getElementById('movies-container');
    const genreButtons = document.querySelectorAll('.searching-category[data-type="genre"] .btn');
    const countryButtons = document.querySelectorAll('.searching-category[data-type="country"] .btn');

    // 선택된 장르 및 국가
    let selectedGenre = null;
    let selectedCountry = null;

    // 저장된 영화 데이터
    let allMovies = [];
    let filteredMovies = [];

    // 버튼의 선택 상태를 관리하는 함수
    function handleSelection(button, type) {
        if (type === 'genre') {
	        if (selectedGenre === button) {
	            // 이미 선택된 버튼을 다시 클릭하면 해제
	            selectedGenre.classList.remove('selected');
	            selectedGenre = null;
	        } else {
	            // 다른 장르 버튼이 선택되어 있으면 해제
	            if (selectedGenre) {
	                selectedGenre.classList.remove('selected');
	            }
	            button.classList.add('selected');
	            selectedGenre = button;
	        }
	    } else if (type === 'country') {
	        if (selectedCountry === button) {
	            // 이미 선택된 버튼을 다시 클릭하면 해제
	            selectedCountry.classList.remove('selected');
	            selectedCountry = null;
	        } else {
	            // 다른 국가 버튼이 선택되어 있으면 해제
	            if (selectedCountry) {

	                selectedCountry.classList.remove('selected');
	            }
	            button.classList.add('selected');
	            selectedCountry = button;
	        }
	    }
	}

    // 영화 필터링 함수
    const filterMovies = () => {
        let result = allMovies;
		
		// 장르 필터링
        if (selectedGenre) {
            const genreId = selectedGenre.getAttribute('data-value');
            result = result.filter(movie => movie.genre_ids.includes(parseInt(genreId)));
        }
		
		// 국가 필터링
		if (selectedCountry) {
            const countryCode = selectedCountry.getAttribute('data-value');
            result = result.filter(movie => 
                movie.production_countries.some(country => country.iso_3166_1 === countryCode)
            );
        }

        // 결과 렌더링
        renderMovies(result);
		
		/*
        if (selectedCountry) {
            const countryCode = selectedCountry.getAttribute('data-value');
            fetch(`${baseUrl}/discover/movie?api_key=${apiKey}&language=ko-KR&region=${countryCode}`)
                .then(response => response.json())
                .then(data => {
                    const countryFilteredMovies = data.results;
                    result = result.filter(movie => countryFilteredMovies.some(countryMovie => countryMovie.id === movie.id));
                    renderMovies(result);
                })
                .catch(error => console.error('Error fetching data:', error));
        } else {
            renderMovies(result);
        } */
    };

    // 영화 카드를 생성하는 함수
    const createMovieCard = (movie) => {
        const { id, title, poster_path } = movie;

        const card = document.createElement('div');
        const image = document.createElement('img');
        const titleElement = document.createElement('h2');

        card.className = 'movie-card';
        image.className = 'poster-image';
        titleElement.className = 'title';

        // 포스터 이미지가 없을 경우 대체 이미지 제공
        image.src = poster_path 
            ? `${imageBaseUrl}${poster_path}` 
            : 'https://via.placeholder.com/500x750?text=No+Image';

        titleElement.textContent = title;

        card.appendChild(image);
        card.appendChild(titleElement);

        // 영화 클릭 시 상세 페이지로 이동
        card.addEventListener('click', () => {
            window.location.href = `details.html?id=${id}`;
        });

        return card;
    };

    // 검색 결과를 렌더링하는 함수
    const renderMovies = (movies) => {
        moviesContainer.innerHTML = ''; // 기존 내용을 지우기 위해 초기화
        if (movies.length === 0) {
            moviesContainer.innerHTML = '<p>No results found.</p>';
            return;
        }

        movies.forEach(movie => {
            const movieCard = createMovieCard(movie);
            moviesContainer.appendChild(movieCard);
        });
    };

    // 영화 목록을 가져오는 함수
    const fetchMovies = async () => {
        try {
            const response = await fetch(`${baseUrl}/discover/movie?api_key=${apiKey}&language=ko-KR`);
            const data = await response.json();
            if (data.results) {
                // 영화의 상세 정보를 가져오는 함수 호출
                allMovies = await Promise.all(data.results.map(async movie => {
                    const movieDetailResponse = await fetch(`${baseUrl}/movie/${movie.id}?api_key=${apiKey}&language=ko-KR`);
                    const movieDetail = await movieDetailResponse.json();
                    return { ...movie, production_countries: movieDetail.production_countries };
                }));
                filteredMovies = allMovies; // 초기 필터링은 전체 영화
                filterMovies(); // 필터 적용
            }
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    /*
    const fetchMovies = () => {
        fetch(`${baseUrl}/discover/movie?api_key=${apiKey}&language=ko-KR`)
            .then(response => response.json())
            .then(data => {
                if (data.results) {
                    allMovies = data.results;
                    filteredMovies = allMovies; // 초기 필터링은 전체 영화
                    filterMovies(); // 필터 적용
                }
            })
            .catch(error => console.error('Error fetching data:', error));
    }; */

    // 영화 검색 함수
    const searchMovies = async (query) => {
        if (!query) {
            fetchMovies(); // 검색어가 없는 경우 전체 영화 목록을 가져옴
        } else {
            try {
                const response = await fetch(`${baseUrl}/search/movie?api_key=${apiKey}&query=${encodeURIComponent(query)}&language=ko-KR`);
                const data = await response.json();
                if (data.results) {
                    allMovies = await Promise.all(data.results.map(async movie => {
                        const movieDetailResponse = await fetch(`${baseUrl}/movie/${movie.id}?api_key=${apiKey}&language=ko-KR`);
                        const movieDetail = await movieDetailResponse.json();
                        return { ...movie, production_countries: movieDetail.production_countries };
                    }));
                    filteredMovies = allMovies; // 초기 필터링은 전체 영화
                    filterMovies(); // 필터 적용
                }
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        }
    };
    /*
    const searchMovies = (query) => {
        if (!query) {
            fetchMovies(); // 검색어가 없는 경우 전체 영화 목록을 가져옴
        } else {
            fetch(`${baseUrl}/search/movie?api_key=${apiKey}&query=${encodeURIComponent(query)}&language=ko-KR`)
                .then(response => response.json())
                .then(data => {
                    if (data.results) {
                        allMovies = data.results;
                        filteredMovies = allMovies; // 초기 필터링은 전체 영화
                        filterMovies(); // 필터 적용
                    }
                })
                .catch(error => console.error('Error fetching data:', error));
        }
    }; */

    // 사람 검색 함수
    const searchPeople = (query) => {
        fetch(`${baseUrl}/search/person?api_key=${apiKey}&query=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                if (data.results) {
                    renderPeople(data.results);
                }
            })
            .catch(error => console.error('Error fetching data:', error));
    };

    // 사람 검색 결과를 렌더링하는 함수
    const renderPeople = (people) => {
        moviesContainer.innerHTML = ''; // 기존 내용을 지우기 위해 초기화
        if (people.length === 0) {
            moviesContainer.innerHTML = '<p>No results found.</p>';
            return;
        }

        people.forEach(person => {
            const nameElement = document.createElement('h2');
            nameElement.textContent = person.name;
            moviesContainer.appendChild(nameElement);

            // 그 사람의 영화 목록 가져오기
            fetch(`${baseUrl}/person/${person.id}/movie_credits?api_key=${apiKey}&language=ko-KR`)
                .then(response => response.json())
                .then(data => renderMovies(data.cast)) // 참여한 영화 목록을 렌더링
                .catch(error => console.error('Error fetching movie credits:', error));
        });
    };

    // 실시간 검색 기능: 입력할 때마다 검색
    searchInput.addEventListener('input', () => {
        const query = searchInput.value.trim();
        searchMovies(query);
    });

    // 버튼 클릭 이벤트 리스너 설정
    genreButtons.forEach(button => {
        button.addEventListener('click', function() {
            handleSelection(this, 'genre');
            filterMovies();
        });
    });

    countryButtons.forEach(button => {
        button.addEventListener('click', function() {
            handleSelection(this, 'country');
            filterMovies();
        });
    });

    // 페이지 로드 시 영화 목록을 가져옴
    fetchMovies();
});

