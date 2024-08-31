/*
 * search.html 
 */

 document.addEventListener('DOMContentLoaded', function() {

    const clearInput = document.getElementById('clear-input');
	const searchInput = document.getElementById('search-input');
	const genreButtons = document.querySelectorAll('.searching-category[data-type="genre"] .btn');
	const countryButtons = document.querySelectorAll('.searching-category[data-type="country"] .btn');
	const movieList = document.querySelector('.movie-list');
	
	let selectedGenre = null;
	let selectedCountry = null;
	
	// searchInput에 키워드 입력하면 clear-input 버튼이 나타남 
    const toggleClearButton = () => {
        if (searchInput.value.trim() !== '') {
            clearInput.style.display = 'block';
        } else {
            clearInput.style.display = 'none';
        }
    };

    // 입력 필드에서 입력이 발생할 때마다 X 아이콘 표시/숨기기
    searchInput.addEventListener('input', toggleClearButton);

    // X 아이콘 클릭 시 입력값 지우기
    clearInput.addEventListener('click', () => {
        searchInput.value = '';
        toggleClearButton(); 
        searchInput.focus();
        updateMovieList([]); // 검색 결과를 비웁니다.
    });

    // 페이지 로드 시 X 아이콘 상태 업데이트
    toggleClearButton();	
	
	// 장르 버튼 클릭 이벤트 
	genreButtons.forEach(button => {
		button.addEventListener('click', function() {
			handleSelection(this, 'genre');		
		});
	});
	
	// 국가 버튼 클릭 이벤트
	countryButtons.forEach(button => {
		button.addEventListener('click', function() {
			handleSelection(this, 'country');
		});
	});
	
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
			filterMovies();
			
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
			filterMovies();
			
		}
	}
	
	function filterMovies() {
        let queryParams = '';
        
        if (selectedGenre) {
            const genreCode = selectedGenre.getAttribute('data-value');
            queryParams += `genreCode=${encodeURIComponent(genreCode)}`;
        }
        
        if (selectedCountry) {
            const countryCode = selectedCountry.getAttribute('data-value');
            if (queryParams.length > 0) queryParams += '&';
            queryParams += `countryCode=${encodeURIComponent(countryCode)}`;
        }
        
        if (queryParams.length > 0) {
            // 장르와 국가 코드로 영화 정보를 찾는 API 호출
            fetch(`/pickflo/api/search/movies?${queryParams}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(movies => {
                    console.log('Filtered Movies:', movies);
                    updateMovieList(movies);
                })
                .catch(error => {
                    console.error('Error fetching movies:', error.message);
                });
        } else {
            // 장르와 국가가 선택되지 않은 경우 영화 목록 비우기
            updateMovieList([]);
        }
    }
	
	function updateMovieList(movies) {
        // 영화 목록을 업데이트하는 함수
	    movieList.innerHTML = ''; // 기존 목록 지우기
	    
	    if (movies.length === 0) {
	        movieList.innerHTML = '';
	    } else {
	        movies.forEach(movie => {
	            if (movie.movieImg && movie.movieImg.trim() !== '') {
	                const movieCard = document.createElement('div');
	                movieCard.classList.add('movie-card');
	                
	                movieCard.innerHTML = `
	                    <img src="${movie.movieImg}" class="poster-image">
	                `;
	                
	                movieList.appendChild(movieCard);
	            }
	        });
	    }
    }
});