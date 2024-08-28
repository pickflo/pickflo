document.addEventListener('DOMContentLoaded', function() {
	// TMDb API 키 및 기본 URL 설정
	const apiKey = 'a03e20d82852309465ec0d0f691d5ed9';
	const baseUrl = 'https://api.themoviedb.org/3';
	const imageBaseUrl = 'https://image.tmdb.org/t/p/w500';

	// DOM 요소 선택
	const searchInput = document.getElementById('search-input');
	const movieList = document.querySelector('.movie-list');
	const searchButton = document.getElementById('search-button');
	const clearButton = document.getElementById('clear-button');
    const clearInput = document.getElementById('clear-input');
	const genreButtons = document.querySelectorAll('.searching-category[data-type="genre"] .btn');
	const countryButtons = document.querySelectorAll('.searching-category[data-type="country"] .btn');

	// 선택된 장르 및 국가
	let selectedGenre = null;
	let selectedCountry = null;

	// 저장된 영화 데이터
	let allMovies = [];
	let filteredMovies = [];
	let currentPage = 1;
	let totalPages = 1;
	let isLoading = false;
	let hiddenMovies = [];  // 저장된 숨겨진 영화 목록
	
	// X 아이콘 표시/숨기기
    const toggleClearButton = () => {
        if (searchInput.value.trim() !== '') {
            clearButton.style.display = 'block';
        } else {
            clearButton.style.display = 'none';
        }
    };

    // 입력 필드에서 입력이 발생할 때마다 X 아이콘 표시/숨기기
    searchInput.addEventListener('input', toggleClearButton);

    // X 아이콘 클릭 시 입력값 지우기
    clearInput.addEventListener('click', () => {
        searchInput.value = '';
        toggleClearButton(); // X 아이콘 숨기기
        searchInput.focus(); // 입력 필드에 포커스 유지
    });

    // 페이지 로드 시 X 아이콘 상태 업데이트
    toggleClearButton();	
	
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
	        console.log('Selected country code:', countryCode);
	
	        result = result.filter(movie => {
	            console.log('Movie origin countries:', movie.origin_country);
	            return Array.isArray(movie.origin_country) && movie.origin_country.includes(countryCode);
	        });
	    }

		// 검색어 필터링
		const query = searchInput.value.trim().toLowerCase();
		if (query) {
			result = result.filter(movie => {
				const cast = movie.cast || [];
				const crew = movie.crew || [];

				console.log(`Filtering movie: ${movie.title}`);
				console.log('Cast:', cast);
				console.log('Crew:', crew);

				return movie.title.toLowerCase().includes(query) ||
					cast.some(person => person.name.toLowerCase().includes(query)) ||
					crew.some(person => person.name.toLowerCase().includes(query) && person.job.toLowerCase() === 'director');
			});
		}

		// 장르, 국가 또는 검색어가 없으면 결과를 비웁니다
		if (!query && !selectedGenre && !selectedCountry) {
			result = []; // 검색어, 장르, 국가가 없으면 빈 배열로 설정
		}

		// 중복된 영화 제거
		result = Array.from(new Map(result.map(movie => [movie.id, movie])).values());
		renderMovies(result);
	};

	// 영화 검색 함수
	const searchMovies = async (query) => {
		if (!query) {
			// 검색어가 없을 때는 전체 영화 목록을 비웁니다
			allMovies = [];
			filteredMovies = [];
			filterMovies(); // 필터 적용
		} else {
			try {
				const response = await fetch(`${baseUrl}/search/movie?api_key=${apiKey}&query=${encodeURIComponent(query)}&language=ko-KR`);
				const data = await response.json();
				console.log('Search results:', data); // 디버깅: 검색 결과 로그
				if (data.results) {
					// 새로 검색된 영화 목록을 allMovies로 설정
					allMovies = await Promise.all(data.results.map(async movie => {
						const movieDetailResponse = await fetch(`${baseUrl}/movie/${movie.id}?api_key=${apiKey}&language=ko-KR`);
						const movieDetail = await movieDetailResponse.json();
						return { ...movie, origin_country: movieDetail.origin_country, cast: movieDetail.cast || [], crew: movieDetail.crew || [] };
					}));

					// 검색 결과에 맞는 영화 필터링
					filteredMovies = [...allMovies];
					filterMovies(); // 필터 적용
				}
			} catch (error) {
				console.error('Error fetching data:', error);
			}
		}
	};


	// 영화 카드를 생성하는 함수
	const createMovieCard = (movie) => {
		const { id, poster_path } = movie;

		const card = document.createElement('div');
		const image = document.createElement('img');

		card.className = 'movie-card';
		image.className = 'poster-image';

		// 포스터 이미지가 없을 경우 대체 이미지 제공
		image.src = poster_path
			? `${imageBaseUrl}${poster_path}`
			: 'https://via.placeholder.com/500x750?text=No+Image';

		card.appendChild(image);

		// 영화 클릭 시 상세 페이지로 이동
		card.addEventListener('click', () => {
			window.location.href = `details.html?id=${id}`;
		});

		return card;
	};

	// 검색 결과를 렌더링하는 함수
	const renderMovies = (movies) => {
		movieList.innerHTML = ''; // 기존 내용을 지우기 위해 초기화
		if (movies.length === 0) {
			movieList.innerHTML = '';
			return;
		}

		movies.forEach(movie => {
			const movieCard = createMovieCard(movie);
			movieList.appendChild(movieCard);
		});
	};

	// 영화 목록을 가져오는 함수
	const fetchMovies = async (page = 1) => {
		try {
			const response = await fetch(`${baseUrl}/discover/movie?api_key=${apiKey}&language=ko-KR&page=${page}`);
			const data = await response.json();
			if (data.results) {
				const newMovies = await Promise.all(data.results.map(async movie => {
					const movieDetailResponse = await fetch(`${baseUrl}/movie/${movie.id}?api_key=${apiKey}&language=ko-KR`);
					const movieDetail = await movieDetailResponse.json();
								
					return { ...movie, origin_country: movieDetail.origin_country };
				}));

				// 페이지가 1이면 전체 목록 초기화, 아니면 새 영화 추가
				if (page === 1) {
					allMovies = newMovies;
					hiddenMovies = newMovies.slice(0, 14); // 처음 14개 영화를 숨김
                    movieList.innerHTML = ''; // 기존 내용 삭제
                    renderMovies(hiddenMovies); // 처음 14개를 화면에 표시
					
					// 스크롤이 생기지 않으면 추가로 불러오기
	                if (newMovies.length < 15 && currentPage < totalPages) {
	                    currentPage++;
	                    await fetchMovies(currentPage);
	                }
					
				} else {
					allMovies = [...allMovies, ...newMovies];
				}

				totalPages = data.total_pages;
				filteredMovies = [...allMovies]; // 전체 영화 데이터를 복사하여 필터링에 사용
				filterMovies(); // 필터 적용
			}
		} catch (error) {
			console.error('Error fetching data:', error);
		}
	};

	// 영화 ID로 캐스트와 크루 정보 가져오기
	const fetchMovieCredits = async (movieId) => {
		try {
			const response = await fetch(`${baseUrl}/movie/${movieId}/credits?api_key=${apiKey}&language=ko-KR`);
			const data = await response.json();
			if (data.cast || data.crew) {
				// 캐스트 정보를 바탕으로 사람 검색하기
				searchPeopleFromCast(data.cast);
				// 크루 정보에서 Director를 바탕으로 사람 검색하기
				searchPeopleFromCrew(data.crew);
			}
		} catch (error) {
			console.error('Error fetching movie credits:', error);
		}
	};

	// 캐스트 및 크루 정보를 사용하여 사람 검색
	const searchPeopleFromCastAndCrew = (cast, crew) => {
		const castNames = cast.map(person => person.name).join(',');
		const directorNames = crew.filter(person => person.job === 'Director').map(director => director.name).join(',');
		const queries = [castNames, directorNames].filter(Boolean).join(',');

		if (queries) {
			searchPeople(queries);
		}
	};

	// 사람 검색 함수
	const searchPeople = async (query) => {
		try {
			const response = await fetch(`${baseUrl}/search/person?api_key=${apiKey}&query=${encodeURIComponent(query)}`);
			const data = await response.json();
			if (data.results) {
				renderPeople(data.results);
			}
		} catch (error) {
			console.error('Error fetching people data:', error);
		}
	};

	// 사람 검색 결과를 렌더링하는 함수
	const renderPeople = (people) => {
		movieList.innerHTML = ''; // 기존 내용을 지우기 위해 초기화
		if (people.length === 0) {
			movieList.innerHTML = '';
			return;
		}

		people.forEach(person => {
			const nameElement = document.createElement('h2');
			nameElement.textContent = person.name;
			// 그 사람의 영화 목록 가져오기
			fetch(`${baseUrl}/person/${person.id}/movie_credits?api_key=${apiKey}&language=ko-KR`)
				.then(response => response.json())
				.then(data => {
					const moviesHeader = document.createElement('h3');
					moviesHeader.textContent = `${person.name}의 영화 목록:`;
					movieList.appendChild(moviesHeader);

					renderMovies(data.cast); // 참여한 영화 목록을 렌더링
				})
				.catch(error => console.error('Error fetching movie credits:', error));
		});
	};

	// 페이지 로드 시 영화 목록을 가져옴
	fetchMovies(currentPage).then(() => {
	    // 스크롤이 없으면 추가 데이터를 자동으로 불러오기
	    if (document.documentElement.scrollHeight <= window.innerHeight) {
	        loadMoreMovies(); // 무한 스크롤 강제 트리거
	    }
	});


	// 무한 스크롤 처리
	const loadMoreMovies = async () => {
		if (isLoading || currentPage > totalPages) return;
		isLoading = true;

		try {
			currentPage++;
			await fetchMovies(currentPage); // 다음 페이지 영화 로드
		} catch (error) {
			console.error('Error fetching data:', error);
		} finally {
			isLoading = false;
		}
	};
	
	const checkForHiddenMovies = () => {
        // 스크롤 시 숨겨진 영화가 화면에 나타나도록 설정
        const cards = movieList.querySelectorAll('.movie-card');
        cards.forEach(card => {
            if (card.getBoundingClientRect().top < window.innerHeight) {
                card.style.opacity = 1;
            }
        });
    };

	// 스크롤 이벤트 리스너 추가
	window.addEventListener('scroll', () => {
		if (window.innerHeight + window.scrollY >= document.documentElement.scrollHeight - 100) {
        loadMoreMovies();
        /*
		if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
			loadMoreMovies(); */
		}
		checkForHiddenMovies();
	});

	// 검색 버튼 클릭 시 검색 수행
	searchButton.addEventListener('click', () => {
		const query = searchInput.value.trim().toLowerCase();
		currentPage = 1; // 검색어가 변경되면 페이지를 초기화
		searchMovies(query);
	});

	// 엔터 검색
	searchInput.addEventListener('keydown', (event) => {
		if (event.key === 'Enter') {
			event.preventDefault();
			const query = searchInput.value.trim().toLowerCase();
			console.log('Enter key pressed. Query:', query);
			currentPage = 1; // 검색어가 변경되면 페이지를 초기화
			searchMovies(query);
		}
	});


	// 실시간 검색 기능: 입력할 때마다 검색
	/*searchInput.addEventListener('input', () => {
		const query = searchInput.value.trim();
		currentPage = 1; // 검색어가 변경되면 페이지를 초기화
		searchMovies(query, currentPage);
	});*/

	// 장르 버튼 클릭 이벤트 
	genreButtons.forEach(button => {
		button.addEventListener('click', function() {
			console.log('Genre button clicked:', this);
			handleSelection(this, 'genre');
			currentPage = 1; // 필터가 변경되면 페이지를 초기화
			filterMovies();
		});
	});
	
	// 국가 버튼 클릭 이벤트
	countryButtons.forEach(button => {
		button.addEventListener('click', function() {
			console.log('Country button clicked:', this);
			handleSelection(this, 'country');
			currentPage = 1; // 필터가 변경되면 페이지를 초기화
			filterMovies();
		});
	});

	// 페이지 로드 시 영화 목록을 가져옴
	fetchMovies(currentPage);
});
